import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;


import java.io.File;

public class DB {
  public static final int NUM_RECORDS = 1908;
  public static final int RECORD_SIZE = 110;
  public int tempRecordNum, tempId;
  public String tempState, tempCity, tempName;
  public Scanner inputScanner;


  private RandomAccessFile config, data, overflow;
  private int num_records, num_overflow;
  private Boolean isOpen, isOverflow;
  private String dataFile, configFile, overflowFile;



  public DB() {
    this.config = null;
    this.data = null;
    this.overflow = null;
    this.num_records = 0;
    this.num_overflow = 0;
    this.isOpen = false;
    this.isOverflow = false;
    this.tempRecordNum = -1;
    this.tempId = 0;
    this.tempState = null;
    this.tempCity = null;
    this.tempName = null;
    this.dataFile = null;
    this.configFile = null;
    this.overflowFile = null;
    inputScanner = new Scanner(System.in);
  }


  /**
   * Opens the file in read/write mode
   * 
   * @param filename (e.g., input.txt)
   * @return status true if operation successful
   */
  public boolean open(String filename, Boolean isConfigGenerated) {
    // Set the number of records
    this.num_records = NUM_RECORDS;
    // Open file in read/write mode
    try {
      String fileToCheck = filename + ".data";
      File checkedFile = new File(fileToCheck);
    
      boolean fileExists = checkedFile.exists();
      if(!this.isOpen && fileExists)
      {
        this.dataFile = filename + ".data";
        this.configFile = filename + ".config";
        this.overflowFile = filename + ".overflow";
        this.config = new RandomAccessFile(configFile, "rw");
        this.data = new RandomAccessFile(dataFile, "rw");
        this.overflow = new RandomAccessFile(overflowFile, "rw");
        this.isOpen = true;
        try{
         if(isConfigGenerated)
         {
          this.config.seek(0);
          String[] overflowString = this.config.readLine().split("=");
          this.num_overflow = Integer.parseInt(overflowString[1]);
         }
         else
         {
           this.num_overflow = 0;
         }
        }
        catch(IOException e)
        {
          System.out.println(e.toString());
          return false;
        }
        
        return true;
      }
      else
      {
        if(!fileExists)
        {
          System.out.println("Sorry, that database was not found. Please try a new prefix");
        }
        else
        {
          System.out.println("A database is already open.");
        }
        return false;
      }
    } catch (FileNotFoundException e) {
      System.out.println("Could not open file\n");
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Close the database file
   */
  public void close() {
    try {
   
      if(this.isOpen == true)
      {
        num_records = NUM_RECORDS - 1;
        String numRecordsString = "num_records=" + num_records;
        String overflowString = "num_overflow=" + this.num_overflow + "\n";
        byte[] bytesToWrite = overflowString.getBytes();
        config.seek(0);
        config.write(bytesToWrite);
        bytesToWrite = numRecordsString.getBytes();
        config.write(bytesToWrite);
        this.config.close();
        this.data.close();
        this.overflow.close();
      }
      this.isOpen = false;
      this.num_records = 0;
      this.num_overflow = 0;
      this.dataFile = null;
      this.configFile = null;
      this.overflowFile = null;
    } catch (IOException e) {
      System.out.println("There was an error while attempting to close the database file.\n");
      e.printStackTrace();
    }
  }

  
  //Returns the status of the database.
  public Boolean isOpen()
  {
    return this.isOpen; 
  }
 

  /*
  Fills the temporary variables with values read in from the record at the given record number.
  Could read in the fields from the .config file instead of hardcoding, but instead decided on the faster route
  for this project.
  */
  public boolean readRecord(int record_num, String filename) {
    
    String[] fields;

    if ((record_num >= 0) && (record_num < this.num_records)) {
      try {
        RandomAccessFile file = new RandomAccessFile(filename, "rw");
        file.seek(0); // return to the top of the file
        file.skipBytes(record_num * RECORD_SIZE);
        // parse record and update fields
        fields = file.readLine().split(",",6); 
        this.tempRecordNum = Integer.parseInt(fields[0]);
        this.tempId = Integer.parseInt(fields[1]);
        this.tempState = fields[2];
        this.tempCity = fields[3];
        this.tempName = fields[4];
        file.close();
      } catch (IOException e) {
        System.out.println("There was an error while attempting to read a record from the database file.\n");
        e.printStackTrace();
        return false;
      }
    }

    return true;
  }
  

  //Returns the name of the datafile open;
  public String getDataFile()
  {
    return this.dataFile;
  }

  //Writes a record to the end of the data file. Used for creating the DB
  public Boolean writeRecord(int recordNum, int id, String state, String city, String name)
  {
    if(this.isOpen = true)
    {
      byte[] bytesToWrite = new byte[RECORD_SIZE - 1];
      String recordString = recordNum + "," + id + "," + state + "," + city + "," + name + ","; 
      try{
        byte[] recordBytes = recordString.getBytes("UTF8");
        for(int i = 0; i < RECORD_SIZE; i++)
        {
          if(i < recordBytes.length)
          {
            bytesToWrite[i] = recordBytes[i];
          }
          if(i >= recordBytes.length && i < RECORD_SIZE - 1)
          {
            bytesToWrite[i] = ' ';
          }
        }
        String newline = "\n";
        byte[] newlineBytes = newline.getBytes();
        this.data.write(bytesToWrite);
        this.data.write(newlineBytes);
      }
      catch(IOException e)
      {
        System.out.print(e.toString());
      }
      return true;
    }
    else
    {
      return false;
    }
  }

  //Calls binary search on the data file, and if not found sequentially looks through the overflow file
  public Boolean findRecord(int id)
  {
    int recordId = binarySearch(id, this.dataFile);
    this.isOverflow = false;
    if(recordId == -1)
    {
      boolean found = false;
   
      if(this.num_overflow != 0)
      {
        for(int i = 0; i < this.num_overflow; i++)
        {
          readRecord(i, overflowFile);
          int idFound = id - this.tempId;
          if(idFound == 0)
          {
            recordId = i;
            this.isOverflow = true;
            found = true;
            break;
          }
        }
        if(!found)
        {
          System.out.println("\nRecord was not found.\n");
          return false;
        }
      }
      else
      {
        System.out.println("\nRecord was not found.\n");
        return false;
      }
    }
    String fileToRead;
    if(this.isOverflow == true)
    {
      fileToRead = this.overflowFile;
      System.out.print("Successfully read file!");
    }
    else
    {
      fileToRead = this.dataFile;
    }
    boolean recordRead = readRecord(recordId, fileToRead);
    if(recordRead && !this.isOverflow)
    {
      int printRecordId = recordId + 1; //Printing records starting at #1
      System.out.println("\nData for Record #" + printRecordId + "\n-------------------\n" +
      "ID: " + this.tempId + "\nState: " + this.tempState + "\nCity: " + this.tempCity + "\nName: " + this.tempName + "\n");
      return true;
    }
    else if(recordRead && this.isOverflow)
    {
      int printOverflowId = recordId + NUM_RECORDS + 1;
      System.out.println("\nData for Record #" + printOverflowId + " found in the overflow file.\n-------------------\n" +
      "ID: " + this.tempId + "\nState: " + this.tempState + "\nCity: " + this.tempCity + "\nName: " + this.tempName + "\n");
      return true;
    }
    else
    {
      System.out.println("Record could not be found.\n");
      return false;
    }
  }

  //Prompts the user for the ID of a record to search, and what field they want updated. Then calls overwriteRecord
  public boolean updateRecord()
  {
    if(!this.isOpen)
    {
      System.out.println("\nA database is not open");
      return false;
    }
    System.out.print("Please input the ID of the record you would like to update: ");
    String updateInput = inputScanner.nextLine();
    int updateId = 0;
    try{
      updateId = Integer.parseInt(updateInput);
    }catch(NumberFormatException e){
      System.out.println("Sorry that is not a valid input\n");
      return false;
    }
    boolean recordFound = findRecord(updateId);
    if(!recordFound)
    {
      return false;
    }
    System.out.print("Update options\n---------------\n1) State \n2) City \n3) Name \n4) Exit\n\nEnter the integer"
    + " of the field you want to update: ");
    updateInput = inputScanner.nextLine();
    try{
      updateId = Integer.parseInt(updateInput);
    }catch(NumberFormatException e){
      System.out.println("Sorry that is not a valid input\n");
      return false;
    }
    switch(updateId)
    {
      case 1:
        System.out.print("\n\"" + this.tempState + "\" will be updated. Please enter the new State here: ");
        updateInput = inputScanner.nextLine();
        overwriteRecord(updateInput, this.tempCity, this.tempName);
        break;
      case 2:
        System.out.print("\n\"" + this.tempCity + "\" will be updated. Please enter the new City here: ");
        updateInput = inputScanner.nextLine();
        overwriteRecord(this.tempState, updateInput, this.tempName);
        break;
      case 3:
        System.out.print("\n\"" + this.tempName + "\" will be updated. Please enter the new name here: ");
        updateInput = inputScanner.nextLine();
        overwriteRecord(this.tempState, this.tempCity, updateInput);
        break;
      case 4:
        System.out.println("No fields were updated.");
        return false;
      default:
        System.out.println("That input is not valid.");
        return false;
    }
    return true;
  }

  //Appends a record to the end of the overflow file.
  public Boolean addRecord(int id, String state, String city, String name)
  {
    if(!this.isOpen)
    {
      return false;
    }
    try{
      overflow.seek(num_overflow * RECORD_SIZE);
    }
    catch(IOException e)
    {
      System.out.println(e.toString());
      return false;
    }
    byte[] bytesToWrite = new byte[RECORD_SIZE - 1];
    int overflowNum = this.num_overflow + 1;
    String recordString = overflowNum + "," + id + "," + state + "," + city + "," + name + ","; 
    try{
      byte[] recordBytes = recordString.getBytes("UTF8");
      for(int i = 0; i < RECORD_SIZE; i++)
      {
        if(i < recordBytes.length)
        {
          bytesToWrite[i] = recordBytes[i];
        }
        if(i >= recordBytes.length && i < RECORD_SIZE - 1)
        {
          bytesToWrite[i] = ' ';
        }
      }
      String newline = "\n";
      byte[] newlineBytes = newline.getBytes();
      overflow.write(bytesToWrite);
      overflow.write(newlineBytes);
      this.num_overflow++;
    }
    catch(IOException e)
    {
      System.out.print(e.toString());
    }
    return true;
  }

  //Checks if its overwriting overflow or data file, then writes a new record at the old record location
  public Boolean overwriteRecord(String state, String city, String name)
  {
    if(!this.isOpen)
    {
      return false;
    }
    RandomAccessFile file = data;
    if(this.isOverflow)
    {
      file = overflow;
    }
    try{
      file.seek((this.tempRecordNum - 1) * RECORD_SIZE);
    }catch(IOException e)
    {
      System.out.println(e.toString());
    }
    byte[] bytesToWrite = new byte[RECORD_SIZE - 1];
    String recordString = this.tempRecordNum + "," + this.tempId + "," + state + "," + city + "," + name + ","; 
    try{
      byte[] recordBytes = recordString.getBytes("UTF8");
      for(int i = 0; i < RECORD_SIZE; i++)
      {
        if(i < recordBytes.length)
        {
          bytesToWrite[i] = recordBytes[i];
        }
        if(i >= recordBytes.length && i < RECORD_SIZE - 1)
        {
          bytesToWrite[i] = ' ';
        }
      }
      String newline = "\n";
      byte[] newlineBytes = newline.getBytes();
      file.write(bytesToWrite);
      file.write(newlineBytes);
    }
    catch(IOException e)
    {
      System.out.print(e.toString());
    }
    return true;
  }

  //Finds a record by id then deletes it
  public Boolean deleteRecord(int id)
  {
    if(!this.isOpen)
    {
      System.out.println("A database is not open");
      return false;
    }
    Boolean foundRecordToDelete = findRecord(id);
    if(!foundRecordToDelete)
    {
      return false;
    }
    System.out.println("Please enter 'Y' or 'N' to confirm if this is the record you want to delete.");
    String deleteInput = inputScanner.nextLine();
    switch(deleteInput.toLowerCase())
    {
      case "y":
        boolean deletedSucces = overwriteRecord("", "", "");
        if(deletedSucces)
        {
          return true;
        }
        else
        {
          return false;
        }
      case "n":
        System.out.println("Returning to menu...");
        return false;
      default:
        System.out.println("Not a valid input.");
        return false;
    }

  }

  /**
   * Binary Search by record id
   * 
   * @param id
   * @return Record number (which can then be used by read to
   *         get the fields) or -1 if id not found
   */
  public int binarySearch(int id, String filename) {
    int Low = 0;
    int High = NUM_RECORDS - 1;
    int Middle = 0;
    boolean Found = false;
    

    while (!Found && (High >= Low)) {
      Middle = (Low + High) / 2;
      readRecord(Middle, filename);

      // int result = MiddleId[0].compareTo(id); // DOES STRING COMPARE
      int result = this.tempId - id; // DOES INT COMPARE of MiddleId[0] and id
      if (result == 0)
        Found = true;
      else if (result < 0)
        Low = Middle + 1;
      else
        High = Middle - 1;
    }
    if (Found) {
      return Middle; // the record number of the record
    } else
      return -1;
  }
}