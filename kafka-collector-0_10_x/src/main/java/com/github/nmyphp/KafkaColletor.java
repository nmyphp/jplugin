package com.github.nmyphp;

import com.github.nmyphp.interfaces.Collector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;

public class KafkaColletor implements Collector {

    @Override
    public List<String> listTable() {
        try (KafkaConsumer<String, String> consumer = client()) {
            Map<String, List<PartitionInfo>> topics = consumer.listTopics();
            return new ArrayList<>(topics.keySet());
        } catch (Exception ex) {
            logger.error("", ex);
            return Collections.emptyList();
        }
    }

    private KafkaConsumer<String, String> client() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.202.145.240:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group4QdamCollection");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }
}
