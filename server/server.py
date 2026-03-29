from flask import Flask, request, jsonify
import datetime

app = Flask(__name__)

@app.route('/dados', methods=['POST'])
def receber_dados():
    data = request.json
    
    bateria = data.get('bateria')
    memoria = data.get('memoria')
    
    timestamp = datetime.datetime.now()

    log = f"{timestamp} | Bateria: {bateria}% | Memória: {memoria}\n"

    print(log)

    with open("log.txt", "a") as arquivo:
        arquivo.write(log)

    return jsonify({"status": "ok"})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)