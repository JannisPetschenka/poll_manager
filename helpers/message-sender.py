import pika

EXCHANGE_NAME = 'pollsExchange'

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

channel.exchange_declare(exchange=EXCHANGE_NAME, exchange_type='fanout',
                         durable=True)

message = "{pollId: 10, caption: \"Yes\"}"

channel.basic_publish(
    exchange=EXCHANGE_NAME,
    routing_key='',
    body=message
)

print(f" [x] Published vote: {message}")
connection.close()
