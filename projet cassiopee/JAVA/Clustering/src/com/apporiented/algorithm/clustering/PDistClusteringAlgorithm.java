

package com.apporiented.algorithm.clustering;

import java.util.ArrayList;
import java.util.List;

public class PDistClusteringAlgorithm implements ClusteringAlgorithm {

    @Override
    public Cluster performClustering(double[][] distances,
                                     String[] clusterNames, LinkageStrategy linkageStrategy) {

		/* retour d'exceptions si arguments nuls */
        if (distances == null || distances.length == 0) {
            throw new IllegalArgumentException("matrice de distances invalide");
        }
        if (distances[0].length != clusterNames.length
                * (clusterNames.length - 1) / 2) {
            throw new IllegalArgumentException("nom de cluster non valable");
        }
        if (linkageStrategy == null) {
            throw new IllegalArgumentException("algo de liens indéfini");
        }

		/* Setup model */
        List<Cluster> clusters = createClusters(clusterNames);
        DistanceMap linkages = createLinkages(distances, clusters);

		/* Processus de ierarchisation */
        HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
        while (!builder.isTreeComplete()) {
            builder.agglomerate(linkageStrategy);
        }

        return builder.getRootCluster();
    }

    @Override
    public Cluster performWeightedClustering(double[][] distances, String[] clusterNames,
                                             double[] weights, LinkageStrategy linkageStrategy) {
        return performClustering(distances, clusterNames, linkageStrategy);
    }

    private DistanceMap createLinkages(double[][] distances,
                                       List<Cluster> clusters) {
        DistanceMap linkages = new DistanceMap();
        for (int col = 0; col < clusters.size(); col++) {
            Cluster cluster_col = clusters.get(col);
            for (int row = col + 1; row < clusters.size(); row++) {
                ClusterPair link = new ClusterPair();
                Double d = distances[0][accessFunction(row, col,
                        clusters.size())];
				link.setLinkageDistance(d);
                link.setlCluster(cluster_col);
                link.setrCluster(clusters.get(row));
                linkages.add(link);
            }
        }
        return linkages;
    }

    private List<Cluster> createClusters(String[] clusterNames) {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for (String clusterName : clusterNames) {
            Cluster cluster = new Cluster(clusterName);
            clusters.add(cluster);
        }
        return clusters;
    }

    
    // http://stackoverflow.com/questions/13079563/how-does-condensed-distance-matrix-work-pdist
    private static int accessFunction(int i, int j, int n) {
        return n * j - j * (j + 1) / 2 + i - 1 - j;
    }

}
