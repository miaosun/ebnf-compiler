#!/bin/bash
if [ -n "$1" ]
then
  FILE=$1
else  
  FILE=Ebnf
fi 

jjtree $FILE.jjt
javacc $FILE.jj
javac *.java
