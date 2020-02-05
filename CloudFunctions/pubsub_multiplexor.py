import base64
import json

def pubsub_multiplexor(event, context):

    """Triggered from a message on a Cloud Pub/Sub topic.
    Args:
         event (dict): Event payload.
         context (google.cloud.functions.Context): Metadata for the event.
    """
    
    pubsub_message = base64.b64decode(event['data']).decode('utf-8')
    json_string = pubsub_message[1:-1]
    json_object = json.loads(json_string)

    table_name = json_object['metadata']['TableName']
    print(table_name)

    operation_name = json_object['metadata']['OperationName']
    print(operation_name)
    
    domain_object = json_object['data']
    print(domain_object)

    if table_name == 'SCOTT.CUSTOMER':    
        print(domain_object['LAST_NAME'])
        print(domain_object['NAME'])
        print(domain_object['ID'])
