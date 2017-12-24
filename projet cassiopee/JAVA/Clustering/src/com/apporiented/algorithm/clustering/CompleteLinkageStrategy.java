

package com.apporiented.algorithm.clustering;

import java.util.Collection;

public class CompleteLinkageStrategy implements LinkageStrategy {

	@Override
	public Distance calculateDistance(Collection<Distance> distances) {
		double max = Double.NaN;

		for (Distance dist : distances) {
		    if (Double.isNaN(max) || dist.getDistance() > max)// l'algorithme complete likage retourne le maximum des distances
		        max = dist.getDistance();
		}
		return new Distance(max);
	}
}
