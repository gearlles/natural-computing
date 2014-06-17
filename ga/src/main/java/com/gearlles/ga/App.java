package com.gearlles.ga;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gearlles.ga.core.Chromosome;
import com.gearlles.ga.core.Population;
import com.gearlles.ga.core.crossover.OnePoint;
import com.gearlles.ga.core.crossover.TwoPoint;
import com.gearlles.ga.core.fitness.Rastrigin;
import com.gearlles.ga.core.fitness.Sphere;
import com.gearlles.ga.core.mutation.BitMutation;
import com.gearlles.ga.core.mutation.UniformMutation;
import com.gearlles.ga.core.selection.Tournament;

public class App
{
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args)
    {
	// The maximum number of generations for the simulation.
	final int maxGenerations = 1000;

	// The probability of crossover for any member of the population,
	// where 0.0 <= crossoverRatio <= 1.0
	final float crossoverRatio = 0.8f;

	// The portion of the population that will be retained without change
	// between evolutions, where 0.0 <= elitismRatio < 1.0
	// 0 means no elitism (new population)
	final float elitismRatio = 0.25f;

	// The probability of mutation for any member of the population,
	// where 0.0 <= mutationRatio <= 1.0
	final float mutationRatio = 0.08f;
	
	final int dimension = 30;
	
	// The size of the simulation population
	final int populationSize = dimension;

	Chromosome.crossover = new OnePoint();
	Chromosome.fitnessFunction = new Rastrigin();
	Chromosome.mutation = new BitMutation();
	
	Population.selection = new Tournament(5);
	
	
	double[] generationResults = new double[maxGenerations];
	
	for (int k = 0; k < 30; k++)
	{
	    // Create the first population and initializate it
	    Population pop = new Population(dimension, populationSize,
		    crossoverRatio, elitismRatio, mutationRatio);

	    // Start evolving the population, stopping when the maximum number
	    // of
	    // generations is reached, or when we find a solution.
	    Chromosome best = pop.getPopulation()[0];
	    for (int i = 0; i < maxGenerations; i++)
	    {
		pop.evolve();
		best = pop.getPopulation()[0];
		generationResults[i] += best.getFitness();
	    }
	}
	
	for (int i = 0; i < generationResults.length; i++)
	{
	    log.debug(String.format("%f", generationResults[i]/30));
	}

    }
}
