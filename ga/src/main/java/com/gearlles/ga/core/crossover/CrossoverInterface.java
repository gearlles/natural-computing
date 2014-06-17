package com.gearlles.ga.core.crossover;

import com.gearlles.ga.core.Chromosome;

public interface CrossoverInterface
{
    public Chromosome[] mate(Chromosome dad, Chromosome mom);
}
