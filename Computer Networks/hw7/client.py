import socket

HOST, PORT = "localhost", 9999

print('Welcome to your bank account! Please wait as we connect with the server.')

with socket.socket() as sock:
    # Connect to server and send data
    connected = True
    print('Successfully connected to the server.')
    sock.connect((HOST, PORT))
    data = "Client from " + str(sock.getsockname()) + " on port " + str(PORT) + " has connected."
    sock.sendall(bytes(str(data), "utf-8"))
    menuOptions = True
    while menuOptions:
        print('Please select an option.\n1) Check your balance\n2) Deposit \n3) Withdraw\n4) Exit')
        data = input()
        if data == "1":
            sock.sendall(bytes(str(data), "utf-8"))
            received = str(sock.recv(1024), "utf-8")
            print('Your current balance is $' + received)
        elif data == "2" or data == "3":
            sock.send(bytes(str(data), "utf-8"))
            recieved = str(sock.recv(1024), "utf-8")
            print(recieved)
            print('Amount: ', end='')
            intToSend = input()
            validInput = False
            while not validInput:
                try:
                    intToSend = int(intToSend)
                    if intToSend > 0:
                        validInput = True
                    else:
                        print('Invalid input. Please try again.')
                        print('Amount: ', end='')
                        intToSend = input()
                except ValueError:
                    print('Invalid input. Please try again.')
                    print('Amount: ', end='')
                    intToSend = input()
            sock.sendall(bytes(str(intToSend), "utf-8"))
            recieved = str(sock.recv(1024), "utf-8")
            print(recieved)

        elif data == "4":
            menuOptions = False
            sock.send(bytes(str('4'), "utf-8"))
        else:
            print('Invalid input. Please try again')
            data = input()
    
    data = 'Client from ' + str(sock.getsockname()) + ' has disconnected.'
    sock.sendall(bytes(data + "\n", "utf-8"))
    
print('Exiting now...')