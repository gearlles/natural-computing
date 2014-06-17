package com.gearlles.ga.core.mutation;

import java.util.Random;

import com.gearlles.ga.core.Chromosome;
import com.gearlles.ga.core.Population;

public class UniformMutation implements MutationInterface
{
    private static Random rand = new Random();

    public Chromosome mutate(Chromosome chromosome)
    {
	double lowerLimit = Chromosome.fitnessFunction.LOWER_LIMIT;
	double upperLimit = Chromosome.fitnessFunction.UPPER_LIMIT;

	double[] gene = chromosome.getGene();
	
	for (int i = 0; i < gene.length; i++)
	{
	    if (rand.nextDouble() > Population.mutationRatio)
	    {
		continue;
	    }
	    gene[i] = lowerLimit + (upperLimit - lowerLimit) * rand.nextDouble();
	}
	
	return new Chromosome(gene);
    }

}
