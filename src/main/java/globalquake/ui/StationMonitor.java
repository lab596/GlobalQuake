package globalquake.ui;

import globalquake.core.station.AbstractStation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class StationMonitor extends JFrame {

	public StationMonitor(Component parent, AbstractStation station) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		StationMonitorPanel panel = new StationMonitorPanel(station);
		setContentPane(panel);

		pack();
		setLocationRelativeTo(parent);
		setResizable(true);
		setTitle("Station Monitor - " + station.getNetworkCode() + " " + station.getStationCode() + " "
				+ station.getChannelName() + " " + station.getLocationCode());

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				panel.updateImage();
				panel.repaint();
			}
		}, 0, 1000);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				timer.cancel();
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					dispose();
				}
			}
		});

		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				panel.updateImage();
				panel.repaint();
			}
		});

		setVisible(true);
	}

}
