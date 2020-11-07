import socket

client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
string_msg = raw_input("Enter phrase: ")
client.sendto(string_msg, ("127.0.0.1", 8888))
data,addr = client.recvfrom(50)
print("%s" %data)
