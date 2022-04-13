from socket import socket
import socketserver

class TCPHandler(socketserver.BaseRequestHandler):
    def handle(self):
        self.data = self.request.recv(1024).strip()
        self.server.execute(format(self.data))
        print("{} wrote:".format(self.client_address[0]))
        # just send back the same data, but upper-cased
        self.request.sendall(self.data.upper())

class Server(socketserver.TCPServer):
    def __init__(self, server_address, RequestHandlerClass):
        socketserver.TCPServer.__init__(self, server_address, RequestHandlerClass, bind_and_activate=True)
        self.isRunning = True
        try:
            self.balanceFile = open("balance.txt", "r+")
            self.balanceFile.close()
        except IOError:
            self.balance = 100
            
        print('Welcome to your bank account!\n===================================\n')

    def execute(self, data):
        print('Please select an option.')
   


if __name__ == "__main__":
    HOST, PORT = "localhost", 9999
    with Server((HOST, PORT), TCPHandler) as server:
        server.serve_forever()
