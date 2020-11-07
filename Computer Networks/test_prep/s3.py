import socket

server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server.bind(("0.0.0.0", 8888))

print("SERVER RUNNING")
data, addr = server.recvfrom(50)

list_nr = data.split(" ")
last_sum = 0

for number in list_nr:
    last_sum += int(number)

server.sendto(str(last_sum), addr)
