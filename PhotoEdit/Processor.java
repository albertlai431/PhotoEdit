import java.awt.image.BufferedImage;
import java.util.stream.IntStream;
/**
 * Processor - the class that processes images. It performs flips, rotations, and image effects. 
 * 
 * @author Jordan Cohen, Albert Lai, and fabian (stackoverflow)
 * @version November 2019
 */

public class Processor  
{
    /**
     * applyImageEffect - applies a multitude of image effects including color modifications, adjustments for 
     * brightness, transparency, and contrast, and general effects of cooler, warmer, greyscale, and negative. 
     * 
     * @param bi            The BufferedImage (passed by reference) to change.
     * @param modifyType    Specifies the type of image effect
     */
    public static void applyImageEffect (BufferedImage bi, String modifyType)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);

                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                //color modifications
                if(modifyType=="Red Effect"){
                    if(red < 240) red+=2;
                    if (blue >= 20) blue -= 2;
                    if (green >= 20) green -= 2;
                }   
                else if(modifyType=="Blue Effect"){
                    if(blue < 240) blue+=2;
                    if (red >= 20) red -= 2;
                    if (green >= 20) green -= 2;
                }    
                else if(modifyType=="Green Effect"){
                    if(green < 240) green+=2;
                    if (red >= 20) red -= 2;
                    if (blue >= 20) blue -= 2;
                }  
                else if(modifyType=="Yellow Effect"){
                    if(green < 240) green+=2;
                    if(red < 240) red+=2;
                    if (blue >= 20) blue -= 2;
                } 
                else if(modifyType=="Orange Effect"){
                    if(red < 240) red+=2;
                    if(green < 120) green+=2;
                    else if(green>140) green-=2;
                    if (blue >= 20) blue -= 2;
                } 
                else if(modifyType=="Purple Effect"){
                    if(blue < 240) blue+=2;
                    if(red<120) red+=2;
                    else if(red>140) red-=2;
                    if (green >= 20) green -= 2;
                }    
                else if(modifyType=="Cool Effect 1"){
                    green = 255 - green;
                } 
                else if(modifyType=="Cool Effect 2"){
                    red = 255 - red;
                } 
                else if(modifyType=="Cool Effect 3"){
                    blue = 255 - blue;
                }    
                //changes transparency
                else if(modifyType=="Increase Transparency"){
                    if(alpha>=2) alpha-=2;
                }
                else if(modifyType=="Decrease Transparency"){
                    if(alpha<=253) alpha+=2;
                }
                //warmer and cooler
                else if(modifyType=="Cooler"){
                    if (blue < 240) blue += 2;
                    if (red >= 20) red -= 2;
                }
                else if(modifyType == "Warmer"){
                    if (red < 240) red += 2;
                    if (blue >= 20) blue -= 2;
                }    
                //makes image greyscale
                else if(modifyType=="Greyscale"){
                    int average = (red+green+blue)/3;
                    blue = average; 
                    green = average;
                    red = average;
                }    
                //negative effect to the image
                else if(modifyType=="Negative"){
                    blue = 255 - blue;
                    green = 255 - green;
                    red = 255 - red;
                }
                //brighter or darker
                else if(modifyType=="Increase Brightness"){
                    if(blue<240) blue+=2;
                    if(red<240) red+=2;
                    if(green<240) green+=2;
                }  
                else if(modifyType=="Decrease Brightness"){
                    if(blue>20) blue-=2;
                    if(red>20) red-=2;
                    if(green>20) green-=2;
                }
                //contrast adjustment
                else if(modifyType=="Increase Contrast"){
                    if(blue<240) blue*=1.02;
                    if(red<240) red*=1.02;
                    if(green<240) green*=1.02;
                }  
                else if(modifyType=="Decrease Contrast"){
                    if(blue>20) blue*=0.98;
                    if(red>20) red*=0.98;
                    if(green>20) green*=0.98;
                }
                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }

    /**
     * Flips the image horizontally or vertically
     * 
     * @param bi            The BufferedImage (passed by reference) to change.
     * @param flipType      Specifies the type of flip
     */
    public static void flip (BufferedImage bi, String flipType)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);

        //set the new buffered image to be the flipped image 
        for(int x=0;x<xSize;x++){
            for(int y=0;y<ySize;y++){
                if(flipType=="vertical") newBi.setRGB(x,y,bi.getRGB(x,ySize-y-1));
                else if(flipType=="horizontal") newBi.setRGB(x,y,bi.getRGB(xSize-x-1,y));
            }    
        }

        //copy new buffered image into old buffered image
        for(int x=0;x<xSize;x++){
            for(int y=0;y<ySize;y++){
                bi.setRGB(x,y,newBi.getRGB(x,y));
            }    
        }
    }

    /**
     * Rotates the image by either 90 or 180 degrees clockwise.
     * 
     * @param bi                The BufferedImage (passed by reference) to change.
     * @param rotation          Specifies the number of degrees of rotation clockwise
     * 
     * @return GreenfootImage   The GreenfootImage to set the new image to
     */
    public static BufferedImage rotate (BufferedImage bi, int rotation){
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        //create new buffered image 
        BufferedImage newBi;
        if(rotation == 90) newBi = new BufferedImage(ySize,xSize,3);
        else if(rotation == 180) newBi = new BufferedImage(xSize,ySize,3);
        else return bi;

        //rotate image accordingly
        for(int x=0;x<xSize;x++){
            for(int y=0;y<ySize;y++){
                if(rotation == 90) newBi.setRGB(y,x,bi.getRGB(x,ySize-y-1));
                else if(rotation == 180) newBi.setRGB(x,y,bi.getRGB(xSize-x-1,ySize-y-1));
            }    
        }    

        //return new greenfoot image
        return newBi;
    }    
    
    /**
     * blur - blurs the image slightly
     * 
     * @author fabian (stackoverflow)
     * 
     * @param bi            The BufferedImage (passed by reference) to change.
     */
    public static void blur(BufferedImage image) {
        int xSize = image.getWidth();
        int ySize = image.getHeight();
        
        int[] filter = {1, 2, 1, 2, 4, 2, 1, 2, 1};
        int filterWidth = 3;
        if (filter.length % filterWidth != 0) {
            throw new IllegalArgumentException("filter contains a incomplete row");
        }

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int sum = IntStream.of(filter).sum();

        int[] input = image.getRGB(0, 0, width, height, null, 0, width);

        int[] output = new int[input.length];

        final int pixelIndexOffset = width - filterWidth;
        final int centerOffsetX = filterWidth / 2;
        final int centerOffsetY = filter.length / filterWidth / 2;

        // apply filter
        for (int h = height - filter.length / filterWidth + 1, w = width - filterWidth + 1, y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                for (int filterIndex = 0, pixelIndex = y * width + x;
                filterIndex < filter.length;
                pixelIndex += pixelIndexOffset) {
                    for (int fx = 0; fx < filterWidth; fx++, pixelIndex++, filterIndex++) {
                        int col = input[pixelIndex];
                        int factor = filter[filterIndex];

                        // sum up color channels seperately
                        r += ((col >>> 16) & 0xFF) * factor;
                        g += ((col >>> 8) & 0xFF) * factor;
                        b += (col & 0xFF) * factor;
                    }
                }
                r /= sum;
                g /= sum;
                b /= sum;
                // combine channels with full opacity
                output[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
            }
        }

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        result.setRGB(0, 0, width, height, output, 0, width);
        
        for(int x=0;x<xSize;x++){
            for(int y=0;y<ySize;y++){
                image.setRGB(x,y,result.getRGB(x,y));
            }    
        }
    }

    /**
     * Takes in an rgb value - the kind that is returned from BufferedImage's
     * getRGB() method - and returns 4 integers for easy manipulation.
     * 
     * By Jordan Cohen
     * Version 0.2
     * 
     * @param rgbaValue The value of a single pixel as an integer, representing<br>
     *                  8 bits for red, green and blue and 8 bits for alpha:<br>
     *                  <pre>alpha   red     green   blue</pre>
     *                  <pre>00000000000000000000000000000000</pre>
     * @return int[4]   Array containing 4 shorter ints<br>
     *                  <pre>0       1       2       3</pre>
     *                  <pre>alpha   red     green   blue</pre>
     */
    public static int[] unpackPixel (int rgbaValue)
    {
        int[] unpackedValues = new int[4];
        // alpha
        unpackedValues[0] = (rgbaValue >> 24) & 0xFF;
        // red
        unpackedValues[1] = (rgbaValue >> 16) & 0xFF;
        // green
        unpackedValues[2] = (rgbaValue >>  8) & 0xFF;
        // blue
        unpackedValues[3] = (rgbaValue) & 0xFF;

        return unpackedValues;
    }

    /**
     * Takes in a red, green, blue and alpha integer and uses bit-shifting
     * to package all of the data into a single integer.
     * 
     * @param   int red value (0-255)
     * @param   int green value (0-255)
     * @param   int blue value (0-255)
     * @param   int alpha value (0-255)
     * 
     * @return int  Integer representing 32 bit integer pixel ready
     *              for BufferedImage
     */
    public static int packagePixel (int r, int g, int b, int a)
    {
        int newRGB = (a << 24) | (r << 16) | (g << 8) | b;
        return newRGB;
    }
}
