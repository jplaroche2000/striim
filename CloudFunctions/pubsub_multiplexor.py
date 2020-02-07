import base64
import json
import datetime
from google.cloud import datastore

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

    if operation_name == 'INSERT':

        print('Inserting data into Firestore database...')

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
            add_address(datastore_client, domain_object['ID'],domain_object['STREET_NUMBER'],domain_object['STREET'],domain_object['CITY'])

        elif table_name == 'SCOTT.ADDRESS_LINK':
            print(domain_object['ID'])
            print(domain_object['ADDRESS_ID'])
            print(domain_object['CUSTOMER_ID'])     
            add_address_link(datastore_client, domain_object['ID'],domain_object['ADDRESS_ID'],domain_object['CUSTOMER_ID'])

        elif table_name == 'SCOTT.CUSTOMER_ORDER':
            print(domain_object['ID'])
            print(domain_object['ORDER_DATE'])
            print(domain_object['CUSTOMER_ID'])     
            print(domain_object['TOTAL'])     
            add_customer_order(datastore_client, domain_object['ID'],domain_object['ORDER_DATE'],domain_object['CUSTOMER_ID'],domain_object['TOTAL'])

        else:
            print(domain_object['ID'])
            print(domain_object['CUSTOMER_ORDER_ID'])
            print(domain_object['DESCRIPTION'])     
            print(domain_object['QUANTITY'])     
            print(domain_object['PRICE'])   
            add_customer_order_item(datastore_client, domain_object['ID'],domain_object['CUSTOMER_ORDER_ID'],domain_object['DESCRIPTION'],domain_object['QUANTITY'],domain_object['PRICE'])

    elif operation_name == 'DELETE':
    
        print('To be implemented...')
    
    else:
    
        print('To be implemented...')
        

def create_client(project_id):
    """Create database client.
    Args:
         GCP project id where firestore was create
    """
    return datastore.Client(project_id)


def add_customer(db_client, id, name, lastName):
    """Adds a Customer entity to the Firestore database.
    Args:
         client: db client
         id: Customer's id from src database
         name: Customer's name
         lastName: Customer's last name
    """
    key = db_client.key('Customer')

    new_customer = datastore.Entity(key, exclude_from_indexes=['LAST_UPDATE'])

    new_customer.update({
        'ID': id,
        'NAME': name,
        'LAST_NAME': lastName,
        'LAST_UPDATE': datetime.datetime.utcnow()
    })

    db_client.put(new_customer)

    return new_customer.key


def add_address(db_client, id, street_number, street, city):
    """Adds a Address entity to the Firestore database.
    Args:
         client: db client
         id: Customer's id from src database
         street_number: Street number
         street: Street name
         city: City
    """
    key = db_client.key('Address')

    new_address = datastore.Entity(key, exclude_from_indexes=['STREET_NUMBER'])

    new_address.update({
        'ID': id,
        'STREET_NUMBER': street_number,
        'STREET': street,
        'CITY': city
    })

    db_client.put(new_address)

    return new_address.key
    
def add_address_link(db_client, id, address_id, customer_id):
    """Adds a Address entity to the Firestore database.
    Args:
         client: db client
         id: Customer's id from src database
         street_number: Street number
         street: Street name
         city: City
    """
    key = db_client.key('AddressLink')

    new_address_link = datastore.Entity(key)

    new_address_link.update({
        'ID': id,
        'ADDRESS_ID': address_id,
        'CUSTOMER_ID': customer_id
    })

    db_client.put(new_address_link)

    return new_address_link.key

def add_customer_order(db_client, id, orderDate, customerId, total):
    key = db_client.key('CustomerOrder')

    new_customer_order = datastore.Entity(key, exclude_from_indexes=['TOTAL'])

    new_customer_order.update({
        'ID': id,
        'ORDER_DATE': orderDate,
        'CUSTOMER_ID': customerId,
        'TOTAL': total
    })

    db_client.put(new_customer_order)

    return new_customer_order.key


def add_customer_order_item(db_client, id, customer_oder_id, description, quantity, price):

    key = db_client.key('CustomerOrderItem')

    new_customer_order_item = datastore.Entity(key, exclude_from_indexes=['QUANTITY', 'PRICE', 'DESCRIPTION'])

    new_customer_order_item.update({
        'ID': id,
        'CUSTOMER_ORDER_ID': customer_oder_id,
        'DESCRIPTION': description,
        'QUANTITY': quantity,
        'PRICE': price
    })

    db_client.put(new_customer_order_item)

    return new_customer_order_item.key