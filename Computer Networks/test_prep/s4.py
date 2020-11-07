import socket

server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server.bind(("0.0.0.0", 8888))

print("SERVER RUNNING")
data, addr = server.recvfrom(50)

print(data)
print(addr)
summ = 0

number = int(addr[1])
while number:
    summ += number % 10
    number /= 10

server.sendto(str(summ), addr)
