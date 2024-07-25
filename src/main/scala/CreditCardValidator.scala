import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import spray.json._

import scala.io.StdIn

object CreditCardValidator extends App with DefaultJsonProtocol {
  implicit val system = ActorSystem("creditCardValidator")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  case class ValidationResult(valid: Boolean)
  implicit val validationResultFormat = jsonFormat1(ValidationResult)

  def luhnCheck(cardNumber: String): Boolean = {
    val digits = cardNumber.map(_.asDigit)
    val sum = digits.reverse.zipWithIndex.map {
      case (digit, idx) =>
        if (idx % 2 == 1) {
          val doubled = digit * 2
          if (doubled > 9) doubled - 9 else doubled
        } else {
          digit
        }
    }.sum
    sum % 10 == 0
  }

  def isValidCardNumber(cardNumber: String): Boolean = {
    val sanitizedCardNumber = cardNumber.filter(_.isDigit)
    sanitizedCardNumber.length >= 13 && sanitizedCardNumber.length <= 19 && luhnCheck(sanitizedCardNumber)
  }

  val route =
    path("validateCard") {
      get {
        parameters("cardNumber") { cardNumber =>
          val valid = isValidCardNumber(cardNumber)
          complete(HttpEntity(ContentTypes.`application/json`, ValidationResult(valid).toJson.toString))
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
