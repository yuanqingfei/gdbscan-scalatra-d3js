import javax.servlet.ServletContext

import com.afei.akkaangular.api.DbscanServlet
import org.scalatra._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new DbscanServlet, "/*")
  }
}
