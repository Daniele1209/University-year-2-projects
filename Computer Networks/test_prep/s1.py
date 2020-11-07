import socket

server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server.bind(("0.0.0.0", 8888))

print("UDP SERVER UP AND RUNNING")
data, addr = server.recvfrom(20)

count = str(len(data))

server.sendto(count, addr)
