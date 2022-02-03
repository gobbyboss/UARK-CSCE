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
    Scanner inputScanner = new Scanner(System.in);
    System.out.println("Please select an operation.\n1) Create new database \n2) Open database \n3) Close database\n9) Exit");
    System.out.print("Enter here: ");
    String input = inputScanner.nextLine();
    while(input != "9")
    {
      switch(input) 
      {
        case "0":
          System.out.println("Please select an operation.\n1) Create new database \n2) Open database \n3) Close database\n9) Exit");
          break;
        case "1":
          DB b = new DB();

          System.out.print("Please enter a .csv file to create a new database: ");
          String csvFile = inputScanner.nextLine();

          File checkedFile = new File(csvFile);
          boolean csvExists = checkedFile.exists();

          String rawFile = csvFile.substring(0, csvFile.length() - 4);
          String dataFile = rawFile + ".data";
          String configFile = rawFile + ".config";
          String overflowFile = rawFile + ".overflow";

          checkedFile = new File(dataFile);
          boolean databaseExists = checkedFile.exists();
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
              for(int i = 0; i < DB.NUM_RECORDS; i++)
              {
                String[] fieldData = readLine(csvFile, rowCount);
                
                try{
                  long fileptr = data.getFilePointer();
                  b.writeRecord(fileptr, recordNum, Integer.parseInt(fieldData[0]), fieldData[1], fieldData[2], fieldData[3]);
                  recordNum++;
                }
                catch(IOException e){
                  System.out.println(e.toString());
                }
  
                rowCount++;
              }
              
              data.close();
              config.close();
              overflow.close();
            }
            catch(FileNotFoundException e){
              System.out.println("File not found.");
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
              System.out.println("The .csv file was not found.");
            }
            if(databaseExists)
            {
             System.out.println("This database already exists.");
            }
          }
          break;
        case "2":
          break;
        case "3":
          break;
        case "9":
          System.out.println("Exiting now...");
          System.exit(0);
        default:
          System.out.println("That is not a valid input. Please enter an integer between 1-9");
      }
      System.out.print("Please input a new operation or 0 to see the options again: ");
      input = inputScanner.nextLine();
    }
    inputScanner.close();
  }

  //Runs the menu 
  public static void main(String[] args)
  {
    menu();
  }
}
  