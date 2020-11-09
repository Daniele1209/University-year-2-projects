import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(("127.0.0.1", 8888))
s.listen(5)

while True:
    csock, addr = s.accept()
    print(str(addr) + " -Hello there !")
    csock.send(str("General Kenobi"))
