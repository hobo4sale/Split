from flask import Flask, request
import localconfig


app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'LIZARD APP: SPLIT PAYMENTS!!!'

@app.route('/client-token')
def client_token():
    return braintree.ClientToken.generate()

@app.route('/payment-methods', methods=['POST'])
def create_payment_method():

    result = braintree.PaymentMethod.create({
        "customer_id": "the_customer_id",
        "payment_method_nonce": request.args.get("payment_method_nonce"),
        "options": {
            "verify_card": True
        }
    })

    if result.is_success:
        return "ok"
    else:
        return "error"

@app.route('/payment/<amount>', methods=['POST'])
def payment(amount):
    result = braintree.Transaction.sale({
        "amount": amount,
        "payment_method_nonce": request.args.get("payment_method_nonce"),
        "options": {
            "submit_for_settlement": True
        }
    })

    if result.is_success:
        return "ok"
    else:
        return "error"




if __name__ == '__main__':
    app.run('0.0.0.0')
