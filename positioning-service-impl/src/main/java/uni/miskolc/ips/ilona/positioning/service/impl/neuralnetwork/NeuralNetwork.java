package uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * The class for the MultilayerPerceptron and its inicializing values. It contains methods for evaluate the MultilayerPerceptron.
 * @author tamas13
 *
 */
public class NeuralNetwork implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7283677675909544183L;
	/**
	 * The learningRate of the MultilayerPerceptron.
	 */
	private final double learningRate;
	/**
	 * The momentum of the MultilayerPerceptron.
	 */
	private final double momentum;
	/**
	 * The training time of the MultilayerPerceptron.
	 */
	private final int trainingTime;
	/**
	 * The hidden layers of the MultilayerPerceptron.
	 */
	private final String hiddenLayers;
	/**
	 * The MultilayerPerceptron of the NeuralNetwork.
	 */
	private MultilayerPerceptron mlp;
	/**
	 * The logger of the NeuralNetwork class.
	 */
	private static final Logger LOG = LogManager.getLogger(NeuralNetwork.class);

	/**
	 * The constructor of the NeuralNetwork.
	 * @param learningRate The learningRate of the MultilayerPerceptron.
	 * @param momentum The momentum of the MultilayerPerceptron.
	 * @param trainingTime The training time of the MultilayerPerceptron.
	 * @param hiddenLayers The hidden layers of the MultilayerPerceptron
	 * @param trainingfilepath The path of the trainingset for the MultilayerPerceptron
	 * @throws FileNotFoundException The trainingset file on the path is not found.
	 * @throws IOException
	 * @throws Exception
	 */
	public NeuralNetwork(final double learningRate, final double momentum, final int trainingTime,
			final String hiddenLayers, final String trainingfilepath)
			throws FileNotFoundException, IOException, Exception {
		super();
		this.learningRate = learningRate;
		this.momentum = momentum;
		this.trainingTime = trainingTime;
		this.hiddenLayers = hiddenLayers;
		this.mlp = buildMultilayerPerceptron(trainingfilepath);

	}
	
	
	public static NeuralNetwork deserialization(String serializedFilePath){
		NeuralNetwork result;
		try {
			FileInputStream fileIn = new FileInputStream(serializedFilePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			result = (NeuralNetwork) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			LOG.info(String.format("Error occured during deserialization: " + i.getMessage()));
			result = null;
		} catch (ClassNotFoundException c) {
			LOG.error("Serialised Neural Network not found on " + serializedFilePath + " \n");
			result = null;
		}
		return result;
	}
	

	public double getEvaluation(final String filepath) throws Exception {
		Instances instances = readInstances(filepath);
		Evaluation eval = new Evaluation(instances);
		eval.evaluateModel(mlp, instances);
		double errorRate = eval.errorRate();
		double result = (1 - errorRate);
		return result;
	}

	private MultilayerPerceptron buildMultilayerPerceptron(final String trainingfilepath) throws Exception {
		Instances trainingInstances = readInstances(trainingfilepath);
		trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);
		MultilayerPerceptron mlp = new MultilayerPerceptron();
		mlp.setLearningRate(learningRate);
		mlp.setMomentum(momentum);
		mlp.setTrainingTime(trainingTime);
		mlp.setHiddenLayers(hiddenLayers);
		mlp.buildClassifier(trainingInstances);
		return mlp;

	}

	private Instances readInstances(final String filepath) throws IOException, FileNotFoundException {
		FileReader instancesreader = new FileReader(filepath);
		Instances instances = new Instances(instancesreader);
		instances.setClassIndex(instances.numAttributes() - 1);
		return instances;
	}

	public final double getLearningRate() {
		return learningRate;
	}

	public final double getMomentum() {
		return momentum;
	}

	public final int getTrainingTime() {
		return trainingTime;
	}

	public final String getHiddenLayers() {
		return hiddenLayers;
	}

	public final MultilayerPerceptron getMultilayerPerceptron() {
		return mlp;
	}

	public static final void serializeNeuralNetwork(NeuralNetwork serializable, final String targetPath)
			throws FileNotFoundException, IOException, Exception {
		try {
			FileOutputStream fileOut = new FileOutputStream(targetPath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(serializable);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + targetPath + " \n");
		} catch (IOException i) {
			LOG.error("Problem occured while serialize neural network: " + i.getMessage());
		}

	}

	/**
	 * A method for deserialize the NeuralNetwork based on the filepath.
	 * 
	 * @param serializedPath
	 *            The path to the serialized NeuralNetwork
	 * @return The deserialied NeuralNetwork
	 */

	@Override
	public final boolean equals(final Object obj) {
		boolean result;
		NeuralNetwork other;
		try {
			other = (NeuralNetwork) obj;
			if (this.hiddenLayers.equals(other.getHiddenLayers()) && this.learningRate == other.getLearningRate()
					&& this.momentum == other.getMomentum() && this.trainingTime == other.getTrainingTime()) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			result = false;
		}

		return result;
	}

	@Override
	public final int hashCode() {
		return super.hashCode();
	}

	/**
	 * Convert the measurement to Instance for the NeuralNetwork.
	 * 
	 * @param meas
	 *            The incoming measurement
	 * @return The instance of the converted measurement.
	 */
	public final Instance convertMeasurementToInstance(final Measurement meas) {
		ArrayList<Attribute> header = getHeader(this.getMultilayerPerceptron());

		Instance instance = new DenseInstance(header.size());
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < header.size(); i++) {
			attributes.add(new Attribute(header.get(i).name()));
		}
		LOG.info("The attributes are " + attributes.toString());
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i).name().equals("measx") && meas.getMagnetometer() != null) {
				instance.setValue(i, meas.getMagnetometer().getxAxis());
			} else if (attributes.get(i).name().equals("measy") && meas.getMagnetometer() != null) {
				instance.setValue(i, meas.getMagnetometer().getyAxis());
			} else if (attributes.get(i).name().equals("measz") && meas.getMagnetometer() != null) {
				instance.setValue(i, meas.getMagnetometer().getzAxis());
			} else if (attributes.get(i).name().contains(":")) {
				instance.setValue(i, measurementSeeBluetooth(meas, attributes.get(i).name()));
			} else if (attributes.get(i).name().equals(attributes.get(attributes.size() - 1))) {
				instance.setValue(i, -1);
			} else {
				instance.setValue(i, measurementHowSeeWiFi(meas, attributes.get(i).name()));
			}

		}
		LOG.info("The created instance is " + instance);
		return instance;
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
	private int measurementSeeBluetooth(final Measurement meas, final String bluetooth) {
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

	/**
	 * Determine if the attribute Wifi access point is heard by the measurement.
	 * 
	 * @param meas
	 *            The incoming measurement.
	 * @param wifi
	 *            The name of Wifi access point.
	 * @return The dB value of the hearing, -100 if it is not heard.
	 */
	private double measurementHowSeeWiFi(final Measurement meas, final String wifi) {
		double notSeenWifi = -100;
		if (meas.getWifiRSSI() != null) {
			if (meas.getWifiRSSI().getRssiValues().containsKey(wifi)) {
				return meas.getWifiRSSI().getRSSI(wifi);
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
	private String getBluetoothHardwareAddress(final String bluetooth) {
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
	 * A method to get the traning header of a MultilayerPerceptron.
	 * 
	 * @param mlp
	 *            The MultilayerPerceptron of the NeuralNetwork.
	 * @return List of the training attributes.
	 */
	private ArrayList<Attribute> getHeader(final MultilayerPerceptron mlp) {
		try {
			Field field = MultilayerPerceptron.class.getDeclaredField("m_instances");
			field.setAccessible(true);
			Object value = field.get(mlp);
			field.setAccessible(false);
			Instances header = (Instances) value;

			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			for (int i = 0; i < header.numAttributes(); i++) {
				attributes.add(header.attribute(i));
			}
			return attributes;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}