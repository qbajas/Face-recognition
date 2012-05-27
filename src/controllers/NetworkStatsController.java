package controllers;

import javax.swing.JFrame;

import views.NetworkStats;
import ann.TrainingListener;

public class NetworkStatsController implements TrainingListener {
	
	NetworkStats view;

	@Override
	public void trainingStarted() {
		view = new NetworkStats();
		JFrame frame = new JFrame();
		frame.getContentPane().add(view);
		frame.setBounds(100, 0, 800, 700);
		frame.setVisible(true);
		System.out.println("Opening statistics");
	}

	@Override
	public void trainingFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trainingUpdate(double errorRate, double trainingAccuracy,
			double generalizationAccuracy) {
		
		view.adminChartPanel1.updateTrainingSetAccuracy(trainingAccuracy);
		view.adminChartPanel1.updateGeneralizationSetAccuracy(generalizationAccuracy);		
		
	}

	@Override
	public void testUpdate(double errorRate, double accuracy,
			double falsePositiveAccuracy) {
		// TODO Auto-generated method stub
		
	}

}
