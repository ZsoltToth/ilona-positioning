package uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork;
import java.io.File;

import uni.miskolc.ips.ilona.positioning.service.impl.CriticalWrite;

public class EvaluatingThread extends Thread {

	private final double learningRate;
	private NeuralNetwork mlp;
	private final String trainingfilepath;
	private final String testfilepath;
	private final String resultfilepath;
	private final String type;

	public EvaluatingThread(double learningRate, String trainingfilepath, String testfilepath, String resultfilepath,
			String type) {
		super();
		this.learningRate = learningRate;
		this.trainingfilepath = trainingfilepath;
		this.testfilepath = testfilepath;
		this.resultfilepath = resultfilepath;
		this.type = type;
	}

	@Override
	public void run() {
		for (double momentum = 0.2; momentum < 1.0; momentum += 0.1) {
			for (int trainingtime = 20; trainingtime < 400; trainingtime += 20) {
				for (int hiddenlayer = 10; hiddenlayer < 30; hiddenlayer++) {
					String layers = hiddenlayer + "";
					try {
						this.mlp = new NeuralNetwork(learningRate, momentum, trainingtime, layers, trainingfilepath);
						double eval = mlp.getEvaluation(trainingfilepath);
						int exist=0;

						String pathToScan = "/home/ilona/probaworkspace/neuralnetwork/serialized/";
						String fileThatYouWantToFilter;
						File folderToScan = new File(pathToScan); // import ->
																	// import
																	// java.io.File;
						File[] listOfFiles = folderToScan.listFiles();
						if(listOfFiles.length == 0){
							NeuralNetwork.serializeNeuralNetwork(mlp, "/home/ilona/probaworkspace/neuralnetwork/serialized/"
									+ eval + "_" + type + "_" + learningRate + "_" + momentum + "_"
									+ trainingtime + "_" + layers+".ser");
							CriticalWrite.critialWrite(eval, eval, mlp, resultfilepath);
						}
						else{
						for (int i = 0; i < listOfFiles.length; i++) {
								fileThatYouWantToFilter = listOfFiles[i].getName();
								File file = new File(trainingfilepath);
								if (fileThatYouWantToFilter.startsWith(eval + "_" + type ) && file.lastModified() < listOfFiles[i].lastModified()) {
									exist=1;
									break;
								}			
						}
						if(exist == 0){
							NeuralNetwork.serializeNeuralNetwork(mlp, "/home/ilona/probaworkspace/neuralnetwork/serialized/"
									+ eval + "_" + type + "_" + learningRate + "_" + momentum + "_"
									+ trainingtime + "_" + layers+".ser");
							CriticalWrite.critialWrite(eval, eval, mlp, resultfilepath);
						}
						else{
							System.out.println("There was a similar performance with this sensor");
						}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

}
