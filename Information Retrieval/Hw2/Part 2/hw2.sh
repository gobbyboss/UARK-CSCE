#! /bin/bash
echo 'Homework 2 Part 2 written by Robert Goss'
echo
echo 'compiling'
jflex tokenizer.jflex
javac MultipleFiles.java GlobalHashtable.java Hashtable.java Lexer.java

echo
echo 'indexing'
/bin/rm out/*
java MultipleFiles $1 $2

echo
echo '-------- results --------'
ls -lt $2
more $2/*
