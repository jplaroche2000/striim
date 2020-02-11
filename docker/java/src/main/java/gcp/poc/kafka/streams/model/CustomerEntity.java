package gcp.poc.kafka.streams.model;

import org.json.JSONObject;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class CustomerEntity extends FirestoreEntity {

	private Key key;
	private Integer id;
	private String name;	
	private String lastName;

	
	public CustomerEntity(JSONObject data) {
		if(data.has("ID")) {
			id = data.getInt("ID");
		}
		if (data.has("NAME")) {
			name = data.getString("NAME");
		}
		if(data.has("LAST_NAME")) {
			lastName = data.getString("LAST_NAME");
		}
	}


	public Key getKey() {
		return key;
	}


	public void setKey(Key key) {
		this.key = key;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	@Override
	protected Entity newEntity(Datastore datastore) {
		
		Entity customerEntity = Entity.newBuilder(datastore.newKeyFactory()
			    .setKind("Customer")
			    .newKey(id))
			    .set("ID", id)
			    .set("NAME", name)
			    .set("LAST_NAME", lastName)
			    .build();
		
		return customerEntity;
	}


	@Override
	protected Key getKey(Datastore datastore) {
		return datastore.newKeyFactory()
			    .setKind("Customer")
			    .newKey(id);
	}


	@Override
	protected Entity updatedEntity(Datastore datastore) {
		Key key = datastore.newKeyFactory()
	    .setKind("Customer")
	    .newKey(id);
		
		Entity entity = datastore.get(key);
		
		Entity.Builder entityBuilder = Entity.newBuilder(entity);
		
		if(id != null) {
			entityBuilder.set("ID", id);
		}
		if (name != null) {
			entityBuilder.set("NAME", name);
		}
		if(lastName != null) {
			entityBuilder.set("LAST_NAME", lastName);
		}
		
		return entityBuilder.build();
		
	}
	

	
}
