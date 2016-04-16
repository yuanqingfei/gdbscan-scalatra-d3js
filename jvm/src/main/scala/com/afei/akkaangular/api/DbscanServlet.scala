package com.afei.akkaangular.api

import com.afei.akkaangular.rest.DataService
import com.afei.model._
import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger, SwaggerSupport}
import org.scalatra.{MethodOverride, ScalatraServlet}
import upickle.default._

// use http://petstore.swagger.io/#/ to try out

class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object GdbscanApiInfo extends ApiInfo(
  "The GDBSCAN API",
  "Docs for the GDBSCAN API",
  "http://scalatra.org",
  "yuanqingfei@126.com",
  "MIT",
  "http://opensource.org/licenses/MIT")

class GdbscanSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", GdbscanApiInfo)

class DbscanServlet(implicit val swagger: Swagger) extends ScalatraServlet with BaseScalatraWebAppStack with MethodOverride with SwaggerSupport {

  // to keep increased points
  var dataCache: Seq[(Double, Double)] = DataService.getOrginalData
  var eps: Int = 5
  var minP: Int = 15

  protected val applicationDescription = "The GDBSCAN API. It exposes operations for getAll, getNoise and getClusters methods, also updateParam."

  val getClusters =
    (apiOperation[List[Location]]("getClusters")
      summary "Show clustered points"
      notes "Shows clustered points"
      )
  get("/getClusters", operation(getClusters)) {
    write(new DataService(dataCache, eps, minP).getClustersAsLocations)
  }

  val getAll =
    (apiOperation[List[Location]]("getAll")
      summary "Show all points"
      notes "Shows all the points"
      )
  get("/getAll", operation(getAll)) {
    write(new DataService(dataCache, eps, minP).getAll())
  }

  val getNoise =
    (apiOperation[List[Location]]("getNoise")
      summary "Show noise points"
      notes "Shows noise points"
      )
  get("/getNoise", operation(getNoise)) {
    write(new DataService(dataCache, eps, minP).getNoise())
  }


  post("/addLocation") {
    val newLocString = request.body
    val newLoc = read[Location](newLocString)
    dataCache = dataCache :+(newLoc.x, newLoc.y)
    println("all data number: " + dataCache.size)
    """{"message": "create new point successfully"}"""
  }

  val updatePara =
    (apiOperation("updatePara")
      summary "update eps and minmum points parameters"
      notes "update eps and minmum points parameters"
      parameter bodyParam[Param]
      )
  post("/updatePara", operation(updatePara)) {
    val newParamString = request.body
    val newParam = read[Param](newParamString)
    eps = newParam.eps
    minP = newParam.minP

    println("new parameter eps: " + eps + " minP: " + minP)

    response.addHeader("ACK", "parameter update successfully")

    """{"message": "parameter update successfully"}"""

  }


}