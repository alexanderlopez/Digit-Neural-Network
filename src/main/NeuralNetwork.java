package main;

public class NeuralNetwork {
	
	static final int INPUTS = 784;
	static final int MID_LAYER_LENGTH = 16;
	static final int OUTPUTS = 10;
	
	private double learning_rate;
	
	Matrix layer1Weights;
	Matrix layer1Biases;
	
	Matrix layer2Weights;
	Matrix layer2Biases;
	
	Matrix outputLayerWeights;
	Matrix outputLayerBiases;
	
	public NeuralNetwork(double _learning_rate) {
		layer1Weights = Matrix.randomize(MID_LAYER_LENGTH, INPUTS, 2);
		layer2Weights = Matrix.randomize(MID_LAYER_LENGTH, MID_LAYER_LENGTH, 2);
		outputLayerWeights = Matrix.randomize(OUTPUTS, MID_LAYER_LENGTH, 2);
		
		layer1Biases = Matrix.randomize(MID_LAYER_LENGTH, 1, 2);
		layer2Biases = Matrix.randomize(MID_LAYER_LENGTH, 1, 2);
		outputLayerBiases = Matrix.randomize(OUTPUTS, 1, 2);
		
		learning_rate = _learning_rate;
	}
	
	public void init() {
	}
	
	public int analyze(float[] data) {
		
		Matrix input = new Matrix(INPUTS, 1);
		
		for (int i = 0; i < INPUTS; i++)
			input.setValue(data[i], i, 0);
		
		Matrix layer1Act = Matrix.sigmoid(Matrix.add(Matrix.dot(layer1Weights, input), layer1Biases));
		Matrix layer2Act = Matrix.sigmoid(Matrix.add(Matrix.dot(layer2Weights, layer1Act), layer2Biases));
		Matrix output2Act = Matrix.sigmoid(Matrix.add(Matrix.dot(outputLayerWeights, layer2Act), outputLayerBiases));
		
		double largestVal = 0;
		int largest_index = 0;
		for (int i = 0; i < output2Act.getRows(); i++) {
			if (output2Act.getValueAt(i, 0) > largestVal) {
				largestVal = output2Act.getValueAt(i, 0);
				largest_index = i;
			}
		}
		
		System.out.println(Matrix.printString(output2Act));
		
		return largest_index;
	}
	
	public void train(ResourceHandler handler, int numTrials) {
		int[] data = handler.getResource(1);
		int label = handler.getLabel(1);
		
		Matrix inputActivations = new Matrix(data.length, 1);
		
		for (int k = 0; k < data.length; k++)
			inputActivations.setValue(((double)data[k]/255.0d), k, 0);
		
		Matrix layer1Sum = Matrix.add(Matrix.dot(layer1Weights, inputActivations), layer1Biases);
		Matrix layer1Act = Matrix.sigmoid(layer1Sum);
		
		Matrix layer2Sum = Matrix.add(Matrix.dot(layer2Weights, layer1Act), layer2Biases);
		Matrix layer2Act = Matrix.sigmoid(layer2Sum);
		
		Matrix outputLayerSum = Matrix.add(Matrix.dot(outputLayerWeights, layer2Act), outputLayerBiases);
		Matrix outputLayerAct = Matrix.sigmoid(outputLayerSum);
		
		Matrix error = errorVector(outputLayerAct, label);
		
		Matrix outputLayerPartial = Matrix.hadamard(error, Matrix.sigprime(outputLayerSum));
		Matrix inputGradient = Matrix.dot(outputLayerPartial, Matrix.transpose(layer2Act));
		
	}
	
	private Matrix errorVector(Matrix output, int label) {
		Matrix error = new Matrix(OUTPUTS, 1);
		
		for (int i = 0; i < OUTPUTS; i++) {
			double base = 0;
			if (i == label)
				base = 1;
			
			error.setValue(0.5*Math.pow(output.getValueAt(i, 0)-base, 2), i, 0);
		}
		
		return error;
	}
}
