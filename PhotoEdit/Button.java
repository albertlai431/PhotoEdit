import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Generic Button to display text or an image. The World controls click capturing. 
 * 
 * @author Albert Lai and Jordan Cohen
 */
public class Button extends Actor
{
    // Declare private variables
    private GreenfootImage myImage;
    private String buttonText;
    private String buttonType;
    private int textSize;

    /**
     * Construct a Button with a given String and a specified type
     * 
     * @param buttonType    the type of the button
     * @param buttonText    String value to display
     * 
     */
    public Button (String buttonType, String buttonText)
    {
        this.buttonText = buttonText;
        this.textSize = textSize;
        this.buttonType = buttonType;
        update(buttonText);
    }
    
    /**
     * Construct a Button with a given GreenfootImage 
     * 
     * @param GreenfootImage    the image of the button
     * @param buttonText        String value to display
     * 
     */
    public Button (GreenfootImage i, String buttonText){
        setImage(i);
        this.buttonText = buttonText; 
    }    

    /**
     * Updates the text on the button
     * 
     * @param String    String to display
     */
    public void update (String text)
    {
        buttonText = text;
        
        if(buttonType == "functionButton"){
            //creates the button and text
            textSize = 17;
            GreenfootImage tempTextImage = new GreenfootImage (text,textSize,Color.WHITE, Color.DARK_GRAY);
            myImage = new GreenfootImage (154, tempTextImage.getHeight()+8);
            myImage.setColor (Color.DARK_GRAY);
            myImage.fill();
            myImage.drawImage (tempTextImage, 4, 4);
            myImage.setColor (Color.BLACK);
            myImage.drawRect (0,0,myImage.getWidth(), myImage.getHeight()-1);
            setImage(myImage);
        }
        else if(buttonType == "menuNonFunctionButton"){
            //creates the button and text
            textSize = 20;
            GreenfootImage tempTextImage = new GreenfootImage (text,textSize,Color.CYAN, Color.DARK_GRAY);
            myImage = new GreenfootImage (154, tempTextImage.getHeight()+8);
            myImage.setColor (Color.DARK_GRAY);
            myImage.fill();
            myImage.drawImage (tempTextImage, 4, 4);

            myImage.setColor (Color.BLACK);
            myImage.drawRect (0,0,myImage.getWidth(), myImage.getHeight()-1);
            setImage(myImage);
        }    
        else if(buttonType == "nonMenuFunctionButton"){
            //creates the button and text
            textSize = 20;
            GreenfootImage tempTextImage = new GreenfootImage (text,textSize,Color.RED, Color.WHITE);
            myImage = new GreenfootImage (tempTextImage.getWidth() + 8, tempTextImage.getHeight() + 8);
            myImage.setColor (Color.WHITE);
            myImage.fill();
            myImage.drawImage (tempTextImage,4, 4);
            myImage.setColor (Color.BLACK);
            myImage.drawRect (0,0,tempTextImage.getWidth() + 7, tempTextImage.getHeight() + 7);
            setImage(myImage);
        }
        else if(buttonType == "hoverButton"){
            //creates the button and text
            textSize = 15;
            GreenfootImage tempTextImage = new GreenfootImage (text,textSize,Color.BLACK, Color.YELLOW);
            myImage = new GreenfootImage (tempTextImage.getWidth() + 8, tempTextImage.getHeight() + 8);
            myImage.setColor (Color.YELLOW);
            myImage.fill();
            myImage.drawImage (tempTextImage,4, 4);
            myImage.setColor (Color.BLACK);
            myImage.drawRect (0,0,tempTextImage.getWidth() + 7, tempTextImage.getHeight() + 7);
            setImage(myImage);
        }  
    }   
    
    /**
     * Returns the text of the button
     * 
     * @return String    Current text of the button
     */
    public String getButtonText(){
        return buttonText;
    }    
}
