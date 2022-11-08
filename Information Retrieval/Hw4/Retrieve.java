//Robert Goss
//IR Hw4 11/8/2022
import java.io.*;
import java.util.*;

public class Retrieve {

    ArrayList<String> query;
    int numDocs;
    int[] weight;

    //Constants for fixed length file + location of inverted file
    final int MAP_RECORD_SIZE = 15;
    final int DICT_RECORD_SIZE = 33;
    final int POST_RECORD_SIZE = 18;

    final String MAP_LOCATION = "out/map.html";
    final String DICT_LOCATION = "out/dict.html";
    final String POST_LOCATION = "out/post.html";
    
    public Retrieve()
    {
        this.query = new ArrayList<>();
        this.numDocs = getNumDocs();
        this.weight = new int[numDocs]; 
    }   

    //Uses the size of map.html and constant record sizes to calculate number of documents in the collection
    private int getNumDocs()
    {
        File map = new File(MAP_LOCATION);
        long size = map.length();
        return (int)size / MAP_RECORD_SIZE;
    }

    //Takes in args from the command line and processes it into an ArrayList of tokenized strings 
    public void processQuery(String[] args)
    {
        //Appends each arg from the command line to a String Builder, then converts it into a single String to be read by the lexer 
        StringBuilder querySB = new StringBuilder();
        for(int i = 0; i < args.length; i++)
        {
            querySB.append(args[i] + " ");
        }
        String queryString = querySB.toString();

        //Run the lexer using StringReader on the previously created String 
        try{
            Reader rdr = new StringReader(queryString);
            Lexer lexer = new Lexer(rdr);
            String token = "";
            String result = "";

            while(token != null)
            {
                result = lexer.yylex();
                if(result != null)
                {
                    this.query.add(result);
                }
                else
                {
                    token = result;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public void accumulateWeight()
    {

    }

    public int findDictLine()
    {
        return -1;
    }

    public int findPostLine()
    {
        return -1;
    }

    public void sortAccumulator()
    {
        
    }

    //Prints out for the top 10 ranked documents: id, name, and weight
    public void displayResults()
    {

    }

    public static void main(String[] args)
    {
      Retrieve retriever = new Retrieve();
      retriever.processQuery(args);
    }
}
