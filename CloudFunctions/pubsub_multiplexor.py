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
        print(domain_object['ID'])
        print(domain_object['NAME'])
        print(domain_object['LAST_NAME'])

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
         
