//Robert Goss Information Retrieval Hw3
//10/26/2022
// -----------------------------------------------------------------
//  Filename: MultipleFiles.java
// 	Have a separate jflex file to do tokenization named: lexer.jflex
//  First, need to compile: jflex lexer.jflex
//  To run:   javac MultipleFiles.java Lexer.java
// 			  java MultipleFiles <indir> <outdir>  
// -----------------------------------------------------------------

import java.io.*;


public class MultipleFiles {
	public static void main (String args[])
	{
		File  inputDir = null;
		File  outputDir = null;
		PrintStream console = System.out;
        DocumentHashtable docHt = new DocumentHashtable(3000);
		StopHashtable stopHt = new StopHashtable(102);
        int docTokens = 0;

		try
		{
			//Validate args
			if(args.length != 2)
				throw new Error ("Wrong number of arguments, expecting 2 received " + args.length);
			for (int i = 0; i <args.length; i++)
			{
				File f = new File(args[i]);
				if (!f.isDirectory())
				{
					throw new Error ("Not a valid directory in args " + i);
				}
				else
				{
					if(i == 0)
						inputDir = f; //Store user input for Input directory
					else
						outputDir = f; //Store user input for Output directory
				}
			}
			
			File[] listOfInputs = inputDir.listFiles(); // Array of Files (documents we will tokenize and index)
			GlobalHashtable globalHash = new GlobalHashtable(30000); //Global Hashtable Init
			int docId = 0; //Increments on each file
			int totalTokens = 0;
			
			//Create reader for map
			String outFile = "map.html";
			PrintStream o = new PrintStream(new File(outputDir, outFile));
			for(File file : listOfInputs) //loop through all files in input directory
			{
				if(file.isFile())
				{				
					//Add filename to the map
				
					System.setOut(o);
					System.out.print(globalHash.createMapString(file.getName()));
					System.setOut(console);
					//Set the reader to the next file in the input directory
					Reader rdr = new InputStreamReader(new FileInputStream(file));
				
					Lexer lexer = new Lexer(rdr);				
					String result = "";
					String token = "";
					
					while(token != null)
					{
						result = lexer.yylex();
						// For each token come back if it's not null
						// then we'll add it to the document hashtable
						// otherwise we know we're at the end of the file
						if(result != null && result.length() > 1 && !stopHt.isStopWord(result)) {
                            docHt.insert(result);
                            docTokens++;
							totalTokens++;
						}
						else
							token = result;
					}
                    // Add doc hash to global hash
					for(int i = 0; i < docHt.getSize(); i++)
					{
						String key = docHt.getKey(i);
						double rtf = docHt.getFreq(key) / docTokens;
						globalHash.insert(key, docId, rtf);
					}
                    // Finished using the document hashtable, reset it
                    docHt.init();
                    docTokens = 0;
					docId++;
				}
			}
			//Process the global hashtable into dict/post using docId as the number of documents
			System.out.println("\nThe corpus contained " + totalTokens + " total tokens in " + docId + " documents.");
			globalHash.processGlobalHash(outputDir, docId);
			System.setOut(console);
			System.out.println("Successfully created inverted file!");
		}
		catch (Exception e)
		{
			System.out.println("Exception");
			System.out.println(e.getMessage());
			System.out.println("StackTrace: ");
			e.printStackTrace();
		}
	}
}