import scala.collection.mutable
object Main extends cask.MainRoutes {

  val messages = mutable.ListBuffer.empty[Message]

  messages += Message("John Smith", 0, "Hello, world!")

  @cask.get("/")
  def get() = Templates.page(messages.toList).render

  @cask.postJson("/")
  def post(message: Message) = {
    messages += message
  }

  @cask.staticResources("/assets")
  def statics() = "assets"

  initialize()
}
