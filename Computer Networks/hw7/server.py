from socket import socket
import socketserver

class TCPHandler(socketserver.BaseRequestHandler):
    def handle(self):
        self.data = self.request.recv(1024).strip()
        self.dataToSend = self.server.execute(format(self.data))
        print("{} wrote:".format(self.client_address[0]))
        self.request.sendall(bytes(self.dataToSend + "\n", "utf-8"))

class Server(socketserver.TCPServer):
    def __init__(self, server_address, RequestHandlerClass):
        socketserver.TCPServer.__init__(self, server_address, RequestHandlerClass, bind_and_activate=True)
        self.firstRequest = True
        try:
            self.balanceFile = open("balance.txt", "r+")
            self.balanceFile.close()
        except IOError:
            self.balance = 100
            
        
    def execute(self, data):
        if self.firstRequest:
            self.firstRequest = False
            return ('Welcome to your bank account!\n===================================\nPlease select an option.'
            + '\n1) Check your balance\n2) Select an input\n')
    
        
if __name__ == "__main__":
    HOST, PORT = "localhost", 9999
    print('Server starting...')
    with Server((HOST, PORT), TCPHandler) as server:
        server.serve_forever()
    print('Server exiting...')
