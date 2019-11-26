import mill._, scalalib._, scalajslib._

object server extends ScalaModule {
  def scalaVersion = "2.13.1"

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.3.6", // web framework, http://www.lihaoyi.com/cask/
    ivy"com.lihaoyi::scalatags:0.7.0", // html rendering DSL http://www.lihaoyi.com/scalatags/
    ivy"io.getquill::quill-jdbc:3.4.10", // language integrated queries https://getquill.io/#docs
    ivy"org.xerial:sqlite-jdbc:3.28.0" // actual database driver, note the single ':'
  )

  // This includes the resulting javascript file so that it can be served
  // as a classpath resource and is packaged in the final jar.
  def localClasspath = T {
    super.localClasspath() ++ List(webapp.asAsset())
  }

}

object webapp extends ScalaJSModule {
  def scalaVersion = "2.13.1"
  def scalaJSVersion = "0.6.31" // https://www.scala-js.org/

  def ivyDeps = Agg(
    ivy"org.scala-js::scalajs-dom::0.9.7" // http://scala-js.github.io/scala-js-dom/
  )

  // friendlier name than the default 'out.js'
  def asAsset = T {
    val out = T.ctx().dest / "assets" / "app.js"
    os.copy(fastOpt().path, out, createFolders = true)
    PathRef(T.ctx().dest)
  }

}
