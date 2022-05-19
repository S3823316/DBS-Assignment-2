/******************************************************************************
* B+ btindex Indexing 
* 
* This program implements the concept of B+ btindex indexing which is done in a
* database for a data file that is provided in txt format. The program will create 
* an index file for the heap file that has been generated by the dbload program.
* This program will capture the byte location and set it as the offset value.
* 
* Once the btindex has been populated, it will be written/output to an index file.
******************************************************************************/
import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.text.SimpleDateFormat;

//Class to hold the btindex nodes
class btindex implements Serializable {
	public List<String> key; 
	public List<btindex> ptr;
	public List<Long> offsetvalue;
	public List<Integer> dataLength; 
	public btindex parent;
	public btindex rightpointer; 
	public btindex leftpointer; 
	public boolean isLeaf;
	
	public btindex() {
		this.key = new ArrayList<String>();
		this.ptr = new ArrayList<btindex>();
		this.offsetvalue = new ArrayList<Long>();
		this.dataLength = new ArrayList<Integer>();
		this.parent = null;
		this.rightpointer = null;
		this.leftpointer = null;
		this.isLeaf = false;
	}

}

public class btindexload {

/*
* static variables of the Class btindex and size of node which 
* will be used throughout the program.
*/
static final int SDTName_SIZE = 24;
static final int RECORD_SIZE = 111;
static btindex root;
static int Nodesize = 0; 
private static final int STD_NAME_SIZE = 24;
private static final int ID_SIZE = 4;
private static final int DATE_SIZE = 8;
private static final int YEAR_SIZE = 4;
private static final int MONTH_SIZE = 9;
private static final int MDATE_SIZE = 4;
private static final int DAY_SIZE = 9;
private static final int TIME_SIZE = 4;
private static final int SENSORID_SIZE = 4;
private static final int SENSORNAME_SIZE = 38;
private static final int COUNTS_SIZE = 4;
private static final int TOTAL_SIZE =  STD_NAME_SIZE + 
ID_SIZE + 
DATE_SIZE + 
YEAR_SIZE + 
MONTH_SIZE + 
MDATE_SIZE + 
DAY_SIZE + 
TIME_SIZE + 
SENSORID_SIZE + 
SENSORNAME_SIZE + 
COUNTS_SIZE;

// Initialize the btindex
public void initialisebtindex() {
	root = new btindex();
}

// Function to read the each page into each record and extract the SDT_Name portion of
// the record. Then create B+ btindex Index on the field and write to an index file.
@SuppressWarnings("null")
public static void populatebtindex(int pageSize)  {

	   String IndexFileName = "btindexIndex." + pageSize;
	   String datafile = "heap." + pageSize;
	   Path path = Paths.get("heap." + pageSize);
        long startTime = 0;
        long finishTime = 0;
        int numBytesInOneRecord = TOTAL_SIZE;
        int numBytesInSdtnameField = STD_NAME_SIZE;
        int numBytesIntField = 4;
        int numRecordsPerPage = pageSize/numBytesInOneRecord;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        byte[] page = new byte[pageSize];
        FileInputStream inStream = null;

        try {
            inStream = new FileInputStream(datafile);
            int numBytesRead = 0;
            byte[] sdtnameBytes = new byte[numBytesInSdtnameField];
			int position=0;
            // until the end of the binary file is reached
            while ((numBytesRead = inStream.read(page)) != -1) {
              
                for (int i = 0; i < numRecordsPerPage; i++) {

                    // Copy record's SdtName (field is located at multiples of the total record byte length)
                    System.arraycopy(page, (i*numBytesInOneRecord), sdtnameBytes, 0, numBytesInSdtnameField);

                    // Check if field is empty; if so, end of all records found (packed organisation)
                    if (sdtnameBytes[0] == 0) {
                        // can stop checking records
                        break;
                    }

                    // Set the sdt name value
                    String sdtNameString = new String(sdtnameBytes);
                
					 insert(root,sdtNameString, position, numBytesInOneRecord);
				
					position += numBytesRead;		
											
                }
				
            }
			
            }
        catch (Exception e) {
            System.err.println("Error occcured " + e.getMessage());
        }

		
 }

 /*
	 * Insert function which inserts the record into the btindex. 
	 * When the nodes inserted equals the node size of the btindex, the split function 
	 * is called and the btindex is balanced.
	 */
private static void insert(btindex node, String key,long offset, int reclength) throws IOException {

		if ((node == null || node.key.isEmpty()) && node == root) {
			node.key.add(key);
			node.offsetvalue.add((Long) offset);
			node.dataLength.add(reclength);
			node.isLeaf = true;
			root = node;
			return;
		}
		else if (node != null || !node.key.isEmpty()) {
			for (int i = 0; i < node.key.size(); i++) {
				
			if (key.compareTo(node.key.get(i)) < 0) {
					if (!node.isLeaf && node.ptr.get(i) != null) {
						insert((btindex) node.ptr.get(i), key, offset, reclength);
						return;
					} 
					else if (node.isLeaf) {
						node.key.add("");
						node.offsetvalue.add(0l);
						node.dataLength.add(0);
						for (int j = node.key.size() - 2; j >= i; j--) {
							node.key.set(j + 1, node.key.get(j));
							node.offsetvalue.set(j + 1, node.offsetvalue.get(j));
							node.dataLength.set(j + 1, node.dataLength.get(j));
						}
						node.key.set(i, key);
						node.offsetvalue.set(i, offset);
						node.dataLength.set(i, reclength);
						if (node.key.size() == Nodesize) {
							split(node);
							return;
						} 
						else 
							return;
					}
				}
				else if (key.compareTo(node.key.get(i)) > 0) {
					if (i < node.key.size() - 1) {
						continue;
					}
					else if (i == node.key.size() - 1) {
						if (!node.isLeaf && node.ptr.get(i + 1) != null) {
							insert((btindex) node.ptr.get(i + 1),key, offset, reclength);
							return;
						}

						else if (node.isLeaf) {
							node.key.add("");
							node.offsetvalue.add(0l);
							node.dataLength.add(0);
							node.key.set(i + 1, key);
							node.offsetvalue.set(i + 1, offset);
							node.dataLength.set(i + 1, reclength);
						}
						
						if (node.key.size() == Nodesize) {
							split(node);
							return;
						} 
						else
							return;
					}
				}
			}
		}
	}

	/*
	 * This function splits the btindex and balances the node in it. 
	 * After split it creates the pointer and stores the pointer of the 
	 * parent, right node and left node to it if it is an internal node and 
	 * if it is a leaf node stores the right pointer to the node. Before splitting
	 * the function sorts the key in ascending order and perform the split.
	 */
	private static void split(btindex node) throws IOException {
		btindex leftnode = new btindex();
		btindex rightnode = new btindex(); 
		btindex tempparent = new btindex(); 
		btindex parent;
		int newPosKey = 0, split = 0;
		
		if (node.isLeaf) {
			if (node.key.size() % 2 == 0)
				split = (node.key.size() / 2) - 1;
			else
				split = node.key.size() / 2;

			rightnode.isLeaf = true;
			for (int i = split; i < node.key.size(); i++) {
				rightnode.key.add(node.key.get(i));
				rightnode.offsetvalue.add(node.offsetvalue.get(i));
				rightnode.dataLength.add(node.dataLength.get(i));
			}
			
			leftnode.isLeaf = true;
			for (int i = 0; i < split; i++) {
				leftnode.key.add(node.key.get(i));
				leftnode.offsetvalue.add(node.offsetvalue.get(i));
				leftnode.dataLength.add(node.dataLength.get(i));
			}
			
			if (node.rightpointer != null)
				rightnode.rightpointer = node.rightpointer;
			else
				rightnode.rightpointer = null;
			if (node.leftpointer != null)
				leftnode.leftpointer = node.leftpointer;
			else
				leftnode.leftpointer = null;

			leftnode.rightpointer = rightnode;
			rightnode.leftpointer = leftnode;

			if (node.parent == null) {
				tempparent.isLeaf = false;
				tempparent.key.add(rightnode.key.get(0));
				tempparent.ptr.add(leftnode);
				tempparent.ptr.add(rightnode);
				leftnode.parent = tempparent;
				rightnode.parent = tempparent;
				root = tempparent;
				node = tempparent;
			}
			else if (node.parent != null) {
				parent = node.parent;				
				parent.key.add(rightnode.key.get(0));
				Collections.sort(parent.key);
				leftnode.parent = parent;
				rightnode.parent = parent;
				newPosKey = parent.key.indexOf(rightnode.key.get(0));

				if (newPosKey < parent.key.size() - 1) {
					parent.ptr.add(null);

					for (int i = parent.key.size() - 1; i > newPosKey; i--) {
						parent.ptr.set(i + 1, parent.ptr.get(i));
					}

					parent.ptr.set(newPosKey + 1, rightnode);
					parent.ptr.set(newPosKey, leftnode);
				}

				else if (newPosKey == parent.key.size() - 1) {
					parent.ptr.set(newPosKey, leftnode);
					parent.ptr.add(rightnode);
				}
				if (node.leftpointer != null) {
					node.leftpointer.rightpointer = leftnode;
					leftnode.leftpointer = node.leftpointer;
				}
				if (node.rightpointer != null) {
					node.rightpointer.leftpointer = rightnode;
					rightnode.rightpointer = node.rightpointer;
				}
				if (parent.key.size() == Nodesize) {
					split(parent);
					return;
				} else
					return;
			}
		}
		else if (!node.isLeaf) {
			rightnode.isLeaf = false;
			if (node.key.size() % 2 == 0)
				split = (node.key.size() / 2) - 1;
			else
				split = node.key.size() / 2;

			String popKey = node.key.get(split);
			int k = 0, p = 0;
			for (int i = split + 1; i < node.key.size(); i++) {
				rightnode.key.add(node.key.get(i));
			}
			for (int i = split + 1; i < node.ptr.size(); i++) {
				rightnode.ptr.add(node.ptr.get(i));
				rightnode.ptr.get(k++).parent = rightnode;
			}
			k = 0;
			for (int i = 0; i < split; i++) {
				leftnode.key.add(node.key.get(i));
			}
			for (int i = 0; i < split + 1; i++) {
				leftnode.ptr.add(node.ptr.get(i));
				leftnode.ptr.get(p++).parent = leftnode;
			}
			p = 0;
			if (node.parent == null) {
				tempparent.isLeaf = false;
				tempparent.key.add(popKey);
				tempparent.ptr.add(leftnode);
				tempparent.ptr.add(rightnode);
				leftnode.parent = tempparent;
				rightnode.parent = tempparent;
				node = tempparent;
				root = tempparent;
				return;
			}
			else if (node.parent != null) {
				parent = node.parent;
				parent.key.add(popKey);
				Collections.sort(parent.key);
				newPosKey = parent.key.indexOf(popKey);

				if (newPosKey == parent.key.size() - 1) {
					parent.ptr.set(newPosKey, leftnode);
					parent.ptr.add(rightnode);
					rightnode.parent = parent;
					leftnode.parent = parent;
				}
				else if (newPosKey < parent.key.size() - 1) {
					int ptrSize = parent.ptr.size();
					parent.ptr.add(null);
					for (int i = ptrSize - 1; i > newPosKey; i--) {
						parent.ptr.set(i + 1, parent.ptr.get(i));
					}

					parent.ptr.set(newPosKey, leftnode);
					parent.ptr.set(newPosKey + 1, rightnode);
					leftnode.parent = parent;
					rightnode.parent = parent;
				}
				
				if (parent.key.size() == Nodesize) {
					split(parent);
					return;
				} else
					return;
			}
		}
	}

	//This function will populate the btindex and then write to the index file
   	private static void Createbtindex(int pageSize) throws IOException {
		try{
		String IndexFileName = "btindexIndex." + pageSize;
		Path path = Paths.get("heap." + pageSize);

		populatebtindex(pageSize);
		writeIndexfile("6", path.toString(), IndexFileName);
	}
	catch (Exception e) {
		System.err.println("Error occcured " + e.getMessage());
	}

	}

    	/*
	 * This function writes the btindex into the index file in the form of bytes. 
	 *It will stores block of data allocated for the meta data and then next
	 *the next blocks contains the key,offset values and data length.
	 */
	private static void writeIndexfile(String key, String heapfilepath, String indexfilename) throws IOException {
	
		FileOutputStream fout = new FileOutputStream(indexfilename);
		byte[] heapFileName = heapfilepath.getBytes();
		byte[] keyLength = key.getBytes();
		byte[] rootOffset = (" " + root.key.get(0)).getBytes();
		FileChannel fc = fout.getChannel();
		fc.write(ByteBuffer.wrap(heapFileName));
		fc.write(ByteBuffer.wrap(keyLength), 257l);
		fc.write(ByteBuffer.wrap(rootOffset), 260l);
		fc.position(1025l);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(root);
		oos.close();
	}

      // Main Method
   public static void main(String[] args) {
     btindexload objbtindex = new btindexload();
     
      try {
       String input = args[0];
       int pageSize = Integer.parseInt(input);
       
       long startTime = System.currentTimeMillis();
       
	   objbtindex.initialisebtindex();
       objbtindex.Createbtindex(pageSize);
     
       long stopTime = System.currentTimeMillis();
       
       System.out.println(stopTime - startTime + " ms");
       System.out.println((stopTime - startTime) * 0.001 + " sec");
       System.out.println("Index file created successfully");
	   
	} catch (Exception e) {   
         System.out.println("Invalid pagesize");
    }

 }
}