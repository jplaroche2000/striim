package gcp.poc.kafka.streams;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

public class SplitStream {
	
	static final String CUSTOMER_TABLE = "\"SCOTT.CUSTOMER\"";
	static final String CUSTOMER_ORDER_TABLE = "\"SCOTT.CUSTOMER_ORDER\"";
	
	static final String INSERT_ACTION = "\"INSERT\"";
	static final String UPDATE_ACTION = "\"UPDATE\"";
	static final String DELETE_ACTION = "\"DELETE\"";
	
	static final String CUSTOMER_ENTITY_TRANSACTION_TOPIC = "customer-entity-tx";
	static final String OTHER_ENTITY_TRANSACTION_TOPIC = "other-entity-tx";
	
	static final String INSERT_TRANSACTION_TOPIC = "insert-tx";
	static final String UPDATE_TRANSACTION_TOPIC = "update-tx";
	static final String DELETE_TRANSACTION_TOPIC = "delete-tx";
	static final String OTHER_TRANSACTION_TOPIC = "other-tx";
	
    public static void main(String[] args) {
    	
        // Set up the configuration.
    	String bootstrapServers = "zoo1";
    	String applicationId = "split-" + System.currentTimeMillis();
    	String sourceTopic ="bucket";
    	
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers + ":9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        
        // Since the input topic uses Strings for both key and value, set the default Serdes to String.
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Get the source stream.
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> source = builder.stream(sourceTopic);

        // Split the stream by entity
        KStream<String, String>[] tableBranches = source
            .branch((key, value) -> value.indexOf(CUSTOMER_TABLE, 0) > -1
            , (key, value) -> true);
        KStream<String, String> customerEntityStream = tableBranches[0];
        KStream<String, String> otherEntityStream = tableBranches[1];

        //Output the transformed data to a topic.
        customerEntityStream.peek((key, value) -> System.out.println("customer key=" + key + ", value=" + value));
        otherEntityStream.peek((key, value) -> System.out.println("otherTable key=" + key + ", value=" + value));
        
        customerEntityStream.to(CUSTOMER_ENTITY_TRANSACTION_TOPIC);
        otherEntityStream.to(OTHER_ENTITY_TRANSACTION_TOPIC);

        // Split the stream by tx actions
        KStream<String, String>[] actionBranches = source
            .branch((key, value) -> value.indexOf(INSERT_ACTION, 0) > -1
            , (key, value) -> value.indexOf(UPDATE_ACTION, 0) > -1
            , (key, value) -> value.indexOf(DELETE_ACTION, 0) > -1
            , (key, value) -> true);
        KStream<String, String> insertTxStream = actionBranches[0];
        KStream<String, String> updateTxStream = actionBranches[1];
        KStream<String, String> deleteTxStream = actionBranches[2];
        KStream<String, String> otherTxStream = actionBranches[3];

        //Output the transformed data to a topic.
        
        insertTxStream.peek((key, value) -> System.out.println("insert key=" + key + ", value=" + value));
        updateTxStream.peek((key, value) -> System.out.println("update key=" + key + ", value=" + value));
        deleteTxStream.peek((key, value) -> System.out.println("delete key=" + key + ", value=" + value));
        otherTxStream.peek((key, value) -> System.out.println("otherAction key=" + key + ", value=" + value));

        insertTxStream.to(INSERT_TRANSACTION_TOPIC);
        updateTxStream.to(UPDATE_TRANSACTION_TOPIC);
        deleteTxStream.to(DELETE_TRANSACTION_TOPIC);
        otherTxStream.to(OTHER_TRANSACTION_TOPIC);
        
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        // Print the topology to the console.
        System.out.println(topology.describe());
        final CountDownLatch latch = new CountDownLatch(1);

        // Attach a shutdown handler to catch control-c and terminate the application gracefully.
        Runtime.getRuntime().addShutdownHook(new Thread("streams-wordcount-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }

}
