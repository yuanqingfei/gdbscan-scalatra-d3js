package com.simple

import com.greencatsoft.angularjs.Angular
import com.simple.controller.D3Controller

import scala.scalajs.js.JSApp

object GdbscanClient extends JSApp {
  def main(): Unit = {

    val module = Angular.module("d3App", Seq("nvd3"))

    module.controller[D3Controller]
  }
}