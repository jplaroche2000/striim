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
    """print(json_string)"""    
    json_object = json.loads(json_string)
    """"print(json_object)"""
    print(json_object['metadata']['TableName'])
    domain_object = json_object['data']
    print(domain_object)
    """
    print(customer['LAST_NAME'])
    print(customer['NAME'])
    print(customer['ID'])
    """
