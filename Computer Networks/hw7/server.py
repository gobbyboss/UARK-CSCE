import socket

class Server():
    def __init__(self):
        self.balance = 100
    def depositBalance(self, transaction):
        self.balance += transaction
    def withdrawBalance(self, transcation):
        if self.balance - transcation < 0:
            return False
        self.balance -= transcation
        return True

if __name__ == "__main__":
    HOST, PORT = "localhost", 9999
    serverSock = socket.socket()
    server = Server()

    serverSock.bind((HOST, PORT))
    while True:
        serverSock.listen(3)
        connection, address = serverSock.accept()
        connected = connection.recv(1024).decode()
        print(connected)
        menu = 0
        while menu != "4":
            menu = connection.recv(1024).decode()
            if menu ==  "1":
                connection.sendall(bytes(str(server.balance), "utf-8"))
            if menu == "2":
                message = 'How much money would you like to deposit?'
                connection.sendall(bytes(str(message), "utf-8"))
                recieved = connection.recv(1024).decode()
                server.depositBalance(int(recieved))
                message = ('Successfully deposited! Your new balance is $' + str(server.balance))
                connection.sendall(bytes(str(message), "utf-8"))
            if menu == "3":
                message = ('You have currently $' + str(server.balance) 
                + '. How much would you like to withdraw?')
                connection.sendall(bytes(str(message), "utf-8"))
                recieved = connection.recv(1024).decode()
                if server.withdrawBalance(int(recieved)):
                    message = ('Successfully withdrew! Your new balance is $' + str(server.balance))
                else:
                    message = ('Not enough funds to withdraw. Please try again later.')
                connection.sendall(bytes(str(message), "utf-8"))
            if menu == "4":
                recieved = connection.recv(1024).decode()
                print(recieved)
                connection.close()
