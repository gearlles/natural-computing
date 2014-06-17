package com.gearlles.ga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gearlles.ga.core.Chromosome;
import com.gearlles.ga.core.Population;
import com.gearlles.ga.core.crossover.OnePoint;
import com.gearlles.ga.core.crossover.TwoPoint;
import com.gearlles.ga.core.fitness.Rastrigin;
import com.gearlles.ga.core.mutation.BitMutation;
import com.gearlles.ga.core.mutation.UniformMutation;
import com.gearlles.ga.core.selection.Tournament;

public class App
{
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args)
    {
	final int maxGenerations = 10000;
	final int dimension = 30;
	final int populationSize = 30;
	
	Population.crossoverRatio = 0.8f;
	Population.elitismRatio = 0.25f;
	Population.mutationRatio = 0.05f;

	Chromosome.crossover = new OnePoint();
	Chromosome.fitnessFunction = new Rastrigin();
	Chromosome.mutation = new UniformMutation();
	
	Population.selection = new Tournament(5);
	
	double[] generationResults = new double[maxGenerations];
	double bestFitness = Double.MAX_VALUE;
	
	for (int k = 0; k < 30; k++)
	{
	    // Create the first population and initializate it
	    Population pop = new Population(dimension, populationSize);

	    Chromosome best = pop.getPopulation()[0];
	    bestFitness = best.getFitness();
	    
	    for (int i = 0; i < maxGenerations; i++)
	    {
		pop.evolve();
		best = pop.getPopulation()[0];
		
		if (bestFitness > best.getFitness()) {
		    bestFitness = best.getFitness();
		}
		
		generationResults[i] += bestFitness;
	    }
	}
	
	for (int i = 0; i < generationResults.length; i++)
	{
	    log.debug(String.format("%f", generationResults[i]/30));
	}

    }
}
