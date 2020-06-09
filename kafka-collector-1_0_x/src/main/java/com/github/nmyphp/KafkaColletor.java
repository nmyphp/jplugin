package com.github.nmyphp;

import com.github.nmyphp.interfaces.Collector;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;

public class KafkaColletor implements Collector {


    @Override
    public List<String> listTable() {
        try (AdminClient client = client()) {
            Collection<TopicListing> topicListings = client.listTopics(new ListTopicsOptions().timeoutMs(10 * 1000))
                .listings().get();
            return topicListings.stream().map(TopicListing::name).collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error(null, ex);
            return Collections.emptyList();
        }
    }

    private AdminClient client() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.202.145.240:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group4QdamCollection");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 1 * 1024 * 1024);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return AdminClient.create(props);
    }
}
