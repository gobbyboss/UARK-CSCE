import mysql.connector
import re

def checkRangeInput(minNum, maxNum):
    check = input()
    while(int(check) < minNum or int(check) > int(maxNum)):
        print("That is not a valid input. Please try again.")
        check = input()
    return int(check)

def deleteQuery(id, table):
    query = 'DELETE FROM ' + table + ' WHERE ID = \"' + id + '\"'
    return query

cnx = mysql.connector.connect(user='rwgoss', password='Ce9cingo',
                              host='127.0.0.1',
                              database='rwgoss')
cursor = cnx.cursor()
menu = 0

print("Robert Goss DB Hw4\n=================================\n")
while menu != 5:
    print('''
Please select an option:\n
1) Inventory\n
2) Add Supplier\n
3) Employee Performance\n
4) Update Item\n
5) Quit\n
    ''')
    
    #Print suppliers option
    menu = checkRangeInput(1, 5)
    if(menu == 1):
        #Capture info for query
        print("Please select a supplier")
        query = ("SELECT NAME FROM SUPPLIER")
        cursor.execute(query)
        index = 1
        Suppliers = []
        for NAME in cursor:
            Supplier = NAME[0] 
            print(index, ') ',Supplier)
            Suppliers.append(Supplier)
            index += 1
        print(index, ') Select ALL suppliers')
        supplierInput = checkRangeInput(1, index)

        #Execute main query
        Supplier = ''
        if int(supplierInput) != index:
            Supplier = Suppliers[int(supplierInput) - 1]
            print("You selected", Supplier)
            query = ('SELECT i.SUPPLIER_ID AS sId, s.NAME AS sName, i.NAME AS iName, i.QUANTITY AS quantity '
            + 'FROM ITEM i, SUPPLIER s WHERE s.ID = i.SUPPLIER_ID AND s.NAME = \"'
            + Supplier + '\"')
        
        else:
            Supplier = "ALL"
            query = ('SELECT i.SUPPLIER_ID AS sId, s.NAME AS sName, i.NAME AS iName, i.QUANTITY AS quantity '
            + 'FROM ITEM i, SUPPLIER s WHERE s.ID = i.SUPPLIER_ID')
           
        cursor.execute(query)

        #Capturing and Printing Query Results
        sId = []
        sName = []
        iName = []
        quantity = []
        for capture in cursor:
            sId.append(capture[0])
            sName.append(capture[1])
            iName.append(capture[2])
            quantity.append(capture[3])

        print("\n==========================\n   Printing Results.....      \n==========================")
        for x in range(len(sId)):
            print('Supplier ID:', sId[x], ', Supplier Name:', sName[x],
             ', Item Name:',iName[x],', Quantity:', quantity[x],'\n')


    if(menu == 2):
        #Capturing and verifying inputs
        print("Enter a new supplier name:")
        supplierName = input()
        #Check if length is inbound of attribute
        while(len(supplierName) > 49 or len(supplierName) < 1):
            print("Sorry that is an invalid input. Please try again")
            supplierName = input()

        #Capturing input data
        print("Enter a phone number in a 10 digit format. Ex: 1234567890")
        phoneNumber = input()
        while(phoneNumber.isnumeric() == False or len(phoneNumber) != 10):
            print("That is not a valid phone number. Please try again")
            phoneNumber = input()
        
        print("Enter an email:")
        email = input()
        regex = r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b'
        validEmail = False
        while validEmail == False:
            if(re.fullmatch(regex, email)):
                validEmail = True
            else:
                print("That is not a valid email. Please try again")
                email = input()
        
        #Find maximum ID and insert data
        query = ('SELECT MAX(ID) AS id FROM SUPPLIER')
        cursor.execute(query)
        supplierId = 0
        for id in cursor:
            supplierId = id[0] + 1
        query = ('INSERT INTO SUPPLIER VALUES(' + str(supplierId) + ', \'' + supplierName + '\', \'' 
        + phoneNumber + '\', \'' + email + '\')')
        cursor.execute(query)

        print('\n******************************************************************************\n          '
        + 'A new supplier has been successfully created!'
        + '\n******************************************************************************\n')

        #Find and insert tea data
        isOpen = True
        teaAdded = False

        while isOpen == True:
            print('Please enter the name of a tea to insert or type quit to exit:')
            teaName = input()
            if(teaName == 'quit'):
                isOpen = False
                if teaAdded == False:
                    query = deleteQuery(str(supplierId),'SUPPLIER')
                    cursor.execute(query)
                break
            while(len(teaName) == 0 or len(teaName) > 49):
                print('Invalid input. Please try again.')
                teaName = input()
            
            print('Please enter the quantity of the tea')
            quantity = input()
            while(quantity.isnumeric() == False or int(quantity) < 1):
                print('Invalid input. Please try again')
                quantity = input()

            print('Please enter the price of the tea:')
            price = input()
            success = False
            while success == False:
                isFloat = False
                while isFloat == False:
                    try:
                        price = float(price)
                        isFloat = True
                    except ValueError:
                        print('Not a valid input. Please try again')
                        price = input()
                success = True
                if price < 0:
                    print('Not a valid value. Please try again')
                    price = input()
                    success = False

            query = ('SELECT MAX(ID) AS id FROM ITEM')
            cursor.execute(query)
            itemId = 0
            for id in cursor:
                itemId = id[0] + 1
            formattedPrice = "{:.2f}".format(float(price))
            query = ('INSERT INTO ITEM VALUES(' + str(itemId) + ', \'' + teaName + '\', ' + str(supplierId)
            + ', ' + str(quantity) + ', ' + str(price) + ')')
            cursor.execute(query)
            teaAdded = True
            

    if(menu == 3):
        print("Option 3")
    
    if(menu == 4):
        #Choosing supplier in case of duplicate item
        print('Please select the supplier ID for the item you want to update\n-----------------------------------------------------------------')
        query = ('SELECT ID, NAME FROM SUPPLIER')
        cursor.execute(query)
        idArray = []
        nameArray = []
        for capture in cursor:
            idArray.append(capture[0])
            nameArray.append(capture[1])

        for x in range(len(idArray)):
            print('ID) ' + str(idArray[x]) + ', Supplier Name) ' + str(nameArray[x]) + '\n')

        supplierId = input()
        found = False
        while found == False:
            for x in range(len(idArray)):
                if str(idArray[x]) == supplierId:
                    found = True 

            if found:
                query = ('SELECT i.NAME FROM ITEM i, SUPPLIER s WHERE s.ID = i.SUPPLIER_ID AND s.ID = ' + supplierId)
                cursor.execute(query)
                itemArray = []
                for capture in cursor:
                    itemArray.append(capture[0])
                for x in range(len(itemArray)):
                    print('Item ' + str(x + 1) + ') ' + str(itemArray[x]))
                print('Please enter the name of an item to update: ')

            else:
                print('Supplier not found. Please try again')
                supplierId = input()
        #Updating item
        teaName = input()
        while(len(teaName) < 1 or len(teaName) > 49):
            print('Invalid input, please try again.')
            teaName = input()
        query = ('SELECT i.QUANTITY AS q, s.NAME AS name FROM ITEM i, SUPPLIER s WHERE s.ID = ' + supplierId + ' AND i.NAME = \'' + teaName + '\'')
        cursor.execute(query)
        quantity = -1
        for q in cursor:
            quantity = q[0]

        if quantity == -1:
            print('Tea was not found. Returning...')
        else:
            print('\nItem successfully found!\n------------------------')
            print('Item: ' + teaName + '\nQuantity: ' + str(quantity))
            

print("Exiting database...")
cnx.close()