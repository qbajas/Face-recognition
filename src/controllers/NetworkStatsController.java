package controllers;

import javax.swing.JFrame;

import views.AdminChartPanel;
import views.NetworkStats;
import ann.TrainingListener;

public class NetworkStatsController implements TrainingListener {

	AdminChartPanel view;

	@Override
	public void trainingStarted() {
		view = new AdminChartPanel();
		JFrame frame = new JFrame();
		frame.getContentPane().add(view);	
		frame.setBounds(0, 0, 700, 480);
		frame.setVisible(true);
		System.out.println("Opening statistics");
	}

	@Override
	public void trainingFinished() {
	}

	@Override
	public void trainingUpdate(double errorRate, double trainingAccuracy,
			double generalizationAccuracy) {

		view.updateTrainingSetAccuracy(trainingAccuracy);
		view.updateGeneralizationSetAccuracy(generalizationAccuracy);

	}

	@Override
	public void testUpdate(double errorRate, double accuracy,
			double falsePositiveAccuracy) {
	}

}
