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
		System.out.println("persist");
		batch.add(newEntity(batch.getDatastore()));
	}

	public void update(Batch batch) {
		System.out.println("update");
		batch.update(updatedEntity(batch.getDatastore()));
	}

	public void delete(Batch batch) {
		System.out.println("delete");
		batch.delete(getKey(batch.getDatastore()));

	}
	
	protected abstract Key getKey(Datastore datastore);

	protected abstract Entity updatedEntity(Datastore datastore);
	
	protected abstract Entity newEntity(Datastore datastore);

}
