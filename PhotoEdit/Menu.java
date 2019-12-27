import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedHashMap;

/**
 * Menu - displays the functionButtons of the current menuButton. 
 * 
 * @author Albert Lai 
 * @version November 2019
 */
public class Menu extends Actor
{
    //Objects and Variables:
    private Button currentMenuButton;
    private Button currentMenuText;
    private GreenfootImage image;
    private Color color = Color.DARK_GRAY;
    private LinkedHashMap <String,Button> functionButtons = new LinkedHashMap <String,Button>();
    
    /**
     * Constructor of Menu - sets the menu image and initializes the functionButtons
     */
    public Menu(Button currentMenuButton, String [] functionButtonsList){
        this.currentMenuButton = currentMenuButton;
        
        //sets Menu image
        image = new GreenfootImage(154, 680);
        image.setColor(color);
        image.fillRect(0,0,154, 680);
        setImage(image);
        
        
        //initialize functionButtons
        for(String functionButtonName: functionButtonsList){
            functionButtons.put(functionButtonName, new Button("functionButton",functionButtonName));
        }    
    }
    
    /**
     * addedToWorld - inherited public method 
     * 
     * @param w         current world of the menu
     */
    public void addedToWorld(World w){
        //adds Menu Text 
        PhotoEdit world = (PhotoEdit) getWorld();
        currentMenuText = new Button("menuNonFunctionButton",currentMenuButton.getButtonText());
        world.addObject (currentMenuText, 123,currentMenuText.getImage().getHeight()/2);
        
        //adds functionButtons to the screen
        int i=1;
        for(String key: functionButtons.keySet()){
            Button newButton = functionButtons.get(key);
            world.addObject(newButton, 123, i*newButton.getImage().getHeight() + newButton.getImage().getHeight()/2);
            i++;
        }
    }    
    
    /**
     * checkButtonClick - checks if functionButtons are clicked
     */
    public void checkButtonClick(){
        PhotoEdit world = (PhotoEdit) getWorld();
        
        //loops through functionButtons
        for(String functionButton: functionButtons.keySet()){
            if(Greenfoot.mouseClicked(functionButtons.get(functionButton))){
                world.modifyImage(functionButton);
                break;
            }    
        }
    }    
    
    /**
     * getCurrentMenuButton - returns the current menu button
     * 
     * @return Button       current menu button
     */
    public Button getCurrentMenuButton(){
        return currentMenuButton;
    }    
    
    /**
     * removeFromWorld - removes the menu from the world
     */
    public void removeFromWorld(){
        PhotoEdit world = (PhotoEdit) getWorld();
        //remove Buttons
        world.removeObject(currentMenuText);
        for(String key: functionButtons.keySet()){
            world.removeObject(functionButtons.get(key));
        }
        world.removeObject(this);
    }    
}
