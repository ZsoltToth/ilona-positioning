package uni.miskolc.ips.ilona.positioning.model.neuralnetwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.positioning.model.MeasurementToInstanceConverter;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

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
	 * The header attributes of the multilayer perceptron classifier.
	 */
	private List<Attribute> header;
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
		DataSource source = new DataSource(filepath);
		Instances instances = source.getDataSet();
		instances.setClassIndex(instances.numAttributes()-1);
		Evaluation eval = new Evaluation(instances);
		eval.evaluateModel(mlp, instances);
		double errorRate = eval.errorRate();
		double result = (1 - errorRate);
		return result;
	}

	private MultilayerPerceptron buildMultilayerPerceptron(final String trainingfilepath) throws Exception {
		DataSource source = new DataSource(trainingfilepath);
		Instances trainingInstances = source.getDataSet();
		trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);
		MultilayerPerceptron mlp = new MultilayerPerceptron();
		mlp.setLearningRate(learningRate);
		mlp.setMomentum(momentum);
		mlp.setTrainingTime(trainingTime);
		mlp.setHiddenLayers(hiddenLayers);
		mlp.buildClassifier(trainingInstances);
		this.header = MeasurementToInstanceConverter.getHeader(trainingInstances);
		return mlp;

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
	
	public final List<Attribute> getHeader(){
		return header;
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

	

}