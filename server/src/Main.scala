import scala.collection.mutable
object Main extends cask.MainRoutes {

  val messages = mutable.ListBuffer.empty[Message]

  messages += Message("John Smith", 0, "Hello, world!")

  @cask.get("/")
  def get() = upickle.default.write(messages, indent = 2) // indent = 2 to pretty-print

  @cask.postJson("/")
  def post(message: Message) = {
    messages += message
  }

  initialize()
}
