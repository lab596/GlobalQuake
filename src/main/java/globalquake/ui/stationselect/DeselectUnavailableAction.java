package globalquake.ui.stationselect;

import globalquake.database.Network;
import globalquake.database.StationDatabaseManager;

import javax.swing.*;

import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class DeselectUnavailableAction extends AbstractAction {

    private final StationDatabaseManager stationDatabaseManager;
    private final Window parent;

    public DeselectUnavailableAction(StationDatabaseManager stationDatabaseManager, Window parent) {
        super("Deselect Unavailable");
        this.stationDatabaseManager=stationDatabaseManager;
        this.parent=parent;

        putValue(SHORT_DESCRIPTION, "Deselects All Unavailable Stations");

        ImageIcon deselectUnavailable = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image_icons/deselectUnavailable.png")));
        Image image = deselectUnavailable.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);
        putValue(Action.SMALL_ICON, scaledIcon);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        boolean alreadyDeselected = true;
        stationDatabaseManager.getStationDatabase().getDatabaseWriteLock().lock();
        try{
            for(Network network : stationDatabaseManager.getStationDatabase().getNetworks()){
                for(globalquake.database.Station station : network.getStations()){
                    if(station.getSelectedChannel() != null && !station.getSelectedChannel().isAvailable()){
                        alreadyDeselected = false;
                        break;
                    }
                }
                network.getStations().forEach(station -> {
                    if(station.getSelectedChannel() != null && !station.getSelectedChannel().isAvailable()) {
                        station.setSelectedChannel(null);
                    }
                });
            }
            if(alreadyDeselected){
                JOptionPane.showMessageDialog(parent, "All Unavailable Stations Already Deselected");
            }
            stationDatabaseManager.fireUpdateEvent();
        }finally {
            stationDatabaseManager.getStationDatabase().getDatabaseWriteLock().unlock();
        }
    }
}
