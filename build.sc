import mill._, scalalib._

object server extends ScalaModule {
  def scalaVersion = "2.13.1"

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.3.6" // web framework, http://www.lihaoyi.com/cask/
  )

}
