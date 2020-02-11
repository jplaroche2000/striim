package gcp.poc.kafka.streams;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import org.json.JSONArray;

import com.google.cloud.datastore.Batch;
import com.google.cloud.datastore.Batch.Response;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

import gcp.poc.kafka.streams.model.FirestoreEntity;
import gcp.poc.kafka.streams.model.StriimRecord;

public class KafkaToFirestoreStream {

	public static void main(String[] args) {

		// Set up the configuration.
		final Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stateless-transformations-example");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "zoo1:9092");
		props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

		// Get the source stream.
		final StreamsBuilder builder = new StreamsBuilder();
		final KStream<String, String> source = builder.stream("bucket");

		source.peek((key, value) -> {

			try {
				
				JSONArray striimRecords = new JSONArray(value);
				
			    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
			    
			    Batch batch = datastore.newBatch();

				striimRecords.forEach(item -> {

					StriimRecord striimRecord = new StriimRecord(item.toString());
					
					FirestoreEntity firestoreEntity = FirestoreEntity.createFirestoreEntity(striimRecord);

					switch (striimRecord.getOperation()) {
					case INSERT:							
						firestoreEntity.persist(batch);
						break;
					case UPDATE:
						firestoreEntity.update(batch);
						break;
					case DELETE:
						firestoreEntity.delete(batch);
						break;
					default:
						break;
					}
					
			
				});
				
				Response resp = batch.submit();
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		final Topology topology = builder.build();
		final KafkaStreams streams = new KafkaStreams(topology, props);
		// Print the topology to the console.
		System.out.println(topology.describe());
		final CountDownLatch latch = new CountDownLatch(1);

		// Attach a shutdown handler to catch control-c and terminate the application
		// gracefully.
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