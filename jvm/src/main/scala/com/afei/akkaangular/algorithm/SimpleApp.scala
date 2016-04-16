package com.afei.akkaangular.algorithm

import com.afei.akkaangular.io.DataGenerator

import breeze.linalg.DenseMatrix
import nak.cluster.DBSCAN
import nak.cluster.GDBSCAN
import nak.cluster.Kmeans

object SimpleApp {
  def main(args: Array[String]) {
    //        val logFile = "/home/aaron/app/hadoop-2.6.4/README.txt" // Should be some file on your system
    //        val conf = new SparkConf().setAppName("Simple Application")
    //        val sc = new SparkContext(conf)
    //        val logData = sc.textFile(logFile).cache()
    //        val numAs = logData.filter(line => line.contains("a")).count()
    //        val numBs = logData.filter(line => line.contains("b")).count()
    //        println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
  }

  def createDataArea() = {

    val orginalDataA = DataGenerator.getClusterA()
    val orginalDataB = DataGenerator.getClusterB()
    val orginalDataC = DataGenerator.getClusterC()
    val orginalDataNoice = DataGenerator.getClusterNoise()
    val data = orginalDataA ++ orginalDataB ++ orginalDataC ++ orginalDataNoice
    data.toArray


    data foreach {
      println
    }

  }

  def dbscan(v: DenseMatrix[Double]) = {
    val gdbscan = new GDBSCAN(
      DBSCAN.getNeighbours(epsilon = 1, distance = Kmeans.euclideanDistance),
      DBSCAN.isCorePoint(minPoints = 2))
    val clusters = gdbscan cluster v
    clusters
  }
}

