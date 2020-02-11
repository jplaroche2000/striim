package gcp.poc.kafka.streams.model;

import org.json.JSONObject;

public class StriimRecord {

	private JSONObject data;
	private JSONObject metadata;
    String tableName;
    OperationName operation;
    
    public StriimRecord(String json) {
    	
    	JSONObject striimRecord = new JSONObject(json);
		
    	data = striimRecord.getJSONObject("data");
		metadata = striimRecord.getJSONObject("metadata");
		
		tableName = (String) metadata.get("TableName");
		operation = OperationName.valueOf(metadata.get("OperationName").toString());

    }

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public JSONObject getMetadata() {
		return metadata;
	}

	public void setMetadata(JSONObject metadata) {
		this.metadata = metadata;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public OperationName getOperation() {
		return operation;
	}

	public void setOperation(OperationName operation) {
		this.operation = operation;
	}
    
    
    
}
