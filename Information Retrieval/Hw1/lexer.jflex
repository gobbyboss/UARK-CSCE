//  Robert Goss Information Retrieval Hw1 Part 2
//  9/2/2022
import java.io.*;

%%

%{

%}

%class Lexer
%type String
uppercase = [A-Z]
digits = [0-9]


%%


/* Find floats and remove the decimal portion */
[+-]?\d*\.\d+ { float f = Float.parseFloat(yytext()); System.out.println((int)f); }

/* Rule for finding words */
[\w]+ { System.out.println(yytext().toLowerCase()); }

/* Returning on a new line */
[\n]    { return yytext(); }

/* Ignore all formatting */
[\s\t\r]+  { }

/* All other characters */
.		{ return yytext(); }
