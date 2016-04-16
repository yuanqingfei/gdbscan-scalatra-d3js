package com.afei.akkaangular.rest

import breeze.linalg.DenseMatrix
import com.afei.akkaangular.algorithm.ClusterAlgo
import com.afei.akkaangular.io.DataGenerator
import com.afei.model.Location

class DataService(val orginalData: Seq[(Double, Double)], val eps: Int = 5, val minP: Int = 15) {

  def getArrayData() = {
    val a1 = orginalData map (_._1)
    val a2 = orginalData map (_._2)
    val arrayData = a1 ++ a2
    arrayData
  }

  def getAll() = {
    val result = orginalData.map { case (x, y) => Location(x, y) }
    println("all points number: " + result.size)
    result
  }

  def getNoise() = {
    val result = getAll() diff getClustersAsLocations().flatten
    println("noise point number: " + result.size)
    result
  }

  def getClustersAsLocations() = {
    val custers = getClusters()
    val result = custers.map(_.points.map(a => toLocation(a.value.toArray)))
    println("custer number: " + custers.size + " all cluster points number: " + result.foldLeft(0) { (sum, b) => sum + b.size })
    result
  }

  def getClusters() = {
    val arrayData = getArrayData()
    new ClusterAlgo(new DenseMatrix(orginalData.size, 2, arrayData.toArray), eps, minP) dbscan
  }

  def getClustersAsArray() = {
    getClusters().map(_.points.map(_.value.toArray))
  }

  def toLocation(a: Array[Double]) = {
    Location(a(0), a(1))
  }

}

object DataService {

  def getOrginalData = {
    val orginalDataA = DataGenerator.getClusterA()
    val orginalDataB = DataGenerator.getClusterB()
    val orginalDataC = DataGenerator.getClusterC()
    val orginalDataNoise = DataGenerator.getClusterNoise()
    val orginalData = orginalDataA ++ orginalDataB ++ orginalDataC ++ orginalDataNoise
    orginalData
  }

}