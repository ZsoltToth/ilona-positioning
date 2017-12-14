package uni.miskolc.ips.ilona.positioning.service.impl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import uni.miskolc.ips.ilona.positioning.model.neuralnetwork.NeuralNetwork;

/**
 * Unit test for simple App.
 */
public class CriticalWrite {

	public static void main(String[] args) throws Exception {

	}

	public synchronized static void critialWrite(double eval, double validation, NeuralNetwork mlp,
			String resultfilepath) throws Exception {
		String textToAppend = mlp.getLearningRate() + "," + mlp.getMomentum() + "," + mlp.getTrainingTime() + ",\""
				+ mlp.getHiddenLayers() + "\"," + eval + "," + validation + "\n";
		Files.write(Paths.get(resultfilepath), textToAppend.getBytes(), StandardOpenOption.APPEND);
	}

}
