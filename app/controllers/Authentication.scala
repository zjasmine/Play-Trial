package controllers

import javax.inject.Inject

import play.api.Logger
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

/**
  * Created by jasmine.zhao on 9/19/17.
  */
class Authentication @Inject() (cc: MessagesControllerComponents) extends MessagesAbstractController(cc){

  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (username, password) => check(username, password)
    })
  )

  def check(username: String, password: String) = {
    (username == "time" && password == "refapp")
  }

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def test = Action { implicit request =>
    Ok(views.html.home())
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.AppController.home).withSession(Security.username -> user._1)
    )
  }

  def logout = Action {
    Redirect(routes.Authentication.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }
}
