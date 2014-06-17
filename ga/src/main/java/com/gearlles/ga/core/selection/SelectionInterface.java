package com.gearlles.ga.core.selection;

import com.gearlles.ga.core.Chromosome;

public interface SelectionInterface
{
    public Chromosome[] select(Chromosome[] population);
}
