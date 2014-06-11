package usb14.themeCourse.ee.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import usb14.themeCourse.ee.framework.Controllable;
import usb14.themeCourse.ee.framework.Controller;
import usb14.themeCourse.ee.framework.Network;
import net.miginfocom.swing.MigLayout;

/**
 * Class that implements both a View and Controller that observes changes in 
 * Controllables and shows them in plots.
 */
public class View_testBattery extends JFrame implements Observer {
	
	private static final long serialVersionUID = 9125637833500321931L;
	
	private Controller controller;
	
	private Battery battery;
	private XYSeries batteryUsageSeries;
	private XYSeries batteryLoadSeries;
	
	// Holds the usage of the last interval for every Controllable in order to make a better usage plot
	private Map<Controllable, Integer> lastUsageByControllable;

	public View_testBattery(){
		lastUsageByControllable = new HashMap<Controllable, Integer>();		
		
		battery = new Battery("Battery");
		battery.addObserver(this);
				
		Controller.initialise(battery, 1);
		controller = Controller.getInstance();
		
		initComponents();
		
		controller.start();
	}
	
	private void initComponents(){
		setTitle("Energy Efficiency Simulation");
		setLayout(new MigLayout("wrap 1", "[grow]", ""));
		
		// Battery Usage Plot
		batteryUsageSeries = new XYSeries("Battery Usage");
		XYSeriesCollection batteryUsageData = new XYSeriesCollection(batteryUsageSeries);
		JFreeChart batteryUsageChart = ChartFactory.createXYLineChart(
				"Battery Usage", "Time","Usage", batteryUsageData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel batteryUsageChartPanel = new ChartPanel(batteryUsageChart);
		batteryUsageChartPanel.setMaximumDrawWidth(10000);
		
		// Battery Load Plot
		batteryLoadSeries = new XYSeries("Battery Load");
		XYSeriesCollection batteryLoadData = new XYSeriesCollection(batteryLoadSeries);
		JFreeChart batteryLoadChart = ChartFactory.createXYLineChart(
				"BatteryLoad", "Time", "Load", batteryLoadData, PlotOrientation.VERTICAL,
				false,false,false);
		ChartPanel batteryLoadChartPanel = new ChartPanel(batteryLoadChart);
		batteryLoadChartPanel.setMaximumDrawWidth(10000);
				
		this.add(batteryUsageChartPanel, "growx");
		this.add(batteryLoadChartPanel, "growx");
		
		this.setSize(750, 750);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void update(Observable observable, Object object) {
		int time = controller.getTime();
		if (observable == battery){
			if(lastUsageByControllable.containsKey(battery))
				batteryUsageSeries.add(time, lastUsageByControllable.get(battery));
			batteryUsageSeries.add(time, battery.getCurrentUsage());
			lastUsageByControllable.put(battery, battery.getCurrentUsage());
			
			batteryLoadSeries.add(time,battery.getLoad());
			System.out.println(battery.getCostFunction());
			System.out.println("Time: "+time+" Price: 500"+" load: "+battery.getLoad());
			//System.out.println(time+" -\tstate: "+battery.getState());
		}
	}

}
