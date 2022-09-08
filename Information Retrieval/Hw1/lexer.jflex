//  Robert Goss Information Retrieval Hw1 Part 2
//  9/2/2022
import java.io.*;

%%

%{

%}

%class Lexer
%type String



%%


[\n]    { return yytext(); }
.		{ return yytext(); }
<<EOF>> { return null; }