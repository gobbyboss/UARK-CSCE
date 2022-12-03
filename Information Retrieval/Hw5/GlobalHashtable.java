//Robert Goss Information Retrieval Hw4
//11/08/2022
import java.io.*;

public class GlobalHashtable {
    private int size;
    private long used;
    private long collision;
    private long lookups;
    private Node[] GlobalHashtable;
    
    /**
     * Initializes a GlobalHashtable with size 3 times the size given.
     * @param size One third of the GlobalHashtable size;
     */
    public GlobalHashtable(int size)
    {
        this.size=size*3;
        init();
    }
    

    /**
     * Method to be called by constructors.
     * Also an easy way to reset a already made GlobalHashtable.
     * Requires that size already be set.
     */
    public void init()
    {
        used=0;
        collision=0;
        lookups=0;
        GlobalHashtable=new Node[this.size];
		
		//initialize the GlobalHashtable
        for(int i=0;i<this.size;i++)
            GlobalHashtable[i]=new Node("",0,null,null);
    }
    
    /**
     * Prints the non empty contents of the GlobalHashtable to the file given.
     * @param filename String
     */
    public void print(String filename)
    {
        try
        {
            FileWriter fwriter=new FileWriter(filename);
            BufferedWriter out=new BufferedWriter(fwriter);
            
            for(int i=0;i<size;i++)
            {
                if(!GlobalHashtable[i].getKey().equals("")) //if key is not empty string
                    out.write(GlobalHashtable[i].getKey()+" "+GlobalHashtable[i].getNumDocs()+"\n");
            }
            out.close();
        }
        catch(IOException e)
        {
            System.err.println("IOEror: "+e.getMessage());
        } 
        //System.out.println("Collisions: "+collision+" Used: "+used+" Lookups: "+lookups);
    }

    /**
     * Prints the non empty contents of the GlobalHashtable to an already opened file.
     * @param filename String
     */
    public void print(BufferedWriter out)
    {
        try
        {
            for(int i=0;i<size;i++)
            {
                if(!GlobalHashtable[i].getKey().equals("")) //if key is not empty string
                    out.write(GlobalHashtable[i].getKey()+" "+GlobalHashtable[i].getNumDocs()+"\n");
            }
            out.close();
        }
        catch(IOException e)
        {
            System.err.println("IOEror: "+e.getMessage());
        } 
        //System.out.println("Collisions: "+collision+" Used: "+used+" Lookups: "+lookups);
    }

    /**
     * Prints the non empty contents of the GlobalHashtable to stdout
     * @param FileWriter fwriter
     */
    public void print(PrintStream out)
    {
        for(int i=0;i<size;i++)
        {
            if(!GlobalHashtable[i].getKey().equals("")) //if key is not empty string
            {
                DataNode head = GlobalHashtable[i].getHead();
                System.out.print(GlobalHashtable[i].getKey()+" NumDocs: "+GlobalHashtable[i].getNumDocs() + " ID: " + head.getDocId() + " Freq: " + head.getRtf() + " || ");
                while(head.next != null)
                {
                    System.out.print("ID: " + head.getDocId() + " Freq: " + head.getRtf() + " || ");
                    head = head.next;
                }
                System.out.print("\n");
            }
        }
    }
    
    /**
     * Insert string key, and int numDocs into GlobalHashtable, hashes on key.
     * @param key String to be hashed.
     * @param numDocs int
     */
    public void insert(String key, int docId, double rtf)
    {
        int index = find(key);
        DataNode newNode = new DataNode(docId, rtf);
		//if not already in the table, insert it
        if(GlobalHashtable[index].getNumDocs() == 0)
        {
            GlobalHashtable[index].setKey(key);
            GlobalHashtable[index].setHead(newNode);
            GlobalHashtable[index].setTail(newNode);
            used++;
        }
        else
        {
            DataNode tail = GlobalHashtable[index].getTail();
            tail.next = newNode;
            GlobalHashtable[index].setTail(newNode);
        }
        //increment the numDocs
		int temp = GlobalHashtable[index].getNumDocs() + 1;
        GlobalHashtable[index].setNumDocs(temp);
    }
    
    /**
     * Returns the index of the word in the table, or the index of a free space
     * in the table.
     * @param str String to hash.
     * @return index of the word, or of free space in which to place the word.
     */
    public int find(String str)
    {
        long sum=0;
        long index;
        
			//add all the characters of the string together
        for(int i=0;i<str.length();i++)
            sum=(sum*19)+str.charAt(i); //multiply sum by 19 and add byte value of char
        
		if(sum < 0)				// If calculation of sum was negative, make it positive
			sum = sum * -1;
		
        index= sum%size;
        int index2 = (int) index;
        
        /*
         * check to see if the word is in that location
         * if not there, do linear probing until word is found\
         * or empty location found
         */
        while(!GlobalHashtable[index2].getKey().equals(str) && !GlobalHashtable[index2].getKey().equals(""))
        {
            index2++;
            collision++;
            if(index2 >= size)
                index2 = 0;
        }
        
        return index2;
    }

    public void processGlobalHash(File outputDir, int totalDocs)
    {
        try{
            //Create streams for dict and post files
            String postFile = "post.html";
            PrintStream postStream = new PrintStream(new File(outputDir, postFile));

            String dictFile = "dict.html";
            PrintStream dictStream = new PrintStream(new File(outputDir, dictFile));
            
            //Counts line numbers in Post file
            int postLines = 0;
            //Iterate through entire hash
            for(int i = 0; i < size; i++)
            {
                boolean lowFreq = false;
                if(GlobalHashtable[i].getNumDocs() == 1 && GlobalHashtable[i].getHead().getNext() == null)
                {
                    lowFreq = true;
                }
                if(!GlobalHashtable[i].getKey().equals("") && !lowFreq) 
                {
                    //Write to dict/post if data
                    System.setOut(dictStream);
                    System.out.print(createDictString(GlobalHashtable[i].getKey(), GlobalHashtable[i].getNumDocs(), postLines));
                    
                    System.setOut(postStream);
                    DataNode head = GlobalHashtable[i].getHead();

                    double idf = Math.log10(totalDocs/GlobalHashtable[i].getNumDocs()) + 1; 
                    
                    while(head != null)
                    {
                        double weighted = Math.round(head.getRtf() * idf * 10000000) / 10.0;
                        System.out.print(createPostString(head.getDocId(), weighted));
                        head = head.next;
                        postLines++;
                    }
                }
                else
                {
                    //Write only to dict if empty and not a 1 term in 1 document frequency entry
                    System.setOut(dictStream);
                    System.out.print("-1 -1 -1                        \n");
                }
            }
        }
        catch(IOException e)
        {
            System.err.println("Could not find output directory");
        }
    }

    public String createDictString(String term, int numDocs, int postLine)
    {
        int recordSize = 33;
        int termSize = 20;
        //Maximum term length is 20 characters, cuts the string if it exceeds that
        if(term.length() > termSize - 1)
        {
            term = term.substring(0, termSize);
        }
        //Create dict record string and format it
        String record = term + " " + numDocs + " " + postLine;
        record = fillSpace(record.getBytes(), recordSize - 1) + "\n";
        return record;
    }

    public String createPostString(int docId, double weight)
    {
        int recordSize = 18;
        String record = docId + " " + weight;
        record = fillSpace(record.getBytes(), recordSize - 1) + "\n";
        return record;
    }

    public String createMapString(String filename)
    {
        int recordSize = 15;
        String record = fillSpace(filename.getBytes(), recordSize - 1) + "\n";
        return record;
    }
    //Fills the end of a given byte array to the length of recordSize. Does not append or account for a null terminator
    private String fillSpace(byte[] record, int recordSize)
    {
        byte[] newRecord = new byte[recordSize];
        for(int i = 0; i < recordSize; i++)
        {
            if(i < record.length)
            {
                newRecord[i] = record[i];
            }
            else
            {
                newRecord[i] = ' ';
            }
        }
        return new String(newRecord);
    }
    
    /**
     * Returns the numDocs at the hashed location of key.
     * @param key String to be hashed.
     * @return numDocs in the table at the location of key.
     */
    public int getNumDocs(String key)
    {
        int index=find(key);
        lookups++;
        return GlobalHashtable[index].getNumDocs();
    }

    public DataNode getHead(String key)
    {
        int index=find(key);
        lookups++;
        return GlobalHashtable[index].getHead();
    }
    
    /**
     * Get the three statistics as a string.  Used, Collisions, and Lookups.
     * @return Used, Collisions, and Lookups as a string.
     */
    public String getUsage()
    {
        return "Used: "+used+" Collisions: "+collision+" Lookups: "+lookups;
    }
    
    /**
     * Get the amount in the table.
     * @return How full the table is. long
     */
    public long getUsed()
    {
        return used;
    }
    
    /**
     * Get the number of collisions.
     * @return How much you need to improve your hash function. long
     */
    public long getCollisions()
    {
        return collision;
    }
    
    /**
     * The number of lookups made.
     * @return long
     */
    public long getLookups()
    {
        return lookups;
    }
    
    /**
     * Gets the size of the array.
     * @return size, long
     */
    public int getSize()
    {
        return size;
    }
    

    
    
    /**
     * Private class node to whole the actual data stored in the GlobalHashtable.
     * Provides standard accessor and mutator methods.
     */
    private class Node
    {
        private String key;
        private int numDocs;
        private DataNode head, tail;
        
        public Node(String key,int numDocs, DataNode head, DataNode tail)
        {
            this.key=key;
            this.numDocs=numDocs;
            this.head=head;
            this.tail=tail;
        }
        
        public String getKey()
        {
            return key;
        }
        
        public int getNumDocs()
        {
            return numDocs;
        }

        public DataNode getHead()
        {
            return head;
        }

        public DataNode getTail()
        {
            return tail;
        }
        
        public void setKey(String key)
        {
            this.key=key;
        }
        
        public void setNumDocs(int numDocs)
        {
            this.numDocs=numDocs;
        }

        public void setHead(DataNode head)
        {
            this.head=head;
        }

        public void setTail(DataNode tail)
        {
            this.tail=tail;
        }
    }

    public class DataNode{
        int docId;
        double rtf;
        DataNode next;

        public DataNode(int docId, double rtf)
        {
            this.docId=docId;
            this.rtf=rtf;
            this.next=null;
        }

        public int getDocId()
        {
            return docId;
        }

        public double getRtf()
        {
            return rtf;
        }

        public DataNode getNext()
        {
            return next;
        }
    }
}
