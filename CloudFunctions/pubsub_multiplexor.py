import base64
import json
import datetime
from google.cloud import datastore

def create_client(project_id):
    return datastore.Client(project_id)

def add_customer(client, id, name, lastName):
    key = client.key('Customer')

    new_customer = datastore.Entity(key, exclude_from_indexes=['LAST_UPDATE'])

    new_customer.update({
        'ID': id
        'NAME': name,
        'LAST_NAME': lastName,
        'LAST_UPDATE': datetime.datetime.utcnow(),
    })

    client.put(new_customer)

    return new_customer.key
    
    

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
    
    datastore_client = create_client('telus-project-001');

    if table_name == 'SCOTT.CUSTOMER':    
        print(domain_object['ID'])
        print(domain_object['NAME'])
        print(domain_object['LAST_NAME'])
        add_customer(datastore_client, domain_object['ID'], domain_object['NAME'], domain_object['LAST_NAME'])

    elif table_name == 'SCOTT.ADDRESS':
        print(domain_object['ID'])
        print(domain_object['STREET_NUMBER'])
        print(domain_object['STREET'])    
        print(domain_object['CITY'])    

    elif table_name == 'SCOTT.ADDRESS_LINK':
        print(domain_object['ID'])
        print(domain_object['ADDRESS_ID'])
        print(domain_object['CUSTOMER_ID'])     

    elif table_name == 'SCOTT.CUSTOMER_ORDER':
        print(domain_object['ID'])
        print(domain_object['ORDER_DATE'])
        print(domain_object['CUSTOMER_ID'])     
        print(domain_object['TOTAL'])     

    else:
        print(domain_object['ID'])
        print(domain_object['CUSTOMER_ORDER_ID'])
        print(domain_object['DESCRIPTION'])     
        print(domain_object['QUANTITY'])     
        print(domain_object['PRICE'])   
         
