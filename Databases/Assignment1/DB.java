import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DB {
  public static final int NUM_RECORDS = 10;
  public static final int RECORD_SIZE = 71;

  private RandomAccessFile config;
  private RandomAccessFile data;
  private RandomAccessFile overflow;
  private int num_records;
  private Boolean isOpen;

  public DB() {
    this.config = null;
    this.data = null;
    this.overflow = null;
    this.num_records = 0;
    this.isOpen = false;
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
      if(!this.isOpen)
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
        System.out.println("A database is already open.");
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

  /**
   * Get record number n (Records numbered from 0 to NUM_RECORDS-1)
   * 
   * @param record_num
   * @return values of the fields with the name of the field and
   *         the values read from the record
   */
  // public Record readRecord(int record_num) {
  //   Record record = new Record();
  //   String[] fields;

  //   if ((record_num >= 0) && (record_num < this.num_records)) {
  //     try {
  //       Din.seek(0); // return to the top of the file
  //       Din.skipBytes(record_num * RECORD_SIZE);
  //       // parse record and update fields
  //       fields = Din.readLine().split("\\s+", 0);
  //       record.updateFields(fields);
  //     } catch (IOException e) {
  //       System.out.println("There was an error while attempting to read a record from teh database file.\n");
  //       e.printStackTrace();
  //     }
  //   }

  //   return record;
  // }

  public Boolean writeRecord(long fileptr, int recordNum, int id, String state, String city, String name)
  {
    if(this.isOpen = true)
    {
      byte[] bytesToWrite = new byte[RECORD_SIZE];
      String recordString = fileptr + "," + recordNum + "," + id + "," + state + "," + city + "," + name + "\n"; 
      bytesToWrite = recordString.getBytes();
      try{
        // this.data.set
        this.data.write(bytesToWrite);
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
//   public int binarySearch(String id) {
//     int Low = 0;
//     int High = NUM_RECORDS - 1;
//     int Middle = 0;
//     boolean Found = false;
//     Record record;

//     while (!Found && (High >= Low)) {
//       Middle = (Low + High) / 2;
//       record = readRecord(Middle);
//       String MiddleId = record.Id;

//       // int result = MiddleId[0].compareTo(id); // DOES STRING COMPARE
//       int result = Integer.parseInt(MiddleId) - Integer.parseInt(id); // DOES INT COMPARE of MiddleId[0] and id
//       if (result == 0)
//         Found = true;
//       else if (result < 0)
//         Low = Middle + 1;
//       else
//         High = Middle - 1;
//     }
//     if (Found) {
//       return Middle; // the record number of the record
//     } else
//       return -1;
//   }
  }
