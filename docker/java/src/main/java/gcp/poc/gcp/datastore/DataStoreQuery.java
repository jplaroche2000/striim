package gcp.poc.gcp.datastore;

import java.util.Iterator;
import java.util.Map;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery.OrderBy;

/**
 * Getting started with the Firestore in Datastore mode API:
 * https://cloud.google.com/datastore/docs/datastore-api-tutorial#datastore-datastore-build-service-java
 * 
 * @author duke2
 *
 */	
public class DataStoreQuery {

	/**
	 * Query a all Customer entities that were inserted via Striim/GCP PubSub to Firestore in Datastore mode
	 * @param args
	 */
	public static void main(String[] args) {
		

			Map envMap = System.getenv();
		
			System.out.print(envMap.get("GOOGLE_APPLICATION_CREDENTIALS"));
			
		    // Instantiates a client
		    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

		    // The kind for the new entity
		    String kind = "Customer";

//		    // The name/ID for the new entity
//		    String name = "sampletask1";
//
//		    // The Cloud Datastore key for the new entity
//		    Key taskKey = datastore.newKeyFactory().setKind(kind).newKey(name);
//
//		    // Prepares the new entity
//		    Entity task = Entity.newBuilder(taskKey)
//		        .set("description", "Buy milk")
//		        .build();
		      
		    Query<Entity> query = Query.newEntityQueryBuilder().setKind("Customer").setOrderBy(OrderBy.asc("ID")).build();
		    
		    	  
		    Iterator<Entity> list = datastore.run(query);
		    	  
		    while(list.hasNext()) {
			    System.out.println(list.next());
		    	
		    }
		    	 
	}
	
	
}
