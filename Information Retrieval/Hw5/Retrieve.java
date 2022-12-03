//Robert Goss
//IR Hw4 11/8/2022
import java.io.*;
import java.util.*;

public class Retrieve {

    ArrayList<String> query;
    int totalDocs, numDocs, postline, docId;
    double[] accumulator;
    HashSet<Integer> weightedIds;
    double weight;
    GlobalHashtable hash;

    final int MAP_RECORD_SIZE = 15;
    final int DICT_RECORD_SIZE = 33;
    final int POST_RECORD_SIZE = 18;

    String MAP_LOCATION;
    String DICT_LOCATION;
    String POST_LOCATION;
    
    public Retrieve(String args[])
    {
        this.query = new ArrayList<>();
        processQuery(args);
        this.totalDocs = getTotalDocs();
        this.accumulator = new double[this.totalDocs];
        this.hash = new GlobalHashtable(getDictRecordCount() / 3); 
        this.weightedIds = new HashSet<Integer>();
    }   

    //Takes in args from the command line and processes it into an ArrayList of tokenized strings.
    //The first argument takes in the directory to find the inverted file followed by any amount of query terms
    public void processQuery(String[] args)
    {
        this.MAP_LOCATION = args[0] + "/map.html";
        this.POST_LOCATION = args[0] + "/post.html";
        this.DICT_LOCATION = args[0] + "/dict.html";
        //Appends each arg from the command line to a String Builder, then converts it into a single String to be read by the lexer 
        StringBuilder querySB = new StringBuilder();
        for(int i = 1; i < args.length; i++)
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
    //Uses the size of map.html and constant record sizes to calculate number of documents in the collection
    private int getTotalDocs()
    {
        File map = new File(this.MAP_LOCATION);
        long size = map.length();
        return (int)size / MAP_RECORD_SIZE;
    }

    private int getDictRecordCount()
    {
        File dict = new File(this.DICT_LOCATION);
        long size = dict.length();
        return (int)size / DICT_RECORD_SIZE;
    }


    //Accumlates weight for each doc using the query terms
    public void accumulateWeight()
    {
        Iterator<String> iterator = this.query.iterator();
        while(iterator.hasNext())
        {
            String dictTerm = iterator.next();
            String dictRecord = findDictLine(dictTerm);
            if(dictRecord != "-1")
            {
                //Go through postings and add each weight to accumulator and track unique ids with a HashSet
                setDictValues(dictRecord);
                for(int i = 0; i < this.numDocs; i++)
                {
                    readPostLine();
                    this.accumulator[this.docId] += this.weight;
                    this.weightedIds.add(this.docId);
                }
            }
        }
    }

    //Seeks to the start of the line that the hash function predicts the key will.
    //Then iterates through the dict file until the key or an empty record is found.
    //Returns the entire Dict record string if found or the String "-1" otherwise
    public String findDictLine(String key)
    {
        try{
            RandomAccessFile dictFile = new RandomAccessFile(this.DICT_LOCATION, "r");
            int index = this.hash.find(key);
            dictFile.seek(DICT_RECORD_SIZE * index);
            String dictRecord = dictFile.readLine();
            String dictTerm = getDictTerm(dictRecord);

            while(!dictTerm.equals(key) && !dictRecord.contains("-1"))
            {
                index++;
                dictFile.seek(DICT_RECORD_SIZE * index);
                dictRecord = dictFile.readLine();
                dictTerm = getDictTerm(dictRecord);
            }
            dictFile.close();
            if(dictRecord.contains("-1"))
            {
                return "-1";
            }
            return dictRecord;
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return "-1";
        }
    }
    //Returns the Term from a Dict record as a String
    private String getDictTerm(String dictRecord)
    {
        int index = 0;
        while(dictRecord.charAt(index) != ' ')
        {
            index++;
        }
        return dictRecord.substring(0,index).trim();
    }

    //Sets the numDocs and postline member variables from the dict record given. Member variables are used for reading the postings file in accumulateWeight()
    private void setDictValues(String dictRecord)
    {
        int firstIndex = dictRecord.indexOf(" ") + 1;
        int secondIndex = dictRecord.indexOf(" ", firstIndex);
        this.numDocs = Integer.valueOf(dictRecord.substring(firstIndex, secondIndex));

        secondIndex++;
        int thirdIndex = dictRecord.indexOf(" ", secondIndex);
        this.postline = Integer.valueOf(dictRecord.substring(secondIndex, thirdIndex));
    }

    private void readPostLine()
    {
        try{
            //Opens up the post file, and reads the line at the current postline value
            RandomAccessFile postFile = new RandomAccessFile(POST_LOCATION, "r");
            postFile.seek(this.postline * POST_RECORD_SIZE);
            String postRecord = postFile.readLine();

            //Find the docId and weight and set the member variables to be used in the accumulator
            int firstSpace = postRecord.indexOf(" ");
            this.docId = Integer.valueOf(postRecord.substring(0, firstSpace));

            firstSpace++;
            int secondSpace = postRecord.indexOf(" ", firstSpace);
            this.weight = Double.valueOf(postRecord.substring(firstSpace, secondSpace));

            this.postline++;
            postFile.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public ResultList sortAccumulator()
    {
        ResultList list = new ResultList(10);
        for(int i: this.weightedIds)
        {
            int docId = i;
            double weight = this.accumulator[i];
	    list.insertSort(docId, weight);
        }
        return list;
    }

    public String getMapName(int docId)
    {
        try{
            RandomAccessFile map = new RandomAccessFile(MAP_LOCATION, "r");
            map.seek(docId * MAP_RECORD_SIZE);
            String line = map.readLine().trim();
            map.close();
            return line;
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return null;
        }
    }
    


    //Prints out for the top 10 ranked documents: id, name, and weight
    public void displayResults(ResultList list)
    {
        list.printList(this);
    }

    public void run(Retrieve retriever)
    {
        retriever.accumulateWeight();
        ResultList list = retriever.sortAccumulator();
        retriever.displayResults(list);
    }

    public static void main(String[] args)
    {
        Retrieve retriever = new Retrieve(args);
        retriever.run(retriever);
    }
}
