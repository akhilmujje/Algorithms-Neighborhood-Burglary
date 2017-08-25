import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Name: Devisriram Akhil Mujje
 * EID: dam4335
 */
public class Graph {

	private Map<String, List<String>> adj_list;
	private Map<String, Integer> num_keys;
	private ArrayList<String> unlocked_houses;
	

	/*
	 * Creates a graph to represent the neighborhood, where unlocked is the file
	 * name for the unlocked houses and keys is the file name for which houses
	 * have which keys.
	 */
	public Graph(String unlocked, String keys) {
		
		//file I/O for unlocked and keys files
		File unlock = new File(unlocked);
		File key = new File(keys);

		Scanner keys_input;

		try {
			keys_input = new Scanner(key);
		} catch (FileNotFoundException e) {
			System.out.println("Files not found! Graph not created");
			
			return;
		}

		Scanner unlocked_input;
		try {
			unlocked_input = new Scanner(unlock);
		} catch (FileNotFoundException e1) {
			System.out.println("Files not found! Graph not created");
			
			return;
		}

		unlocked_houses = new ArrayList<String>();
		ArrayList<String> trusting_houses = new ArrayList<String>();

		adj_list = new HashMap<String, List<String>>();
		num_keys = new HashMap<String, Integer>();

		// retrieve data from unlocked.txt
		while (unlocked_input.hasNextLine()) {

			unlocked_houses.add(unlocked_input.nextLine());
		}

		unlocked_input.close();

		// retrieve data from keys.txt

		int num_vertices = 0;
		String colon_delims = ": ";
		String comma_delims = ", ";
		String[] arr1, arr2;

		while (keys_input.hasNext()) {
			// get next line of input
			String line = keys_input.nextLine();
			// ignore new line
			if (line.length() != 0) {

				arr1 = line.split(colon_delims);
				// if there is no keys distributed to other houses, there is a colon (:)
				// character after house name
				// remove : from house name
				if (arr1[0].charAt(arr1[0].length() - 1) == ':')
					arr1[0] = arr1[0].substring(0, arr1[0].length() - 1);
				//add houses in neighborhood to array list
				trusting_houses.add(arr1[0]);

				//splits keys by commas and adds them to the adjacency list for each house
				if (arr1.length == 2) {
					arr2 = arr1[1].split(comma_delims);
				} else {
					arr2 = null;
				}
				ArrayList<String> key_list = new ArrayList<String>();
				if (arr2 == null) {
					

				} else {
					//fill out keylist and num_keys mapping for each key
					for (int i = 0; i < arr2.length; i++) {
						key_list.add(arr2[i]);
						try{
							if(num_keys.containsKey(arr2[i])){
								Integer value = num_keys.get(arr2[i]);
								int num = value.intValue();
								num++;
								value = num;
								num_keys.put(arr2[i], value);
							}
							else{
								Integer one = new Integer(1);
								num_keys.put(arr2[i],one);
							}
						}
						catch(NullPointerException n){
							
						}
						
						
					}
				}

				adj_list.put(trusting_houses.get(num_vertices), key_list);

				num_vertices++;

			}

		}
		keys_input.close();
		
		

	}

	/*
	 * This method should return true if the Graph contains the vertex described
	 * by the input String.
	 */
	public boolean containsVertex(String node) {
		return adj_list.containsKey(node);
	}

	/*
	 * This method should return true if there is a direct edge from the vertex
	 * represented by start String and end String.
	 */
	public boolean containsEdge(String start, String end) {
		if (!isLocked(end))
			return false;
		if (containsVertex(start) && containsVertex(end) && adj_list.get(start) != null) {
			List<String> list = adj_list.get(start);
			if (list.contains(end))
				return true;
		} else {

		}
		return false;
	}

	/*
	 * This method returns true if the house represented by the input String is
	 * locked and false is the house has been left unlocked.
	 */
	public boolean isLocked(String house) {
		if (unlocked_houses.contains(house))
			return false;
		else
			return true;
	}
	
	/*
	 * This method returns an array list of unlocked houses
	 */

	public ArrayList<String> getUnlockedHouses() {
		return unlocked_houses;
	}
	
	/*
	 * This method returns the main adjacency list hash map that represents the graph
	 */

	public Map<String, List<String>> getAdjList() {
		return adj_list;
	}
	
	/*
	 * This method returns the mapping of the number of keys for each particular key
	 */
	public Map<String,Integer> getNumKeysMapping(){
		return num_keys;
	}
}
