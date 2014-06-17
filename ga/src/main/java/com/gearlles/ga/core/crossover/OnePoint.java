package com.gearlles.ga.core.crossover;

import java.util.Random;

import com.gearlles.ga.core.Chromosome;

public class OnePoint implements CrossoverInterface
{

    private static Random rand = new Random();
    
    public Chromosome[] mate(Chromosome dad, Chromosome mom)
    {
	// Convert the genes to arrays to make thing easier.
	double[] dadGene = dad.getGene();
	double[] momGene = mom.getGene();

	// Select a random pivot point for the mating
	int pivot = rand.nextInt(dadGene.length);

	// Provide a container for the child gene data
	double[] child1 = new double[dadGene.length];
	double[] child2 = new double[dadGene.length];

	// Copy the data from each gene to the first child.
	System.arraycopy(dadGene, 0, child1, 0, pivot);
	System.arraycopy(momGene, pivot, child1, pivot, (child1.length - pivot));

	// Repeat for the second child, but in reverse order.
	System.arraycopy(momGene, 0, child2, 0, pivot);
	System.arraycopy(dadGene, pivot, child2, pivot, (child2.length - pivot));

	return new Chromosome[] { new Chromosome(child1),
		new Chromosome(child2) };
    }

}
