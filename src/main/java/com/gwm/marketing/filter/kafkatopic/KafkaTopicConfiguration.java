package com.gwm.marketing.filter.kafkatopic;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author:hongtaofan
 * @Version:1.0
 * @Description:
 * @Date: 2023/4/14 10:19
 */
@Configuration
@RefreshScope
public class KafkaTopicConfiguration {

    public static String grafanaTopic;

    public static String userlogrecordTopic;

    @Value("${spring.orakafka.producer.grafana-topic}")
    public  void setGrafanaTopic(String grafanaTopic) {
        KafkaTopicConfiguration.grafanaTopic = grafanaTopic;
    }

    @Value("${spring.orakafka.producer.userlogrecord-topic}")
    public  void setUserlogrecordTopic(String userlogrecordTopic) {
        KafkaTopicConfiguration.userlogrecordTopic = userlogrecordTopic;
    }
}
