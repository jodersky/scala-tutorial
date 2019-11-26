
import org.scalajs.dom
import scalajs.js.annotation

@annotation.JSExportTopLevel("Main")
object Main {

  @annotation.JSExport
  def func() = {
    dom.console.log("hello from ScalaJS!")
  }

}
