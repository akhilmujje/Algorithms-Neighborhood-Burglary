/*
 * Name: Devisriram Akhil Mujje
 * EID: dam4335
 */
public class Driver {

	/*
	 * This driver is provided to help you test your program. You can make
	 * changes to this file, but we will not use them for grading your program.
	 * We will use our own driver for grading your program. Hence, the
	 * correctness of your algorithms should not be dependent on the code in
	 * this file.
	 */
	public static void main(String[] args) {
		Graph neighborhood = new Graph("unlocked2.txt", "keys2.txt");

		Robber fruitcake = new Robber();
		fruitcake.canRobAllHouses(neighborhood);
		fruitcake.maximizeLoot("ingredients2.txt");
		fruitcake.scheduleMeetings("buyers.txt");
	}
}
