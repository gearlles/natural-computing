package com.gearlles.ga.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.management.RuntimeErrorException;

import com.gearlles.ga.core.selection.SelectionInterface;

public class Population
{
    public static double       crossoverRatio;
    public static double       elitismRatio;
    public static double       mutationRatio;
    
    private int chromosomeSize;
    private int populationSize;
    
    private Chromosome[] population;
    private Random       rand = new Random();
    
    public static SelectionInterface selection;

    public Population(int chromosomeSize, int populationSize)
    {
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
    
    public Population(int chromosomeSize, int populationSize, Chromosome[] population)
    {
	this.chromosomeSize = chromosomeSize;
	this.populationSize = populationSize;
	this.population = population;

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
	    if (rand.nextDouble() <= crossoverRatio)
	    {
		Chromosome[] parents = Population.selection.select(population);
		Chromosome[] children = Chromosome.crossover.mate(parents[0], parents[1]);

		nextPopulation[i] = Chromosome.mutation.mutate(children[0]);

		// Repeat for the second child, if there is enough space in the
		// population.
		if (i + 1 < nextPopulation.length)
		{
		    nextPopulation[++i] = Chromosome.mutation
			    .mutate(children[1]);
		}
	    } 
	    else
	    {
		nextPopulation[i] = Chromosome.mutation.mutate(population[i]);
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
