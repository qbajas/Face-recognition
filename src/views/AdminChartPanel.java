/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.Color;
import java.util.Locale;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class AdminChartPanel extends JPanel{
    private XYSeries trainingSetAccuracy  = new XYSeries("Training set accuracy");
    
    private XYSeries generalizationSetAccuracy = new XYSeries("Generalization set accuracy");
    
    
    private XYSeriesCollection datasetA = new XYSeriesCollection();

    JFreeChart chart;
    ChartPanel panel;
    
    int tsaIter=0;
    int tsMSEIter=0;
    int gsaIter=0;
    int gsMSEIter=0;

    
    public AdminChartPanel(){
        super();
        resetSeries();
        
        
        
        datasetA.addSeries(trainingSetAccuracy);
        
        datasetA.addSeries(generalizationSetAccuracy);
          
        
        
        chart=ChartFactory.createXYLineChart("ANN training","iteration",
                "ACC",datasetA,PlotOrientation.VERTICAL,
                true,true,false);
        
        
        panel=new ChartPanel(chart,true,true,false,true,true);
        
        
        final XYPlot plot = chart.getXYPlot();

        
        plot.mapDatasetToRangeAxis(1, 1);
        final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.black);
        renderer2.setSeriesPaint(1, Color.ORANGE);

        plot.setRenderer(1, renderer2);
        
        

        panel.setBackground(Color.CYAN);
        add(panel);
        setBackground(Color.LIGHT_GRAY);
    }

    public ChartPanel getChartPanel(){
        return panel;
    }
    
    public final void resetSeries(){
        
        tsaIter=0;
        tsMSEIter=0;
        gsaIter=0;
        gsMSEIter=0;
        
        trainingSetAccuracy.clear();
        
        generalizationSetAccuracy.clear();
        
        
        trainingSetAccuracy.add(tsaIter, 0);
        
        generalizationSetAccuracy.add(gsaIter, 0);
        
        
    }


    
    
    
    public void updateTrainingSetAccuracy(double accuracy) {
        trainingSetAccuracy.add(++tsaIter, accuracy);
    }

    


    
    public void updateGeneralizationSetAccuracy(double accuracy) {
        generalizationSetAccuracy.add(++gsaIter, accuracy);
    }

    


    
    public void nextEpoch() {
    }
    
}