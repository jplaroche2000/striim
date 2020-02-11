package gcp.poc.kafka.streams.model;

import org.json.JSONObject;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class AddressLinkEntity extends FirestoreEntity {

	private Key key;
	private Integer id;
	private Integer addressId;	
	private Integer customerId;

	
	public AddressLinkEntity(JSONObject data) {
		if(data.has("ID")) {
			id = data.getInt("ID");
		}
		if (data.has("ADDRESS_ID")) {
			addressId = data.getInt("ADDRESS_ID");
		}
		if(data.has("CUSTOMER_ID")) {
			customerId = data.getInt("CUSTOMER_ID");
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





	public Integer getAddressId() {
		return addressId;
	}





	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}





	public Integer getCustomerId() {
		return customerId;
	}





	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}





	@Override
	protected Entity newEntity(Datastore datastore) {
		
		Entity customerEntity = Entity.newBuilder(datastore.newKeyFactory()
			    .setKind("AddressLink")
			    .newKey(id))
			    .set("ADDRESS_ID", addressId)
			    .set("CUSTOMER_ID", customerId)
			    .build();
		
		return customerEntity;
	}


	@Override
	protected Key getKey(Datastore datastore) {
		return datastore.newKeyFactory()
			    .setKind("AddressLink")
			    .newKey(id);
	}


	@Override
	protected Entity updatedEntity(Datastore datastore) {
		Key key = datastore.newKeyFactory()
	    .setKind("AddressLink")
	    .newKey(id);
		
		Entity entity = datastore.get(key);
		
		Entity.Builder entityBuilder = Entity.newBuilder(entity);
		
		if(id != null) {
			entityBuilder.set("ID", id);
		}
		if (customerId != null) {
			entityBuilder.set("CUSTOMER_ID", customerId);
		}
		if (addressId != null) {
			entityBuilder.set("ADDRESS_ID", addressId);
		}

		return entityBuilder.build();
		
	}
}
