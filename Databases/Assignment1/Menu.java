import java.util.Scanner;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
public class Menu {

  //Reads in one line at a time, returns an array of fields
  public static String[] readLine(String filename, int rowCount)
  {
    String[] recordData = new String[4];
    String delimiter = ",|\\n";
    try{
      Scanner rowScan = new Scanner(new File(filename));
      int comparator = 0;
      while(rowScan.hasNext() && comparator <= rowCount)
      {
        if(rowCount == comparator)
        {
          for(int i = 0; i < 4; i++)
          {
            rowScan.useDelimiter(delimiter);
            recordData[i] = rowScan.next();
          }
        }
        else
        {
          rowScan.next();
        }
        comparator++;
      }
      rowScan.close();
    }
    catch(FileNotFoundException e){
      System.out.println("File not found.");
    }
    return recordData;
  }

  //Prints out the options for the user to select and executes the function
  public static void menu()
  {
    DB b = new DB();
    
    System.out.println("Please select an operation.\n\n1) Create new database \n2) Open database \n3) Close database\n" +
    "4) Display record\n5) Update record\n6) Create report of first 10 records\n7) Add record\n8) Delete record\n9) Exit\n");
    System.out.print("Enter here: ");
    String input = b.inputScanner.nextLine();
    while(input != "9")
    {
      switch(input) 
      {
        //Gives the user the options
        case "0":
          System.out.println("Please select an operation.\n\n1) Create new database \n2) Open database \n3) Close database\n" +
          "4) Display record\n5) Update record\n6) Create report of first 10 records\n7) Add record\n8) Delete record\n9) Exit\n");
          break;
        //Creates the DB
        case "1":
          System.out.print("Please enter a .csv file to create a new database: ");
          String csvFile = b.inputScanner.nextLine();
          if(csvFile.length() < 4)
          {
            System.out.println("Sorry, that is an invalid file. Please try again.");
            break;
          }
          File checkedFile = new File(csvFile);
          boolean csvExists = checkedFile.exists();

          String rawFile = csvFile.substring(0, csvFile.length() - 4);
          String dataFile = rawFile + ".data";
          String configFile = rawFile + ".config";
          String overflowFile = rawFile + ".overflow";

          checkedFile = new File(dataFile);
          boolean databaseExists = checkedFile.exists();

          //Checks if the .csv is valid and if the database doesn't already exist. If that is true, generates 
          if(csvExists && !databaseExists)
          {
            try{
              //Generates files
              RandomAccessFile data = new RandomAccessFile(dataFile, "rw");
              RandomAccessFile config = new RandomAccessFile(configFile, "rw");
              RandomAccessFile overflow = new RandomAccessFile(overflowFile, "rw");
              b.open(rawFile);
              
              int rowCount = 0;
              int recordNum = 1;

              //Writes to the datafile
              for(int i = 0; i < DB.NUM_RECORDS; i++)
              {
                String[] fieldData = readLine(csvFile, rowCount);
                b.writeRecord(recordNum, Integer.parseInt(fieldData[0]), fieldData[1], fieldData[2], fieldData[3]);
                recordNum++;
                rowCount++;
              }

              //Writes metadata after generating .data file
              String numRecordsString = "num_records=" + --recordNum;
              String overflowString = "num_overflow=0\n";
              byte[] bytesToWrite = overflowString.getBytes();
              config.write(bytesToWrite);
              bytesToWrite = numRecordsString.getBytes();
              config.write(bytesToWrite);
              
              data.close();
              config.close();
              overflow.close();
              b.close();

              System.out.println("Database created successfully!\n");
            }
            catch(FileNotFoundException e){
              System.out.println("File not found.\n");
            }
            catch(IOException e)
            {
              System.out.println(e.toString());
            }
          }
          else 
          {
            if(!csvExists)
            {
              System.out.println("The .csv file was not found.\n");
            }
            if(databaseExists)
            {
             System.out.println("This database already exists.\n");
            }
          }
          break;
        //Opens the DB
        case "2":
          System.out.print("Please enter the prefix of the database to open: ");
          String filename = b.inputScanner.nextLine();
          boolean success = b.open(filename);
          if(success) System.out.println("Database opened successfully!\n");
          break;
        //Closes the DB
        case "3":
          boolean isOpened = b.isOpen();
          if(!isOpened)
          {
            System.out.println("There are no active databases curerntly.\n");
            break;
          }
          b.close();
          System.out.println("Database closed successfully!\n");
          break;
        //Displays a record
        case "4":
          if(!b.isOpen())
          {
            System.out.println("A database is not open\n");
            break;
          }
          System.out.print("Please input the ID of the record you would like to display: ");
          String idInput = b.inputScanner.nextLine();
          int searchId = 0;
          try{
            searchId = Integer.parseInt(idInput);
          }catch(NumberFormatException e){
            System.out.println("Sorry that is not a valid input\n");
            break;
          }
          b.findRecord(searchId);
          break;
        //Updates a record
        case "5":
          boolean updateSuccess = b.updateRecord();
          if(updateSuccess)
          {
            System.out.println("\nSuccessfully updated record!");
          } 
          else
          {
            System.out.println("Record did not update successfully.\n");
          }
          break;
        //Prints report
        case "6":
          break;
        //Adds record
        case "7":
          if(!b.isOpen())
          {
            System.out.println("A database isn't open. Please try again.");
            break;
          }
          System.out.print("To add a new record, enter your data for each field prompted.\nID: ");
          int addId;
          String inputId, addState, addCity, addName;
          
          inputId = b.inputScanner.nextLine();
          try{
            addId = Integer.parseInt(inputId);
          }catch(NumberFormatException e)
          {
            System.out.println("Not a valid input, please input an integer.");
            break;
          } 
          System.out.print("\nState: ");
          addState = b.inputScanner.nextLine();
          System.out.print("\nCity: ");
          addCity = b.inputScanner.nextLine();
          System.out.print("\nName: ");
          addName = b.inputScanner.nextLine();
          boolean addSuccess = b.addRecord(addId, addState, addCity, addName);
          if(!addSuccess)
          {
            System.out.println("Failed to add record");
            break;
          }
          System.out.println("Successfully added record!");
          break;
        //Deletes record
        case "8":
          break;
        //Exits
        case "9":
          System.out.println("Exiting now...");
          b.inputScanner.close();
          System.exit(0);
        default:
          System.out.println("That is not a valid input. Please enter an integer between 1-9");
      }
      System.out.print("Please input a new operation or 0 to see the options again: ");
      input = b.inputScanner.nextLine();
    }
  }

  //Runs the menu 
  public static void main(String[] args)
  {
    menu();
  }
}
  