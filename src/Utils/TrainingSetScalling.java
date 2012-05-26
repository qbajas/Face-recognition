package Utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.BinarizeDescriptor;
import javax.media.jai.operator.MedianFilterDescriptor;

/**
 *
 * @author Michal
 */
public class TrainingSetScalling {
    private static final int WIDTH =640, HEIGHT=480;
    
    public static void main(String[] args) {
        new TrainingSetScalling().resizeAll(Config.dataPath);
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
        ImageWriter writer = (ImageWriter) iter.next();
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
                    output = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
                    
                    
                    
                    RenderedOp op = MedianFilterDescriptor.create(img, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 3, null);
                    op = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 3, null);
                    op = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 3, null);
                    op = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 3, null);
                    RenderedOp op2 = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 7, null);
                    op2 = MedianFilterDescriptor.create(op2, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 7, null);
                    
                    
                    float[] kernelMatrix = { -2, -4, -2,
                                                -4, 24, -4,
                                                -2, -4, -2 };
                    KernelJAI kernel = new KernelJAI(3,3,kernelMatrix);
                    PlanarImage convolved = JAI.create("convolve", op2, kernel);
                    
                    PlanarImage binarized = BinarizeDescriptor.create(convolved, 120.0, null);
                    
                    
                    Rectangle  rect = getFaceBounds(binarized.getAsBufferedImage());
                   // processed = JAI.create("convolve", processed, kernel);
                    g = output.createGraphics();
                    
                    
                    g.setComposite(AlphaComposite.Src);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                    
                    g.drawImage(binarized.getAsBufferedImage().getSubimage(rect.x, rect.y, rect.width, rect.height), 0, 0, rect.width, rect.height, null);
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
    
    private Rectangle getFaceBounds(BufferedImage img){
        Rectangle rect = new Rectangle();
        int wMargin = 10;
        int hMargin = 10;
        
        int tmpW = img.getWidth()-2*wMargin;
        int tmpH = img.getHeight()-hMargin*2;
        double pixels[]= new double[tmpW*tmpH] ;
        
        img.getRaster().getPixels(wMargin, hMargin, tmpW, tmpH, pixels);
        
        int minX =tmpW,  maxX= 0;
        
        for(int col = 0; col<tmpW; ++col)
            for(int row=0;row<tmpH;++row){
                if(pixels[col + row*tmpW]==1){
                    if(minX>col)
                        minX=col;
                    if(maxX<col)
                        maxX=col;
                }
                    
            }
        rect.x = minX +wMargin;
        rect.width = maxX;
        rect.y =  hMargin;
        rect.height = tmpH;
        
        return rect;
    }
}
