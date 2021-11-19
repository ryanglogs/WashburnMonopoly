import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.HPos;

import java.io.*;
public class MonopolyGUI extends Application
{
    StackPane root = new StackPane();//main pane that holds all the other panes in it. this is the one that is displayed in the scene
    Pane players = new Pane();//pane that displays they players coins wherever they are
    StackPane image = new StackPane();//displays the board image
    GridPane gridPane = new GridPane();//holds all the buttons
    GridPane menu = new GridPane();//displays the text areas
        
    private TextArea taDisplayBalance = new TextArea();//displays events that happen in the game    
    private TextArea taDisplayActions = new TextArea();//constantly displays players balances as well as free tuition balances
    private Button btRoll = new Button("Roll");//rolls a six sided die and moves you the corresponding amount of tiles
    private Button btChance = new Button("Get your chance card");//button to display the chance card if you land on a chance tile
    private Button btEndTurn = new Button("End turn");//button to end turn also increases count by 1
    private Button btStartTurn = new Button("Start turn");//button to start a players turn
    private Button btStartGame = new Button("Start game");//button to start the game
    private Button btBuy = new Button("Buy property");//button to buy an available property
    private Button btTest = new Button("test");
    
    private Circle p1 = new Circle(875,875,15,Color.ORANGE);//player 1 coin
    private Circle p2 = new Circle(905,875,15,Color.BLUE);//player 2 coin
    private Circle p3 = new Circle(875,905,15,Color.PURPLE);//player 3 coin
    private Circle p4 = new Circle(905,905,15,Color.GREEN);//player 4 coin
    
    private Game game = new Game();//creates a new game object which allows the interaction with player and property objects
    
    private int spacesMoved;//global spaces moved for use in any method where needed. holds the value of the die rolled when rolled
    private int chance;//global chance that represents the chance card rolled when you land and collect a chance card
    @Override
    public void start(Stage primaryStage){
        //sets up gui layout
        Image board = new Image("Wopoly_board.PNG");//image containing the monopoly board
        ImageView display = new ImageView();//imageview containing the board image
        display.setImage(board);//displays the image
        gridPane.setHgap(55);//this is the value that is horizontally between buttons 
        gridPane.setVgap(25);//this is the value that is vetically between buttons in the button menu
        menu.setHgap(5);//value that is between the text areas
        menu.setVgap(5);//value that is vetically between the text areas
        image.getChildren().add(display);//adds the image to the very bottom pane which displays the monopoly board
        
        //this sets up the vertical stack of all the buttons that will be used.
        //they are all in column 4 because that is how far away they need to be in order to not be on the very far left side of the window
        gridPane.add(btStartGame,4,0);
        gridPane.add(btStartTurn,4,1);
        gridPane.add(btRoll,4,2);
        gridPane.add(btBuy,4,3);
        gridPane.add(btChance,4,4);
        gridPane.add(btEndTurn,4,5);
        gridPane.add(btTest,4,6);
        
        //sets up verical stack of the two text areas
        menu.add(taDisplayActions,22,2);
        menu.add(taDisplayBalance,22,3);
        //sets up size of the text area that displays the balances
        //i disabled the editable feature because there will be no need for user input 
        taDisplayBalance.setPrefColumnCount(25);
        taDisplayBalance.setPrefRowCount(5);
        taDisplayBalance.setEditable(false);
        taDisplayBalance.setWrapText(true);//wrap text means that if a text string goes over the given width, it will wrap it around to a new line automatically.
                                          // theoretically wrap text shouldnt be   needed for this text area but i included just in case
        //sets up the size of the text area that displays all events
        //i disabled the editable feature because there will be no need for user input 
        taDisplayActions.setPrefColumnCount(25);
        taDisplayActions.setPrefRowCount(10);
        taDisplayActions.setEditable(false);
        taDisplayActions.setWrapText(true);//wrap text means that if a text string goes over the given width, it will wrap it around to a new line automatically.
        
        //setting alignment of all the buttons and panes
        //most buttons start off as disabled because there is no need to click the buy button when you first start the game
        //the buttons that are disabled will be enabled when they are needed/available to be pressed
        gridPane.setAlignment(Pos.CENTER_LEFT);
        menu.setAlignment(Pos.CENTER);
        
        btStartGame.setAlignment(Pos.TOP_RIGHT);
        
        btStartTurn.setAlignment(Pos.TOP_RIGHT);
        btStartTurn.setDisable(true);
        
        btRoll.setAlignment(Pos.TOP_RIGHT);
        btRoll.setDisable(true);
        
        btBuy.setAlignment(Pos.TOP_RIGHT);
        btBuy.setDisable(true);
        
        btChance.setAlignment(Pos.TOP_RIGHT);
        btChance.setDisable(true);
               
        btEndTurn.setAlignment(Pos.TOP_RIGHT);
        btEndTurn.setDisable(true);
        
        //event handlers for when the buttons is clicked
        btStartGame.setOnAction(e -> startGame());//when pressed it will activate all the buttons and start the game
        btStartTurn.setOnAction(e -> startTurn());//when pressed it will displays whos turn it is and disables itself and allows roll and end turn to be clicked
        btRoll.setOnAction(e -> rollDie());//when pressed it will roll the die for whoevers turn it is
        btBuy.setOnAction(e -> buy());//when a player lands on a property that is available it will become clickable, when clicked it will subtract the property cost from the players balance
                                      //and make the owner of the property in the property array equal to whomevers turn it is
        btChance.setOnAction(e -> getChance());//when pressed it will display the chance card text and do any moves or calculations required to complete chance card text
        btEndTurn.setOnAction(e -> endTurn());//when pressed it will increase count by 1 and make all buttons except start turn and info unclickable
        btTest.setOnAction(e -> test());
        
        //creates the scene and displays the gui
        primaryStage.setTitle("Washburn-Opoly");
        root.getChildren().addAll(image,players,menu,gridPane);
        Scene scene = new Scene(root,985,985);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    //when the StartGame button is pressed it will call this method which displays the welcome text,
    //displays the player coins and what player corresponds to which colored coin, initializes all the variables
    //and arrays needed to play the game, and makes the start turn button clickable.
    private void startGame(){
        btStartTurn.setDisable(false);
        btStartGame.setDisable(true);
        taDisplayActions.appendText("Welcome to Washburn-opoly. \nCreated by Ryan Glogau\n");
        taDisplayActions.appendText("Player 1 is orange, player 2 is blue, player 3 is purple, and player 4 is green");
        taDisplayBalance.appendText(String.format("Free Tuition: %1d%n",game.getFreeTuition()));
        taDisplayBalance.appendText(String.format("Player 1: %1d.%n",game.getBalance(0)));
        taDisplayBalance.appendText(String.format("Player 2: %1d.%n",game.getBalance(1)));
        taDisplayBalance.appendText(String.format("Player 3: %1d.%n",game.getBalance(2)));
        taDisplayBalance.appendText(String.format("Player 4: %1d.%n",game.getBalance(3)));
        //try catch block will set try to create the player and property information. 
        //it reads property information from a file and if the program cant find the file it disables all buttons and displays an error message
        //if the error message pops up the user must exit out of the game and go in and fix the properties file in order to fix the error
        try{
            game.startGame();
        }catch (FileNotFoundException ex){
            btStartGame.setDisable(true);
            btStartTurn.setDisable(true);
            btRoll.setDisable(true);
            btBuy.setDisable(true);
            btChance.setDisable(true);
            btEndTurn.setDisable(true);
            taDisplayActions.setText("Error loading data.");
        }
        //this displays the players coins in the go tile when they start the game
        players.getChildren().add(p1);
        players.getChildren().add(p2);
        players.getChildren().add(p3);
        players.getChildren().add(p4);
    }
    //When pressed it displays whos turn it is, makes roll button clickable, and disables the startturn button
    private void startTurn(){
        taDisplayActions.setText("\u000c");
        btStartTurn.setDisable(true);
        btRoll.setDisable(false);
        btEndTurn.setDisable(false);
        taDisplayActions.setText(String.format("Player %1s's turn.%n",game.getCount()+1));
        taDisplayActions.appendText(String.format("Player %1s is on tile: %2d.%n",game.getCount()+1,game.getTileOn(game.getCount())));
    }
    //when pressed it will generate a random number from 1-6, move the player the given number of spaces
    //and displays the correct coin in the correct property, if the property is available the buy button will become clickable
    //and in the textarea it will print out that the property is available to buy.
    
    //game.move will set the current players tileon equal to the current tileon plus spaces moved
    private void rollDie(){
        btStartTurn.setDisable(true);
        btRoll.setDisable(true);
        spacesMoved = game.rollDie();
        taDisplayActions.appendText(String.format("You rolled a %1d.%n",spacesMoved));//putputs what the player rolled
        boolean passed = game.passedGO(game.getCount(),spacesMoved);//tests whether the player passes go
        if(passed==true){//if you did pass go
            taDisplayActions.appendText(game.movePastGo(game.getCount()));
            game.move(spacesMoved,game.getCount());
            if(game.getPropAvailable(game.getCount()) == true){//if the property is available the player will be given a chance to buy it
                taDisplayActions.appendText(String.format("You landed on %1s. You may buy the property.%n",game.getProperty(game.getCount())));
                btBuy.setDisable(false);
            } else {//if the property is unavailable after they pass go, it either means they landed on chance or a property already owned by a player
                if(game.getTileOn(game.getCount()) == 4 || game.getTileOn(game.getCount()) == 8  ||game.getTileOn(game.getCount()) == 16){
                    taDisplayActions.appendText("You landed on chance. Get your card.\n");
                    btChance.setDisable(false);
                } else{
                    taDisplayActions.appendText(String.format("You landed on %1s. Rent is $%1d.%n",game.getProperty(game.getCount()),game.getPropRent(game.getCount())));
                    payRent();
                }
            }
            moveCoin();//moveCoin moves the current players coin to the proper tile
        }else if (passed==false){//if you didnt pass go
            if(game.getJail(game.getCount())){//tests to see if the player is in jail
                if(game.getJC(game.getCount()) <= 3){//getjailcount is the value that holds how mnay turns a player has been in jail.
                    taDisplayActions.appendText("You are in jail and will not move");
                    game.setJC(game.getCount());
                }else if(game.getJC(game.getCount()) > 3){//last turn in jail so they can move when they roll
                    taDisplayActions.appendText("You are no longer in jail and may move now.\n");
                    game.setJail(game.getCount(),false);
                    game.resetJC(game.getCount());
                    game.move(spacesMoved,game.getCount());
                    moveCoin();
                    if(game.getPropAvailable(game.getCount()) == true){//this tesets wheter you landed on an available property or not
                        taDisplayActions.appendText(String.format("You landed on %1s. You may buy the property.%n",game.getProperty(game.getCount())));
                        btBuy.setDisable(false);
                    } else {//if the player did not land on an available property that means they landed on either a chance tile,go to jail, jail, or free tuition. 
                            //this tests for what tile they land on and performs the correct operations
                         if(game.getTileOn(game.getCount()) == 4 || game.getTileOn(game.getCount()) == 8  ||game.getTileOn(game.getCount()) == 16){//if the player landed on chance
                             taDisplayActions.appendText("You landed on chance. Get your card.\n");
                             btChance.setDisable(false);
                         }else if(game.getTileOn(game.getCount()) == 18){//if the player landed on go to jail
                             game.setTileOn(game.getCount(),6);
                             if(game.getCount() == 0){
                                 p1.setCenterX(130);
                                 p1.setCenterY(945);
                             }else if(game.getCount() == 1){
                                 p2.setCenterX(160);
                                 p2.setCenterY(945);
                             }else if(game.getCount() == 2){
                                 p3.setCenterX(130);
                                 p3.setCenterY(975);
                             }else if(game.getCount() == 3){
                                 p4.setCenterX(160);
                                 p4.setCenterY(975); 
                             }
                             taDisplayActions.appendText("You got sent to jail and will not collect $200 from passing go\n");
                             taDisplayActions.appendText("You will be in jail for 3 turns, you may roll, but you may not move.\n");
                             taDisplayActions.appendText("You will still collect rent.\n");
                             game.setJail(game.getCount(),true);
                         }else{ //if the player landed on any other tile that is unavailable that means that the tile is already owned by another player. rent is payed automatically but it will display the value of the rent for that property
                            taDisplayActions.appendText(String.format("You landed on %1s. Rent is $%1d.%n",game.getProperty(game.getCount()),game.getPropRent(game.getCount())));
                            game.payRent(game.getCount());
                        }
                    }
                }
            }else if (!game.getJail(game.getCount())){//if the player is not in jail
                game.move(spacesMoved,game.getCount());
                if(game.getPropAvailable(game.getCount()) == true){//if the property is available
                    taDisplayActions.appendText(String.format("You landed on %1s. You may buy the property.%n",game.getProperty(game.getCount())));
                    btBuy.setDisable(false);
                    moveCoin();
                } else {//if the property is not available
                     if(game.getTileOn(game.getCount()) == 4 || game.getTileOn(game.getCount()) == 8  ||game.getTileOn(game.getCount()) == 16){//tests if they are on a chance tile
                         taDisplayActions.appendText("You landed on chance. Get your card.\n");
                         btChance.setDisable(false);
                         moveCoin();
                     }else if(game.getTileOn(game.getCount()) == 18){//go to jail tile
                         if(game.getCount() == 0){
                                 p1.setCenterX(130);
                                 p1.setCenterY(945);
                         }else if(game.getCount() == 1){
                                 p2.setCenterX(160);
                                 p2.setCenterY(945);
                         }else if(game.getCount() == 2){
                                 p3.setCenterX(130);
                                 p3.setCenterY(975);
                         }else if(game.getCount() == 3){
                                 p4.setCenterX(160);
                                 p4.setCenterY(975);
                         }
                         taDisplayActions.appendText("You got sent to jail and will not collect $200 from passing go\n");
                         taDisplayActions.appendText("You will be in jail for 3 turns, you may roll, but you may not move.\n");
                         taDisplayActions.appendText("You will still collect rent.\n");
                         game.setTileOn(game.getCount(),6);
                         game.setJail(game.getCount(),true);
                         moveCoin();
                     }else if(game.getTileOn(game.getCount()) == 12){//if the player lands on free tuition
                         taDisplayActions.appendText(String.format("You landed on free tuition and collected %1d",game.getFreeTuition()));
                         game.collectFree(game.getCount());
                         taDisplayBalance.setText(String.format("Free Tuition: %1d%n",game.getFreeTuition()));
                         taDisplayBalance.appendText(String.format("Player 1: %1d.%n",game.getBalance(0)));
                         taDisplayBalance.appendText(String.format("Player 2: %1d.%n",game.getBalance(1)));
                         taDisplayBalance.appendText(String.format("Player 3: %1d.%n",game.getBalance(2)));
                         taDisplayBalance.appendText(String.format("Player 4: %1d.%n",game.getBalance(3)));
                         moveCoin();
                    }else {//if the player landed on anything not included in the if statements above that means that they landed on a player owned property and must pay rent.
                        taDisplayActions.appendText(String.format("You landed on %1s. Rent is $%1d.%n",game.getProperty(game.getCount()),game.getPropRent(game.getCount())));
                        moveCoin();
                        payRent();
                    }
                }
            }
        }
    }
    //when pressed it subtracts the property price from the current players balance, and assigns
    //the owner of the property to the property. ie, if player 1 buys yager stadium it sets yager 
    //stadiums owner equal to 0 for player 1
    private void buy(){
        btBuy.setDisable(true);
        game.buyProp(game.getCount());
        //if time allows for it: add a try catch block that catches if the person is too poor to buy the property.
        taDisplayActions.appendText("Property bought");
        taDisplayBalance.setText(String.format("Free Tuition: %1d%n",game.getFreeTuition()));
        taDisplayBalance.appendText(String.format("Player 1: %1d.%n",game.getBalance(0)));
        taDisplayBalance.appendText(String.format("Player 2: %1d.%n",game.getBalance(1)));
        taDisplayBalance.appendText(String.format("Player 3: %1d.%n",game.getBalance(2)));
        taDisplayBalance.appendText(String.format("Player 4: %1d.%n",game.getBalance(3)));
    }
    private void test(){
        taDisplayActions.appendText(game.getMonopoly());
    }
    //is only clickable if the current player lands on a chance tile.
    //when pressed it will output the text of the chance card and it will 
    //automatically make any payments or moves that the card requires
    private void getChance(){
        btChance.setDisable(true);
        chance = game.rollChance();
        taDisplayActions.appendText(game.getChanceCard(game.getCount(),chance));
        if(chance == 4){//get sent to go
            if(game.getCount() == 0){
                  p1.setCenterX(910);
                  p1.setCenterY(910);
            }else if(game.getCount() == 1){
                  p2.setCenterX(940);
                  p2.setCenterY(970);
            }else if(game.getCount() == 2){
                  p3.setCenterX(910);
                  p3.setCenterY(940);
            }else if(game.getCount() == 3){
                  p4.setCenterX(940);
                  p4.setCenterY(910); 
            }
        }else if(chance == 5){//get sent to jail
            if(game.getCount() == 0){
                  p1.setCenterX(150);
                  p1.setCenterY(900);
            }else if(game.getCount() == 1){
                  p2.setCenterX(160);
                  p2.setCenterY(925);
            }else if(game.getCount() == 2){
                  p3.setCenterX(150);
                  p3.setCenterY(925);
            }else if(game.getCount() == 3){
                  p4.setCenterX(160);
                  p4.setCenterY(900); 
            }
        }
        taDisplayBalance.setText(String.format("Free Tuition: %1d%n",game.getFreeTuition()));
        taDisplayBalance.appendText(String.format("Player 1: %1d.%n",game.getBalance(0)));
        taDisplayBalance.appendText(String.format("Player 2: %1d.%n",game.getBalance(1)));
        taDisplayBalance.appendText(String.format("Player 3: %1d.%n",game.getBalance(2)));
        taDisplayBalance.appendText(String.format("Player 4: %1d.%n",game.getBalance(3)));
    }
    //increases count by 1
    //makes all buttons unclickable except info and start turn
    private void endTurn(){
        btStartTurn.setDisable(false);
        btEndTurn.setDisable(true);
        btChance.setDisable(true);
        btBuy.setDisable(true);
        btRoll.setDisable(true);
        if(game.getCount() == 3){
            game.setCount(0);
        }else
            game.setCount(game.getCount()+1);
    }
    //pay rent pays rent and is ccalled when a player lands on another players property
    private void payRent(){
        game.payRent(game.getCount());
        taDisplayBalance.setText(String.format("Free Tuition: %1d%n",game.getFreeTuition()));
        taDisplayBalance.appendText(String.format("Player 1: %1d.%n",game.getBalance(0)));
        taDisplayBalance.appendText(String.format("Player 2: %1d.%n",game.getBalance(1)));
        taDisplayBalance.appendText(String.format("Player 3: %1d.%n",game.getBalance(2)));
        taDisplayBalance.appendText(String.format("Player 4: %1d.%n",game.getBalance(3)));
    }
    //move coin moves the proper coin to the proper property
    private void moveCoin(){
        if(game.getCount()==0){//player 1
            if(game.getTileOn(game.getCount()) == 1){//morgan
                p1.setCenterX(756);
                p1.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 2){//henderson
                p1.setCenterX(625);
                p1.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 3){//business school
                p1.setCenterX(490);
                p1.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 4){//chance 1
                p1.setCenterX(370);
                p1.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 5){//art
                p1.setCenterX(242);
                p1.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 6){//visiting jail
                p1.setCenterX(30);
                p1.setCenterY(860);
            }else if(game.getTileOn(game.getCount()) == 7){//yager
                p1.setCenterX(90);
                p1.setCenterY(760);
            }else if(game.getTileOn(game.getCount()) == 8){//chance 2
                p1.setCenterX(90);
                p1.setCenterY(650);
            }else if(game.getTileOn(game.getCount()) == 9){//nursing
                p1.setCenterX(90);
                p1.setCenterY(520);
            }else if(game.getTileOn(game.getCount()) == 10){//falley
                p1.setCenterX(90);
                p1.setCenterY(380);
            }else if(game.getTileOn(game.getCount()) == 11){//lee
                p1.setCenterX(90);
                p1.setCenterY(250);
            }else if(game.getTileOn(game.getCount()) == 12){//free
                p1.setCenterX(90);
                p1.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 13){//stoffer
                p1.setCenterX(235);
                p1.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 14){//garvey
                p1.setCenterX(365);
                p1.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 15){//law
                p1.setCenterX(489);
                p1.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 16){//chance3
                p1.setCenterX(610);
                p1.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 17){//union
                p1.setCenterX(750);
                p1.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 18){//gojail
                p1.setCenterX(910);
                p1.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 19){//llc
                p1.setCenterX(910);
                p1.setCenterY(255);
            }else if(game.getTileOn(game.getCount()) == 20){//lincoln
                p1.setCenterX(910);
                p1.setCenterY(380);
            }else if(game.getTileOn(game.getCount()) == 21){//arts science
                p1.setCenterX(910);
                p1.setCenterY(515);
            }else if(game.getTileOn(game.getCount()) == 22){//village
                p1.setCenterX(910);
                p1.setCenterY(640);
            }else if(game.getTileOn(game.getCount()) == 23){//kbi
                p1.setCenterX(910);
                p1.setCenterY(755);
            }else if(game.getTileOn(game.getCount()) == 0){//go
                p1.setCenterX(910);
                p1.setCenterY(910);
            }
            
        }else if(game.getCount() ==1){//player 2
            if(game.getTileOn(game.getCount()) == 1){//morgan
                p2.setCenterX(785);
                p2.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 2){//henderson
                p2.setCenterX(655);
                p2.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 3){//business school
                p2.setCenterX(520);
                p2.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 4){//chance 1
                p2.setCenterX(400);
                p2.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 5){//art
                p2.setCenterX(270);
                p2.setCenterY(900);
            }else if(game.getTileOn(game.getCount()) == 6){//visiting jail
                p2.setCenterX(30);
                p2.setCenterY(830);
            }else if(game.getTileOn(game.getCount()) == 7){//yager
                p2.setCenterX(120);
                p2.setCenterY(760);
            }else if(game.getTileOn(game.getCount()) == 8){//chance 2
                p2.setCenterX(120);
                p2.setCenterY(650);
            }else if(game.getTileOn(game.getCount()) == 9){//nursing
                p2.setCenterX(120);
                p2.setCenterY(520);
            }else if(game.getTileOn(game.getCount()) == 10){//falley
                p2.setCenterX(120);
                p2.setCenterY(380);
            }else if(game.getTileOn(game.getCount()) == 11){//lee
                p2.setCenterX(120);
                p2.setCenterY(250);
            }else if(game.getTileOn(game.getCount()) == 12){//free
                p2.setCenterX(120);
                p2.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 13){//stoffer
                p2.setCenterX(265);
                p2.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 14){//garvey
                p2.setCenterX(395);
                p2.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 15){//law
                p2.setCenterX(520);
                p2.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 16){//chance3
                p2.setCenterX(640);
                p2.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 17){//union
                p2.setCenterX(780);
                p2.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 18){//gojail
                p2.setCenterX(940);
                p2.setCenterY(90);
            }else if(game.getTileOn(game.getCount()) == 19){//llc
                p2.setCenterX(940);
                p2.setCenterY(255);
            }else if(game.getTileOn(game.getCount()) == 20){//lincoln
                p2.setCenterX(940);
                p2.setCenterY(380);
            }else if(game.getTileOn(game.getCount()) == 21){//arts science
                p2.setCenterX(940);
                p2.setCenterY(515);
            }else if(game.getTileOn(game.getCount()) == 22){//village
                p2.setCenterX(940);
                p2.setCenterY(640);
            }else if(game.getTileOn(game.getCount()) == 23){//kbi
                p2.setCenterX(940);
                p2.setCenterY(755);
            }else if(game.getTileOn(game.getCount()) == 0){//go
                p2.setCenterX(940);
                p2.setCenterY(970);
            }
        }else if(game.getCount() == 2){//player 3
            if(game.getTileOn(game.getCount()) == 1){//morgan
                p3.setCenterX(756);
                p3.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 2){//henderson
                p3.setCenterX(625);
                p3.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 3){//business school
                p3.setCenterX(490);
                p3.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 4){//chance 1
                p3.setCenterX(370);
                p3.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 5){//art
                p3.setCenterX(242);
                p3.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 6){//visiting jail
                p3.setCenterX(130);
                p3.setCenterY(973);
            }else if(game.getTileOn(game.getCount()) == 7){//yager
                p3.setCenterX(90);
                p3.setCenterY(790);
            }else if(game.getTileOn(game.getCount()) == 8){//chance 2
                p3.setCenterX(90);
                p3.setCenterY(680);
            }else if(game.getTileOn(game.getCount()) == 9){//nursing
                p3.setCenterX(90);
                p3.setCenterY(550);
            }else if(game.getTileOn(game.getCount()) == 10){//falley
                p3.setCenterX(90);
                p3.setCenterY(410);
            }else if(game.getTileOn(game.getCount()) == 11){//lee
                p3.setCenterX(90);
                p3.setCenterY(280);
            }else if(game.getTileOn(game.getCount()) == 12){//free
                p3.setCenterX(90);
                p3.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 13){//stoffer
                p3.setCenterX(235);
                p3.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 14){//garvey
                p3.setCenterX(365);
                p3.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 15){//law
                p3.setCenterX(489);
                p3.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 16){//chance3
                p3.setCenterX(610);
                p3.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 17){//union
                p3.setCenterX(750);
                p3.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 18){//gojail
                p3.setCenterX(910);
                p3.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 19){//llc
                p3.setCenterX(910);
                p3.setCenterY(285);
            }else if(game.getTileOn(game.getCount()) == 20){//lincoln
                p3.setCenterX(910);
                p3.setCenterY(410);
            }else if(game.getTileOn(game.getCount()) == 21){//arts science
                p3.setCenterX(910);
                p3.setCenterY(545);
            }else if(game.getTileOn(game.getCount()) == 22){//village
                p3.setCenterX(910);
                p3.setCenterY(680);
            }else if(game.getTileOn(game.getCount()) == 23){//kbi
                p3.setCenterX(910);
                p3.setCenterY(785);
            }else if(game.getTileOn(game.getCount()) == 0){//go
                p3.setCenterX(910);
                p3.setCenterY(940);
            }
        }else if (game.getCount() ==3){//player 4
            if(game.getTileOn(game.getCount()) == 1){//morgan
                p4.setCenterX(785);
                p4.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 2){//henderson
                p4.setCenterX(655);
                p4.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 3){//business school
                p4.setCenterX(520);
                p4.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 4){//chance 1
                p4.setCenterX(400);
                p4.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 5){//art
                p4.setCenterX(270);
                p4.setCenterY(930);
            }else if(game.getTileOn(game.getCount()) == 6){//visiting jail
                p4.setCenterX(160);
                p4.setCenterY(975);
            }else if(game.getTileOn(game.getCount()) == 7){//yager
                p4.setCenterX(120);
                p4.setCenterY(790);
            }else if(game.getTileOn(game.getCount()) == 8){//chance 2
                p4.setCenterX(120);
                p4.setCenterY(680);
            }else if(game.getTileOn(game.getCount()) == 9){//nursing
                p4.setCenterX(120);
                p4.setCenterY(550);
            }else if(game.getTileOn(game.getCount()) == 10){//falley
                p4.setCenterX(120);
                p4.setCenterY(410);
            }else if(game.getTileOn(game.getCount()) == 11){//lee
                p4.setCenterX(120);
                p4.setCenterY(280);
            }else if(game.getTileOn(game.getCount()) == 12){//free
                p4.setCenterX(120);
                p4.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 13){//stoffer
                p4.setCenterX(265);
                p4.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 14){//garvey
                p4.setCenterX(395);
                p4.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 15){//law
                p4.setCenterX(520);
                p4.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 16){//chance3
                p4.setCenterX(640);
                p4.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 17){//union
                p4.setCenterX(780);
                p4.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 18){//gojail
                p4.setCenterX(940);
                p4.setCenterY(120);
            }else if(game.getTileOn(game.getCount()) == 19){//llc
                p4.setCenterX(940);
                p4.setCenterY(285);
            }else if(game.getTileOn(game.getCount()) == 20){//lincoln
                p4.setCenterX(940);
                p4.setCenterY(410);
            }else if(game.getTileOn(game.getCount()) == 21){//arts science
                p4.setCenterX(940);
                p4.setCenterY(545);
            }else if(game.getTileOn(game.getCount()) == 22){//village
                p4.setCenterX(940);
                p4.setCenterY(680);
            }else if(game.getTileOn(game.getCount()) == 23){//kbi
                p4.setCenterX(940);
                p4.setCenterY(785);
            }else if(game.getTileOn(game.getCount()) == 0){//go
                p4.setCenterX(940);
                p4.setCenterY(910);
            }
        }
    }
}