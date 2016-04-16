package com.simple.controller

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.XMLHttpRequest

import com.afei.model.Location
import com.afei.model.Param
import com.greencatsoft.angularjs.AbstractController
import com.greencatsoft.angularjs.core.Scope
import com.greencatsoft.angularjs.injectable

import upickle.default.SeqishR
import upickle.default.SeqishW
import upickle.default.read
import upickle.default.write


@js.native
trait D3Scope extends Scope {
  var ref: js.Any
  var content: js.Any
  var myapi: js.Dynamic

  var myFormEps: String
  var myFormMinP: String
}

case class ElementData(x: Double, y: Double, size: Double, shape: String)

case class Group(key: String, values: Seq[ElementData])

@JSExport
@injectable("d3Ctrl")
class D3Controller(scope: D3Scope) extends AbstractController[D3Scope](scope) {

  val optionsText =
    """
{
    "chart": {
        "type": "scatterChart", 
        "height": 900, 
        "width": 900, 
        "margin": {
            "top": 20, 
            "right": 20, 
            "bottom": 60, 
            "left": 55
        }, 
        "showValues": true, 
        "transitionDuration": 350, 
        "xAxis": {
            "axisLabel": "X Axis"
        }, 
        "yAxis": {
            "axisLabel": "Y Axis"
        },

        "title": {
            "enable": true,
            "text": "GDBSCAN DIGRAM"
        }       
    }
}
    """
  // zoom not work yet because of a bug in angular-nvd3
  // https://github.com/krispo/angular-nvd3/issues/296
  val optionsText2 =
    """
{
    "chart": {
        "type": "scatterChart",
        "height": 900,
        "width": 900,
        "scatter": {
          "onlyCircles": false
        },
        "showDistX": true,
        "showDistY": true,
        "duration": 350,
        "xAxis": {
          "axisLabel": "X Axis"
        },
        "yAxis": {
          "axisLabel": "Y Axis",
          "axisLabelDistance": -5
        },
        "zoom": {
          "enabled": true,
          "scaleExtent": [
            1,
            10
          ],
          "useFixedDomain": false,
          "useNiceScale": false,
          "horizontalOff": false,
          "verticalOff": false,
          "unzoomEventType": "dblclick.zoom"
        }
    },
    "title": {
        "enable": true,
        "text": "Title for Line Chart"
    },
    "subtitle": {
        "enable": true,
        "text": "Subtitle for simple line chart. Lorem ipsum dolor sit amet...",
        "css": {
            "text-align": "center",
            "margin": "10px 13px 0px 7px"
        }
    },
    "caption": {
        "enable": true,
        "html": "Figure 1. Lorem ipsum dolor sit amet...",
        "css": {
            "text-align": "justify",
            "margin": "10px 13px 0px 7px"
        }
    }
} 

    """


  val dataText =
    """
[
    {
        "key": "Group 0", 
        "values": [
            {
                "x": 0.10330622161870882, 
                "y": 0.20793365176444703, 
                "size": 0.21713723632390303, 
                "shape": "circle"
            }, 
            {
                "x": -1.0515291118697523, 
                "y": 0.5268680632861393, 
                "size": 0.15527655327486833, 
                "shape": "circle"
            }

        ]
    }, 
    {
        "key": "Group 1", 
        "values": [
            {
                "x": 0.879273868254847, 
                "y": -0.22946412397382013, 
                "size": 0.7037522513596903, 
                "shape": "circle"
            }, 
            {
                "x": -0.9436832130706864, 
                "y": 0.5691370697657997, 
                "size": 0.4912816430899889, 
                "shape": "circle"
            }, 
            {
                "x": -0.7109594820239431, 
                "y": -1.3585336319861274, 
                "size": 0.16759546555180815, 
                "shape": "circle"
            } 
        ]
    }, 
    {
        "key": "Group 2", 
        "values": [
            {
                "x": -0.639723023171846, 
                "y": 0.3938662438748171, 
                "size": 0.18809809076337558, 
                "shape": "circle"
            }, 
            {
                "x": -0.17864217164349058, 
                "y": -0.9361676411737182, 
                "size": 0.6606834389779213, 
                "shape": "circle"
            }
        ]
    }, 
    {
        "key": "Group 3", 
        "values": [
            {
                "x": 0.2863606664498774, 
                "y": -0.5258771670134756, 
                "size": 0.2464482337597793, 
                "shape": "circle"
            }, 
            {
                "x": 0.4070678013559303, 
                "y": -0.7892207116233403, 
                "size": 0.45762126195592434, 
                "shape": "circle"
            }, 
            {
                "x": 0.0464110421771623, 
                "y": -1.434419234761621, 
                "size": 0.3471898734529575, 
                "shape": "circle"
            }
        ]
    }
]    
    """
  //  scope.content = JSON.parse(dataText)
  scope.ref = JSON.parse(optionsText2)
  Ajax.get("api/gdbscan/getClusters")
    .onSuccess {
      case response => dealResult(response)
    }

  def dealResult(response: XMLHttpRequest) = {
    val clusters = read[Seq[Seq[Location]]](response.responseText)
    val comboRes = ((1 to clusters.size) zip clusters)
      .map { combo => Group("group:" + combo._1, combo._2.map(e => ElementData(e.x, e.y, 4, "circle"))) }

    Ajax.get("api/gdbscan/getNoise")
      .onSuccess {
        case response => {
          val noises = read[Seq[Location]](response.responseText)
          val allRes = comboRes :+ Group("group:" + (clusters.size + 1), noises.map { e => ElementData(e.x, e.y, 3, "circle") })

          val content = JSON.parse(write(allRes))
          scope.myapi.updateWithData(content)
        }
      }
  }

  @JSExport
  def submit() = {

    val pa = Param(scope.myFormEps.toInt, scope.myFormMinP.toInt)

    // NOTE: if you write “Accept” part in the map, will cause 406 (Not Acceptable)
    Ajax.post("api/gdbscan/updatePara", write(pa), 0, Map("Content-Type" -> "application/json"))
      .onSuccess {
        case response => {
          js.Dynamic.global.window.location.reload()
        }
      }

  }

}

