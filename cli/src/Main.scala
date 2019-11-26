
import java.time.Instant
import scala.collection.mutable
import scala.concurrent._
import scala.concurrent.duration._
import scala.util.control.NonFatal

object Main extends App {
  
  val usage = """Usage: cli [--author=<author>] content"""
  
  def error(message: String) = {
    System.err.println(message)
    System.err.println(usage)
    sys.exit(1)
  }

  // process arguments
  var author: Option[String] = None
  val content = new mutable.StringBuilder

  val it = args.iterator
  while(it.hasNext) {
    val arg = it.next()
    if (arg == "--author") {
      if (it.hasNext) {
        author = Some(it.next())
      } else {
        error("expected author")
      }
    } else if (arg.startsWith("--")) {
      error("invalid option")
    } else {
      content ++= arg
    }
  }

  // we could use the shared message model if we used a serialization library
  // that also supports scala native, such as https://github.com/jodersky/spray-json/
  //
  // val message = Message(author, Instant.now.getEpochSecond, content)

  val message = s"""{"message":{
    "author": "${author.getOrElse(sys.env("USER"))}",
    "date" : ${Instant.now().toEpochMilli / 1000},
    "content": "$content"
  }}"""
  val req = http.Request("POST", "http://localhost:8080", body = message.getBytes)
  
  try {
    Await.result(http.CurlBackend.send(req), 10.seconds) match {
      case resp if 200 <= resp.statusCode && resp.statusCode <= 300 =>
        sys.exit(0)
      case resp =>
        System.err.println(
          s"Bad response code while posting message ${resp.statusCode}."
        )
        sys.exit(1)
    }
  } catch {
    case NonFatal(e) =>
      System.err.println(e.getMessage)
      sys.exit(1)
  }
  
}
