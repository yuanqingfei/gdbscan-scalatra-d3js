package com.afei.akkaangular.api

import com.afei.akkaangular.rest.DataService
import com.afei.model._
import org.scalatra.{MethodOverride, ScalatraServlet}
import upickle.default._


//class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase
//
//object FlowersApiInfo extends ApiInfo(
//  "The Flowershop API",
//  "Docs for the Flowers API",
//  "http://scalatra.org",
//  "apiteam@scalatra.org",
//  "MIT",
//  "http://opensource.org/licenses/MIT")
//
//class FlowersSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", FlowersApiInfo)

class DbscanServlet extends ScalatraServlet with BaseScalatraWebAppStack with MethodOverride {

  // to keep increased points
  var dataCache: Seq[(Double, Double)] = DataService.getOrginalData
  var eps: Int = 5
  var minP: Int = 15

  get("/api/gdbscan/getClusters") {
    write(new DataService(dataCache, eps, minP).getClustersAsLocations)
  }
  get("/api/gdbscan/getAll") {
    write(new DataService(dataCache, eps, minP).getAll())
  }
  get("/api/gdbscan/getNoise") {
    write(new DataService(dataCache, eps, minP).getNoise())
  }

  post("/api/gdbscan/addLocation") {
    val newLocString = request.body
    val newLoc = read[Location](newLocString)
    dataCache = dataCache :+(newLoc.x, newLoc.y)
    println("all data number: " + dataCache.size)
    """{"message": "create new point successfully"}"""
  }

  post("/api/gdbscan/updatePara") {
    val newParamString = request.body
    val newParam = read[Param](newParamString)
    eps = newParam.eps
    minP = newParam.minP

    println("new parameter eps: " + eps + " minP: " + minP)

    response.addHeader("ACK", "parameter update successfully")

    """{"message": "parameter update successfully"}"""

  }


}