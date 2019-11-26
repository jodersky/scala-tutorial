
import org.scalajs.dom
import scalajs.js.annotation

@annotation.JSExportTopLevel("Main")
object Main {

  object Templates extends Templates(scalatags.JsDom)

  @annotation.JSExport
  def main() = {

    val container = dom.document.getElementById("conversation")
    val address = {
      val location = dom.window.location
      val protocol = if (location.protocol == "https:") {
        "wss"
      } else {
        "ws"
      }
      s"$protocol://${location.host}/feed"
    }
    val ws = new dom.WebSocket(address)

    ws.onmessage = event => {
      val msg = upickle.default.read[Message](
        event.data.asInstanceOf[String])

      container.appendChild(Templates.message(msg).render)
      dom.window.scrollTo(0, dom.document.body.scrollHeight)
    }
  }

}
