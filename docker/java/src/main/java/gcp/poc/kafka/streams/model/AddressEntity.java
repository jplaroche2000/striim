package gcp.poc.kafka.streams.model;

import org.json.JSONObject;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class AddressEntity extends FirestoreEntity {

	private Key key;
	private Integer id;
	private String street;	
	private String city;
	private Integer number;
	

	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public Integer getNumber() {
		return number;
	}


	public void setNumber(Integer number) {
		this.number = number;
	}


	public AddressEntity(JSONObject data) {
		if(data.has("ID")) {
			id = data.getInt("ID");
		}
		if (data.has("STREET")) {
			street = data.getString("STREET");
		}
		if (data.has("STREET_NUMBER")) {
			number = data.getInt("STREET_NUMBER");
		}
		if (data.has("CITY")) {
			city = data.getString("CITY");
		}
	}


	public Key getKey() {
		return key;
	}


	public void setKey(Key key) {
		this.key = key;
	}




	@Override
	protected Entity newEntity(Datastore datastore) {
		
		Entity customerEntity = Entity.newBuilder(datastore.newKeyFactory()
			    .setKind("Address")
			    .newKey(id))
			    .set("ID", id)
			    .set("STREET", street)
			    .set("STREET_NUMBER", number)
			    .set("CITY", city)
			    .build();
		
		return customerEntity;
	}


	@Override
	protected Key getKey(Datastore datastore) {
		return datastore.newKeyFactory()
			    .setKind("Address")
			    .newKey(id);
	}


	@Override
	protected Entity updatedEntity(Datastore datastore) {
		
		Key key = datastore.newKeyFactory()
	    .setKind("Address")
	    .newKey(id);
		
		Entity entity = datastore.get(key);
		
		Entity.Builder entityBuilder = Entity.newBuilder(entity);
		
		if(id != null) {
			entityBuilder.set("ID", id);
		}
		if (street != null) {
			entityBuilder.set("STREET", street);
		}
		if(number != null) {
			entityBuilder.set("STREET_NUMBER", number);
		}
		if(city != null) {
			entityBuilder.set("CITY", city);
		}		
		return entityBuilder.build();
		
	}
	
}
