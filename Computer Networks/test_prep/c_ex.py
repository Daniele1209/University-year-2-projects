import socket
import sys
import pickle

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(("127.0.0.1", 8888))

arg = raw_input("Enter your name: ")
arg = pickle.dumps(arg)
s.send(arg)

message = s.recv(1024)
final_msg = pickle.loads(message)
print("Your port sum: " + str(final_msg))
