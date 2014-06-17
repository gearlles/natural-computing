package com.gearlles.ga.core.selection;

import java.util.Random;

import com.gearlles.ga.core.Chromosome;

public class Roulette implements SelectionInterface
{
    private Random rand = new Random();

    public Chromosome[] select(Chromosome[] population)
    {
	Chromosome[] parents = new Chromosome[2];

	double fitnessSum = 0;
	for (int i = 0; i < population.length; i++)
	{
	    fitnessSum += population[i].getFitness();
	}

	// execute the roulette two times to select two individuals
	for (int i = 0; i < 2; i++)
	{
	    double random = rand.nextDouble();
	    double accumulatedSum = 0;

	    for (int j = 0; j < population.length; j++)
	    {
		double proportion = population[j].getFitness() / fitnessSum;
		accumulatedSum += proportion;

		if (accumulatedSum > random)
		{
		    parents[i] = population[j];
		    break;
		}
	    }
	}

	return parents;
    }
}
