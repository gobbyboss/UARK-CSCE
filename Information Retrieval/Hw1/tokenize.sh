#! /bin/bash
echo Robert Goss
java MultipleFiles $1 $2
cat output/* | sort | uniq -c > output/alpha.txt
sort -rn output/alpha.txt > output/freqs.txt