import socket
import time
import os
import threading
import pickle

def get_sum(number):
    summ = 0
    while number:
        summ += number % 10
        number /= 10
    return summ

def multi(cs, addr):
    data = cs.recv(1024)
    string = pickle.loads(data)
    print(str(string) + " -> " + str(addr[1]))
    summ = get_sum(addr[1])
    
    data = pickle.dumps(str(summ))
    cs.send(data)
    cs.close()


server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(("127.0.0.1", 8888))
server.listen(5)

print("SERVER RUNNING...")

while True:
    con, addr = server.accept()
    print(str(addr) + " Connected...")
    t = threading.Thread(target=multi, args=(con, addr))
    t.start()
