from flask import Flask, request, jsonify

app = Flask(_name_)

@app.route('/dados', methods=['POST'])
def receber_dados():
    data = request.json
    print("\nDados recebidos:")
    print(f"Bateria: {data.get('bateria')}%")
    print(f"Memória: {data.get('memoria')}")
    return jsonify({"status": "ok"})

if _name_ == '_main_':
    app.run(host='0.0.0.0', port=5000)
    