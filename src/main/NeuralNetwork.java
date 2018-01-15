package main;

public class NeuralNetwork {
	
	static final int INPUTS = 784;
	static final int MID_LAYER_LENGTH = 16;
	static final int OUTPUTS = 10;
	static final int TRAIN_LENGTH = 100;
	
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
		
		return largest_index;
	}
	
	public void train(ResourceHandler handler, int numTrials) {
		
		int offset = 0;
		
		for (int j = 0; j < numTrials; j++) {
		
			Matrix outputGradientSum = Matrix.zero(OUTPUTS, MID_LAYER_LENGTH);
			Matrix layer2GradientSum = Matrix.zero(MID_LAYER_LENGTH, MID_LAYER_LENGTH);
			Matrix layer1GradientSum = Matrix.zero(MID_LAYER_LENGTH, INPUTS);
			Matrix outputBiasSum = Matrix.zero(OUTPUTS, 1);
			Matrix layer2BiasSum = Matrix.zero(MID_LAYER_LENGTH, 1);
			Matrix layer1BiasSum = Matrix.zero(MID_LAYER_LENGTH, 1);
			
			for (int i = 0; i < TRAIN_LENGTH; i++) {
				
				int[] data = handler.getResource(offset);
				int label = handler.getLabel(offset);
			
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
				Matrix outputGradient = Matrix.dot(outputLayerPartial, Matrix.transpose(layer2Act));
				
				Matrix layer2Partial = Matrix.hadamard(Matrix.dot(Matrix.transpose(outputLayerWeights), outputLayerPartial), 
						Matrix.sigprime(layer2Sum));
				Matrix layer2Gradient = Matrix.dot(layer2Partial, Matrix.transpose(layer1Act));
				
				Matrix layer1Partial = Matrix.hadamard(Matrix.dot(Matrix.transpose(layer2Weights), layer2Partial),
						Matrix.sigprime(layer1Sum));
				Matrix layer1Gradient = Matrix.dot(layer1Partial, Matrix.transpose(inputActivations));
				
				outputGradientSum = Matrix.add(outputGradientSum, outputGradient);
				layer2GradientSum = Matrix.add(layer2GradientSum, layer2Gradient);
				layer1GradientSum = Matrix.add(layer1GradientSum, layer1Gradient);
				
				outputBiasSum = Matrix.add(outputBiasSum, outputLayerPartial);
				layer2BiasSum = Matrix.add(layer2BiasSum, layer2Partial);
				layer1BiasSum = Matrix.add(layer1BiasSum, layer1Partial);
			
				offset++;
			}
			
			outputLayerWeights = Matrix.add(outputLayerWeights, Matrix.multiply(outputGradientSum,-1*learning_rate));
			layer2Weights = Matrix.add(layer2Weights, Matrix.multiply(layer2GradientSum, -1*learning_rate));
			layer1Weights = Matrix.add(layer1Weights, Matrix.multiply(layer1GradientSum, -1*learning_rate));
			
			outputLayerBiases = Matrix.add(outputLayerBiases, Matrix.multiply(outputBiasSum,-1*learning_rate));
			layer2Biases = Matrix.add(layer2Biases, Matrix.multiply(layer2BiasSum,-1*learning_rate));
			layer1Biases = Matrix.add(layer1Biases, Matrix.multiply(layer1BiasSum,-1*learning_rate));
		}
		
	}
	
	private Matrix errorVector(Matrix output, int label) {
		Matrix error = new Matrix(OUTPUTS, 1);
		
		for (int i = 0; i < OUTPUTS; i++) {
			double base = 0;
			if (i == label)
				base = 1;
			
			error.setValue(output.getValueAt(i, 0)-base, i, 0);
		}
		
		return error;
	}
	
	public void setLayerWeights(int layer, Matrix weightMatrix) {
		switch (layer) {
		case 0:
			layer1Weights = weightMatrix;
			break;
		case 1:
			layer2Weights = weightMatrix;
			break;
		case 2:
			outputLayerWeights = weightMatrix;
			break;
		}
	}
	
	public void setLayerBiases(int layer, Matrix biasMatrix) {
		switch (layer) {
		case 0:
			layer1Biases = biasMatrix;
			break;
		case 1:
			layer2Biases = biasMatrix;
			break;
		case 2:
			outputLayerBiases = biasMatrix;
			break;
		}
	}
	
	public Matrix getLayerWeights(int layer) {
		Matrix returnMatrix = null;
		
		switch (layer) {
		case 0:
			returnMatrix = layer1Weights;
			break;
		case 1:
			returnMatrix = layer2Weights;
			break;
		case 2:
			returnMatrix = outputLayerWeights;
			break;
		}
		
		return returnMatrix;
	}
	
	public Matrix getLayerBiases(int layer) {
		Matrix returnMatrix = null;
		
		switch (layer) {
		case 0:
			returnMatrix = layer1Biases;
			break;
		case 1:
			returnMatrix = layer2Biases;
			break;
		case 2:
			returnMatrix = outputLayerBiases;
			break;
		}
		
		return returnMatrix;
	}
}
