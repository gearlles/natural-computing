package com.gearlles.fss.core;

public class Fish {
	
	private double[] position;
	private double weight;
	private double deltaFitness;
	private double[] deltaPosition;
	
	public Fish() {
	}
	
	public double[] getPosition() {
		return position;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getDeltaFitness() {
		return deltaFitness;
	}

	public void setDeltaFitness(double deltaFitness) {
		this.deltaFitness = deltaFitness;
	}

	public double[] getDeltaPosition() {
		return deltaPosition;
	}

	public void setDeltaPosition(double[] deltaPosition) {
		this.deltaPosition = deltaPosition;
	}
}
