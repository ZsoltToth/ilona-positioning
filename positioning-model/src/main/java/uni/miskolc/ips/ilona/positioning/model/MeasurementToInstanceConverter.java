package uni.miskolc.ips.ilona.positioning.model;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;



public class MeasurementToInstanceConverter {
	
	private static final Logger LOG = LogManager.getLogger(MeasurementToInstanceConverter.class);
	

	/**
	 * Convert the measurement to Instance for weka classifiers.
	 * 
	 * @param meas
	 *            The incoming measurement
	 * @return The instance of the converted measurement.
	 */
	public static Instance convertMeasurementToInstance(Measurement meas, List<Attribute> header){
		Instance instance = new DenseInstance(header.size());
		List<Attribute> attributes = header;
		LOG.info("The attributes are " + attributes.toString());
		LOG.info("The incoming measurement is  " + meas.toString());
		for (int i = 0; i < attributes.size(); i++) {
			if ((attributes.get(i).name().equals("measx")|| attributes.get(i).name().equalsIgnoreCase("magnetometerX")) && meas.getMagnetometer() != null) {
				instance.setValue(i, meas.getMagnetometer().getxAxis());
			} else if ((attributes.get(i).name().equals("measy")|| attributes.get(i).name().equalsIgnoreCase("magnetometery"))  && meas.getMagnetometer() != null) {
				instance.setValue(i, meas.getMagnetometer().getyAxis());
			} else if ((attributes.get(i).name().equals("measz") || attributes.get(i).name().equalsIgnoreCase("magnetometerz")) && meas.getMagnetometer() != null) {
				instance.setValue(i, meas.getMagnetometer().getzAxis());
			} else if (attributes.get(i).name().contains(":")) {
				instance.setValue(i, measurementSeeBluetooth(meas, attributes.get(i).name()));
			} else if (attributes.get(i).name().equals(attributes.get(attributes.size() - 1))) {
				instance.setValue(i, -1);
			} else {
				instance.setValue(i, measurementHowSeeWiFi(meas, attributes.get(i).name()));
			}

		}
		System.out.println("The instance is "+instance); 
		return instance;
	}
	
	public static List<Attribute> getHeader(Instances trainingSet) {
		Enumeration<Attribute> attrs = trainingSet.enumerateAttributes();
		List<Attribute> list = Collections.list(attrs);
		list.add(trainingSet.classAttribute());
		return list;	
	}
	

	/**
	 * Determine if the attribute Wifi access point is heard by the measurement.
	 * 
	 * @param meas
	 *            The incoming measurement.
	 * @param wifi
	 *            The name of Wifi access point.
	 * @return The dB value of the hearing, -100 if it is not heard.
	 */
	private static double measurementHowSeeWiFi(final Measurement meas, final String wifi) {
		double notSeenWifi = -100;
		if (meas.getWifiRSSI() != null) {
			if (meas.getWifiRSSI().getRssiValues().containsKey(wifi)) {
				return meas.getWifiRSSI().getRSSI(wifi);
			}
			if(meas.getWifiRSSI().getRssiValues().containsKey(wifi.trim())){
				return meas.getWifiRSSI().getRSSI(wifi.trim());
			}
		}
		return notSeenWifi;
	}

	/**
	 * A method to cut the Bluetooth hardware address out of the Bluetooth
	 * device name.
	 * 
	 * @param bluetooth
	 *            The name of the Bluetooth device.
	 * @return The hardware address of the Bluetooth device.
	 */
	private static String getBluetoothHardwareAddress(final String bluetooth) {
		String[] bluetoothAddress = bluetooth.split(":");
		StringBuilder builder = new StringBuilder();
		builder.append(bluetoothAddress[0].substring(bluetoothAddress[0].length() - 2, bluetoothAddress[0].length()));
		builder.append(":");
		for (int i = 1; i < bluetoothAddress.length; i++) {
			builder.append(bluetoothAddress[i]);
			builder.append(":");
		}
		builder.setLength(builder.length() - 1);
		String result = builder.toString();
		return result;
	}
	
	/**
	 * Determine if the attribute bluetooth device is seen by the measurement.
	 * 
	 * @param meas
	 *            The incoming measurement.
	 * @param bluetooth
	 *            The bluetooth device name.
	 * @return 1 if seen, 0 if not seen.
	 */
	private static int measurementSeeBluetooth(final Measurement meas, final String bluetooth) {
		String hardwareAddress = getBluetoothHardwareAddress(bluetooth);
		if (meas.getBluetoothTags() != null) {
			Set<String> measurementBluetoothTags = meas.getBluetoothTags().getTags();
			for (String bl : measurementBluetoothTags) {
				if (bl.toUpperCase().contains(hardwareAddress.toUpperCase())) {
					return 1;
				}
			}
		}
		return 0;
	}


}
