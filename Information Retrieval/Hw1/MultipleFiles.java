//  Robert Goss Information Retrieval Hw1 Part 2
//  9/2/2022
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

		try
		{
			//Validate args\
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
			
			// totalTokens is used to show that it shares the same memory when running lex
			// which can be applied for your hash table
			int totalTokens = 0;
			for(File file : listOfInputs) //loop through all files in input directory
			{
				if(file.isFile())
				{				
					//Collect the tokens from the document
					Reader rdr = new InputStreamReader(new FileInputStream(file));

					//Create output file and rename it to file.out and set output stream to the file
					String outFile = file.getName().replace("txt", "out");
					PrintStream o = new PrintStream(new File(outputDir, outFile));
					System.setOut(o);

					Lexer lexer = new Lexer(rdr);				
					String result = "";
					String token = "";
					
					while(token != null)
					{
						result = lexer.yylex();
						// For each token come back if it's not null
						// then we'll output it to the file
						// otherwise we know we're at the end of the file
						if(result != null) {
							System.out.print(result);
							++totalTokens;
						}
						else
							token = result;
						
					}
					o.close();

					processDocumentHashtable();
				}
			}

			System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
			System.out.println("Total Tokens: " + totalTokens);

			System.out.println("Done tokenizing.  Good place to write the dict and post files.");

		}
		catch (Exception e)
		{
			System.out.println("Exception");
			System.out.println(e.getMessage());
			System.out.println("StackTrace: ");
			e.printStackTrace();
		}
	}

	public static void processDocumentHashtable() {
		System.out.println("Document hashtable should have been filled.");
		System.out.println("Time to deal with its contents.");
	}
   
}
