/* Brendan Wright
 * 12-05-2017
 * Java version 8
 * Includes: Bestiary.java, Monster.java
 * COP2552.0M1
 * Final Project */

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Optional;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
public class MonsterManager extends Application {
	
	// Bestiary object handles Bestiary methods
	Bestiary bestiary = new Bestiary();
	
	// TableView object of Monster objects
    TableView<Monster> table;
    
    // TextField objects for input
    TextField tfNameInput, tfFoodInput;
    
    // Holds images
    ImageView view = new ImageView();
    
    // ObservableList of Monster objects is table data
    ObservableList<Monster> monsters = FXCollections.observableArrayList();
    
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Monster Diet Manager");

		// Table title
		Label labelTitle = new Label("Monster Diets");
		labelTitle.setFont(new Font("Arial", 20));
		labelTitle.setAlignment(Pos.CENTER_LEFT);
		labelTitle.setMinWidth(430);
		
		// Create TableColumn to display Monster Name
        TableColumn<Monster, String> nameColumn = new TableColumn<>("Monster Name");
        nameColumn.setMaxWidth(2250);
        // Retrieves data to be stored in cell of this column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        // Create TableColumn to display Food
        TableColumn<Monster, String> foodColumn = new TableColumn<>("Food");
        foodColumn.setMinWidth(200);
        // Retrieves data to be stored in cell of this column
        foodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
 
        // Text field for monster input
        tfNameInput = new TextField();
        tfNameInput.setPromptText("Monster Name");
        tfNameInput.setMinWidth(100);
        
        // Text field for food input
        tfFoodInput = new TextField();
        tfFoodInput.setPromptText("Food");
        
        // Adds new row with data from text field inputs
        Button btnNew = new Button("New Entry");
        btnNew.setOnAction(e -> newButton());
        
        // Edit row
        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> {
        	try {
				editButton();
			} catch (Exception RuntimeException) {
				// User must select a row
				errorMessage("Select the row you would like to edit.");
			}
        });
        
        // Search name, return foods
        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> searchButton());
        
        // Add image associated with row
        Button btnAddImage = new Button("Add Image");
        btnAddImage.setOnAction(e -> addImageButton());
        
        // How to use program
        Button btnHelp = new Button("Help");
        btnHelp.setOnAction(e -> helpButton());
        
        // Puts table data into text file
        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> saveButton());
        
        // Gets table data from text file
        Button btnLoad = new Button("Load");
        btnLoad.setOnAction(e -> loadButton());
        
        // Container for buttons and label at top of window
        HBox hbTop = new HBox();
        hbTop.setPadding(new Insets(10, 10, 10, 10));
        hbTop.setSpacing(10);
        hbTop.getChildren().addAll(labelTitle, btnHelp, btnSave, btnLoad);
        
        // Container for text fields and buttons at bottom of window
        HBox hbBottom = new HBox();
        hbBottom.setPadding(new Insets(10, 10, 0, 10));
        hbBottom.setSpacing(10);
        hbBottom.getChildren().addAll(tfNameInput, tfFoodInput, btnNew, btnEdit, btnSearch, btnAddImage);
        
        // Maintain aspect ratio of images
        view.setPreserveRatio(true);
        view.setFitWidth(250);
        
        // Load table with ObservableList monsters, set properties
        table = new TableView<>();
        table.setItems(monsters);
        table.setEditable(true);
        table.setMaxWidth(600);
        
        // Listens for a clicked row, show the image for that row
        table.setOnMousePressed(e -> {
        	Monster selectedCell = table.getSelectionModel().getSelectedItem();
        	try {
				view.setImage(selectedCell.getImage());
			} catch (Exception NullPointerException) {
				// Click detected, but no image associated with source
				;
			}
        });
        
        // Table is empty, show basic directions
        table.setPlaceholder(new Label("Either load an existing file "
        		+ "or add data\nand save to automatically generate a new file."));
        
        // Add columns to table
        table.getColumns().addAll(nameColumn, foodColumn);
        
        // Size columns to fit width of table instead of showing empty columns
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Container for table and HBoxes
        VBox vBox = new VBox();
        vBox.setMaxWidth(650);
        vBox.setPadding(new Insets(0, 10, 0, 10));
        vBox.getChildren().addAll(hbTop, table, hbBottom);
        
        // Container for VBox and ImageView
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 5, 0, 0));
        grid.add(vBox, 0, 0);
        grid.add(view, 1, 0);
        
        // Put the grid on the scene, scene on the stage, show stage
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        // File automatically found, ask user if they want to load it
        if (bestiary.isFile()) {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Load File");
        	alert.setHeaderText("File Found!");
        	alert.setContentText("Would you like to load the existing file?");

        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK) {
        	    loadButton();
        	}
        	else {
        		;
        	}
        }
    }
    
    // Put Monster data into ObservableList, return ObservableList
    public ObservableList<Monster> getMonsters() {
    	// Get name, food, and image path from Bestiary class
    	for (Monster monster : bestiary.monsterList) {
    		String tempName = monster.getName();
    		String tempFoodList = monster.getFood();
    		String tempPath = monster.getPath();
    		
    		// Reformat data for display
    		tempName = tempName.replaceAll("_", " ");
    		tempFoodList = tempFoodList.replaceAll("_", " ");
    		
    		// Create the Monster object, add to ObservableList
    		monsters.add(new Monster(tempName, tempFoodList, tempPath));
    	}
    	return monsters;
    }
    
    // Prompts user to pick image, returns image path
    public String getImage() {
    	// Stage object to display browse window
    	Stage window = null;
    	FileChooser fileChooser = new FileChooser();
    	File selectedFile;
    	
    	// Limit the types of files user can select
    	FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
    	
    	fileChooser.setTitle("Open Image");
    	
    	// Display browse window via FileChooser method, return file into File object
		selectedFile = fileChooser.showOpenDialog(window);
		// Convert the absolute file path to a URI
		URI uri = selectedFile.toURI();
		// Convert URI to String
		String path = uri.toString();
    	
    	// Return String representation of URI for Image object
    	return path;
    }
    
    // Adds new row with monster name input and food input
    public void newButton() {
    	// If both inputs aren't empty
    	if (!(tfNameInput.getText().equals("")) && !(tfFoodInput.getText().equals(""))) {
    		// Make a new monster objects, convert inputs to lower case, add to monsters
    		Monster monster = new Monster();
    		monster.setName(tfNameInput.getText().toLowerCase());
    		monster.setFood(tfFoodInput.getText().toLowerCase());
        	monsters.add(monster);
    	}
    	else {
    		errorMessage("Enter a monster and what it eats.");
    	}
    	// Clear text fields
    	tfNameInput.clear();
    	tfFoodInput.clear();
    }
    
    // Searches name, returns foods
    public void searchButton() {
    	boolean flag = false;
    	// User did not enter any search data, let user know
    	if (tfNameInput.getText().equals("")) {
			errorMessage("No search criteria was entered.");
		}
    	// Look through all monster objects for search criteria
    	else {
    		for (Monster monster : monsters) {
        		if (monster.getName().contains(tfNameInput.getText().toLowerCase())) {
        			// Display the image associated with located monster
        			ImageView imageView = new ImageView(monster.getImage());
        			imageView.setPreserveRatio(true);
        	        imageView.setFitWidth(250);
        			
        			// Show user search results
        			Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("Found");
        			alert.setContentText(monster.getName() + " was found! "
        					+ "\n" + monster.getName() + " eats: " + monster.getFood());
        			alert.setHeaderText(null);
        			alert.setGraphic(imageView);
        			alert.showAndWait();
        			flag = true;
        			break;
        		}
        		
        	}
    		// If search criteria was not found, let user know
    		if (!flag) {
    			errorMessage(tfNameInput.getText() + " was not found.");
    		}
    	}
    	// Clear text fields
    	tfNameInput.clear();
    	tfFoodInput.clear();
    }
    
    // Prompt user to edit selected row
    public void editButton() throws Exception {
    	// Get the Monster object of the selected row
    	Monster selectedCell = table.getSelectionModel().getSelectedItem();
    	
    	// User selection is not an existing Monster object
    	if (selectedCell == null) {
    		throw new RuntimeException();
    	}
    	
    	TextInputDialog dialog = new TextInputDialog("");
    	dialog.setTitle("Edit");
    	dialog.setHeaderText("Edit monster name and/or food.\nSeparate multiple foods with commas.");
    	
    	// Get name and food of Monster object, put it in a TextField
    	TextField tfName = new TextField(selectedCell.getName());
    	TextField tfFood = new TextField(selectedCell.getFood());
    	
    	// Container for TextFields and Labels
    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.add(new Label("Name: "), 0, 0);
    	grid.add(new Label("Food: "), 1, 0);
    	grid.add(tfName, 0, 1);
    	grid.add(tfFood, 1, 1);
    	
    	// Open a window, put the GridPane on it
    	dialog.getDialogPane().setContent(grid);
    	// Optional object gets true for OK button press, false for Cancel button press
    	Optional<String> result = dialog.showAndWait();
    	
    	// User clicks OK, take the data in the TextFields and update the Monster object
    	result.ifPresent(e -> {
    		selectedCell.setName(tfName.getText().toString());
    		selectedCell.setFood(tfFood.getText().toString());
    		// Refresh the table
    		table.getColumns().get(0).setVisible(false);
    		table.getColumns().get(0).setVisible(true);
    	});
    }
    
    // Gets a path to an image, put the path into Monster object, display image
    public void addImageButton() {
    	// Get Monster object of selected row
    	Monster selectedCell = table.getSelectionModel().getSelectedItem();
    	
    	// User selection is a Monster object
    	if (selectedCell != null) {
    		try {
    			// Prompt the user to find an image, store the path in Monster object
				selectedCell.setImage(getImage());
			} catch (Exception NullPointerException) {
				// If user cancels image selection, this catches the null path
				;
			}
    		// Put the image on the screen
			view.setImage(selectedCell.getImage());
    	}
    	// User selection is not a Monster object
    	else {
    		errorMessage("Select the row you would like to associate with an image.");
    	}
    }
    
    // Shows menu with program directions
    public void helpButton() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Help");
    	alert.setHeaderText(null);
    	alert.setContentText(
    				"Load: Loads data into table from existing file.\n\n"
    				+ "Save: Saves data in table into a file. Will overwrite existing file.\n\n"
    				+ "New Entry: Adds a new monster and what it eats into table."
    				+ " Separate multiple foods with commas.\n\n"
    				+ "Edit: Opens prompt to edit selected row.\n\n"
    				+ "Search: Searches for specified monster and displays what that monster eats.\n\n"
    				+ "Add Image: Pick an image to associate with selected row."
    			);
    	alert.showAndWait();
    }
    
    // Put table data into text file
    public void saveButton() {
    	// Save only works if there is data in the table, this prevents "deleting" an existing file
    	if (!monsters.isEmpty()) {
	    	// Put ObservableList of Monsters into monsterList ArrayList
			bestiary.setMonsterList(monsters);
			// Saves monsterList ArrayList to file
			try {
				bestiary.monsterListToFile();
			} catch (Exception FileNotFoundException) {
				;
			}
    	}
    }
    
    // Put data from text file into table
    public void loadButton() {
    	/* Load into monsters only if monsters is empty. This prevents overwriting on accidental
    	 * button click */
    	if (monsters.isEmpty()) {
			try {
				bestiary.monsterListFromFile();
			} catch (FileNotFoundException e1) {
				// Display message letting user know there was no file found
				errorMessage("There was no file to load. Adding data and saving will "
						+ "create a new file automatically.");
			}
			monsters = getMonsters();
		}
    }
    
    // Displays a window with String errorMessage as message
    public void errorMessage(String errorMessage) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
    }
} 