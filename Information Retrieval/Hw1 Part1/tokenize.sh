#! /bin/bash
echo Robert Goss
jflex lexer.jflex
javac MultipleFiles.java Lexer.java
java MultipleFiles $1 $2
