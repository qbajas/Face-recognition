package Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
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
import javax.media.jai.operator.CropDescriptor;
import javax.media.jai.operator.MedianFilterDescriptor;

/**
 *
 * @author Michal
 */
public class TrainingSetScalling {
    private static final int margin=15;
    private static final int WIDTH =(640-2*margin)/6, HEIGHT=(480-2*margin)/6;
    
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
        BufferedImage padded = null;
        Graphics2D g = null;

        Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = (ImageWriter) iter.next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(1);

        FileImageOutputStream outStream;

        for (File folder : subFolders) {
            images = folder.listFiles(jpgFilter);



            for (File imageFile : images) {
                System.out.println("Resizing " + imageFile.getName());
                try {
                    BufferedImage img = ImageIO.read(imageFile); 

                    RenderedOp op = MedianFilterDescriptor.create(img, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 3, null);
                 //   op = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 3, null);
                    op = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 5, null);            
                    
                    float[] kernelMatrix = {    -1, 0, 1,
                                                -1, 0, 1,
                                                -1, 0, 1 };
                    float[] kernelMatrix2 = {    1, 0, -1,
                                                 1, 0, -1,
                                                 1, 0, -1 };
                    
                    KernelJAI kernel = new KernelJAI(3,3,kernelMatrix);
                    KernelJAI kernel2 = new KernelJAI(3,3,kernelMatrix2);
                    PlanarImage im1 =JAI.create("gradientmagnitude", op,kernel, kernel2);
                    
                    
                    
                    //PlanarImage convolved = JAI.create("convolve", op, kernel);
                   // PlanarImage convolved2 = JAI.create("convolve", op, kernel2);
                    
                         // Create the ParameterBlock for the operation
                    //ParameterBlock pb = new ParameterBlock();
                   // pb.addSource(convolved);
                 //   pb.addSource(convolved2);

                    // Create the Add operation.
                   // PlanarImage dst = JAI.create("add", pb);
                    PlanarImage binarized = BinarizeDescriptor.create(im1, 60.0, null);
                    kernelMatrix = new  float[]{0,1,0,
                                                1,1,1,
                                                0,1,0};
                    kernel2 = new KernelJAI(3,3,kernelMatrix2);
                    binarized = JAI.create("dilate",binarized,kernel2);
                    binarized = JAI.create("erode", binarized,kernel2);
                    
                    Rectangle  rect = getFaceBounds(binarized.getAsBufferedImage());
                    
                    //op = CropDescriptor.create(op,(float)rect.x,(float)rect.y,(float)rect.width,(float)rect.height, null);
                    
                    float diff = 480/(float)rect.width;
                    
                    int prefW=480,  prefH=560;
                    
                    padded = new BufferedImage(prefW ,(int)(rect.height*diff), BufferedImage.TYPE_BYTE_GRAY);
                    g = padded.createGraphics();
                    //480x600
                    
                    
                    g.setComposite(AlphaComposite.Src);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);                  
                   
                    int imgH= (int)(rect.height*diff);
                    g.drawImage(img.getSubimage(rect.x, rect.y, rect.width, rect.height), 0, 0, prefW,(int)(rect.height*diff),null);
                    g.dispose();
                    
                    rect.height = imgH;
                    if(imgH>prefH){
                        int dff = imgH - prefH ;
                        if(dff>=50){
                            rect.y+=50;
                            rect.height-=50;
                            dff-=50;
                        } else{
                            rect.y+=dff;
                            rect.height-=dff;
                            dff=0;
                        }
                        if(dff!=0){
                            rect.height-=dff;
                        }
                    }
                    double factor = 5.7;
                    BufferedImage copy =new BufferedImage((int)(prefW/factor) ,(int)(prefH/factor), BufferedImage.TYPE_BYTE_GRAY);
                    g = copy.createGraphics();

                    g.setComposite(AlphaComposite.Src);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);                  

                    g.drawImage(padded.getSubimage(0, rect.y,prefW, rect.height), 0, 0, (int)(prefW/factor),(int)(prefH/factor),null);
                    g.dispose();

                    outStream = new FileImageOutputStream(imageFile);
                    writer.setOutput(outStream);
                    IIOImage outImg = new IIOImage(copy, null, null);
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
        int ignoredRows = 90;
        
        int tmpW = img.getWidth()-wMargin;
        int tmpH = img.getHeight()-hMargin;
        double pixels[]= new double[img.getWidth()*img.getHeight()] ;
        
        img.getRaster().getPixels(0, 0, img.getWidth(), img.getHeight(), pixels);
        
        int minX =tmpW,  maxX= 0;
        
        for(int col = wMargin; col<tmpW; ++col)
            for(int row=hMargin;row<tmpH-ignoredRows;++row){
                if(pixels[col + row*img.getWidth()]==1){
                    if(minX>col)
                        minX=col;
                    if(maxX<col)
                        maxX=col;
                }
                    
            }
        rect.x = minX;
        rect.width = maxX - minX ;
        
        rect.y =  0;
        rect.height = img.getHeight();//440
        
        return rect;
    }
}

/*
 * 
 *     public void resizeAll(String source) {
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
        BufferedImage padded = null;
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

                    RenderedOp op = MedianFilterDescriptor.create(img, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 3, null);
                //    op = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 3, null);
                 //   op = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 5, null);
                 //   op = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 5, null);
                 //   RenderedOp op2 = MedianFilterDescriptor.create(op, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 12, null);
//                    op2 = MedianFilterDescriptor.create(op2, MedianFilterDescriptor.MEDIAN_MASK_SQUARE, 7, null);
//                    
//                    
//                    float[] kernelMatrix = {    -1, -2, -1,
//                                                -2, 12, -2,
//                                                -1, -2, -1 };
//                    KernelJAI kernel = new KernelJAI(3,3,kernelMatrix);
//                    PlanarImage convolved = JAI.create("convolve", op2, kernel);
//                   
//                    PlanarImage binarized = BinarizeDescriptor.create(convolved, 120.0, null);
                    
                    
                 //   Rectangle  rect = getFaceBounds(binarized.getAsBufferedImage());
                   // processed = JAI.create("convolve", processed, kernel);
                    padded = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
                    g = padded.createGraphics();
                    
                    
                    g.setComposite(AlphaComposite.Src);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                 //   int w = (480-rect.width)/2;
                 //   g.setColor(new Color(153,153,153));
                    
                 //   BufferedImage pad= op.getAsBufferedImage().getSubimage(rect.x, rect.y, rect.width, rect.height);
                //    g.fillRect(0,0,pad.getWidth()+2*w,padded.getHeight());

               //     g.drawImage(pad,w,padded.getHeight()-pad.getHeight(),null);
                    
                    
                    BufferedImage copy = op.getAsBufferedImage().getSubimage(margin,margin,op.getWidth()-margin*2,op.getHeight()-margin*2);
                    g.drawImage(copy, 0, 0, WIDTH,HEIGHT,null);
                    g.dispose();

                    outStream = new FileImageOutputStream(image);
                    writer.setOutput(outStream);
                    IIOImage outImg = new IIOImage(padded, null, null);
                    writer.write(null, outImg, iwp);
                    outStream.close();
                    writer.reset();

                } catch (IOException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }

        }
    }
 */