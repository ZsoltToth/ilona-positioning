package uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import uni.miskolc.ips.ilona.measurement.model.measurement.BluetoothTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.Magnetometer;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementBuilder;
import uni.miskolc.ips.ilona.measurement.model.measurement.RFIDTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.WiFiRSSI;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
@Ignore
public class NeuralNetworkGenerationTest {

		@Ignore
		public void parameters() throws Exception {
			NeuralNetwork neuralnetwork = new NeuralNetwork(0.2, 0.1, 2000, "20",
					"/home/ilona/probaworkspace/neuralnetwork/trainingset.txt");
			File file = new File("/home/ilona/result.txt");
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write("learningRate, momentum,trainingTime,hiddenLayer, result\n");

			for (double learningrate = 0.2; learningrate < 1.0; learningrate += 0.05) {
				for (double momentum = 0.2; momentum < 1.0; momentum += 0.1) {
					for (int trainigntime = 2000; trainigntime < 4000; trainigntime += 500) {
						for (int hiddenlayer = 10; hiddenlayer < 30; hiddenlayer++) {
							String sHiddenLayer = Integer.toString(hiddenlayer);
							neuralnetwork = new NeuralNetwork(learningrate, momentum, trainigntime, sHiddenLayer,
									"/home/ilona/probaworkspace/neuralnetwork/trainingset.txt");
							writer.write(learningrate
									+ "," + momentum + "," + trainigntime + "," + hiddenlayer + "," + neuralnetwork
											.getEvaluation("/home/ilona/probaworkspace/neuralnetwork/trainingset.txt")
									+ "\n");
							writer.flush();

						}

					}

				}
			}
			writer.close();
		}

		@Ignore
		public void validation() throws Exception {
			NeuralNetwork neuralnetwork = new NeuralNetwork(0.2, 0.1, 2000, "20",
					"/home/ilona/probaworkspace/neuralnetwork/trainingset.txt");
			File file = new File("/home/ilona/resultMoreLayer.txt");
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write("learningRate, momentum,trainingTime,hiddenLayer, result with training set, validation\n");

			for (double learningrate = 0.2; learningrate < 1.0; learningrate += 0.05) {
				for (double momentum = 0.2; momentum < 1.0; momentum += 0.1) {
					for (int trainingtime = 2000; trainingtime < 4000; trainingtime += 500) {
						for (int hiddenlayer = 3; hiddenlayer < 30; hiddenlayer++) {
							String sHiddenLayer = Integer.toString(hiddenlayer);
							neuralnetwork = new NeuralNetwork(learningrate, momentum, trainingtime, sHiddenLayer,
									"/home/ilona/probaworkspace/neuralnetwork/trainingset.txt");
							double evaulated = neuralnetwork
									.getEvaluation("/home/ilona/probaworkspace/neuralnetwork/trainingset.txt");
							double validated = neuralnetwork
									.getEvaluation("/home/ilona/probaworkspace/neuralnetwork/testset.txt");
							writer.write(learningrate + "," + momentum + "," + trainingtime + "," + hiddenlayer + ","
									+ evaulated + "," + validated + "\n");
							writer.flush();
						}
					}
				}
			}
			writer.close();
		}

		@Ignore
		public void test() throws Exception{
			NeuralNetwork neuralnetwork = new NeuralNetwork(0.2, 0.1, 2000, "20,40",
					"/home/ilona/probaworkspace/neuralnetwork/magnetometerBetanito.arff");
			File file = new File("/home/ilona/resultMagnetoClassificationID.txt");
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write("learningRate, momentum,trainingTime,hiddenLayer, result with training set, validation\n");

			double evaulated = neuralnetwork.getEvaluation("/home/ilona/probaworkspace/neuralnetwork/magnetometerBetanito.arff");
			double validated =0.0;// neuralnetwork.getEvaluation("/home/ilona/probaworkspace/neuralnetwork/testset.txt");
			writer.write(0.2 + "," + 0.1 + "," + 2000 + "," + "20" + "," + evaulated + "," + validated + "\n");
			writer.flush();

			writer.close();
		}

		@Ignore
		public void testThreads() throws Exception {
			List<Thread> threads = new ArrayList<Thread>();
			String trainingfilepath = "/home/ilona/probaworkspace/neuralnetwork/magnetometerBetanito.arff";
			String testfilepath = "/home/ilona/probaworkspace/neuralnetwork/magnetometerBetanito.arff";
			String resultfilepath = "/home/ilona/probaworkspace/neuralnetwork/resultsMagnetoThread.txt";
			File file = new File("/home/ilona/probaworkspace/neuralnetwork/resultsMagnetoThread.txt");
			Writer writer = new BufferedWriter(new FileWriter(file));
			writer.write("learningRate, momentum,trainingTime, hiddenLayer, result with training set, validation\n");
			writer.flush();
			writer.close();
			for (double learningrate = 0.2; learningrate <= 1.0; learningrate += 0.1) {
				EvaluatingThread evaluatingThread = new EvaluatingThread(learningrate, trainingfilepath, testfilepath,
						resultfilepath, "Magneto");
				threads.add(evaluatingThread);
				evaluatingThread.start();
			}

			for (Thread thread : threads) {
				thread.join();
			}

		}
		
		
		
		@Ignore
		public void testThreadsEachSensor() throws Exception {
			List<Thread> threads = new ArrayList<Thread>();
		/*	String bTtrainingfilepath = "/home/ilona/probaworkspace/neuralnetwork/trainingset.txt";
			String bTtestfilepath = "/home/ilona/probaworkspace/neuralnetwork/testset.txt";
			String bTresultfilepath = "/home/ilona/probaworkspace/neuralnetwork/results.txt";
			File bTfile = new File("/home/ilona/probaworkspace/neuralnetwork/results.txt");
			Writer bTwriter = new BufferedWriter(new FileWriter(bTfile));*/
			
			String mtrainingfilepath ="/home/ilona/probaworkspace/neuralnetwork/magnetometerBetanito.arff";
			String mtestfilepath = "/home/ilona/probaworkspace/neuralnetwork/magnetometerBetanito.arff";
			String mresultfilepath = "/home/ilona/probaworkspace/neuralnetwork/magnetometerresults.txt";
			File mfile = new File("/home/ilona/probaworkspace/neuralnetwork/magnetometerresults.txt");
			Writer mwriter = new BufferedWriter(new FileWriter(mfile));
			
			
			String wtrainingfilepath = "/home/ilona/probaworkspace/neuralnetwork/wifiBetanito.arff";
			String wtestfilepath = "/home/ilona/probaworkspace/neuralnetwork/wifiBetanito.arff";
			String wresultfilepath = "/home/ilona/probaworkspace/neuralnetwork/wifiresults.txt";
			File wfile = new File(wresultfilepath);
			Writer wwriter = new BufferedWriter(new FileWriter(wfile));
			
			mwriter.write("learningRate, momentum,trainingTime, hiddenLayer, result with training set, validation\n");
			mwriter.flush();
			mwriter.close();
			wwriter.write("learningRate, momentum,trainingTime, hiddenLayer, result with training set, validation\n");
			wwriter.flush();
			wwriter.close();
			
			
		/*	bTwriter.write("learningRate, momentum,trainingTime, hiddenLayer, result with training set, validation\n");
			bTwriter.flush();
			bTwriter.close();*/
			for (double learningrate = 0.2; learningrate <= 1.0; learningrate += 0.1) {
				/*EvaluatingThread bTevaluatingThread = new EvaluatingThread(learningrate, bTtrainingfilepath, bTtestfilepath,
						bTresultfilepath);
				threads.add(bTevaluatingThread);
				bTevaluatingThread.start();*/
				
				EvaluatingThread mevaluatingThread = new EvaluatingThread(learningrate, mtrainingfilepath, mtestfilepath,
						mresultfilepath, "Magneto");
				threads.add(mevaluatingThread);
				mevaluatingThread.start();
				 EvaluatingThread wevaluatingThread = new EvaluatingThread(learningrate, wtrainingfilepath, wtestfilepath,
						wresultfilepath,"Wifi");
				threads.add(wevaluatingThread);
				wevaluatingThread.start();
			}

			for (Thread thread : threads) {
				thread.join();
				if(threads.isEmpty()){
					System.out.println("DONE");
				}
			}
			

		}

		@Ignore
		public void testSerialize() throws FileNotFoundException, IOException, Exception {
			NeuralNetwork neuralnetwork = new NeuralNetwork(0.7, 0.6, 140, "13",
					"/home/ilona/probaworkspace/neuralnetwork/trainingset.txt");

			try {
				FileOutputStream fileOut = new FileOutputStream("/tmp/neuralnetwork.ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(neuralnetwork);
				out.close();
				fileOut.close();
				System.out.printf("Serialized data is saved in /tmp/neuralnetwork.ser");
			} catch (IOException i) {
				i.printStackTrace();
			}

		}

		@Test
		public void testDeserialize() throws FileNotFoundException, IOException, Exception {
			NeuralNetwork e = null;
			try {
				FileInputStream fileIn = new FileInputStream("/home/ilona/Wneuralnetwork.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				e = (NeuralNetwork) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException i) {
				i.printStackTrace();
				return;
			} catch (ClassNotFoundException c) {
				System.out.println("NeuralNetwork class not found");
				c.printStackTrace();
				return;
			}
			
			System.out.println("Deserialized NeuralNetwork...");
			System.out.println("LR: " + e.getLearningRate());
			System.out.println("M: " + e.getMomentum());
			System.out.println("TT: " + e.getTrainingTime());
			System.out.println("HL: " + e.getHiddenLayers());

		}

		@Ignore
		public void convertMeasurementToInstance() throws FileNotFoundException, IOException, Exception {
			MeasurementBuilder measbuilder = new MeasurementBuilder();

			BluetoothTags bluetooth = new BluetoothTags(
					new HashSet<String>(Arrays.asList(new String[] { "001060AA36F8", "001060AA36F4", "001060AA36F2" })));
			Magnetometer magneto = new Magnetometer(12, 32, 23, 0.5);
			RFIDTags rfid = new RFIDTags(new HashSet<byte[]>());
			rfid.addTag(new byte[] { (byte) 12 });
			WiFiRSSI wifi = new WiFiRSSI();
			wifi.setRSSI("doa2", -0.4);
			wifi.setRSSI("doa200", -1.2);
			wifi.setRSSI("109.0", -3.2);
			measbuilder.setbluetoothTags(bluetooth);
			measbuilder.setMagnetometer(magneto);
			measbuilder.setRFIDTags(rfid);
			measbuilder.setWifiRSSI(wifi);
			measbuilder.setPosition(new Position(new Zone("Kukutyinfalva")));
			Measurement incoming = measbuilder.build();

			NeuralNetwork neuralnetwork = null;

			try {
				FileInputStream fileIn = new FileInputStream("/tmp/neuralnetwork.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				neuralnetwork = (NeuralNetwork) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException i) {
				i.printStackTrace();
				return;
			} catch (ClassNotFoundException c) {
				System.out.println("NeuralNetwork class not found");
				c.printStackTrace();
				return;
			}
			MultilayerPerceptron mlp = neuralnetwork.getMultilayerPerceptron();

			Instance instance = convertMeasurementToInstance(incoming, neuralnetwork);

			double cls = mlp.classifyInstance(instance);
			System.out.println(instance.classAttribute().value((int) cls));
			Position result = new Position(new Zone(instance.classAttribute().value((int) cls)));
			// Zóna listát kikérni alapból.
			System.out.println(result);

		}


		private Instance convertMeasurementToInstance(Measurement meas, NeuralNetwork neuralNetwork) {
			ArrayList<Attribute> header= getHeader(neuralNetwork.getMultilayerPerceptron());	
			
			Instance instance = new DenseInstance(header.size());
			List<Attribute> attributes = new ArrayList<Attribute>();
			for (int i = 0; i < header.size(); i++) {
				attributes.add(new Attribute(header.get(i).name()));
			}
			System.out.println("The attributes are "+attributes.toString());
			for (int i = 0; i < attributes.size(); i++) {
				if (attributes.get(i).name().equals("measx")) {
					instance.setValue(i, meas.getMagnetometer().getxAxis());
				} else if (attributes.get(i).name().equals("measy")) {
					instance.setValue(i, meas.getMagnetometer().getyAxis());
				} else if (attributes.get(i).name().equals("measz")) {
					instance.setValue(i, meas.getMagnetometer().getzAxis());
				} else if (attributes.get(i).name().contains(":")) {
					instance.setValue(i, measurementSeeBluetooth(meas, attributes.get(i).name()));
				}else if(attributes.get(i).name().equals(attributes.get(attributes.size()-1))){
						instance.setValue(i, -1);
				}else {
					instance.setValue(i, measurementHowSeeWiFi(meas, attributes.get(i).name()));
				}

			}
			System.out.println(("The created instance is "+instance));
			return instance;
		}

		private int measurementSeeBluetooth(Measurement meas, String bluetooth) {
			if (meas.getBluetoothTags().getTags().contains(bluetooth)) {
				return 1;
			}
			return 0;
		}

		private double measurementHowSeeWiFi(Measurement meas, String wifi) {
			System.out.println(meas.getWifiRSSI().getRssiValues());
			if (meas.getWifiRSSI().getRssiValues().containsKey(wifi)) {
				return meas.getWifiRSSI().getRSSI(wifi);
			}
			return -100;
		}


		@Ignore
		public void testInstances() {
			NeuralNetwork neuralnetwork = null;

			try {
				FileInputStream fileIn = new FileInputStream("/tmp/neuralnetwork.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				neuralnetwork = (NeuralNetwork) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException i) {
				i.printStackTrace();
			} catch (ClassNotFoundException c) {
				System.out.println("NeuralNetwork class not found");
				c.printStackTrace();
			}
			MultilayerPerceptron mlp = neuralnetwork.getMultilayerPerceptron();
			ArrayList<String> result = getClassValues(mlp);
			System.out.println(result);
		
		}
		
		
		private ArrayList<String> getClassValues(MultilayerPerceptron mlp){
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
				int classIndex = header.classIndex();
				Enumeration<Object> values = attributes.get(classIndex).enumerateValues();
				ArrayList<String> classValues = new ArrayList<String>();
				while(values.hasMoreElements()){
					classValues.add(values.nextElement().toString());
				}
				return classValues;
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		
		private ArrayList<Attribute> getHeader(MultilayerPerceptron mlp) {
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
