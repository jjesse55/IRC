package code.Client;
import java.util.ArrayList;
import java.awt.event.*;

public class CRoom extends GuiBase implements ActionListener
{


    //TODO This is the CLIENT class for a room
    //TODO Ktie needs to make this work with the GUI... 
    ArrayList <String> users; //change this
    String RoomName;

    public CRoom(String name){
        RoomName=name;
        users= new ArrayList <>();
    }

    public void addUser(String user){

    }

    public void removeUser(String user){

    } 


    @Override
    public void actionPerformed(ActionEvent e)
    {
    }
    
}