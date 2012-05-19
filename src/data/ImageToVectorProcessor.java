package data;

import java.awt.image.BufferedImage;

/**
 * Klasa do konwersji obrazu na wektor wartosci liczbowych, skaluje obraz
 * aby zmniejszyc ilosc pikseli
 *
 * @author Michal
 */
public class ImageToVectorProcessor implements ImageProcessor {

    private boolean convert2Gray;

    /**
     * Tworzy nowy obiekt konwertera
     *
     * @param enableRGB2GrayConversion Czy ma nastapic konwersja RGB na odcienie
     * szarosci (3-krotne skrocenie wektora wartosci)
     */
    public ImageToVectorProcessor(boolean enableRGB2GrayConversion) {
        convert2Gray = enableRGB2GrayConversion;
    }

    @Override
    public double[] process(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        double[] pixels = null;
        
        pixels = img.getRaster().getPixels(0, 0, w, h, (double[])null);
        
        if (convert2Gray &&img.getColorModel().getColorSpace().getNumComponents()==3 )
            pixels = RGB2Gray(pixels);
        return pixels;
    }

    public double[] RGB2Gray(double[] RGBPixels) {
        double[] gray = new double[RGBPixels.length / 3];
        int j = -1;
        for (int i = 0; i < gray.length; ++i)
            gray[i] = 0.299 * RGBPixels[++j] + 0.587 * RGBPixels[++j] + 0.114 * RGBPixels[++j];
        return gray;
    }
}
