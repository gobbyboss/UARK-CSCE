import socketserver

class Server():
    def __init__(self, data):
        self.isRunning = True
        self.data = data
        try:
            self.balanceFile = open("balance.txt", "r+")
            self.balanceFile.close()
            print('Server is ready.')
        except IOError:
            self.balance = 100
            print('The balance has been initialized to 100')

  
class TCPHandler(socketserver.BaseRequestHandler):
    def handle(self):
        self.data = self.request.recv(1024).strip()
        s = Server(self.data)
        print("{} wrote:".format(self.client_address[0]))
        print(self.data)
        # just send back the same data, but upper-cased
        self.request.sendall(self.data.upper())
    

      


if __name__ == "__main__":
    HOST, PORT = "localhost", 9999
    with socketserver.TCPServer((HOST, PORT), TCPHandler) as server:
        server.serve_forever()
