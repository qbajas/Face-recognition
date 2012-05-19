/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

/**
 *
 * @author Michal
 */
public class TrainingSetScalling {

    public static void main(String[] args) {
         new TrainingSetScalling().resizeAll("dataSet");
    }

    public void resizeAll(String source) {
        File mainFolder = new File(source);
        File subFolders[] = mainFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        FileFilter jpgFilter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                int i = name.lastIndexOf(".");
                if (i == -1) {
                    return false;
                }
                return name.substring(i + 1).equalsIgnoreCase("jpg");
            }
        };

        File images[];
        BufferedImage output = null;
        Graphics2D g = null;

        Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = (ImageWriter)iter.next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(1);
        
        FileImageOutputStream outStream;
        
        for (File folder : subFolders) {
            images = folder.listFiles(jpgFilter);

            

            for (File image : images) {
                System.out.println("Resizing " + image.getName());
                try {
                    BufferedImage img = ImageIO.read(image);
                    output = new BufferedImage(320, 240, BufferedImage.TYPE_BYTE_GRAY);

                    g = output.createGraphics();
                    g.setComposite(AlphaComposite.Src);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.drawImage(img, 0, 0, 320, 240, null);
                    g.dispose();
                    
                    outStream = new FileImageOutputStream(image);
                    writer.setOutput(outStream);
                    IIOImage outImg = new IIOImage(output, null, null);
                    writer.write(null, outImg, iwp);
                    outStream.close();
                    writer.reset();
                    
                } catch (IOException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }

        }
    }
}
