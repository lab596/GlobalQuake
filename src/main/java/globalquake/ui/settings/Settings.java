package globalquake.ui.settings;

import globalquake.main.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class Settings {

	private static final File optionsFile = new File(Main.MAIN_FOLDER, "globalQuake.properties");
	private static final Properties properties = new Properties();
	public static Boolean enableAlarmDialogs;
	
	public static Double homeLat;
	public static Double homeLon;

	public static final double pWaveInaccuracyThresholdDefault = 1800;
	public static Double pWaveInaccuracyThreshold;
	public static final double hypocenterCorrectThresholdDefault = 50;
	public static Double hypocenterCorrectThreshold;

	public static final double hypocenterDetectionResolutionDefault = 40;
	public static Double hypocenterDetectionResolution;

	public static Boolean parallelHypocenterLocations;
	public static final int minimumStationsForEEWDefault = 5;

	public static Integer minimumStationsForEEW;

	public static Boolean displayArchivedQuakes;

	public static Boolean useOldColorScheme;

	public static Boolean displayHomeLocation;

	public static Boolean antialiasing;

	public static Integer fpsIdle;

	public static Integer intensityScaleIndex;
	
	public static final boolean reportsEnabled = false; // not available ATM
	public static Boolean enableSound = true;
	public static Boolean oldEventsTimeFilterEnabled;
	public static Double oldEventsTimeFilter;
	public static Boolean oldEventsMagnitudeFilterEnabled;
	public static Double oldEventsMagnitudeFilter;

	public static int changes = 0;

	public static Double oldEventsOpacity;

	static {
		load();
	}

	private static void load() {
		try {
			properties.load(new FileInputStream(optionsFile));
		} catch (IOException e) {
			System.out.println("Created GlobalQuake properties file at "+optionsFile.getAbsolutePath());
		}
		
		enableAlarmDialogs = Boolean.valueOf((String) properties.getOrDefault("enableAlarmDialogs", "false"));
		
		homeLat = Double.valueOf((String) properties.getOrDefault("homeLat", "0.0"));
		homeLon = Double.valueOf((String) properties.getOrDefault("homeLon", "0.0"));
		displayArchivedQuakes = Boolean.valueOf((String) properties.getOrDefault("displayArchivedQuakes", "true"));
		enableSound = Boolean.valueOf((String) properties.getOrDefault("enableSound", "true"));

		pWaveInaccuracyThreshold = Double.valueOf((String) properties.getOrDefault("pWaveInaccuracyThreshold", String.valueOf(pWaveInaccuracyThresholdDefault)));
		hypocenterCorrectThreshold = Double.valueOf((String) properties.getOrDefault("hypocenterCorrectThreshold", String.valueOf(hypocenterCorrectThresholdDefault)));
		hypocenterDetectionResolution = Double.valueOf((String) properties.getOrDefault("hypocenterDetectionResolution", String.valueOf(hypocenterDetectionResolutionDefault)));
		minimumStationsForEEW = Integer.valueOf((String) properties.getOrDefault("minimumStationsForEEW", String.valueOf(minimumStationsForEEWDefault)));

		useOldColorScheme = Boolean.valueOf((String) properties.getOrDefault("useOldColorScheme", "false"));
		parallelHypocenterLocations = Boolean.valueOf((String) properties.getOrDefault("parallelHypocenterLocations", "false"));
		displayHomeLocation = Boolean.valueOf((String) properties.getOrDefault("displayHomeLocation", "true"));
		antialiasing = Boolean.valueOf((String) properties.getOrDefault("antialiasing", "false"));
		fpsIdle = Integer.valueOf((String) properties.getOrDefault("fpsIdle", "30"));

		intensityScaleIndex = Integer.valueOf((String) properties.getOrDefault("intensityScaleIndex", "0"));

		oldEventsTimeFilterEnabled = Boolean.valueOf((String) properties.getOrDefault("oldEventsTimeFilterEnabled", "false"));
		oldEventsTimeFilter = Double.valueOf((String) properties.getOrDefault("oldEventsTimeFilter", "24.0"));

		oldEventsMagnitudeFilterEnabled = Boolean.valueOf((String) properties.getOrDefault("oldEventsMagnitudeFilterEnabled", "false"));
		oldEventsMagnitudeFilter = Double.valueOf((String) properties.getOrDefault("oldEventsMagnitudeFilter", "4.0"));

		oldEventsOpacity = Double.valueOf((String) properties.getOrDefault("oldEventsOpacity", "100.0"));

		save();
	}
	
	
	public static void save() {
		changes++;
		properties.setProperty("enableAlarmDialogs", String.valueOf(enableAlarmDialogs));
		
		properties.setProperty("homeLat", String.valueOf(homeLat));
		properties.setProperty("homeLon", String.valueOf(homeLon));
		properties.setProperty("displayArchivedQuakes", String.valueOf(displayArchivedQuakes));
		properties.setProperty("enableSound", String.valueOf(enableSound));

		properties.setProperty("pWaveInaccuracyThreshold", String.valueOf(pWaveInaccuracyThreshold));
		properties.setProperty("hypocenterCorrectThreshold", String.valueOf(hypocenterCorrectThreshold));
		properties.setProperty("hypocenterDetectionResolution", String.valueOf(hypocenterDetectionResolution));
		properties.setProperty("minimumStationsForEEW", String.valueOf(minimumStationsForEEW));

		properties.setProperty("useOldColorScheme", String.valueOf(useOldColorScheme));
		properties.setProperty("parallelHypocenterLocations", String.valueOf(parallelHypocenterLocations));
		properties.setProperty("displayHomeLocation", String.valueOf(displayHomeLocation));
		properties.setProperty("antialiasing", String.valueOf(antialiasing));
		properties.setProperty("fpsIdle", String.valueOf(fpsIdle));

		properties.setProperty("intensityScaleIndex", String.valueOf(intensityScaleIndex));

		properties.setProperty("oldEventsTimeFilterEnabled", String.valueOf(oldEventsTimeFilterEnabled));
		properties.setProperty("oldEventsTimeFilter", String.valueOf(oldEventsTimeFilter));
		properties.setProperty("oldEventsMagnitudeFilterEnabled", String.valueOf(oldEventsMagnitudeFilterEnabled));
		properties.setProperty("oldEventsMagnitudeFilter", String.valueOf(oldEventsMagnitudeFilter));

		properties.setProperty("oldEventsOpacity", String.valueOf(oldEventsOpacity));

		try {
			properties.store(new FileOutputStream(optionsFile), "Fun fact: I've never felt an earthquake in my life");
		} catch (IOException e) {
			Main.getErrorHandler().handleException(e);
		}
	}
}
