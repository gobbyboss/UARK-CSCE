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
        Hashtable docHt = new Hashtable(3000);
		StopHashtable stopHt = new StopHashtable(500);
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
			GlobalHashtable globalHash = new GlobalHashtable(45000); //Global Hashtable Init
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
                    System.out.println ("\nProcessing file " + file.getName());
				
					Lexer lexer = new Lexer(rdr);				
					String result = "";
					String token = "";
					
					while(token != null)
					{
						result = lexer.yylex();
						// For each token come back if it's not null
						// then we'll add it to the document hashtable
						// otherwise we know we're at the end of the file
						if(result != null && result.length() > 1 && stopHt.find(result) != 0) {
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
						double rtf = docHt.getFreq(key) / totalTokens;
						globalHash.insert(key, docId, rtf);
					}
				
			        System.out.println("The document contained " + docHt.getUsed() + " unique tokens and "
                                       + docTokens + " total tokens.");
                    
                    // Finished using the document hashtable, reset it
                    docHt.init();
                    docTokens = 0;
					docId++;
				}
			}
			//Process the global hashtable into dict/post using docId as the number of documents
			System.out.println("\nThe corpus contained " + totalTokens + " total tokens.");
			globalHash.processGlobalHash(outputDir, docId);
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