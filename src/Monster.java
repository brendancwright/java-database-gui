/* Brendan Wright
 * 12-05-2017
 * Java version 8
 * COP2552.0M1
 * Final Project */

import javafx.scene.image.Image;

// Monster class to create Monster objects to store in ObservableList of MonsterManager.java
public class Monster {

    private String monsterName;
    private String foodList;
    private Image image;
    private String path; //path for image
    
    public Monster() {
    	this.monsterName = "";
    	this.foodList = "";
    	this.path = "images/no_image.png";
    	this.image = new Image(path);
    }
    
    public Monster(String monsterName, String foodList) {
        this.monsterName = monsterName;
        this.foodList = foodList;
    }
    
    public Monster(String monsterName, String foodList, String path) {
    	this.monsterName = monsterName;
    	this.foodList = foodList;
    	this.path = path;
    	try {
    		// Set image based on path
			this.image = new Image(path);
		} catch (Exception IllegalArgumentException) {
			// Path was null, or invalid
			this.path = "images/no_image.png";
			;
		}
    }
    
    public String getName() {
        return monsterName;
    }

    public void setName(String monsterName) {
        this.monsterName = monsterName;
    }
    
    public void setFood(String foodList) {
    	this.foodList = foodList;
    }
    
    public String getFood() {
    	return foodList;
    }
    
    public void setImage(String path) {
    	this.path = path;
    	this.image = new Image(path);
    }
    
    public Image getImage() {
    	return image;
    }
    
    public String getPath() {
    	return path;
    }
}
