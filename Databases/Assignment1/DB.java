import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;

public class DB {
  public static final int NUM_RECORDS = 10;
  public static final int RECORD_SIZE = 71;

  private RandomAccessFile config, data, overflow;
  private int num_records, num_overflow;
  private Boolean isOpen;
  private int tempRecordNum, tempId;
  private String tempState, tempCity, tempName; 


  public DB() {
    this.config = null;
    this.data = null;
    this.overflow = null;
    this.num_records = 0;
    this.num_overflow = 0;
    this.isOpen = false;
    this.tempRecordNum = -1;
    this.tempId = 0;
    this.tempState = null;
    this.tempCity = null;
    this.tempName = null;
  }


  /**
   * Opens the file in read/write mode
   * 
   * @param filename (e.g., input.txt)
   * @return status true if operation successful
   */
  public boolean open(String filename) {
    // Set the number of records
    this.num_records = NUM_RECORDS;
    // Open file in read/write mode
    try {
      String fileToCheck = filename + ".csv";
      File checkedFile = new File(fileToCheck);
    
      boolean fileExists = checkedFile.exists();
      if(!this.isOpen && fileExists)
      {
        String dataFile = filename + ".data";
        String configFile = filename + ".config";
        String overflowFile = filename + ".overflow";
        this.config = new RandomAccessFile(configFile, "rw");
        this.data = new RandomAccessFile(dataFile, "rw");
        this.overflow = new RandomAccessFile(overflowFile, "rw");
        this.isOpen = true;
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
      this.config.close();
      this.data.close();
      this.overflow.close();
      this.isOpen = false;
      this.num_records = 0;
      this.num_overflow = 0;
      System.out.println("Database closed successfully!");
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
  public boolean readRecord(int record_num) {
    
    String[] fields;

    if ((record_num >= 0) && (record_num < this.num_records)) {
      try {
        data.seek(0); // return to the top of the file
        data.skipBytes(record_num * RECORD_SIZE);
        // parse record and update fields
        fields = data.readLine().split(",",5); 
        for(int i = 0; i < fields.length; i++)
        {
          System.out.println("Field " + i + ": " + fields[i]);
        }
        this.tempRecordNum = Integer.parseInt(fields[0]);
        this.tempId = Integer.parseInt(fields[1]);
        this.tempState = fields[2];
        this.tempCity = fields[3];
        this.tempName = fields[4];
      } catch (IOException e) {
        System.out.println("There was an error while attempting to read a record from the database file.\n");
        e.printStackTrace();
        return false;
      }
    }

    return true;
  }

  public Boolean writeRecord(int recordNum, int id, String state, String city, String name)
  {
    if(this.isOpen = true)
    {
      byte[] bytesToWrite = new byte[RECORD_SIZE - 1];
      String recordString = recordNum + "," + id + "," + state + "," + city + "," + name; 
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

  /**
   * Binary Search by record id
   * 
   * @param id
   * @return Record number (which can then be used by read to
   *         get the fields) or -1 if id not found
   */
  public int binarySearch(int id) {
    int Low = 0;
    int High = NUM_RECORDS - 1;
    int Middle = 0;
    boolean Found = false;
    

    while (!Found && (High >= Low)) {
      Middle = (Low + High) / 2;
      readRecord(Middle);

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
  
