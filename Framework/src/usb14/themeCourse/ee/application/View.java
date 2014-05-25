package usb14.themeCourse.ee.application;

import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.Fridge;
import net.miginfocom.swing.MigLayout;

/**
 * Class that implements both a View and Controller that observes changes in 
 * Controllables and shows them in plots.
 */
public class View extends JFrame implements Observer {
	
	private static final long serialVersionUID = 9125637833500321931L;
	
	Controller controller;
	Fridge fridge;
	XYSeries fridgeUsageSeries;
	XYSeries fridgeTemperatureSeries;

	public View(){
		fridge = new Fridge("Super Fridge X9000");
		fridge.addObserver(this);
		
		Controller.initialise(fridge, 10);
		controller = Controller.getInstance();
		
		initComponents();
		
		controller.start();
	}
	
	private void initComponents(){
		setTitle("Energy Efficiency Simulation");
		setLayout(new MigLayout("wrap 1"));
		
		// Fridge Usage Plot
		fridgeUsageSeries = new XYSeries("Fridge Usage");
		XYSeriesCollection fridgeUsageData = new XYSeriesCollection(fridgeUsageSeries);
		JFreeChart fridgeUsageChart = ChartFactory.createXYLineChart(
				"Fridge Usage", "Time", "Usage", fridgeUsageData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel fridgeUsageChartPanel = new ChartPanel(fridgeUsageChart);
		
		// Fridge Temperature Plot
		fridgeTemperatureSeries = new XYSeries("Fridge Temperature");
		XYSeriesCollection fridgeTemperatureData = new XYSeriesCollection(fridgeTemperatureSeries);
		JFreeChart fridgeTemperatureChart = ChartFactory.createXYLineChart(
				"Fridge Temperature", "Time", "Temperature", fridgeTemperatureData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel fridgeTemperatureChartPanel = new ChartPanel(fridgeTemperatureChart);
		
		this.add(fridgeUsageChartPanel);
		this.add(fridgeTemperatureChartPanel);
		
		this.setSize(500, 500);
		setVisible(true);
	}

	@Override
	public void update(Observable observable, Object object) {
		int time = controller.getTime();
		if (observable == fridge) {
			fridgeUsageSeries.add(time, fridge.getCurrentUsage());
			fridgeUsageSeries.add(time + controller.getIntervalDuration(), fridge.getCurrentUsage());
			fridgeTemperatureSeries.add(time, fridge.getTemp());
		}
	}

}
