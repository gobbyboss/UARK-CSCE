//Robert Goss Information Retrieval Hw2 Part 2
//10/6/2022
//----------------------------------------
// Filename:  tokenizer.jflex
// To compile: jflex lexer.jflex  
//----------------------------------------
import java.io.*;
import java.util.*;

%%
%unicode
%line
%column

%{

public static void handleToken(String tokenWord){
	System.out.println(tokenWord.toLowerCase());
}

public static void processDocumentHashtable(String token){
}


public static void main(String [] args) throws IOException
{
	//create a scanner and use the scanner's yylex function
	//if you want standard input, System.in instead of new FileReader(args[0])
	Lexer lexer = new Lexer(new FileReader(args[0]));
	lexer.yylex();
}

%}

%class Lexer
%type String

//Rules used for tokens

WORD = [A-Za-z][a-z]*
UPPERWORD = [A-Z0-9][A-Z0-9]*
HYPHENWORD = [A-Za-z]+(\-[A-Za-z]+)+
ABBREVIATION = [A-Za-z]+\.([A-Za-z]+\.)+

NUMBER = [0-9]+(,[0-9]+)*
FLOAT = [0-9]*\.[0-9]+
PHONENUMBER = [0-9]{3}-[0-9]{3}-[0-9]{4}
TIME = [0-9]{1,2}(:[0-9]{2})+
VERSION = [0-9]+\.[0-9]+(\.[0-9]+)+

URL = ((http)|(ftp))s?:\/\/[A-Za-z0-9]+([\-\.]{1}[A-Za-z0-9]+)*\.[A-Za-z0-9]{2}(:[0-9]{1})?(\/[A-Za-z0-9_~\.\-]*)*
EMAIL = [A-Za-z0-9_\-\.]+@([A-Za-z0-9_\-]+\.)+[A-Za-z0-9_\-]{2,4}

ATTRIBUTE = [ \n\t]+(([A-Za-z\-_]+)?[ \n\t]*=?[ \n\t]*((\"[^\"]*\")|([A-Za-z0-9]+)|({URL}))[ \n\t]*)+[ \n\t]*
STARTTAG = <\!?[A-Za-z0-9]+{ATTRIBUTE}*[\/]?>
ENDTAG = <[\/][A-Za-z0-9]+>

%%

//Rules being used and their outputs

{EMAIL}				{return yytext().toLowerCase();} 
{URL}				{return yytext().toLowerCase();}
{PHONENUMBER}		{return yytext().toLowerCase();}
{FLOAT}				{}
{NUMBER}			{}
{TIME}				{return yytext().toLowerCase();}
{VERSION}			{return yytext().toLowerCase();}
{STARTTAG}			{}
{ENDTAG}			{}
{UPPERWORD}			{return yytext().toLowerCase();}
{ABBREVIATION}		{return yytext().toLowerCase();}
{HYPHENWORD}		{return yytext().toLowerCase();}
{WORD}				{return yytext().toLowerCase();}
[\ ]		        {}
[\s]		        {}
.		            {}
