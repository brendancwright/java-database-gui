import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class MonsterManager extends Application {
 
	// window is a Stage object
	Stage window;
	
	// Bestiary object handles Bestiary methods
	Bestiary bestiary = new Bestiary();
	
	// TableView object of Monster objects
    TableView<Monster> table;
    
    // TextField objects for input
    TextField tfNameInput, tfFoodInput;
    
    // ObservableList of Monster objects is table data
    ObservableList<Monster> monsters = FXCollections.observableArrayList();
    
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
    	// The primary stage is the window
    	window = primaryStage;
        window.setTitle("Monster Diet Manager");

		// Label object serves as table title
		Label labelTitle = new Label("Monster Diets");
		labelTitle.setFont(new Font("Arial", 20));
		labelTitle.setPadding(new Insets(10, 0, 0, 0));
		
		// Create TableColumn to display Monster Name
        TableColumn<Monster, String> nameColumn = new TableColumn<>("Monster Name");
        nameColumn.setMinWidth(100);
        // Retrieves data to be stored in cell of this column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        // Create TableColumn to display Food
        TableColumn<Monster, String> foodColumn = new TableColumn<>("Food");
        foodColumn.setMinWidth(200);
        // Retrieves data to be stored in cell of this column
        foodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
 
        // Instantiate text field for monster input
        tfNameInput = new TextField();
        tfNameInput.setPromptText("Monster Name");
        tfNameInput.setMinWidth(100);
        
        // Instantiate text field for food input
        tfFoodInput = new TextField();
        tfFoodInput.setPromptText("Food");
        
        // Button adds new row with monster name input and food input
        Button btnNew = new Button("New Row");
        btnNew.setOnAction(e -> newButton());
        
        // Button adds food input to monster input if monster input already exists
        Button btnAddFood = new Button("Edit Food");
        btnAddFood.setOnAction(e -> editButton());
        
        // Searches for monster name only
        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> searchButton());
        
        // Display menu about program and how to use it
        Button btnHelp = new Button("Help");
        btnHelp.setOnAction(e -> {
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("Help");
        	alert.setHeaderText(null);
        	alert.setContentText(
        				"Load: Loads data into table from existing file.\n"
        				+ "Save: Saves data in table into a file. Will overwrite existing file.\n"
        				+ "New Row: Adds a new monster and what it eats into table.\n"
        				+ "Edit Food: Adds specified food to list of things specified monster eats.\n"
        				+ "Search: Searches for specified monster and displays what that monster eats.\n"
        				+ "\n\nYou may enter up to 100 monster names and up to 10 foods per monster."
        			);
        	alert.showAndWait();
        });
        
        // Puts monsterArray of Bestiary class into ObservableList monsters
        Button btnLoad = new Button("Load");
        btnLoad.setOnAction(e -> {
        	/* Load into monsters only if monsters is empty. This prevents overwriting on accidental
        	 * button click */
    		if (monsters.isEmpty()) {
    			try {
					bestiary.monsterListFromFile();
				} catch (FileNotFoundException e1) {
					// Display message letting user know there was no file found
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText(null);
					alert.setContentText("There was no file to load. Adding data and saving will "
							+ "create a new file automatically.");
					alert.showAndWait();
				}
    			monsters = getMonster();
    		}
			
        });
        
        /* Put the list of Monster objects into monsterList of Bestiary class and
         * save to text file. */
        Button btnSave = new Button("Save");
        btnSave.snappedBottomInset();
        btnSave.setOnAction(e -> {
        	// Put ObservableList of Monsters into monsterList ArrayList
			bestiary.setMonsterList(monsters);
			// Saves monsterList ArrayList to file
			try {
				bestiary.monsterListToFile();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
        
        // Horizontal box holds text fields and buttons at bottom of window
        HBox hb1 = new HBox();
        hb1.setPadding(new Insets(10, 10, 10, 10));
        hb1.setSpacing(10);
        hb1.getChildren().addAll(tfNameInput, tfFoodInput, btnNew, btnAddFood, btnSearch);
        
        // Horizontal box holds buttons at top of window
        HBox hb2 = new HBox();
        hb2.setSpacing(10);
        hb2.getChildren().addAll(btnHelp, btnSave, btnLoad);
        
        // AnchorPane formats title of table and buttons in hb2
        AnchorPane topPane = new AnchorPane();
        topPane.setPadding(new Insets(10, 10, 0, 10));
        topPane.getChildren().addAll(labelTitle, hb2);
        AnchorPane.setLeftAnchor(labelTitle, 0.0);
        AnchorPane.setTopAnchor(hb2, 0.0);
        AnchorPane.setRightAnchor(hb2, 0.0);
        
        // Instantiate the table, load with ObservableList monsters
        table = new TableView<>();
        table.setItems(monsters);
        
        // If ObservableList is empty, show basic directions
        table.setPlaceholder(new Label("Either load an existing file "
        		+ "or add data\nand save to automatically generate a new file."));
        table.getColumns().addAll(nameColumn, foodColumn);
        
        // Vertical box container
        VBox vBox = new VBox();
        vBox.getChildren().addAll(topPane, table, hb1);
        
        // Put the VBox on the scene, put the scene on the window, display window
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }
    
    
    public ObservableList<Monster> getMonster() {
    	for (Monster monster : bestiary.monsterList) {
    		String tempName = monster.getName();
    		String tempFoodList = monster.getFood();
    		
    		tempName = tempName.replaceAll("_", " ");
    		tempFoodList = tempFoodList.replaceAll("_", " ");
    		
    		monsters.add(new Monster(tempName, tempFoodList));
    	}
    	return monsters;
    }
    
    // Button adds new row with monster name input and food input
    public void newButton() {
    	// If both inputs aren't empty
    	if (!(tfNameInput.getText().equals("")) && !(tfFoodInput.getText().equals(""))) {
    		// Make a new monster objects, convert inputs to lower case, add to monsters
    		Monster monster = new Monster();
    		monster.setName(tfNameInput.getText().toLowerCase());
    		monster.setFood(tfFoodInput.getText().toLowerCase());
        	monsters.add(monster);
    	}
    	// Clear text fields
    	tfNameInput.clear();
    	tfFoodInput.clear();
    }
    
    // Searches for monster name only
    public void searchButton() {
    	boolean flag = false;
    	// If user did not enter any search data, let user know
    	if (tfNameInput.getText().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No Search Data");
			alert.setHeaderText(null);
			alert.setContentText("No search critera was entered.");
			alert.showAndWait();
		}
    	// Else, look through all monster objects for search criteria
    	else {
    		for (Monster monster : monsters) {
        		if (tfNameInput.getText().toLowerCase().equals(monster.getName())) {
        			// Show user search results
        			Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("Found");
        			alert.setHeaderText(null);
        			alert.setContentText(tfNameInput.getText() + " was found! "
        					+ "\n" + tfNameInput.getText() + " eats: " + monster.getFood());
        			alert.showAndWait();
        			flag = true;
        			break;
        		}
        		
        	}
    		// If search criteria was not found, let user know
    		if (!flag) {
    			Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("Not Found");
    			alert.setHeaderText(null);
    			alert.setContentText(tfNameInput.getText() + " was not found.");
    			alert.showAndWait();
    		}
    	}
    	// Clear text fields
    	tfNameInput.clear();
    	tfFoodInput.clear();
    }
    
    // Button adds food input to monster input if monster input already exists
    public void editButton() {
    	boolean flag = false;
    	// Search for monster in monsters, if found, add food input to existing food
    	for (Monster monster : monsters) {
    		if (tfNameInput.getText().toLowerCase().equals(monster.getName())) {
    			monster.setFood(monster.getFood() + ", " + tfFoodInput.getText().toLowerCase());
    			flag = true;
    		}
    	}
    	// Refreshes the table
    	if (flag) {
    		table.getColumns().get(0).setVisible(false);
    		table.getColumns().get(0).setVisible(true);
    	}
    	// Clear text fields
    	tfNameInput.clear();
    	tfFoodInput.clear();
    }
} 