package com.afei.akkaangular.algorithm

import nak.cluster.DBSCAN
import breeze.linalg.DenseMatrix
import nak.cluster.GDBSCAN
import nak.cluster.Kmeans

class ClusterAlgo(val v: DenseMatrix[Double], val eps: Int, val minP: Int) {
  def dbscan() = {
    val gdbscan = new GDBSCAN(
      DBSCAN.getNeighbours(epsilon = eps, distance = Kmeans.euclideanDistance),
      DBSCAN.isCorePoint(minPoints = minP))
    val clusters = gdbscan cluster v
    clusters
  }
}