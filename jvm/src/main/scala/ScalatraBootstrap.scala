import javax.servlet.ServletContext

import com.afei.akkaangular.api.{DbscanServlet, GdbscanSwagger, ResourcesApp}
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new GdbscanSwagger

  override def init(context: ServletContext) {
    context.mount(new DbscanServlet, "/api/gdbscan/*", "gdbscan")
    context.mount (new ResourcesApp, "/api-docs")
  }
}
