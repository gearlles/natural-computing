package com.gearlles.ga.core;

import java.util.Arrays;
import java.util.Random;

import com.gearlles.ga.core.selection.SelectionInterface;

public class Population
{
    private double       crossoverRatio;
    private double       elitismRatio;
    private double       mutationRatio;
    
    private int chromosomeSize;
    private int populationSize;
    
    private Chromosome[] population;
    private Random       rand = new Random();
    
    public static SelectionInterface selection;

    public Population(int chromosomeSize, int populationSize, double crossoverRatio,
	    double elitismRatio, double mutationRatio)
    {

	this.crossoverRatio = crossoverRatio;
	this.elitismRatio = elitismRatio;
	this.mutationRatio = mutationRatio;
	
	this.chromosomeSize = chromosomeSize;
	this.populationSize = populationSize;

	// 1. Initialize population
	this.population = new Chromosome[populationSize];
	for (int i = 0; i < populationSize; i++)
	{
	    this.population[i] = new Chromosome(this.chromosomeSize);
	}

	Arrays.sort(this.population);
    }

    public void evolve()
    {
	Chromosome[] nextPopulation = new Chromosome[populationSize];

	/* Elitism */
	int idx = (int) Math.round(population.length * elitismRatio);
	System.arraycopy(population, 0, nextPopulation, 0, idx);

	for (int i = idx; i < nextPopulation.length; i++)
	{
	    // Check to see if we should perform a crossover.
	    if (rand.nextDouble() <= crossoverRatio)
	    {
		Chromosome[] parents = Population.selection.select(population);
		Chromosome[] children = Chromosome.crossover.mate(parents[0], parents[1]);

		// Check to see if the first child should be mutated.
		if (rand.nextDouble() <= mutationRatio)
		{
		    nextPopulation[i] = Chromosome.mutation.mutate(children[0]);
		}
		else
		{
		    nextPopulation[i] = children[0];
		}

		// Repeat for the second child, if there is enough space in the
		// population.
		if (i + 1 < nextPopulation.length)
		{
		    if (rand.nextDouble() <= mutationRatio)
		    {
			nextPopulation[++i] = Chromosome.mutation.mutate(children[1]);
		    }
		    else
		    {
			nextPopulation[++i] = children[1];
		    }
		}
	    }
	    else
	    {
		// No crossover, so copy
		// Determine if mutation should occur.
		if (rand.nextDouble() <= mutationRatio) {
		    nextPopulation[i] = Chromosome.mutation.mutate(population[i]);
		} else {
		    nextPopulation[i] = population[i];
		}
	    }
	}

	Arrays.sort(nextPopulation);

	population = nextPopulation;
    }
   

    public Chromosome[] getPopulation()
    {
        return population;
    }
}
