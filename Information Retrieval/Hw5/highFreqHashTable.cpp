/* Filename:  hashtable.cpp
 * Author:    Susan Gauch
string  * Date:      2/11/2010
 * Purpose:   The implementation file for a hash table of words and numbers.
*/

#include <assert.h>
#include <iostream>
#include <fstream>


#include "highFreqHashTable.h"

using namespace std;


/*-------------------------- Constructors/Destructors ----------------------*/

/* : HashTable
 * Author: seg
 * Parameters:  ht: the hashtable to copy
 * Purpose:     copy a hashtable 
 *              NOTE:  this is crucial to include since it is invoked
 *              by call-by-value parameter passing
 * Returns:     nothing
*/
HighFreqHashTable::HighFreqHashTable( const HighFreqHashTable &ht )
{
   size = ht.size;                    // set the size of the array
   used = ht.used;                    // set the size of the array
   if((hashtable = new StringIntPair[size]) == NULL)
       cout << "Out of memory at HashTable::HashTable(const HashTable)" << endl;
   assert( hashtable != 0 );

   for (unsigned long i=0; i < size; i++)     // make a _copy_ of the array elements
   {
      hashtable[i].key = ht.hashtable[i].key;
   }
   
}
           
/* Name:  HashTable
 * Author: seg
 * Parameters:  none
 * Purpose:     allocate a hashtable for an expected number of keys
 *              initializes all values to null (0)
 * Returns:     pointer to the created HashTable or 0 if out of memory
*/
HighFreqHashTable::HighFreqHashTable(const unsigned long NumKeys)
{
   // allocate space for the table, init to null key
   size = NumKeys * 3;   // we want the hash table to be 2/3 empty
   used = 0;
   collisions = 0;
   lookups = 0;
   if((hashtable = new StringIntPair[size]) == NULL)
      cout << "Out of memory at HashTable::HashTable(unsigned long)" << endl;
   assert( hashtable != 0 );

   // initialize the hashtable
   for (unsigned long i=0; i < size; i++)
   {
      hashtable[i].key = "";
   }
}


/* Name:  ~HashTable
 * Author: seg
 * Parameters:  none
 * Purpose:     deallocate a hash table
 * Returns:     nothing
*/
HighFreqHashTable::~HighFreqHashTable()
{
   delete [] hashtable;
}

/*-------------------------- Accessors ------------------------------------*/
/* Name:  Print
 * Author: seg
 * Parameters:  a fileptr
 * Purpose:     print the contents of the hash table
 *              currently, only prints non-null entries
 * Returns:     nothing
*/

/* Name:  Print
 * Author: seg
 * Parameters:  none
 * Purpose:     print the contents of the hash table
 *              currently, only prints non-null entries
 * Returns:     nothing
*/
void HighFreqHashTable::Print() const
{
   //ofstream fpout(filename); 

   // Print out the non-zero contents of the hashtable
   for ( unsigned long i=0; i < size; i++ )
   {  
      if ( !(hashtable[i].key == ""))
          cout << hashtable[i].key << endl;
   }

}


/* Name: Insert
 * Author: sgauch
 * Parameter:
 * 		key : The target of context words to be stored
 * 		frequency: Total frequency count
 * Purpose: 	insert or add a word with its frequency count in hashtable
 * Return:	nothing
*/


//The insert function for the global hash table, which handles the linked lists //current Data is the rtf
void HighFreqHashTable::Insert (const string Key)
{
unsigned long Index;

 if (used >= size)
    cerr << "The hashtable is full; cannot insert.\n";
 else
 {
    Index = Find(Key);

    // If not already in the table, insert it
    if (hashtable[Index].key == "")
    {
       hashtable[Index].key = Key;
       used++;
    }
    // else do nothing ** change this comment to adding to linked list
    else {
        cout << "collision in high freq, Key: " << Key << ", htkey: " << hashtable[Index].key << ", Index:" << Index << endl;
    }
 }
}

/* Init
 * Author: seg
 * Parameters:  Set the hashtable to so that each bucket contains "", 0
 * Purpose:     initialize the hashtable for use
 * Returns:     nothing
*/
void HighFreqHashTable::Init()
{
   collisions = 0;
   lookups = 0;
   used = 0;

   // initialize the hashtable
   for (unsigned long i=0; i < size; i++)
   {
      hashtable[i].key = "";
   }
}

/* Name: GetData
 * Author: sgauch
 * Parameters:	key: the string
 * Purpose:	return the data or -1 if Key is not found
 * Return:	return an int 
*/
int HighFreqHashTable::GetData(const string Key)
{
unsigned long Index;

 lookups++; 
 Index = Find(Key);
 if (hashtable[Index].key == "")
    return -1;
 else   
    return (1);
}

/* Name: GetUsage
 * Author: S. Gauch
 * Parameters:	None
 * Purpose:	return the number of collisions
 * Return:	return a char *
*/
void HighFreqHashTable::GetUsage(int &Used, int &Collisions, int &Lookups) const
{
   Used = used;
   Collisions = collisions; 
   Lookups = lookups;
}

//Function to fill the hash table
void HighFreqHashTable::FillTable(const char *filename) {
    ifstream din(filename); 
    if(din.fail()){
        cout << "Failed to open file" << endl;
        return;
    }
    //Loop to add each term into the hash table
    string word;
    while(din >> word){
        Insert(word);
    }

    din.close();
    //Print();
}



/*-------------------------- Private Functions ----------------------------*/
/* Name:  Find
 * Author: seg
 * Parameters:  key: the word to be located
 * Purpose:     return the index of the word in the table, or
 *              the index of the free space in which to store the word
 * Returns:     index of the word's actual or desired location
*/
unsigned long HighFreqHashTable::Find (const string Key) 
{
unsigned long Sum = 0;
unsigned long Index;

   // add all the characters of the key together
   for (long unsigned i=0; i < Key.length(); i++)
      Sum = (Sum * 19) + Key[i];  // Mult sum by 19, add byte value of char
  
   Index = Sum % size;

   // Check to see if word is in that location
   // If not there, do linear probing until word found
   // or empty location found.
   while (((hashtable[Index].key) != Key) &&
          ((hashtable[Index].key) != "") ) 
   {
      Index = (Index+1) % size;
      collisions++;
   }
   
   return Index;
}
