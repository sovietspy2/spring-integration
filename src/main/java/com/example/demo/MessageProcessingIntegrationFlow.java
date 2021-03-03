package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MessageProcessingIntegrationFlow {

    public static final String OUTBOUND_FILENAME_GENERATOR = "outboundFilenameGenerator";
    public static final String FILE_WRITING_MESSAGE_HANDLER = "fileWritingMessageHandler";
    @Autowired
    public File inboundOutDirectory;

    @Bean
    public IntegrationFlow writeToFile(@Qualifier("fileWritingMessageHandler") MessageHandler fileWritingMessageHandler) {
        return IntegrationFlows.from(Config.INBOUND_CHANNEL)
                .transform(m -> new StringBuilder((String) m).reverse().toString())
                .handle(fileWritingMessageHandler)
                .log(LoggingHandler.Level.DEBUG)
                .get();
    }


    @Bean(name = FILE_WRITING_MESSAGE_HANDLER)
    public MessageHandler fileWritingMessageHandler(@Qualifier(OUTBOUND_FILENAME_GENERATOR) FileNameGenerator fileNameGenerator) {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(inboundOutDirectory);
        handler.setAutoCreateDirectory(true);
        handler.setFileNameGenerator(fileNameGenerator);
        return handler;
    }

    @Bean(name = OUTBOUND_FILENAME_GENERATOR)
    public FileNameGenerator outboundFileName(@Value("${out.filename.dateFormat}") String dateFormat, @Value("${out.filename.suffix}") String filenameSuffix) {
        return message -> DateTimeFormatter.ofPattern(dateFormat).format(LocalDateTime.now()) + filenameSuffix;
    }
}
