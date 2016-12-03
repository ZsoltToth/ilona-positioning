package uni.miskolc.ips.ilona.positioning.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork.EvaluatingThread;
import uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork.NeuralNetwork;

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
