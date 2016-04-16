package com.afei.akkaangular.io

import scala.util.Random

/**
  * assume that we will have 3 clusters , 1000 points.
  *
  * A cluster x:[0,80] y:[20,30]  150 points
  *
  * B cluster x:[45, 50] y:[0, 90] 150 points
  *
  * C cluster x:[40,60] y:[40, 60] 200 points
  *
  * Random points x:[0,100] y:[0, 100] 500 points
  *
  * scope: 5, points: 5
  */
object DataGenerator extends App {

  def getClusterA() = {
    gen((0, 80), (20, 30), 150)
  }

  def getClusterB() = {
    gen((45, 50), (0, 90), 150)
  }

  def getClusterC() = {
    gen((40, 60), (40, 60), 200)
  }

  def getClusterNoise() = {
    gen((0, 100), (0, 100), 500)
  }

  def gen(x: (Double, Double), y: (Double, Double), number: Int): Seq[(Double, Double)] = {
    for (i <- 1 to number; dx = x._2 - x._1; dy = y._2 - y._1)
      yield (x._1 + dx * Random.nextDouble(), y._1 + dy * Random.nextDouble())
  }

}