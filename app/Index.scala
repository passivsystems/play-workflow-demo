package example

import javax.inject.Inject
import play.api.mvc._

class Index @Inject()(implicit components: ControllerComponents) extends AbstractController(components) {

  def index() = Action {
    Ok(views.html.index())
  }
}
