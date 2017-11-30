package main;

public class NeuralNetwork {
	
	public static final int ID_INPUT = 0;
	public static final int ID_OUTPUT = 3;
	public static final int ID_MID_1 = 1;
	public static final int ID_MID_2 = 2;
	
	static final int INPUTS = 784;
	static final int MID_LAYER_LENGTH = 16;
	static final int OUTPUTS = 10;
	
	private Perceptron[] inputLayer;
	private Perceptron[] layer1;
	private Perceptron[] layer2;
	private Perceptron[] outputLayer;
	
	public NeuralNetwork() {
		inputLayer = new Perceptron[INPUTS];
		layer1 = new Perceptron[MID_LAYER_LENGTH];
		layer2 = new Perceptron[MID_LAYER_LENGTH];
		outputLayer = new Perceptron[OUTPUTS];
	}
	
	public void init() {
		for(int i = 0; i < INPUTS; i++) {
			inputLayer[i] = new Perceptron(i, ID_INPUT,0);
		}
		
		for (int i = 0; i < MID_LAYER_LENGTH; i++) {
			layer1[i] = new Perceptron(i, ID_MID_1,INPUTS);
			layer2[i] = new Perceptron(i, ID_MID_2,MID_LAYER_LENGTH);
		}
		
		for (int i = 0; i < OUTPUTS; i++) {
			outputLayer[i] = new Perceptron(i, ID_OUTPUT, MID_LAYER_LENGTH);
		}
	}
	
	public void analyze(float[] data) {
		for (int i = 0; i < data.length; i++){
			inputLayer[i].setValue(data[i]);
		}
		
		for (int i = 0; i < MID_LAYER_LENGTH; i++) {
			layer1[i].computeValue(inputLayer);
		}
		
		for (int i = 0; i < MID_LAYER_LENGTH; i++) {
			layer2[i].computeValue(layer1);
		}
		
		for (int i = 0; i < OUTPUTS; i++) {
			outputLayer[i].computeValue(layer2);
		}
	}
}
