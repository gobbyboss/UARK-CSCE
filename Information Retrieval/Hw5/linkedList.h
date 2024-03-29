//-----------------------------------------------------------------
// File:  template_taillist.h
// Author:  Susan Gauch
// Purpose:  The header file for a linked list class.
//           This list is singly-linked, with a tail pointer.
//	     It also uses the TEMPLATE mechanism for the data type
//	     stored in the node.
//-----------------------------------------------------------------
#include <cmath>
template <class T>	// template to provide run-time class for Item
class List
{
public:
// constructors and destructors
   List();                    // default constructor
   List (const List& L);      // copy constructor
   ~List();                   // destructor

// list operations
   void AddToFront (const T Item);
   void AddToEnd (const T DocID, float rtf, int freq);
   void AddSorted (const T Item);
   void Delete(const T Item);
   void WriteToPost(ofstream& fpout, double idf); //const char *filename
   bool IsEmpty() const;
   void Print() const;
   void Write(const char Filename[]) const;
   bool IsLowFreq();
   void WriteFixedPostRecord(ofstream& fpout, int docId, double termWeight);

private:
   struct Node;
   typedef Node* NodePtr;
   struct Node
   {
  	    T Item;
        float rtf;
        int freq;
        NodePtr Next;	
   };            // defined in implementation file
   void Find (const T Item, NodePtr &Curr, NodePtr &Prev) const;
   NodePtr Head;              // the head of the list
   NodePtr Tail;              // the tail of the list
   int length;
   int DOCID_SIZE = 4;
   int TERM_WEIGHT_SIZE = 10; //4
   int POST_REC_SIZE = DOCID_SIZE + TERM_WEIGHT_SIZE + 2;
   int num_records = 0;
};

//-----------------------------------------------------------------
// For some reason, must have method definitions for templated 
// classes in the same physical file as the header info
//-----------------------------------------------------------------
#include <fstream>
#include <iostream>
using namespace std;

/*The node structure*/
//struct Node
//{
   //T Item;
   //NodePtr Next;
//};           

// ------------------------ constructors and destructors ----------------

//-----------------------------------------------------------------
// Function Name:  The default constructor
// Parameters:  none
// Return Value: none
// Purpose:  Initialize the list to empty
//-----------------------------------------------------------------
template <class T>
List<T>::List()
{
   Head = NULL;
   Tail = NULL;
}

//-----------------------------------------------------------------
// Function Name:  The copy constructor
// Parameters:  none
// Return Value: none
// Purpose:  Initialize the list to be a copy of some other list
//-----------------------------------------------------------------
template <class T>
List<T>::List(const List &Other)
{
NodePtr Curr = Other.Head;        // the current node in the original list

   Head = NULL;               // copy is initially empty
   Tail = NULL;
   
   // loop through the original list, copying nodes one at a time
   while (Curr != NULL)
   {
     AddToEnd (Curr->Item);  
     Curr = Curr->Next;
   }
};     

//-----------------------------------------------------------------
// Function Name:  The destructor
// Parameters:  none
// Return Value: none
// Purpose:  Delete all the nodes in the list, freeing the space
//-----------------------------------------------------------------
template <class T>
List<T>::~List()
{
NodePtr Temp;

   // loop through whole list deleting nodes
   while (Head != NULL)
   {
      Temp = Head;
      Head = Head->Next;
      delete Temp;
   }
}

// ----------------------- list operations ------------------------------

//-----------------------------------------------------------------
// Function Name:  IsEmpty
// Parameters:  none
// Return Value:  true if the list is empty, false otherwise
// Purpose:  Check whether or not the list is empty
//-----------------------------------------------------------------
template <class T>
bool List<T>::IsEmpty() const
{
   return (Head == NULL);
}

//Function to write fixed length records in post file
template <class T>
void List<T>::WriteFixedPostRecord(ofstream& fpout, int docId, double termWeight){   
   //Setting width for docId
   int tempDocId = min (docId, (int)(pow(10, DOCID_SIZE)-1));
   fpout << setw(DOCID_SIZE) << tempDocId << " ";

   //Setting width for term weight
   double tempTermWeight = min (termWeight, (double)(pow(10, TERM_WEIGHT_SIZE)-1));
   fpout << setw(TERM_WEIGHT_SIZE) << tempTermWeight << endl;    
   num_records += 1;
}

//Function to loop over all items in the linked list for each global hash entry const char *filename
template <class T>
void List<T>::WriteToPost(ofstream& fpout, double idf)
{
   //ofstream fpout(filename);
   NodePtr p = Head;
   while (p != nullptr){
      //calculate rtf * idf
      double termWeight = (p->rtf * idf)*10000;
      int termWeightRounded = ceil(termWeight * 100.0); // 100.0;
      //cout << "termWeight: " << termWeightRounded << endl;

      //fpout << p->Item << " " << termWeightRounded << endl; //p->Freq
      WriteFixedPostRecord(fpout, p->Item, termWeight);
      p = p->Next;
   }
}


//-----------------------------------------------------------------
// Function Name:  AddToFront
// Parameters:  Item - input - the info to be added
// Return Value: none
// Purpose:  Add the new entry to the front of the linked list.
//-----------------------------------------------------------------
template <class T>
void List<T>::AddToFront (const T Item)
{
NodePtr NewNode;

   // make the space for the new node and store the data
   NewNode = new Node;
   NewNode->Item = Item;
   
   // empty list case
   if(Head==NULL) 
      Tail=NewNode;
     
   // point from this node to the previous head of the list
   NewNode->Next = Head;

   // make the head of the list point to this node
   Head = NewNode;
}


//-----------------------------------------------------------------
// Function Name:  AddToEnd
// Parameters:  Item - input - the info to be added
// Return Value: none
// Purpose:  Add the new entry to the tail of the linked list.
//-----------------------------------------------------------------
template <class T>
void List<T>::AddToEnd (const T DocId, float rtf, int freq)
{
NodePtr NewNode;

   // make the space for the new node and store the data
   NewNode = new Node;
   NewNode->Item = DocId;
   NewNode->rtf = rtf;
   NewNode->freq = freq;
   NewNode->Next=NULL;
   
   if(Tail==NULL) 
       Head=NewNode;
   else 
       Tail->Next=NewNode;
   Tail=NewNode;
   length += 1;
}

//Function to determine whether a word is a "low frequency word" ie a word that appears once in doc collection
template <class T>
bool List<T>::IsLowFreq() {
   if(Head->freq == 1){
      return true;
   } else {
      return false;
   }
}


//-----------------------------------------------------------------
// Function Name:  AddSorted
// Parameters:  Item - input - the info to be added
// Return Value: none
// Purpose:  Add the new entry to the linked list, sorted in
//           increasing order.
//-----------------------------------------------------------------
template <class T>
void List<T>::AddSorted (const T Item)
{
NodePtr NewNode, Curr, Prev;

   // First make the new node and store the data 
   NewNode = new Node;
   NewNode->Item = Item;

   // Find the node prior to this one in the list 
   Find (Item, Curr, Prev);

   // If Prev is NULL, we're at the head of the list 
   if (Prev == NULL)
   {
      NewNode->Next = Head;
      Head = NewNode;
   }
   // else, insert between Prev and Curr
   else
   {
      Prev->Next = NewNode;
      NewNode->Next = Curr;
   }

   // If this is the last node in the list, move the tail pointer. 
   if (NewNode->Next == NULL)
      Tail = NewNode;
}


//-----------------------------------------------------------------
// Function Name:  Print
// Parameters:  none
// Return Value: none
// Purpose:  Print the contents of the list to stdout.
//-----------------------------------------------------------------
template <class T>
void List<T>::Print() const
{
cout<<"\n\nThe present contents of the list: "<<endl;
NodePtr Temp = Head;

   // loop through whole list printing nodes
   while (Temp != NULL)
   {
      cout << "Item: " << Temp->Item << endl;
      Temp = Temp->Next;
   }
   cout << endl;
}


//-----------------------------------------------------------------
// Function Name:  Delete
// Parameters:  none
// Return Value: none
// Purpose:  Delete a node based on the 'Item' 
//-----------------------------------------------------------------
template <class T>
void List<T>::Delete(const T Item)
{
NodePtr Curr; 
NodePtr Prev;
	
   // Find the node prior to the one that contains Item
   Find (Item, Curr, Prev);
	
   if (Curr == NULL || Curr->Item !=Item)
   { 
      cout<<"\nNo such record to be deleted!"<<endl;
   }

   // Item was found
   else 
   {
      // Reset pointer to Curr 
      if (Curr != Head)
         Prev->Next = Curr->Next;
      else
	 Head = Curr->Next;

      // If Curr was the end of the list
      if (Curr == Tail)
            Tail = Prev;

      delete Curr;
      Curr = NULL;
   }
}

//-----------------------------------------------------------------
// Function Name:  Write
// Parameters:  The file name 
// Return Value: none
// Purpose:  Write the contents back to the file
//-----------------------------------------------------------------
template <class T>
void List<T>::Write(const char FileName[]) const
{
ofstream outfile(FileName,ios::out);
NodePtr Temp = Head;

   // File couldn't be opened
   if (!outfile) 
   {
      cout<<"\nOutput file cannot be opened!"<<endl;
   }
   else
   {
      // loop through whole list printing nodes
      while (Temp != NULL)
      {
         outfile << Temp->Item << endl;
         Temp = Temp->Next;
      }
      outfile.close();
   }
}

// ----------------------- private methods ------------------------------

//-----------------------------------------------------------------
// Function Name:  Find
// Parameters:  Item - input - the Item to be found
//              Curr - output - pointer to the node with Item
//              Curr - output - pointer to the node before Item
// Return Value: none
// Purpose:  Locate a node (It is at Prev->Next)
// Note:  Assumes that > is defined for T.
//-----------------------------------------------------------------
template <class T>
void List<T>::Find (const T Item, NodePtr &Curr, NodePtr &Prev) const
{
   Curr = Head;
   Prev = NULL;

   /* find the proper position in the list */
   while ((Curr != NULL) && (Item > Curr->Item))
   {
      Prev = Curr;
      Curr = Curr->Next;
   }
}
