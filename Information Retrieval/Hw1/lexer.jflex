//  Robert Goss Information Retrieval Hw1 Part 2
//  9/2/2022
import java.io.*;

%%

%{

%}

%class Lexer
%type String
uppercase = [A-Z]


%%


[\w]+ { System.out.println(yytext().toLowerCase()); }
[\n]    { return yytext(); }
[ \t\r]+  { }
.		{ return yytext(); }
<<EOF>> { return null; }