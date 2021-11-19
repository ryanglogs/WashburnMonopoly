import java.util.*;
import java.io.*;
public class Game{
    private final int playerMAX = 4;
    private int count;
    private int spacesMoved;
    private int freeTuition;
    private String propSetup;
    private String[] propertySetup;
    private Player[] players = new Player[4];
    private Board board = new Board();
    private Property[] properties = board.getProperty();
    public void startGame() throws FileNotFoundException{
        Scanner fileINProperty = new Scanner(new File("Properties.txt"));//scanner to read in property info to create property objects
        String propSetup;//trash catches invalid input when asking for a player count so it doesnt get stuck in an inifite loop
        String[] propertySetup;//breaks input from file up on the comma and then stores it in this array
        //reads in property info from a file and fills array 
        while(fileINProperty.hasNext()){
            propSetup = fileINProperty.next();
            propertySetup = propSetup.split(",");//splits input on the comma
            for(int z = 0; z<1; z++){
                //creates property object with the given information from the text file
                board.addProperty(new Property(propertySetup[0],Integer.parseInt(propertySetup[1]),
                        Boolean.parseBoolean(propertySetup[2]),Integer.parseInt(propertySetup[3])));
            }
        }  
    }
    public Game(){
        this.count = 0;
        this.spacesMoved = 0;
        this.freeTuition = 0;        
        players[0] = new Player ("Player 1", 0,1500,false,0);
        players[1] = new Player ("Player 2", 0,1500,false,0);
        players[2] = new Player ("Player 3", 0,1500,false,0);
        players[3] = new Player ("Player 4", 0,1500,false,0);
    }
    public int getCount(){return this.count;}
    public void setCount(int count){this.count = count;}
    public int getSpacesMoved(){return this.spacesMoved;}
    public void setSpacesMoved(int spacesMoved){this.spacesMoved = spacesMoved;}
    public int getFreeTuition(){return this.freeTuition;}
    public void setFreeTuition(int freeTuition){this.freeTuition = freeTuition;}
    public void collectFree(int count){
        players[count].setBalance(players[count].getBalance()+freeTuition);
        freeTuition = 0;
    }
    public int rollDie(){return (int)(Math.random()*6)+1;}
    public void move(int spacesMoved,int count){
        players[count].setTileOn(spacesMoved+players[count].getTileOn());
    }
    public String getProperty(int count){
        return properties[players[count].getTileOn()].getPropName();
    }
    public boolean getPropAvailable(int count){ 
        boolean available = false;
        if(properties[players[count].getTileOn()].getIsAvailable() == true){
            available = true;
        }
        return available;
    }
    public String getMonopoly(){
        properties[1].setOwner(1);
        properties[1].setIsAvailable(false);
        properties[2].setOwner(1);
        properties[2].setIsAvailable(false);
        properties[5].setOwner(1);
        properties[5].setIsAvailable(false);
        properties[7].setOwner(2);
        properties[7].setIsAvailable(false);
        properties[10].setOwner(2);
        properties[10].setIsAvailable(false);
        properties[11].setOwner(2);
        properties[11].setIsAvailable(false);
        properties[13].setOwner(2);
        properties[13].setIsAvailable(false);
        properties[14].setOwner(2);
        properties[14].setIsAvailable(false);
        properties[17].setOwner(2);
        properties[17].setIsAvailable(false);
        properties[19].setOwner(2);
        properties[19].setIsAvailable(false);
        properties[20].setOwner(2);
        properties[20].setIsAvailable(false);
        properties[22].setOwner(2);
        properties[22].setIsAvailable(false);
        properties[23].setOwner(2);
        properties[23].setIsAvailable(false);
        String test = "";
        if(properties[1].getOwner() == 1 && properties[2].getOwner() == 1 && properties[5].getOwner() == 1 ||
            properties[1].getOwner() == 2 && properties[2].getOwner() == 2 && properties[5].getOwner() == 2 ||
            properties[1].getOwner() == 3 && properties[2].getOwner() == 3 && properties[5].getOwner() == 3 ||
            properties[1].getOwner() == 4 && properties[2].getOwner() == 4 && properties[5].getOwner() == 4){
                properties[1].setPropRent(properties[1].getPropRent() * 2);
                properties[2].setPropRent(properties[2].getPropRent() * 2);
                properties[5].setPropRent(properties[5].getPropRent() * 2);
                test = String.format("Player %1d has a monopoly on Morgan, Henderson, and the Art Building. Rent is now Doubled.\n",properties[1].getOwner());
        }else if(properties[7].getOwner() == 1 && properties[10].getOwner() == 1 && properties[11].getOwner() == 1 ||
            properties[7].getOwner() == 2 && properties[10].getOwner() == 2 && properties[11].getOwner() == 2 ||
            properties[7].getOwner() == 3 && properties[10].getOwner() == 3 && properties[11].getOwner() == 3 ||
            properties[7].getOwner() == 4 && properties[10].getOwner() == 4 && properties[11].getOwner() == 4){
                properties[7].setPropRent(properties[7].getPropRent() * 2);
                properties[10].setPropRent(properties[10].getPropRent() * 2);
                properties[11].setPropRent(properties[11].getPropRent() * 2);
                test = String.format("Player %1d has a monopoly on Yager Stadium, Falley Field, and Lee Arena. Rent is now Doubled.\n",properties[1].getOwner());
        }else if(properties[13].getOwner() == 1 && properties[14].getOwner() == 1 && properties[17].getOwner() == 1 ||
            properties[13].getOwner() == 2 && properties[14].getOwner() == 2 && properties[17].getOwner() == 2 ||
            properties[13].getOwner() == 3 && properties[14].getOwner() == 3 && properties[17].getOwner() == 3 ||
            properties[13].getOwner() == 4 && properties[14].getOwner() == 4 && properties[17].getOwner() == 4){
                properties[13].setPropRent(properties[12].getPropRent() * 2);
                properties[14].setPropRent(properties[14].getPropRent() * 2);
                properties[17].setPropRent(properties[17].getPropRent() * 2);
                test = String.format("Player %1d has a monopoly on Stoffer, Garvey Hall, and the Memorial Union. Rent is now Doubled.\n",properties[1].getOwner());
        }else if(properties[19].getOwner() == 1 && properties[20].getOwner() == 1 ||
            properties[19].getOwner() == 2 && properties[20].getOwner() == 2 ||
            properties[19].getOwner() == 3 && properties[20].getOwner() == 3 ||
            properties[19].getOwner() == 4 && properties[20].getOwner() == 4){
                properties[19].setPropRent(properties[19].getPropRent() * 2);
                properties[20].setPropRent(properties[20].getPropRent() * 2);
                test = String.format("Player %1d has a monopoly on the LLC and Lincoln Hall. Rent is now Doubled.\n",properties[1].getOwner());
        }else if(properties[22].getOwner() == 1 && properties[23].getOwner() == 1 ||
            properties[22].getOwner() == 2 && properties[23].getOwner() == 2 ||
            properties[22].getOwner() == 3 && properties[23].getOwner() == 3 ||
            properties[22].getOwner() == 4 && properties[23].getOwner() == 4){
                properties[22].setPropRent(properties[22].getPropRent() * 2);
                properties[23].setPropRent(properties[23].getPropRent() * 2);
                test = String.format("Player %1d has a monopoly on The Village and the KBI Building. Rent is now Doubled.\n",properties[1].getOwner());
        }
        return test;
    }
    public int getOwner(int count){
        return properties[players[count].getTileOn()].getOwner();
    }
    public void buyProp(int count){
        properties[players[count].getTileOn()].setOwner(count+1);
        properties[players[count].getTileOn()].setIsAvailable(false);
        players[count].setBalance(players[count].getBalance() - properties[players[count].getTileOn()].getPropPrice());
    }
    public int getBalance(int count){
        return players[count].getBalance();
    }
    public int getTileOn(int count){
        return players[count].getTileOn();
    }
    public void setTileOn(int count, int tileON){
        players[count].setTileOn(tileON);
    }
    public boolean passedGO(int count,int spacesMoved){
        boolean pass = false;
        if((players[count].getTileOn()+spacesMoved)>23){
            pass = true;
        }
        return pass;
    }
    public String movePastGo(int count){
        players[count].setBalance(players[count].getBalance()+200);
        players[count].setTileOn(players[count].getTileOn()-24);
        return "You passed go and collected $200!\n";
    }
    public boolean getJail(int count){
        return players[count].getJail();
    }   
    public void setJail(int count,boolean jail){
        players[count].setJail(jail);
    }
    public int getJC(int count){
        return players[count].getJailCount();
    }
    public void setJC(int count){
        players[count].setJailCount((players[count].getJailCount()+1));
    }
    public void resetJC(int count){
        players[count].setJailCount(0);
    }   
    public int rollChance(){
        return ((int)(Math.random()*7)+1);
    }
    public int getPropRent(int count){
        return properties[players[count].getTileOn()].getPropRent();
    }
    public String getChanceCard(int count,int chance){
        String output = "";
        if(chance == 0){
            output = "Ah Jeez! You slept through your alarm! Pay $25 to free tuition.\n";
            players[count].setBalance(players[count].getBalance()-25);
        }else if(chance == 1){
            output = "Ah Jeez! You left the sink running and flooded your dorm. Pay $20 to all other players.\n";
            if(count ==0){
                players[0].setBalance(players[0].getBalance()-60);
                players[1].setBalance(players[1].getBalance()+20);
                players[2].setBalance(players[2].getBalance()+20);
                players[3].setBalance(players[3].getBalance()+20);
            }else if(count ==1){
                players[1].setBalance(players[0].getBalance()-60);
                players[0].setBalance(players[1].getBalance()+20);
                players[2].setBalance(players[2].getBalance()+20);
                players[3].setBalance(players[3].getBalance()+20);
            }else if(count ==2){
                players[2].setBalance(players[0].getBalance()-60);
                players[1].setBalance(players[1].getBalance()+20);
                players[0].setBalance(players[2].getBalance()+20);
                players[3].setBalance(players[3].getBalance()+20);
            }else if(count ==3){
                players[3].setBalance(players[0].getBalance()-60);
                players[0].setBalance(players[1].getBalance()+20);
                players[2].setBalance(players[2].getBalance()+20);
                players[1].setBalance(players[3].getBalance()+20);
            }
        }else if(chance == 2){
            output="Good Job! You aced all of your finals! Collect $50.\n";
            players[count].setBalance(players[count].getBalance()+50);
        }else if(chance == 3){
            output = "Your friend is in a concert at White Concert hall and Admission is $10. Pay to free tuition.\n";
            freeTuition = freeTuition + 10;
            players[count].setBalance(players[count].getBalance()-10);
        }else if(chance == 4){
            output = "Advance to go and collect $200.\n";
            players[count].setTileOn(0);
            players[count].setBalance(players[count].getBalance()+200);
        }else if(chance == 5){
            output = "Ah Jeez! You and your friend got caught trying to gift wrap the computer lab as a prank. Go to jail and do not collect $200 from passing go.\n";
            players[count].setTileOn(6);
            players[count].setJail(true);
        }else if(chance == 6){
            output = "You won the flag football tournament! Collect $100.\n";
            players[count].setBalance(players[count].getBalance()+100);
        }else if(chance == 7){
            output = "The bods make it to the championship game in basketball and you want to go. Pay $150 to free tuition.\n";
            freeTuition=freeTuition+150;
            players[count].setBalance(players[count].getBalance()-150);
        }
        return output;
    }
    public void payRent(int count){
        if(properties[players[count].getTileOn()].getOwner() == 1){
            players[count].setBalance(players[count].getBalance()-properties[players[count].getTileOn()].getPropRent());
            players[0].setBalance(players[0].getBalance()+properties[players[count].getTileOn()].getPropRent());
        }else if(properties[players[count].getTileOn()].getOwner() == 2){
            players[count].setBalance(players[count].getBalance()-properties[players[count].getTileOn()].getPropRent());
            players[1].setBalance(players[1].getBalance()+properties[players[count].getTileOn()].getPropRent());
        }else if(properties[players[count].getTileOn()].getOwner() == 3){            
            players[count].setBalance(players[count].getBalance()-properties[players[count].getTileOn()].getPropRent());
            players[2].setBalance(players[2].getBalance()+properties[players[count].getTileOn()].getPropRent());
        }else if(properties[players[count].getTileOn()].getOwner() == 4){            
            players[count].setBalance(players[count].getBalance()-properties[players[count].getTileOn()].getPropRent());
            players[3].setBalance(players[3].getBalance()+properties[players[count].getTileOn()].getPropRent());
        }
    }
}
class Property{
    private String propName ="";
    private int propPrice = 1;
    private boolean isAvailable = true;
    private int propRent = 1;
    private int owner=0;
    public Property(){
        String propName ="";
        propPrice = 1;
        isAvailable = true;
        propRent = 1;
    }
    public Property(String propName, int propPrice, boolean isAvailable, int propRent){
        this.propName = propName;
        this.propPrice = propPrice;
        this.isAvailable = isAvailable;
        this.propRent = propRent;
    }
    public void setOwner(int owner){this.owner = owner;}
    public int getOwner(){return this.owner;}
    public void setPropName(String propName){this.propName = propName;}
    public void setPropPrice(int propPrice){this.propPrice = propPrice;}
    public void setIsAvailable(boolean isAvailable){this.isAvailable = isAvailable;}
    public void setPropRent(int propRent){this.propRent = propRent;}
    public String getPropName(){return this.propName;}
    public int getPropPrice(){return this.propPrice;}
    public boolean getIsAvailable(){return this.isAvailable;}
    public int getPropRent(){return this.propRent;}
}
class Board extends Property{
    private final int MAX = 24;
    private Property[] properties = new Property[MAX];
    private int size=0;
    public void addProperty(Property prop){
        if(this.size<this.MAX){
            properties[size] = prop;
            this.size++;
        }
    }
    public Property[] getProperty(){return properties;}
}
class Player{
    private int tileOn = 0;
    private String playerName = "";
    private int size = 0;
    private int balance = 1500;
    private boolean jail = false;
    private int jailCount = 0;
    public Player(String playerName, int tileOn, int balance,boolean jail,int jailCount){
        this.tileOn = tileOn;
        this.playerName = playerName;
        this.balance = balance;
        this.jail = jail;
        this.jailCount = jailCount;
    }
    public boolean getJail(){return this.jail;}
    public void setJail(boolean jail){this.jail = jail;}
    public int getJailCount(){return this.jailCount;}
    public void setJailCount(int jail){this.jailCount = jail;}
    public double getSize(){return this.size;}
    public void setPlayerName(String playerName){this.playerName = playerName;}
    public void setTileOn(int tileOn){this.tileOn = tileOn;}
    public void setBalance(int balance){this.balance = balance;}
    public String getPlayerName(){return this.playerName;}
    public int getTileOn(){return this.tileOn;}
    public int getBalance(){return this.balance;}
}