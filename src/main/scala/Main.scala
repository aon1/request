import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl._
import akka.http.scaladsl.unmarshalling.Unmarshal
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Failure}
import utils.Utils
import scala.util.Random 

case class Post(userId: Int, id: Int, title: String, body: String)
case class Comment(postId: Int, id: Int, name: String, email: String, body: String)

object Main {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val post : Reads[Post] = ( 
                (JsPath \\ "id").read[Int] and 
                (JsPath \\ "userId").read[Int] and 
                (JsPath \\ "title").read[String] and 
                (JsPath \\ "body").read[String]
            )(Post)

    implicit val comment : Reads[Comment] = ( 
                (JsPath \\ "postId").read[Int] and 
                (JsPath \\ "id").read[Int] and 
                (JsPath \\ "name").read[String] and 
                (JsPath \\ "email").read[String] and 
                (JsPath \\ "body").read[String]
            )(Comment)

    val Url = "https://jsonplaceholder.typicode.com/posts"
    val GET = 1
    val POST = 2
 
    def main (args: Array[String]) {
        val id = scala.util.Random.nextInt(1000)
        val data: String = s"""{"id":$id,"userId":1,"title":"title","body":"body"}"""

        get("/posts")
        get("/posts/1")
        post("/posts", Some(data))
        get("posts/1/comments")
    }

    def request(url: String, method: Int, data: Option[String] = None): Future[HttpResponse] = {

        val _method = method match {
            case GET => HttpMethods.GET
            case POST => HttpMethods.POST
        }

        val entity = data match {
            case None => HttpEntity(ContentTypes.`application/json`, "")
            case Some(data) => HttpEntity(ContentTypes.`application/json`, data)
        }

        val req = HttpRequest(method = _method, uri = url, entity = entity)

        Http().singleRequest(req)
    }

    def get(endpoint: String) {
        val response = request(Url + endpoint, GET)
        handleResponse(response)
    }

    def post(endpoint: String, data: Option[String] = None) {
        val response = request(Url + endpoint, POST, data)
        handleResponse(response)
    }

    def handleResponse(response: Future[HttpResponse]) {

        response onComplete { 
            case Success(content) => { 

                if(content.status.isSuccess) {

                    val r = Random.alphanumeric
                    val name = s"request_${(r take 10).mkString}"
                    val filename = name + "-" + Utils.getDate() + ".txt" 

                    Unmarshal(content.entity).to[String].map { 
                        s => Utils.saveToFile(filename, s.toString)
                    }

                    println("#################### Request result saved to file " + filename)

                } else if(content.status.isFailure) {
                    println("#################### " + content.status.reason)
                }

            } case Failure(t) => { 
                println(t) 
            } 
        }
    }
}