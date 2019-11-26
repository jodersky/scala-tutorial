package chat

object Main extends cask.MainRoutes {

  var data: String = ""

  @cask.get("/")
  def get() = {
    data
  }

  @cask.post("/")
  def post(request: cask.Request) = {
    data = new String(request.readAllBytes())
  }

  initialize()
}
