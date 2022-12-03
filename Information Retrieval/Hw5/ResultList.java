import java.io.*;

public class ResultList 
{
	// Private variables
	ResultNode head; 
    	int size;
	int maxSize;

	static class ResultNode 
        {

		int docId;
		double weight;
		ResultNode next;

		ResultNode(int d, double w)
		{
			docId = d;
			weight = w;
			next = null;
		}
	}

	public ResultList()
 	{
		head = null;
		size = 0;
		maxSize = 10;
	}

	public ResultList(int max)
 	{
		head = null;
		size = 0;
		maxSize = max;
	}

	public void insertHead(int d, double w)
	{
		// Create a new node
		ResultNode new_node = new ResultNode(d, w);
		
		// Insert before head
		new_node.next = head;
		head = new_node;

		// Increment size
 		size++;
        }

	public void insertTail(int d, double w)
	{
		// Create a new node
		ResultNode new_node = new ResultNode(d, w);
		
		// Handle empty list
		if (head == null) 
			head = new_node;

		// Insert at end of list
		else 
		{
			ResultNode node = head;
			while (node.next != null) 
				node = node.next;
			node.next = new_node;
		}

		// Increment size
 		size++;
	}

	public void insertSort(int d, double w)
	{
		// Create a new node
		ResultNode new_node = new ResultNode(d, w);
		
		// Handle empty list
		if (head == null) 
			head = new_node;

		// Handle first node
		else if (head.weight > w)
		{
			new_node.next = head;
			head = new_node;
		}

		// Insert in sorted order
		else 
		{
			ResultNode node = head;
			while (node.next != null && node.next.weight < w) 
				node = node.next;
			new_node.next = node.next;
			node.next = new_node;
		}

		// Increment size
		if (size < maxSize)
 		   size++;
		else
		   head = head.next;
	}

	public void printList(Retrieve retrieve)
	{
		// Print first line
		if (size == 0)
		{	
			System.out.println("No results found");
			return;
		}
		System.out.println("Displaying top " + size + " results\n");
	
		// Traverse list
		ResultNode node = head;
		String output = "";
    		int rank = size;
		while (node != null) 
		{
			// Print results in reverse order
			String result = "Rank: " + rank--
 				      + " Document Name: " + retrieve.getMapName(node.docId) 
				      + " Weight: " + node.weight + "\n";
			output = result + output;
			node = node.next;
		}

		// Printing results
		System.out.println(output);
	}
}
