//----------------------------------------
// Filename:  lexer.jflex
// To compile: jflex lexer.jflex  
//----------------------------------------
import java.io.*;

%%

%{

%}

%class Lexer
%type String

uppercase = [A-Z]
digits = [0-9]

%%

[hH]im   { return "her"; }
[hH]e    { return "she"; }
[hH]is   { return "her"; }
Susan    { return yytext(); }
{uppercase} { return String.valueOf(yytext().toLowerCase().charAt(0)); }
{digits}+ 	{ return "NUMBER"; }
[a-z]+ { return yytext(); }
[\n\r\t ] { }
.		{ return yytext(); }
<<EOF>> { return null; }