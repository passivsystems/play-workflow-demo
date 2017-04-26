package example

import play.api.mvc._
import play.api.data.{Form, Forms}
import workflow._
import workflow.implicits._
import scala.concurrent.Future

import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import scala.concurrent.ExecutionContext.Implicits.global

// ------------- flow ----------------------------------------------------------

object ExampleFlow extends Controller {

  val workflow: Workflow[Unit] =
    for {
      step1Result <- Workflow.step("step1", Step1())
      _           <- Workflow.step("step2", Step2(step1Result))
    } yield ()

  val conf = WorkflowConf[Unit](
    workflow    = workflow,
    router      = routes.ExampleFlow)

  def get(stepId: String) = Action.async { implicit request =>
    WorkflowExecutor.getWorkflow(conf, stepId)
  }

  def post(stepId: String) = Action.async { implicit request =>
    WorkflowExecutor.postWorkflow(conf, stepId)
  }
}

// ------------- step1 ---------------------------------------------------------

case class Step1Result(name: String)

object Step1 extends Controller {

  def apply(): Step[Step1Result] = {

    val form = Form(Forms.mapping(
        "name" -> Forms.nonEmptyText
      )(Step1Result.apply)(Step1Result.unapply))

    def get(ctx: WorkflowContext[Step1Result])(implicit request: Request[Any]): Future[Result] = Future {
      val filledForm = ctx.stepObject match {
        case Some(step1) => form.fill(step1)
        case None        => form
      }
      Ok(views.html.step1(ctx, filledForm))
    }

    def post(ctx: WorkflowContext[Step1Result])(implicit request: Request[Any]): Future[Either[Result, Step1Result]] = Future {
      val boundForm = form.bindFromRequest
      boundForm.fold(
        formWithErrors => Left(BadRequest(views.html.step1(ctx, formWithErrors))),
        step1Result    => Right(step1Result)
      )
    }

    Step[Step1Result](
      get  = ctx => request => get(ctx)(request).map(Some(_)),
      post = ctx => request => post(ctx)(request)
    )
  }
}

// ------------- step2 ---------------------------------------------------------

object Step2 extends Controller {

  def apply(step1Result: Step1Result): Step[Unit] = {

    def get(ctx: WorkflowContext[Unit])(implicit request: Request[Any]): Future[Result] =
      Future(Ok(views.html.step2(ctx, step1Result)))

    Step[Unit](
      get  = ctx => request => get(ctx)(request).map(Some(_)),
      post = ctx => request => Future(Right(()))
    )
  }
}
