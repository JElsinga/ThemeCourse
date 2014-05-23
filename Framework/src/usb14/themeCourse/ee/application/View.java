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

import net.miginfocom.swing.MigLayout;

public class View extends JFrame implements Observer {
	
	private static final long serialVersionUID = 9125637833500321931L;
	
	XYSeries testXYSeries;

	public View(){
		initComponents();
	}
	
	private void initComponents(){
		setTitle("Energy Efficiency Simulation");
		setLayout(new MigLayout("wrap 1"));
		
		// Test Plot
		testXYSeries = new XYSeries("Test Data");
		XYSeriesCollection testData = new XYSeriesCollection(testXYSeries);
		JFreeChart testChart = ChartFactory.createXYLineChart(
				"Test", "Time", "Usage", testData, PlotOrientation.VERTICAL,
				false, false, false);
		ChartPanel testChartPanel = new ChartPanel(testChart);
		
		testXYSeries.add(0, 5);
		testXYSeries.add(1, 5);
		testXYSeries.add(1, 4);
		testXYSeries.add(2, 4);
		testXYSeries.add(2, 5);
		testXYSeries.add(3, 5);
		testXYSeries.add(3, 7);
		testXYSeries.add(4, 7);
		testXYSeries.add(4, 2);
		testXYSeries.add(5, 2);
		testXYSeries.add(5, 5);
		this.add(testChartPanel);
		setVisible(true);
	}

	@Override
	public void update(Observable observable, Object object) {
		// TODO Auto-generated method stub
		
	}

}
