/* Brendan Wright
 * 12-05-2017
 * Java version 8
 * COP2552.0M1
 * Final Project */

import javafx.collections.ObservableList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Bestiary {
	
	// Store Monster objects
	ArrayList<Monster> monsterList = new ArrayList<>();
	
	// Put members of Monster objects stored in monsterList into text file
	public void monsterListToFile() throws FileNotFoundException {
		File openFile = new File("monster_data.txt");
		PrintWriter saveFile = new PrintWriter(openFile);
		
		// Stores name, food, and image path for each Monster object
		for (Monster monster : monsterList) {
			saveFile.print(monster.getName() + " " + monster.getFood() + " " + monster.getPath());
			saveFile.print("\r\n");
		}
		saveFile.close();
	}
	
	// Reads a text file and populates monsterList with data.
	public void monsterListFromFile() throws FileNotFoundException {
		File openFile = new File("monster_data.txt");
		Scanner input = new Scanner(openFile);
		
		/* Each line should be one Monster object. Each member should be delimited by a space.
		 * First member ([0]) should be name, second member ([1]) should be foods delimited by
		 * an underscore, and third member ([2]) should be path of image as URI
		 */
		while (input.hasNextLine()) {
			String[] words = new String[3];
			// Create array of elements from text file. Spaces delimit each element
			words = input.nextLine().split("\\s");
			
			String tempName = words[0];
			String tempFoodList = words[1];
			String tempPath;
			try {
				tempPath = words[2];
			} catch (Exception ArrayIndexOutOfBoudnsException) {
				// No path was associated with this Monster object. Empty string used to avoid null path.
				tempPath = "";
			}
			
			// Create the Monster object
			monsterList.add(new Monster(tempName, tempFoodList, tempPath));
		}
	}
	
	// Accepts ObservableList, copies it into an ArrayList
	public void setMonsterList(ObservableList<Monster> oList) {
		// Current ArrayList is cleared, this prevents duplicate entries
    	monsterList.clear();
    	for (Monster monster : oList) {
    		String tempName = monster.getName().trim();
    		String tempFoodList = monster.getFood().trim();
    		String tempPath = monster.getPath();
    		
    		/* Space characters are replaced with underscore characters so that when this
    		 * data is read back in from a text file, spaces can be used as delimiters to separate
    		 * each piece of data about the Monster object. This is not done with the path since a URI
    		 * may contain underscore characters.
    		 */
    		tempName = tempName.replaceAll("\\s", "_");
    		tempFoodList = tempFoodList.replaceAll("\\s", "_");
    		
    		monsterList.add(new Monster(tempName, tempFoodList, tempPath));
    	}
    }
	
	// Checks if file exists
	public Boolean isFile() {
		File openFile = new File("monster_data.txt");
		
		return openFile.exists();
	}
}