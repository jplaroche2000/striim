package gcp.poc.kafka.consumer;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class SimpleConsumer {
	
   public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

   public static void main(String[] args) {
	   
       // Set up the configuration.
       String topics[] = null;
		
       String bootstrapServers = "zoo1";
       String groupId = System.currentTimeMillis() +"";
       topics = new String[] {"bucket", "address", "address_link", "customer", "customer_order", "customer_order_item"};
       topics = new String[] {"customer-entity-tx", "other-entity-tx"};
       topics = new String[] {"insert-tx", "update-tx", "delete-tx", "other-tx"};
	   
       Properties props = new Properties();
       props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers + ":9092");
       props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
       props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
       props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
       props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
       props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
       
       System.out.println("Topics " + Arrays.toString(topics));
       
       KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
       consumer.subscribe(Arrays.asList(topics));
//       consumer.poll(0);
//       consumer.seekToBeginning(consumer.assignment());
       while (true) {
           ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
           for (ConsumerRecord<String, String> record : records) {
        	   Date date = new Date(record.timestamp());
               System.out.printf("topic = %s, timestamp = %s, offset = %d, key = %s, value = %s%n", record.topic(), DATE_FORMAT.format(date), record.offset(), record.key(), record.value());
           }
       }
   }

}
