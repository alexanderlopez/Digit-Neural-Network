package main;

public class Matrix {

	private int rows;
	private int cols;
	private double[][] data;
	
	public Matrix(double[][] _data) {
		rows = _data.length;
		cols = _data[0].length;
		data = _data;
	}
	
	public Matrix(int _rows, int _cols) {
		data = new double[rows][cols];
		rows = _rows;
		cols = _cols;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public double getValueAt(int row, int col) {
		return data[row][col];
	}
	
	public void setValue(double val, int row, int col) {
		data[row][col] = val;
	}
	
	public double[][] getData(){
		return data;
	}
	
	//STATIC METHODS
	
	public static Matrix transpose(Matrix m) {
		Matrix transposed = new Matrix(m.getRows(), m.getCols());
		
		for (int i = 0; i < m.getRows(); i++)
			for (int j = 0; j < m.getCols(); j++) {
				transposed.setValue(m.getValueAt(i, j), j, i);
			}
		
		return transposed;
	}
	
	public static Matrix multiply(Matrix A, double scalar) {
		Matrix resultant = new Matrix(A.getRows(), A.getCols());
		for (int i = 0; i < A.getRows(); i++)
			for (int j = 0; j < A.getCols(); j++) {
				double val = A.getValueAt(i, j)*scalar;
				resultant.setValue(val, i, j);
			}
		
		return resultant;
	}
	
	public static Matrix add(Matrix A, Matrix B) {
		if (A.getRows() != B.getRows() || A.getCols() != B.getCols())
			return null;
		
		Matrix resultant = new Matrix(A.getRows(), A.getCols());
		
		for (int i = 0; i < A.getRows(); i++)
			for (int j = 0; j < A.getCols(); j++) {
				double sum = A.getValueAt(i, j) + B.getValueAt(i, j);
				resultant.setValue(sum, i, j);
			}
		
		return resultant;
	}
	
	public static Matrix dot(Matrix A, Matrix B) {
		if (A.getCols() != B.getRows())
			return null;
		
		Matrix resultant = new Matrix(A.getRows(), B.getCols());
		
		for (int i = 0; i < A.getRows(); i++) {
			for (int j = 0; j < B.getCols(); j++) {
				double sum = 0;
				
				for (int k = 0; k < A.getCols(); k++) {
					sum += A.getValueAt(i, k) * B.getValueAt(k, j);
				}
				
				resultant.setValue(sum, i, j);
			}
		}
		
		return resultant;
	}
}
