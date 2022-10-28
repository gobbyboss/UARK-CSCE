#! /bin/bash
echo 'Homework 3 written by Robert Goss'
echo
echo 'compiling'
jflex tokenizer.jflex
javac MultipleFiles.java GlobalHashtable.java StopHashtable.java Hashtable.java Lexer.java

echo
echo 'indexing'
/bin/rm out/*
java MultipleFiles $1 $2


