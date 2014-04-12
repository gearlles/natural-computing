package com.gearlles.fss.core;

import java.util.ArrayList;
import java.util.List;

public class FSSSearch {

	public List<Fish> getSchool() {
		List<Fish> toReturn = new ArrayList<Fish>();
		Fish a = new Fish();
		a.setPosition(new double[]{5, 6});
		toReturn.add(a);
		
		Fish b = new Fish();
		b.setPosition(new double[]{50, 40});
		
		toReturn.add(b);
		return toReturn;
	}

}
