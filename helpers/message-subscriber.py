import pika

EXCHANGE_NAME = 'pollsExchange'

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

channel.exchange_declare(exchange=EXCHANGE_NAME, exchange_type='fanout',
                         durable=True)

result = channel.queue_declare('', exclusive=True)
queue_name = result.method.queue

channel.queue_bind(exchange=EXCHANGE_NAME, queue=queue_name)

print(f" [*] Waiting for messages on {queue_name}. To exit press CTRL+C")

def callback(ch, method, properties, body):
    print(f" [x] Received: {body}")

channel.basic_consume(queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()
