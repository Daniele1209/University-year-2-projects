import socket

server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server.bind(("0.0.0.0", 8888))

print("SERVER RUNNING")
data, addr = server.recvfrom(50)

dictionary = {}
count_max = 0
max_word = ""

sentance = data.split(" ")
for word in sentance:
    count = 0
    dictionary[word] = 0
    for l in word:
        if l in "aeiou":
            count += 1
    dictionary[word] = count

for word, length in dictionary.items():
    if length > count_max:
        count_max = length
        max_word = word

server.sendto(str(max_word), addr)


