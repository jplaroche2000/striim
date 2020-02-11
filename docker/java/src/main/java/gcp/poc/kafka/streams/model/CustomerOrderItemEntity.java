package gcp.poc.kafka.streams.model;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public class CustomerOrderItemEntity extends FirestoreEntity {

	private Key key;
	private Integer id;
	private Integer customerOrderId;
	private String description;	
	private Integer quantity;
	private BigDecimal price;

	public CustomerOrderItemEntity(JSONObject data) {
		
		if(data.has("ID")) {
			id = data.getInt("ID");
		}
		if (data.has("CUSTOMER_ORDER_ID")) {
			customerOrderId = data.getInt("CUSTOMER_ORDER_ID");
		}
		if(data.has("DESCRIPTION")) {
			description = data.getString("DESCRIPTION");
		}
		if(data.has("QUANTITY")) {
			quantity = data.getInt("QUANTITY");
		}
		if(data.has("PRICE")) {
			String priceStr = data.getString("PRICE");
			price = new BigDecimal(priceStr);
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




	public Integer getCustomerOrderId() {
		return customerOrderId;
	}




	public void setCustomerOrderId(Integer customerOrderId) {
		this.customerOrderId = customerOrderId;
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public Integer getQuantity() {
		return quantity;
	}




	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}




	public BigDecimal getPrice() {
		return price;
	}




	public void setPrice(BigDecimal price) {
		this.price = price;
	}




	@Override
	protected Entity newEntity(Datastore datastore) {
		
		Entity customerEntity = Entity.newBuilder(datastore.newKeyFactory()
			    .setKind("CustomerOrderItem")
			    .newKey(id))
			    .set("DESCRIPTION", description)
			    .set("CUSTOMER_ORDER_ID", customerOrderId)
			    .set("PRICE", price.toString())
			    .set("QUANTITY", quantity)
			    .build();
		
		return customerEntity;
	}


	@Override
	protected Key getKey(Datastore datastore) {
		return datastore.newKeyFactory()
			    .setKind("CustomerOrderItem")
			    .newKey(id);
	}


	@Override
	protected Entity updatedEntity(Datastore datastore) {
		Key key = datastore.newKeyFactory()
	    .setKind("CustomerOrderItem")
	    .newKey(id);
		
		Entity entity = datastore.get(key);
		
		Entity.Builder entityBuilder = Entity.newBuilder(entity);
		
		if(id != null) {
			entityBuilder.set("ID", id);
		}
		if(description != null) {
			entityBuilder.set("DESCRIPTION", description);
		}
		if (customerOrderId != null) {
			entityBuilder.set("CUSTOMER_ORDER_ID", customerOrderId);
		}
		if(price != null) {
			entityBuilder.set("PRICE", price.toString());
		}
		if(quantity != null) {
			entityBuilder.set("QUANTITY", quantity);
		}
		
		return entityBuilder.build();
		
	}
}