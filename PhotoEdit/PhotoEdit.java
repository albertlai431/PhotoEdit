import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//File and user input
import java.io.File;
import javax.swing.JOptionPane;
import java.awt.FileDialog;
import java.awt.Frame;

//Exceptions
import java.io.IOException;
import java.io.FileNotFoundException;

//Image 
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

//Data Structures
import java.util.LinkedHashMap;
import java.util.ArrayList;

/** 
 * Edit your photos with PhotoEdit! PhotoEdit allows users to open images and save them as png or jpg from any directory on the computer.
 * It also allows users to perform a multitude of changes to their image in real-time, including rotations, flips, color filters, corrections
 * including brightness, transparency, blurs, and contrast adjustions, and general effects like warm, cool, negative, and greyscale. There is also 
 * an undo and redo feature, allowing users to trace their edits. When the user hovers over a menu button, a pop-up hover appears, showing 
 * the name of that menu button. 
 * 
 * @author Albert Lai
 * @version November 2019
 * 
 * Credits: Icons made by Smashicons and Freepik from www.flaticon.com
 */
public class PhotoEdit extends World
{
    // Constants:
    private final String STARTING_FILE = "Example 1.png";
    public static final int menuButtonOffset = 7;

    //Initial Buttons, Menu, and ImageHolder: 
    private Button undo;
    private Button redo;
    private Button pictureName;
    private Menu currentMenu;
    private ImageHolder image;

    //hoverButton:
    private Button hoverButton = new Button("hoverButton","");
    private Button currentMouseHover;

    //functionButton names for each menuButton:
    private static final String [] file = {"Open File","Save File"};
    private static final String [] transformations = {"Horizontal Flip","Vertical Flip","Rotate 90° Clockwise", "Rotate 180° Clockwise"};
    private static final String [] colorEffects = {"Red Effect","Blue Effect", "Green Effect", "Yellow Effect", "Orange Effect", "Purple Effect"};  
    private static final String [] adjustions = {"Increase Brightness","Decrease Brightness","Increase Contrast","Decrease Contrast","Increase Transparency","Decrease Transparency", "Blur"}; 
    private static final String [] generalEffects = {"Warmer","Cooler", "Greyscale","Negative","Cool Effect 1", "Cool Effect 2", "Cool Effect 3"};            

    //LinkedHashMap to hold menuButtons and their functionButton names:
    private static LinkedHashMap<Button,String []> menuButtons = new LinkedHashMap<Button,String []>(){{
                put(new Button(new GreenfootImage("file.png"), "File"), file);
                put(new Button(new GreenfootImage("transformations.png"), "Transformations"), transformations);
                put(new Button(new GreenfootImage("colorEffects.png"), "Color Effects"), colorEffects);
                put(new Button(new GreenfootImage("adjustions.png"), "Adjustions"), adjustions);
                put(new Button(new GreenfootImage("generalEffects.png"), "General Effects"), generalEffects);}};   

    //ArrayList and Variables for undo/redo function: 
    private int index;
    private boolean undoPhase;
    private ArrayList <GreenfootImage> imageList;
    
    private boolean showStartingMessage = false;

    /**
     * Constructor for objects of class PhotoEdit. Initializes objects and adds them to the screen, and initializes arraylist for undo/redo feature.
     */
    public PhotoEdit()
    {    
        // Create a new world with 850x680 cells with a cell size of 1x1 pixels.
        super(850, 680, 1, false); 

        // Initialize buttons and the image
        image = new ImageHolder(STARTING_FILE);
        pictureName = new Button("nonMenuFunctionButton"," [ " + STARTING_FILE + " ]  (" + image.getGreenfootImage().getWidth() + " x " + image.getGreenfootImage().getHeight() + ")  ");
        undo = new Button("nonMenuFunctionButton","Undo");
        redo = new Button("nonMenuFunctionButton","Redo");

        // Add objects to the screen
        addObject (image, 448, 340);
        addObject (pictureName, 448, 20);
        addObject (undo, 523,  660);
        addObject (redo, 373, 660);
        addObject(hoverButton,100,600);
        hoverButton.getImage().setTransparency(0);

        // Add menuButtons to the screen
        int i = 0;
        for(Button menuButton: menuButtons.keySet()){
            int h = menuButton.getImage().getHeight();
            int w = menuButton.getImage().getWidth();
            addObject(menuButton, w/2 + menuButtonOffset, menuButtonOffset + i*(h+menuButtonOffset) + h/2);
            i++;
        }    

        //Initialize ArrayList
        imageList = new ArrayList <GreenfootImage>();
        undoPhase = false;
        index = -1;
        addImagetoArrayList();
    }

    /**
     * Act() method just checks for mouse input and modifies transparency of undo/redo buttons
     */
    public void act ()
    {
        //check for mouse input
        checkMouse();
        
        //show starting message
        if(!showStartingMessage){
            //show starting message
            JOptionPane.showMessageDialog(null, "Edit your photos with PhotoEdit! Open new images, perform a variety of edits from color filters to image adjustions to transformations, and save them! Recommended max image size is 800 x 600.");
            showStartingMessage = true;
        }    
        
        //modifies transparency of undo/redo buttons
        if(index<imageList.size()-1 && undoPhase){
            if(redo.getImage().getTransparency()==120) redo.getImage().setTransparency(255);
        }    
        else if(redo.getImage().getTransparency()==255) redo.getImage().setTransparency(120);
        if(index==0){
            if(undo.getImage().getTransparency()==255) undo.getImage().setTransparency(120);
        }    
        else if(undo.getImage().getTransparency() == 120) undo.getImage().setTransparency(255);
    }

    /**
     * modifyImage - takes input from functionButtons and modifies the image accordingly
     * 
     * @param modifyType        type of modification to the image
     */
    public void modifyImage(String modifyType){
        if(modifyType == "Open File") openFile ();
        else if(modifyType == "Save File") saveImage ();
        else{
            if(modifyType == "Horizontal Flip") Processor.flip(image.getBufferedImage(),"horizontal");
            else if(modifyType == "Vertical Flip") Processor.flip(image.getBufferedImage(),"vertical");
            else if(modifyType == "Blur") Processor.blur(image.getBufferedImage());
            else if(modifyType == "Rotate 90° Clockwise") image.setGreenfootImage(createGreenfootImageFromBI(Processor.rotate(image.getBufferedImage(),90)));
            else if(modifyType == "Rotate 180° Clockwise") image.setGreenfootImage(createGreenfootImageFromBI(Processor.rotate(image.getBufferedImage(),180)));
            else Processor.applyImageEffect(image.getBufferedImage(),modifyType);
            addImagetoArrayList();
        }
    }    

    /**
     * Check for user clicking on a button
     */
    private void checkMouse ()
    {
        // Avoid excess mouse checks - only check mouse if somethething is clicked.
        if (Greenfoot.mouseClicked(null))
        {
            //undo button
            if (Greenfoot.mouseClicked(undo))
            {
                if(index>0){
                    index--;
                    undoPhase = true;
                    image.setGreenfootImage(createGreenfootImageFromBI(deepCopy(imageList.get(index).getAwtImage())));
                }
            }
            //redo button
            else if(Greenfoot.mouseClicked(redo))
            {
                if(index<imageList.size()-1 && undoPhase){
                    index++;
                    image.setGreenfootImage(imageList.get(index));
                }
            }
            //other button clicks
            else{
                //menuButtons
                boolean clickedButton = checkMenuButtonClick();
                //functionButtons
                if(currentMenu!=null && !clickedButton) currentMenu.checkButtonClick();
            }
        }
        //checks for mouse hover over menu buttons
        else{
            if(currentMenu!=null) hoverButton.getImage().setTransparency(0);
            else checkHover();
        }
    }    

    /**
     * checkMenuButtonClick - checks if menuButtons are clicked and modifies the menu accordingly.
     * 
     * @return boolean          returns true if a menuButton is clicked and false if not
     */
    private boolean checkMenuButtonClick(){
        //loop through all menuButtons
        for(Button menuButton : menuButtons.keySet()){
            //checks if menuButton is clicked
            if(Greenfoot.mouseClicked(menuButton)){
                //No menu open, open a new one
                if(currentMenu == null){
                    currentMenu = new Menu(menuButton, menuButtons.get(menuButton));
                    addObject(currentMenu,46+currentMenu.getImage().getWidth()/2,340);

                    //changes location of image and nonMenuFunctionButtons
                    image.setLocation(525, 340);
                    pictureName.setLocation(525, 20);
                    undo.setLocation(450,  660);
                    redo.setLocation(600, 660);
                }
                //close current menu
                else if(currentMenu.getCurrentMenuButton() == menuButton){
                    currentMenu.removeFromWorld();
                    currentMenu = null;

                    //changes location of image and nonMenuFunctionButtons
                    image.setLocation(448, 340);
                    pictureName.setLocation(448, 20);
                    undo.setLocation(523,  660);
                    redo.setLocation(373, 660);
                }
                //change menu
                else{
                    currentMenu.removeFromWorld();
                    currentMenu = new Menu(menuButton, menuButtons.get(menuButton));
                    addObject(currentMenu,46+currentMenu.getImage().getWidth()/2,340);
                }
                return true;
            }   
        }
        return false;
    }    

    /**
     * checkHover - checks if mouse is hovering over a menuButton and displays a hoverButton if it is
     */
    private void checkHover(){
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(mouse!=null){
            //gets x and y coordinates of the mouse
            int mx = mouse.getX();
            int my = mouse.getY();
            boolean newHover = false;

            //checks if there is currently a hoverButton being shown
            if(currentMouseHover==null) newHover = true;
            //checks if mouse is hovering over the same menuButton as previously checked
            else{
                int w = currentMouseHover.getImage().getWidth(), h = currentMouseHover.getImage().getHeight(), xloc=currentMouseHover.getX(), yloc=currentMouseHover.getY();
                if(currentMouseHover!=null && (mx<=xloc-w || mx>=xloc+w || my<=yloc-h || my>=yloc+h)){
                    newHover = true;
                    currentMouseHover=null;
                    hoverButton.getImage().setTransparency(0);
                }    
            }
            if(newHover){
                //loops over menuButtons
                for(Button menuButton : menuButtons.keySet()){
                    int w = menuButton.getImage().getWidth()/2, h = menuButton.getImage().getHeight()/2, xloc=menuButton.getX(), yloc=menuButton.getY();
                    //checks if mouse is hovering over a menuButton
                    if(mx>=xloc-w && mx<=xloc+w && my>=yloc-h && my<=yloc+h){
                        //makes hoverButton visible and updates it
                        hoverButton.getImage().setTransparency(255);
                        hoverButton.update(menuButton.getButtonText());
                        hoverButton.setLocation(menuButton.getX()+menuButton.getImage().getWidth()/2+hoverButton.getImage().getWidth()/2+menuButtonOffset,menuButton.getY());
                        currentMouseHover = menuButton;
                        break;
                    }  
                }
            }
        }
    }    

    /**
     * Allows the user to open a new image file.
     */
    private void openFile ()
    {
        //creates FileDialog 
        FileDialog fd = new FileDialog((Frame)null,"Open File",FileDialog.LOAD);
        fd.setDirectory(System.getProperty("user.dir"));
        fd.setFile("*.jpg;*.png");
        fd.setVisible(true);

        //get selected file name and directory
        String fileName = fd.getFile();
        String fileDir = fd.getDirectory();

        // If the file opening operation is successful, update the text in the pictureName button and adds it to the arraylist
        if (image.openFile (fileDir+fileName))
        {
            pictureName.update (" [ " + fileName + " ]  (" + image.getGreenfootImage().getWidth() + " x " + image.getGreenfootImage().getHeight() + ")  ");
            addImagetoArrayList();
        }
    }

    /**
     * Allows the user to save the current image.
     * 
     * By Albert Lai 
     */
    private void saveImage ()
    {
        //creates FileDialog 
        FileDialog fd = new FileDialog((Frame)null,"Save File",FileDialog.SAVE);
        fd.setDirectory(System.getProperty("user.dir"));
        fd.setFile("*.jpg;*.png");
        fd.setVisible(true);

        //get fileName
        String fileName = fd.getDirectory() + fd.getFile();

        if(fileName!=null){
            //save as png
            if(fileName.indexOf(".png")==fileName.length()-4 && fileName.length()>4){
                try{
                    File f = new File (fileName);
                    ImageIO.write(image.getBufferedImage(), "png", f); 
                    JOptionPane.showMessageDialog(null, "File Successfully Saved.");
                }  
                catch(IOException e){
                    JOptionPane.showMessageDialog(null, "Invalid file name.");
                }  
            }   
            //sae as jpg 
            else if(fileName.indexOf(".jpg")==fileName.length()-4 && fileName.length()>4){
                BufferedImage img = image.getBufferedImage();
                BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
                copy.createGraphics().drawImage(img, 0, 0,null);

                try{
                    File f = new File (fileName);
                    ImageIO.write(copy, "jpg",f); 
                    JOptionPane.showMessageDialog(null, "File Successfully Saved.");
                }
                catch(IOException e){
                    JOptionPane.showMessageDialog(null, "Invalid file name.");
                }   
            }
            //invalid file name
            else{
                JOptionPane.showMessageDialog(null, "Invalid file name.");
            }  
        }
    }

    /**
     * addImagetoArrayList - adds an image to the arraylist for the undo/redo feature
     */
    public void addImagetoArrayList()
    {
        if(undoPhase){
            //removes all images after the current image 
            undoPhase = false;
            imageList.subList(index+1,imageList.size()).clear();
        }   

        //add image to arraylist
        index++;
        imageList.add(createGreenfootImageFromBI(deepCopy(image.getBufferedImage())));
    }

    /**
     * Takes in a BufferedImage and returns a copy of that BufferImage.
     * 
     * By Jordan Cohen
     *
     * @param newBi The BufferedImage to make a copy of.
     *
     * @return BufferedImage The copy of the input BufferedImage
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultip = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultip, null);
    }

    /**
     * Takes in a BufferedImage and returns a GreenfootImage.
     * 
     * By Jordan Cohen
     *
     * @param newBi The BufferedImage to convert.
     *
     * @return GreenfootImage A GreenfootImage built from the BufferedImage provided.
     */
    public static GreenfootImage createGreenfootImageFromBI (BufferedImage newBi)
    {
        GreenfootImage returnImage = new GreenfootImage (newBi.getWidth(), newBi.getHeight());
        BufferedImage backingImage = returnImage.getAwtImage();
        Graphics2D backingGraphics = (Graphics2D)backingImage.getGraphics();
        backingGraphics.drawImage(newBi, null, 0, 0);
        return returnImage;
    }
}