package globalquake.core.earthquake;

import globalquake.core.GlobalQuake;
import globalquake.core.report.EarthquakeReporter;
import globalquake.main.Main;
import globalquake.ui.settings.Settings;
import org.tinylog.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class EarthquakeArchive {

	private final GlobalQuake globalQuake;
	public static final File ARCHIVE_FILE = new File(Main.MAIN_FOLDER, "archive.dat");
	public static final File TEMP_ARCHIVE_FILE = new File(Main.MAIN_FOLDER, "temp_archive.dat");

	private SortedSet<ArchivedQuake> archivedQuakes;

	public EarthquakeArchive(GlobalQuake globalQuake) {
		this.globalQuake = globalQuake;
		loadArchive();
	}

	@SuppressWarnings("unchecked")
	private void loadArchive() {
		if (!ARCHIVE_FILE.exists()) {
			archivedQuakes = new ConcurrentSkipListSet<>();
			System.out.println("Created new archive");
		} else {
			try {
				ObjectInputStream oin = new ObjectInputStream(new FileInputStream(ARCHIVE_FILE));
				archivedQuakes = (ConcurrentSkipListSet<ArchivedQuake>) oin.readObject();
				oin.close();
				System.out.println("Loaded " + archivedQuakes.size() + " quakes from archive.");
			} catch (Exception e) {
				Logger.error(e);
				archivedQuakes = new ConcurrentSkipListSet<>();
			}
		}
		saveArchive();
	}

	public void saveArchive() {
		if (archivedQuakes != null) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEMP_ARCHIVE_FILE));
				System.out.println("Saving " + archivedQuakes.size() + " quakes to " + ARCHIVE_FILE.getName());
				out.writeObject(archivedQuakes);
				out.close();
				boolean res = (!ARCHIVE_FILE.exists() || ARCHIVE_FILE.delete()) && TEMP_ARCHIVE_FILE.renameTo(ARCHIVE_FILE);
				if(!res){
					Logger.error("Unable to save archive!");
				}
			} catch (Exception e) {
				Logger.error(e);
			}
		}
	}

	public GlobalQuake getGlobalQuake() {
		return globalQuake;
	}

	public SortedSet<ArchivedQuake> getArchivedQuakes() {
		return archivedQuakes;
	}

	public void archiveQuakeAndSave(Earthquake earthquake) {
		new Thread("Archive Thread") {
			public void run() {
				ArchivedQuake archivedQuake = new ArchivedQuake(earthquake);
				archivedQuakes.add(archivedQuake);

				saveArchive();
				if(Settings.reportsEnabled) {
					reportQuake(earthquake);
				}

			}
        }.start();
	}

	private void reportQuake(Earthquake earthquake) {
		new Thread("Quake Reporter") {
			@Override
			public void run() {
				try {
					EarthquakeReporter.report(earthquake);
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		}.start();
	}

	public void archiveQuake(Earthquake earthquake) {
		ArchivedQuake archivedQuake = new ArchivedQuake(earthquake);
		archivedQuakes.add(archivedQuake);
	}

	public void update() {
		Iterator<ArchivedQuake> it = archivedQuakes.iterator();
		List<ArchivedQuake> toBeRemoved = new ArrayList<>();
		boolean save = false;
		while (it.hasNext()) {
			ArchivedQuake archivedQuake = it.next();
			long age = System.currentTimeMillis() - archivedQuake.getOrigin();
			if (age > 1000L * 60 * 60 * 24 * 3L) {
				if (!save) {
					save = true;
				}
				toBeRemoved.add(archivedQuake);
			}
		}

		toBeRemoved.forEach(archivedQuakes::remove);

		if (save) {
			new Thread("Archive Save Thread") {
				public void run() {
					saveArchive();
				}
			}.start();
		}
	}
}
