package main;

public class NeuralNetwork {
	
	static final int INPUTS = 784;
	static final int MID_LAYER_LENGTH = 16;
	static final int OUTPUTS = 10;
	
	Matrix layer1Weights;
	Matrix layer1Biases;
	
	Matrix layer2Weights;
	Matrix layer2Biases;
	
	Matrix outputLayerWeights;
	Matrix outputLayerBiases;
	
	public NeuralNetwork() {
		layer1Weights = Matrix.randomize(MID_LAYER_LENGTH, INPUTS, 10);
		layer2Weights = Matrix.randomize(MID_LAYER_LENGTH, MID_LAYER_LENGTH, 10);
		outputLayerWeights = Matrix.randomize(OUTPUTS, MID_LAYER_LENGTH, 10);
		
		layer1Biases = Matrix.randomize(MID_LAYER_LENGTH, 1, 10);
		layer2Biases = Matrix.randomize(MID_LAYER_LENGTH, 1, 10);
		outputLayerBiases = Matrix.randomize(OUTPUTS, 1, 10);
	}
	
	public void init() {
	}
	
	public int analyze(float[] data) {
		
		Matrix input = new Matrix(INPUTS, 1);
		
		for (int i = 0; i < INPUTS; i++)
			input.setValue(data[i], i, 0);
		
		Matrix layer1Act = Matrix.sigmoid(Matrix.dot(layer1Weights, input));
		Matrix layer2Act = Matrix.sigmoid(Matrix.dot(layer2Weights, layer1Act));
		Matrix output2Act = Matrix.sigmoid(Matrix.dot(outputLayerWeights, layer2Act));
		
		double largestVal = 0;
		int largest_index = 0;
		for (int i = 0; i < output2Act.getRows(); i++) {
			if (output2Act.getValueAt(i, 0) > largestVal) {
				largestVal = output2Act.getValueAt(i, 0);
				largest_index = i;
			}
		}
		
		return largest_index;
	}
	
	public void train(ResourceHandler handler, int numTrials) {
		
	}
}
