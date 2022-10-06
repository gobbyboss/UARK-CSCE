//Robert Goss Information Retrieval Hw2 Part 2
//10/6/2022

/**
 * Filename: Hashtable.java
 * Author: Susan Gauch, converted to java by Matt Miller, debugged by Patrick Anderson.
 */

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;

public class Hashtable 
{
    private int size;
    private long used;
    private long collision;
    private long lookups;
    private Node[] hashtable;
    
    /**
     * Initializes a hashtable with size 3 times the size given.
     * @param size One third of the hashtable size;
     */
    public Hashtable(int size)
    {
        this.size=size*3;
        init();
    }
    
    /**
     * Copies a hashtable
     * @param ht
     */
    public Hashtable(Hashtable ht)
    {
        this.size=ht.getSize();
        used=ht.getUsed();
        collision=ht.getCollisions();
        lookups=ht.getLookups();
        hashtable=new Node[this.size];
        
        for(int i=0;i<this.size;i++)
            hashtable[i]=new Node(ht.getNode(i).getKey(),ht.getNode(i).getFreq());
    }
    
    /**
     * Method to be called by constructors.
     * Also an easy way to reset a already made hashtable.
     * Requires that size already be set.
     */
    public void init()
    {
        used=0;
        collision=0;
        lookups=0;
        hashtable=new Node[this.size];
		
		//initialize the hashtable
        for(int i=0;i<this.size;i++)
            hashtable[i]=new Node("",0);
    }
    
    /**
     * Prints the non empty contents of the hashtable to the file given.
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
                if(!hashtable[i].getKey().equals("")) //if key is not empty string
                    out.write(hashtable[i].getKey()+" "+hashtable[i].getFreq()+"\n");
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
     * Prints the non empty contents of the hashtable to an already opened file.
     * @param filename String
     */
    public void print(BufferedWriter out)
    {
        try
        {
            for(int i=0;i<size;i++)
            {
                if(!hashtable[i].getKey().equals("")) //if key is not empty string
                    out.write(hashtable[i].getKey()+" "+hashtable[i].getFreq()+"\n");
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
     * Prints the non empty contents of the hashtable to stdout
     * @param FileWriter fwriter
     */
    public void print(PrintStream out)
    {
        for(int i=0;i<size;i++)
        {
            if(!hashtable[i].getKey().equals("")) //if key is not empty string
                System.out.print(hashtable[i].getKey()+" "+hashtable[i].getFreq()+"\n");
        }
    }
    
    /**
     * Insert string key, and string freq into hashtable, hashes on key.
     * @param key String to be hashed.
     * @param freq String
     */
    public void insert(String key)
    {
        int index = find(key);
        
		//if not already in the table, insert it
        if(hashtable[index].getFreq() == 0)
        {
            hashtable[index].setKey(key);
            used++;
        }
        //increment the frequency
		int temp = hashtable[index].getFreq() + 1;
        hashtable[index].setFreq(temp);
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
        while(!hashtable[index2].getKey().equals(str) && !hashtable[index2].getKey().equals(""))
        {
            index2++;
            collision++;
            if(index2 >= size)
                index2 = 0;
        }
        
        return index2;
    }
    
    /**
     * Returns the freq at the hashed location of key.
     * @param key String to be hashed.
     * @return freq in the table at the location of key.
     */
    public int getFreq(String key)
    {
        int index=find(key);
        lookups++;
        return hashtable[index].getFreq();
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

    public String getKey(int index)
    {
        return hashtable[index].getKey();
    }

    public int getFreq(int index)
    {
        return hashtable[index].getFreq();
    }
    
    /**
     * Returns Node at location index.
     * @param index location in hashtable
     * @return Node at location index.
     */
    private Node getNode(int index)
    {
        return hashtable[index];
    }
    
    
    /**
     * Private class node to whole the actual data stored in the hashtable.
     * Provides standard accessor and mutator methods.
     */
    private class Node
    {
        private String key;
        private int freq;
        
        public Node(String key,int freq)
        {
            this.key=key;
            this.freq=freq;
        }
        
        public String getKey()
        {
            return key;
        }
        
        public int getFreq()
        {
            return freq;
        }
        
        public void setKey(String key)
        {
            this.key=key;
        }
        
        public void setFreq(int freq)
        {
            this.freq=freq;
        }
    }
}

