//  Robert Goss Information Retrieval Hw2 Part 1
//  9/27/2022
import java.io.*;

%%

%{

%}

%class Lexer
%type String
EMAIL 	=		[A-Za-z0-9_\-\.]+@([A-Za-z0-9_\-]+\.)+[A-Za-z0-9_\-]{2,4}
%%
/* Removes HTML Tags and keeps the ALT/SRC content */
\<[^>]*> {  String tags = yytext();
            boolean src = tags.contains("src=");
            boolean alt = tags.contains("alt=");
            if(src || alt)
            {
                String tokens[] = new String[2];
                if(src)
                {
                    int pos = tags.indexOf("src=") + 5;
                    int end = tags.indexOf("\"", pos);
                    if(end != -1)
                    {
                        tokens[0] = tags.substring(pos, end);
                    }
                    else
                    {
                        break;
                    }
                    if(!alt)
                    {
                        return tokens[0].toLowerCase();
                    }
                }
                if(alt)
                {
                    int pos = tags.indexOf("alt=") + 5;
                    int end = tags.indexOf("\"", pos);
                    if(end != -1)
                    {
                        tokens[1] = tags.substring(pos, end);
                    }
                    else
                    {
                        break;
                    }
                    tokens[1] = tokens[1].replace(' ', '\n');
                    if(!src)
                    {
                        return tokens[1].toLowerCase();
                    }
                }
                return (tokens[0].toLowerCase() + "\n" + tokens[1].toLowerCase());
            }   
}


/* HTML Tag in Word */
[\w]+\<[^>]*>[\w]+(\<[^>]*>)? { 
                String tags = yytext();
                int pos = tags.indexOf('<');
                int end = tags.indexOf('>');
                if(pos == -1 || end == -1)
                {
                    System.out.println();
                }
                String printString = tags.substring(0, pos);
                String endString = tags.substring(end + 1, tags.length());
                if(endString.indexOf('<') != -1 && endString.indexOf('>') != -1)
                {
                    pos = endString.indexOf('<');
                    end = endString.indexOf('>');
                    printString += endString.substring(0, pos) + endString.substring(end + 1, endString.length());
                }
                else
                {
                    printString += endString;
                }
                System.out.println(printString.toLowerCase());
 }

/* Emails */
{EMAIL} { System.out.println(yytext().toLowerCase()); }

/* Finds and keeps URLs */
https?:\/\/(www\.)?[a-zA-Z0-9-]{1,128}\.[a-zA-Z0-9()]{1,4}\.? { return yytext(); }

/* Find floats and remove the decimal portion */
-?\d*\.\d+ { float f = Float.parseFloat(yytext()); System.out.println((int)f); }

/* Remove Punctuation */
[.,:;?()[]\"\'!_/-] { }

/* Rule for finding words */
[\w]+ { System.out.println(yytext().toLowerCase()); }

/* Ignore all formatting */
[\n\s\t\r]+  { }

/* All other characters */
.		{ }
