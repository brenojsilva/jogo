import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private List<Item> items;
    private int chavesEncontradas;


    public Room(String description) 
    {
        items = new ArrayList<>();
        this.description = description;
        exits = new HashMap<>();
        chavesEncontradas = 0;
    }

    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    public void addItem(Item item){
        items.add(item);
    }

    public List<Item> getItems(){
        return items;
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public int getChavesEncontradas() 
    {
        return chavesEncontradas;
    }

    public void setChavesEncontradas(int chavesEncontradas) 
    {
        this.chavesEncontradas = chavesEncontradas;
    }

    public Object getName() {
        return "sala";
    }

    public String getShortDescription()
    {
        return description;
    }


    public String getLongDescription()
    {
        return "Você está neste cômodo: " + description + ".\n" + getExitString();
    }

    private String getExitString()
    {
        String returnString = "Saídas:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

   
}

