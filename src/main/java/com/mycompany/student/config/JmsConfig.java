//package com.mycompany.student.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.annotation.EnableJms;
//import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
//
//import jakarta.jms.ConnectionFactory;
//
///**
// *
// * @author AJ Catambay of Bridging Code
// *
// */
//@Configuration
//@EnableJms
//public class JmsConfig {
//
//    @Bean
//    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
//            ConnectionFactory connectionFactory) {
//
//        DefaultJmsListenerContainerFactory factory
//                = new DefaultJmsListenerContainerFactory();
//
//        factory.setConnectionFactory(connectionFactory);
//        factory.setConcurrency("5-10");
//
//        return factory;
//    }
//}

package com.mycompany.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import jakarta.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("5-10");
        factory.setMessageConverter(jacksonJmsMessageConverter()); // Set the message converter

        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}

