import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.image.BufferedImage;

/**
 * Simple class that serves to be an Actor to display the image.
 * 
 * @author Albert Lai and Jordan Cohen
 * @version November 2019
 */
public class ImageHolder extends Actor
{
    private GreenfootImage imageToDisplay; 

    /**
     * Construct an ImageHolder with a file name. If there is an error, 
     * show a blank GreenfootImage.
     * 
     * @param fileName  Name of image file to be displayed.
     */
    public ImageHolder (String fileName)
    {
        openFile (fileName);
    }

    /**
     * Attempt to open a file and assign it as this Actor's image
     * 
     * @param fileName  Name of the image file to open (must be in this directory)
     * @return boolean  True if operation successful, otherwise false
     */
    public boolean openFile (String fileName)
    {
        try {
            if (fileName != null)
            {
                imageToDisplay = new GreenfootImage (fileName);
                setImage(imageToDisplay);
            }
            else
                return false;
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Allows access to my awtImage - the backing data underneath the GreenfootImage class.
     * 
     * @return BufferedImage returns the backing image for this Actor as an AwtImage
     */
    public BufferedImage getBufferedImage ()
    {
        return this.getImage().getAwtImage();
    }
    
    /**
     * Returns the current GreenfootImage
     * 
     * @return GreenfootImage returns current GreenfootImage 
     */
    public GreenfootImage getGreenfootImage ()
    {
        return this.getImage();
    }
    
    /**
     * Sets current image to a new GreenfootImage
     * 
     * @param GreenfootImage new GreenfootImage to set the current image to
     */
    public void setGreenfootImage (GreenfootImage i)
    {
        imageToDisplay = i;
        setImage(imageToDisplay);
    }
}
