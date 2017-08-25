import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/*
 * Name: Devisriram Akhil Mujje
 * EID: dam4335
 */
public class Robber {

	// helper inner class for scheduleMeetings method to represent intervals
	class Interval {
		public int st_hour;
		public int st_min;
		public int fin_hour;
		public int fin_min;
		public String name;
		public String times;
		public int newIndex;

		public Interval(String name, String times, int st_hour, int st_min, int fin_hour, int fin_min, int newIndex) {
			this.st_hour = st_hour;
			this.st_min = st_min;
			this.fin_hour = fin_hour;
			this.fin_min = fin_min;
			this.name = name;
			this.times = times;
			this.newIndex = newIndex;
		}

	}

	/*
	 * This method should return true if the robber can rob all the houses in
	 * the neighborhood, which are represented as a graph, and false if he
	 * cannot. The function should also print to the console the order in which
	 * the robber should rob the houses if he can rob the houses. You do not
	 * need to print anything if all the houses cannot be robbed.
	 */
	public boolean canRobAllHouses(Graph neighborhood) {

		Map<String, List<String>> adj_list = neighborhood.getAdjList();
		Map<String, Integer> num_keys = neighborhood.getNumKeysMapping();
		Set<String> houses = adj_list.keySet();
		int houses_size = houses.size();

		ArrayList<String> init_houses = new ArrayList<String>();
		for (String s : houses) {
			init_houses.add(s);
		}

		ArrayList<String> unlocked_houses = neighborhood.getUnlockedHouses();
		ArrayList<String> init_searched = new ArrayList<String>();
		ArrayList<String> all = new ArrayList<String>();
		ArrayList<String> neighbors;

		String start = "";

		// go through all the houses or nodes in graph and check the unlocked
		// houses since
		// they are the place where robber starts
		// if all of them are locked, return false because robber cannot enter
		// any one
		// else select the first, random unlocked house and place that as start
		// if you encounter a house that has already been searched, continue

		boolean initial_start_done = true;

		while (initial_start_done) {

			if (init_searched.size() == houses.size()) {
				return false;
			}

			for (String first : init_houses) {
				if (init_searched.contains(first))
					continue;

				start = first;
				init_searched.add(start);

				break;
			}

			if (neighborhood.isLocked(start)) {
				init_houses.remove(start);
				continue;

			} else {
				initial_start_done = false;
			}
		}

		while (!(houses.isEmpty())) {

			neighbors = (ArrayList<String>) adj_list.get(start);

			// if this house has no neighbors, this is the end

			if (neighbors == null || neighbors.size() == 0) {

				houses.remove(start);

				if (!(all.contains(start)))
					all.add(start);

				for (int i = 0; i < unlocked_houses.size(); i++) {
					if (!(all.contains(unlocked_houses.get(i)))) {
						all.add(unlocked_houses.get(i));
					}
				}

				if (all.size() == houses_size) {

					outputHouses(all);

					return true;

				} else {
					return false;
				}

			} else {

				// else, add this house to path, if not already in it
				if (!(all.contains(start)))
					all.add(start);

				ArrayList<String> list = (ArrayList<String>) adj_list.get(start);

				// remove any houses from the list that have already been
				// explored
				// because there is no need to go in them

				removeRemovedValues(list, houses);

				// reduce the key counts for all of the keys each house has
				// because every time you land on a house, you gain keys
				// if there are any houses for which you have all keys,
				// that will be your next target
				for (int i = 0; i < neighbors.size(); i++) {

					String next = list.get(i);

					int num = num_keys.get(next).intValue();
					num--;
					num_keys.put(next, num);

					if (num == 0) {
						houses.remove(start);
						start = next;
					}

				}

			}

		}

		return false;

	}

	/*
	 *
	 */
	public void maximizeLoot(String lootList) {
		// FILE I/O
		File loot = new File(lootList);
		Scanner loot_input;
		try {
			loot_input = new Scanner(loot);
		} catch (FileNotFoundException e) {
			System.out.println("File not found! maximizeLoot did not run");
			return;
		}

		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> items = new ArrayList<String>();
		ArrayList<Double> weights = new ArrayList<Double>();
		ArrayList<Double> pricePerPound = new ArrayList<Double>();
		Map<Double, String> pricePP_item_map = new HashMap<Double, String>();
		Map<String, Double> item_weight_map = new HashMap<String, Double>();

		// retrieve data
		while (loot_input.hasNextLine()) {

			list.add(loot_input.nextLine());
		}
		loot_input.close();

		double maxWeight = Double.parseDouble(list.get(0));

		String comma_delims = ", ";
		String[] arr1;

		for (int i = 1; i < list.size(); i++) {
			arr1 = list.get(i).split(comma_delims);
			items.add(arr1[0]);
			weights.add(Double.parseDouble(arr1[1]));
			pricePerPound.add(Double.parseDouble(arr1[2]));
			pricePP_item_map.put(pricePerPound.get(i - 1), items.get(i - 1));

		}

		// sort the values of pricerPerPound in non-decreasing order
		Collections.sort(pricePerPound);

		double current_weight = 0; // current weight
		double precision = 0.01;

		for (int i = pricePerPound.size(); i >= 0; i--) {

			double curr_ppp = pricePerPound.get(i - 1);
			String item_name = pricePP_item_map.get(curr_ppp);
			int index = items.indexOf(item_name);
			double item_weight = weights.get(index);

			if (current_weight + item_weight <= maxWeight) {

				current_weight += item_weight;
				item_weight_map.put(item_name, item_weight);
				weights.set(index, 0.00);

			} else {

				double remaining = maxWeight - current_weight;

				if (remaining < 0.00) {
					break;
				}

				double fract_item_weight;
				for (fract_item_weight = 0.00; fract_item_weight < remaining; fract_item_weight += precision) {

					current_weight += precision;

				}
				item_weight_map.put(item_name, fract_item_weight);
				weights.set(index, item_weight - fract_item_weight);
			}

		}

		outputIngredients(item_weight_map);

	}

	public void scheduleMeetings(String buyerList) {
		// FILE I?O

		File buyer = new File(buyerList);
		Scanner buyer_input;
		try {
			buyer_input = new Scanner(buyer);
		} catch (FileNotFoundException e) {
			System.out.println("File not found! scheduleMeetings did not run");
			return;
		}

		ArrayList<String> list = new ArrayList<String>();

		ArrayList<String> buyers = new ArrayList<String>();
		ArrayList<String> times = new ArrayList<String>();

		ArrayList<String> start = new ArrayList<String>();
		ArrayList<Integer> start_hour = new ArrayList<Integer>();
		ArrayList<Integer> start_min = new ArrayList<Integer>();

		ArrayList<String> finish = new ArrayList<String>();
		ArrayList<Integer> finish_hour = new ArrayList<Integer>();
		ArrayList<Integer> finish_min = new ArrayList<Integer>();

		// retrieve data
		while (buyer_input.hasNextLine()) {

			list.add(buyer_input.nextLine());
		}
		buyer_input.close();

		String comma_delims = ", ";
		String hyphen_delims = "-";
		String colon_delims = ":";
		String[] arr;

		for (int i = 0; i < list.size(); i++) {
			arr = list.get(i).split(comma_delims);
			buyers.add(arr[0]);
			times.add(arr[1]);
		}

		for (int i = 0; i < buyers.size(); i++) {
			arr = times.get(i).split(hyphen_delims);
			start.add(arr[0]);
			finish.add(arr[1]);
		}

		int hour = 0;
		int min = 0;

		boolean am;

		for (int i = 0; i < start.size(); i++) {

			String time = start.get(i);

			if (time.substring(time.length() - 2).equals("am")) {

				am = true;

			} else {

				am = false;

			}

			// remove am or pm from time string
			time = time.substring(0, time.length() - 2);

			// split time string into hours and minutes
			arr = time.split(colon_delims);

			if (arr.length == 1) {
				hour = Integer.parseInt(arr[0]);
			} else {
				hour = Integer.parseInt(arr[0]);
				min = Integer.parseInt(arr[1]);
			}

			// if 12am, make it 0 for military
			if (am) {
				if (hour == 12)
					hour = 0;
			} else {

				// if pm, add 12 hours to convert to military hours

				hour += 12;
				if (hour == 24)
					hour = 0;
			}

			start_hour.add(hour);
			start_min.add(min);
		}

		hour = 0;
		min = 0;

		for (int i = 0; i < finish.size(); i++) {

			String time = finish.get(i);

			if (time.substring(time.length() - 2).equals("am")) {

				am = true;

			} else {

				am = false;

			}

			// remove am or pm from time string
			time = time.substring(0, time.length() - 2);

			// split time string into hours and minutes
			arr = time.split(colon_delims);

			if (arr.length == 1) {
				hour = Integer.parseInt(arr[0]);
			} else {
				hour = Integer.parseInt(arr[0]);
				min = Integer.parseInt(arr[1]);
			}

			// if 12am, make it 0 for military
			if (am) {
				if (hour == 12)
					hour = 0;
			} else {
				// if pm, add 12 hours to convert to military hours
				hour += 12;
				if (hour == 24)
					hour = 0;
			}

			finish_hour.add(hour);
			finish_min.add(min);
		}

		ArrayList<Integer> new_finish_times = new ArrayList<Integer>();
		ArrayList<Integer> visitedIndices = new ArrayList<Integer>();
		
		ArrayList<Interval> intervals = new ArrayList<Interval>();
		ArrayList<Interval> final_set = new ArrayList<Interval>();

		//selection sort for earliest finish time by hour
		
		int k = finish_hour.size() - 1;
		int low = finish_hour.get(k);
		int low_index = k;
		for (int j = finish_hour.size() - 1; j >= 0; j--) {
			for (; k >= 0; k--) {
				if (visitedIndices.contains(k))
					continue;
				if (finish_hour.get(k) < low) {
					low = finish_hour.get(k);
					low_index = k;
				}

			}

			visitedIndices.add(low_index);

			Interval fresh = new Interval(buyers.get(low_index), times.get(low_index), start_hour.get(low_index),
					start_min.get(low_index), low, finish_min.get(low_index), new_finish_times.size());

			new_finish_times.add(low);
			intervals.add(fresh);

			if (intervals.size() == 1) {
				final_set.add(fresh);
			}

			k = finish_hour.size() - 1;
			while (visitedIndices.contains(k)) {
				k--;
			}

			if (k >= 0) {
				low = finish_hour.get(k);
				low_index = k;
			}

		}

		int fin_hour = intervals.get(0).fin_hour;
		int fin_min = intervals.get(0).fin_min + 15;
		for (int i = 1; i < new_finish_times.size(); i++) {

			if (fin_min >= 60) {
				fin_min -= 60;
				fin_hour++;
			}

			int st_hour = intervals.get(i).st_hour;
			int st_min = intervals.get(i).st_min;

			
			if (st_hour >= fin_hour && st_min >= fin_min) {
				
				
				final_set.add(intervals.get(i));
				fin_hour = intervals.get(i).fin_hour;
				fin_min = intervals.get(i).fin_min + 15;
			}

		}

		outputTimings(final_set);

	}

	private void outputHouses(ArrayList<String> order) {
		for (int i = 0; i < order.size() - 1; i++) {
			System.out.print(order.get(i) + ", ");
		}

		System.out.print(order.get(order.size() - 1));
		System.out.println();
	}

	private void outputIngredients(Map<String, Double> item_weight_map) {
		Set<String> item_names = item_weight_map.keySet();

		for (String s : item_names) {
			System.out.print(s + " ");
			System.out.print(item_weight_map.get(s) + "\n");
		}

	}

	private void outputTimings(ArrayList<Interval> timings) {
		for (Interval i : timings) {
			System.out.println(i.name);
		}

	}

	private void removeRemovedValues(ArrayList<String> list, Set<String> houses) {
		for (int i = list.size() - 1; i >= 0; i--) {
			String s = list.get(i);
			if (!(houses.contains(s))) {
				list.remove(s);
			}
		}
	}
}
