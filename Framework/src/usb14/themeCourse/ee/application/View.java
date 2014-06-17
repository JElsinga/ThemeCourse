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

import usb14.themeCourse.ee.framework.*;
import net.miginfocom.swing.MigLayout;

/**
 * Class that implements both a View and Controller that observes changes in 
 * Controllables and shows them in plots.
 */
public class View extends JFrame implements Observer {
	
	private static final long serialVersionUID = 9125637833500321931L;
	
	private Controller controller;
	
	private Fridge fridge;
	private XYSeries fridgeUsageSeries;
	private XYSeries fridgeTemperatureSeries;
	
	private WashingMachine washer;
	private XYSeries washerUsageSeries;
	
	private Battery battery;
	private XYSeries batteryUsageSeries;
	private XYSeries batteryChargeSeries;
	
	private Network network;
	private XYSeries networkUsageSeries;
	
	// Holds the usage of the last interval for every Controllable in order to make a better usage plot
	private Map<Controllable, Integer> lastUsageByControllable;

	public View(){
		lastUsageByControllable = new HashMap<Controllable, Integer>();
		
		fridge = new Fridge("Super Fridge X9000");
		fridge.addObserver(this);
		
		washer = new WashingMachine("Mega Washer 1000");
		washer.addObserver(this);
		
		battery = new Battery("Battery");
		battery.addObserver(this);
		
		network = new Network();
		network.addControllable(fridge);
		network.addControllable(washer);
		network.addControllable(battery);
		network.addObserver(this);
		
		Controller.initialise(network, 10);
		controller = Controller.getInstance();
		
		initComponents();
		
		controller.start();
	}
	
	private void initComponents(){
		setTitle("Energy Efficiency Simulation");
		setLayout(new MigLayout("wrap 1", "[grow]", ""));
		
		
		// Fridge Usage Plot
		fridgeUsageSeries = new XYSeries("Fridge Usage");
		XYSeriesCollection fridgeUsageData = new XYSeriesCollection(fridgeUsageSeries);
		JFreeChart fridgeUsageChart = ChartFactory.createXYLineChart(
				"Fridge Usage", "Time", "Usage", fridgeUsageData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel fridgeUsageChartPanel = new ChartPanel(fridgeUsageChart);
		fridgeUsageChartPanel.setMaximumDrawWidth(10000);
		
		// Fridge Temperature Plot
		fridgeTemperatureSeries = new XYSeries("Fridge Temperature");
		XYSeriesCollection fridgeTemperatureData = new XYSeriesCollection(fridgeTemperatureSeries);
		JFreeChart fridgeTemperatureChart = ChartFactory.createXYLineChart(
				"Fridge Temperature", "Time", "Temperature", fridgeTemperatureData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel fridgeTemperatureChartPanel = new ChartPanel(fridgeTemperatureChart);
		fridgeTemperatureChartPanel.setMaximumDrawWidth(10000);
		
		// Washer Usage Plot
		washerUsageSeries = new XYSeries("Washer Usage");
		XYSeriesCollection washerUsageData = new XYSeriesCollection(washerUsageSeries);
		JFreeChart washerUsageChart = ChartFactory.createXYLineChart(
				"Washer Usage", "Time", "Usage", washerUsageData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel washerUsageChartPanel = new ChartPanel(washerUsageChart);
		washerUsageChartPanel.setMaximumDrawWidth(10000);
				
		// Battery Usage Plot
		batteryUsageSeries = new XYSeries("Battery Usage");
		XYSeriesCollection batteryUsageData = new XYSeriesCollection(batteryUsageSeries);
		JFreeChart batteryUsageChart = ChartFactory.createXYLineChart(
				"Battery Usage", "Time","Usage", batteryUsageData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel batteryUsageChartPanel = new ChartPanel(batteryUsageChart);
		batteryUsageChartPanel.setMaximumDrawWidth(10000);
		
		// Battery Charge Plot
		batteryChargeSeries = new XYSeries("Battery Charge");
		XYSeriesCollection batteryLoadData = new XYSeriesCollection(batteryChargeSeries);
		JFreeChart batteryChargeChart = ChartFactory.createXYLineChart(
				"Battery Charge", "Time","Charge", batteryLoadData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel batteryChargeChartPanel = new ChartPanel(batteryChargeChart);
		batteryChargeChartPanel.setMaximumDrawWidth(10000);
		
		// Network Usage Plot
		networkUsageSeries = new XYSeries("Network Usage");
		XYSeriesCollection networkUsageData = new XYSeriesCollection(networkUsageSeries);
		JFreeChart networkUsageChart = ChartFactory.createXYLineChart(
				"Network Usage", "Time","Usage", networkUsageData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel networkUsageChartPanel = new ChartPanel(networkUsageChart);
		networkUsageChartPanel.setMaximumDrawWidth(10000);
		
		this.add(washerUsageChartPanel, "growx");
		this.add(fridgeUsageChartPanel, "growx");
		this.add(fridgeTemperatureChartPanel, "growx");
		this.add(batteryUsageChartPanel, "growx");
		this.add(batteryChargeChartPanel, "growx");
		this.add(networkUsageChartPanel, "growx");
		
		this.setSize(750, 750);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void update(Observable observable, Object object) {
		int time = controller.getTime();
		if (observable == fridge) {
			if (lastUsageByControllable.containsKey(fridge))
				fridgeUsageSeries.add(time, lastUsageByControllable.get(fridge));
			fridgeUsageSeries.add(time, fridge.getCurrentUsage());
			lastUsageByControllable.put(fridge, fridge.getCurrentUsage());
			
			fridgeTemperatureSeries.add(time, fridge.getTemp());
		}
		if (observable == washer) {
			if (lastUsageByControllable.containsKey(washer))
				washerUsageSeries.add(time, lastUsageByControllable.get(washer));
			washerUsageSeries.add(time, washer.getCurrentUsage());
			lastUsageByControllable.put(washer, washer.getCurrentUsage());
		}
		if (observable == battery){
			if(lastUsageByControllable.containsKey(battery))
				batteryUsageSeries.add(time, lastUsageByControllable.get(battery));
			batteryUsageSeries.add(time, battery.getCurrentUsage());
			batteryChargeSeries.add(time, battery.getCharge());
			
			lastUsageByControllable.put(battery, battery.getCurrentUsage());
		}
		if (observable == network){
			if(lastUsageByControllable.containsKey(network))
				networkUsageSeries.add(time, lastUsageByControllable.get(network));
			networkUsageSeries.add(time, network.getCurrentUsage());
			
			lastUsageByControllable.put(network, network.getCurrentUsage());
			
		}
	}

}
