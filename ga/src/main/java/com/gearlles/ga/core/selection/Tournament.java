package com.gearlles.ga.core.selection;

import java.util.Random;

import com.gearlles.ga.core.Chromosome;

public class Tournament implements SelectionInterface
{
    private int    tournamentSize;
    private Random rand = new Random();

    public Tournament(int tournamentSize)
    {
	this.tournamentSize = tournamentSize;
    }

    public Chromosome[] select(Chromosome[] population)
    {
	Chromosome[] parents = new Chromosome[2];

	// select two individuals
	for (int i = 0; i < 2; i++)
	{
	    parents[i] = population[rand.nextInt(population.length)];
	    for (int j = 0; j < tournamentSize; j++)
	    {
		int idx = rand.nextInt(population.length);
		if (population[idx].compareTo(parents[i]) < 0)
		{
		    parents[i] = population[idx];
		}
	    }
	}

	return parents;
    }
}
