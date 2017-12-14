package uni.miskolc.ips.ilona.positioning.model.svm;

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
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

public class SupportVectorMachine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8920303706052961753L;

	private LibSVM libsvm;
	
	private List<Attribute> header;
	
	private Instances trainingSet;
	
	private static final Logger LOG = LogManager.getLogger(SupportVectorMachine.class);
	
	public SupportVectorMachine(final String trainingfilepath) throws Exception {
		DataSource source = new DataSource(trainingfilepath);
		Instances trainingInstances = source.getDataSet();
		trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);
		this.libsvm = new LibSVM();
		libsvm.setSVMType(new SelectedTag(LibSVM.SVMTYPE_NU_SVC, LibSVM.TAGS_SVMTYPE));
		libsvm.setNu(0.03);
		libsvm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_POLYNOMIAL, LibSVM.TAGS_KERNELTYPE));
		libsvm.buildClassifier(trainingInstances);
		this.trainingSet= trainingInstances;
		this.header = MeasurementToInstanceConverter.getHeader(trainingInstances);

	}

	public LibSVM getLibsvm() {
		return libsvm;
	}

	public List<Attribute> getHeader() {
		return header;
	}
	
	public Instances getTrainingSet() {
		return trainingSet;
	}
	
	public static SupportVectorMachine deserialization(String serializedFilePath){
		SupportVectorMachine result;
		try {
			FileInputStream fileIn = new FileInputStream(serializedFilePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			result = (SupportVectorMachine) in.readObject();
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
	
	public static final void serializeSupportVectorMachine(SupportVectorMachine serializable, final String targetPath)
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

	
	
}
