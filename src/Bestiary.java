/* Brendan Wright
 * 11-13-2017
 * Java version 8
 * COP2552.0M1
 * Project 4 */

import java.util.Scanner;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Bestiary {
	
	ArrayList<Monster> monsterList = new ArrayList<>();
	
	public void monsterListToFile() throws FileNotFoundException {
		File openFile = new File("monster_data.txt");
		PrintWriter saveFile = new PrintWriter(openFile);
		
		for (Monster monster : monsterList) {
			saveFile.print(monster.getName() + " " + monster.getFood());
			saveFile.print("\r\n");
		}
		saveFile.close();
	}
	
	public void monsterListFromFile() throws FileNotFoundException {
		File openFile = new File("monster_data.txt");
		Scanner input = new Scanner(openFile);
		
		while (input.hasNextLine()) {
			String[] words = new String[2];
			words = input.nextLine().split("\\s");
			
			String tempName = words[0];
			String tempFoodList = words[1];
			
			monsterList.add(new Monster(tempName, tempFoodList));
		}
	}
	
	public void setMonsterList(ObservableList<Monster> oList) {
    	monsterList.clear();
    	for (Monster monster : oList) {
    		String tempName = monster.getName().trim();
    		String tempFoodList = monster.getFood().trim();
    		
    		tempName = tempName.replaceAll("\\s", "_");
    		tempFoodList = tempFoodList.replaceAll("\\s", "_");
    		
    		monsterList.add(new Monster(tempName, tempFoodList));
    	}
    }
}