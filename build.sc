import mill._, scalalib._

object server extends ScalaModule {
  def scalaVersion = "2.13.1"

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.3.6", // web framework, http://www.lihaoyi.com/cask/
    ivy"com.lihaoyi::scalatags:0.7.0", // html rendering DSL http://www.lihaoyi.com/scalatags/
    ivy"io.getquill::quill-jdbc:3.4.10", // language integrated queries https://getquill.io/#docs
    ivy"org.xerial:sqlite-jdbc:3.28.0" // actual database driver, note the single ':'
  )

}
