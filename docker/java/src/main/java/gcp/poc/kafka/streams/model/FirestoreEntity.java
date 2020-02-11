package gcp.poc.kafka.streams.model;

import com.google.cloud.datastore.Batch;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

public abstract class FirestoreEntity {

	public static FirestoreEntity createFirestoreEntity(StriimRecord record) {
		
		if (record.getTableName().equals("SCOTT.CUSTOMER")) {
			return new CustomerEntity(record.getData());
		
		} else if (record.getTableName().equals("SCOTT.CUSTOMER_ORDER")) {
			return new CustomerOrderEntity(record.getData());
		
		} else if (record.getTableName().equals("SCOTT.CUSTOMER_ORDER_ITEM")) {
			return new CustomerOrderItemEntity(record.getData());
		
		} else if (record.getTableName().equals("SCOTT.ADDRESS")) {
			return new AddressEntity(record.getData());
		
		} else if (record.getTableName().equals("SCOTT.ADDRESS_LINK")) {
			return new AddressLinkEntity(record.getData());
		
		} else {
			// not implemented yet
			throw new UnsupportedOperationException("Table mapping "+record.getTableName()+" not supported!");
		}

	}

	public void persist(Batch batch) {
		Entity entity = newEntity(batch.getDatastore());
		System.out.println("Adding add operation to datastore batch - key: "+entity.getKey());
		batch.add(entity);
	}

	public void update(Batch batch) {
		Entity entity = updatedEntity(batch.getDatastore());
		System.out.println("Adding update operation to datastore batch - key: "+entity.getKey());
		batch.update(entity);
	}

	public void delete(Batch batch) {
		Key key = getKey(batch.getDatastore());
		System.out.println("Adding delete operation to datastore batch - key: "+key.toString());
		batch.delete(key);
	}
	
	protected abstract Key getKey(Datastore datastore);

	protected abstract Entity updatedEntity(Datastore datastore);
	
	protected abstract Entity newEntity(Datastore datastore);

}
