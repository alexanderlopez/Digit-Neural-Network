package main;

import java.util.Random;

public class Perceptron {
	
	static final float RANGE = 10f;
	
	private int id;
	private int layer;
	
	private float[] weights;
	private float bias;
	
	private float activation;
	
	public Perceptron(int _id, int _layer, int wlengths) {
		id = _id;
		layer = _layer;
		activation = 0;
		
		
	}
	
	private void init(int length) {
		weights = new float[length];
		
		for (int i = 0; i < length; i++) {
			Random r = new Random();
			weights[i] = (r.nextFloat()*RANGE) - (RANGE/2);
		}
	}

	public void computeValue(Perceptron[] inputs) {
		float sum = bias;
		for (int i = 0; i < inputs.length; i++) {
			sum += inputs[i].getActivation()*weights[i];
		}
		
		float _activation = (float) (1/(1+Math.pow(Math.E, -(sum))));
		activation = _activation;
	}
	
	public float getActivation() {
		return activation;
	}
	
	public void setValue(float _activation) {
		activation = _activation;
	}
}
