import socket
import datetime

HOST = '0.0.0.0'
PORT = 5000

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((HOST, PORT))
server.listen()

print("Servidor aguardando conexões...")

while True:
    conn, addr = server.accept()
    print("Conectado por", addr)

    data = conn.recv(1024).decode()

    timestamp = datetime.datetime.now()
    log = f"{timestamp} | {data}\n"

    print(log)

    with open("log.txt", "a") as arquivo:
        arquivo.write(log)

    conn.send("Dados recebidos".encode())

    conn.close()