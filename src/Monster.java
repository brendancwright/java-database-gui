
// Monster class to create Monster objects to store in ObservableList of MonsterManager.java
public class Monster {

    private String monsterName;
    private String foodList;
    
    public Monster() {
    	this.monsterName = "";
    	this.foodList = "";
    }
    
    public Monster(String monsterName, String foodList) {
        this.monsterName = monsterName;
        this.foodList = foodList;
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
    
}
