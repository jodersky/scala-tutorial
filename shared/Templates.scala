import scalatags.generic.Bundle

class Templates[Builder, Output <: FragT, FragT](val backend: Bundle[Builder, Output, FragT]) {
  import backend.all._

  def message(msg: Message): Tag = {
    div(cls := "col-xs-12 col-sm-6 col-md-3 col-lg-2")(
      div(cls := s"card text-white mb-3 bg-primary")(
        div(cls := "card-header")(
          msg.author
        ),
        div(`class` := "card-body")(
          div(`class` := "card-text")(
            msg.content
          )
        )
      )
    )
  }

  def conversation(messages: Seq[Message]): Tag =
    div(`class` := "container-fluid")(
      div(id := "conversation", `class` := "row")(
        for (msg <- messages.sortBy(_.date)) yield message(msg)
      )
    )
  
  def page(messages: Seq[Message]): Tag = html(
    head(
      link(
        rel := "stylesheet",
        `type` := "text/css",
        href := "/assets/lib/bootstrap-4.1.0/css/bootstrap-reboot.min.css"
      ),
      link(
        rel := "stylesheet",
        `type` := "text/css",
        href := "/assets/lib/bootstrap-4.1.0/css/bootstrap-grid.min.css"
      ),
      link(
        rel := "stylesheet",
        `type` := "text/css",
        href := "/assets/lib/bootstrap-4.1.0/css/bootstrap.min.css"
      ),
      link(
        rel := "stylesheet",
        `type` := "text/css",
        href := "/assets/main.css"
      ),
      meta(
        name := "viewport",
        content := "width=device-width, initial-scale=1, shrink-to-fit=no"
      ),
      script(`type` := "text/javascript", src := "/assets/app.js")
    ),
    body(
      conversation(messages),
      script(`type` := "text/javascript")(
        raw(
          """|document.addEventListener("DOMContentLoaded", function(event) {
             |  console.info("Starting ScalaJS application...")
             |  Main.main() // run ScalaJS application
             |})
             |""".stripMargin
        )
      )
    )
  )

}
