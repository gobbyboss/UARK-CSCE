import java.io.*;

%%

%{

%}

%class Lexer
%type String



%%


[\r\t]  { }
[\n]    { return yytext(); }
.		{ return yytext(); }
<<EOF>> { return null; }