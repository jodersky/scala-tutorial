
import io.getquill.{SnakeCase, SqliteJdbcContext}
import scala.collection.mutable
import com.typesafe.config.ConfigFactory
import java.nio.file.Files

object Main extends cask.MainRoutes {

  object ctx extends SqliteJdbcContext(SnakeCase, ConfigFactory.parseString(
    // This config uses the HOCON syntax https://github.com/lightbend/config/blob/master/HOCON.md
    // It is usually read from a file.
    s"""{driverClassName = "org.sqlite.JDBC", jdbcUrl = "jdbc:sqlite:messages.db"}"""
  ))
  import ctx._ // this import allows us to construct SQL queries in a nice DSL

  // tables aren't typically create from within a program, but we do it here'
  // for demonstration purposes
  ctx.executeAction(
    """
    create table if not exists message(
      author string not null,
      date integer not null,
      content string not null
    );
    """
  )

  @cask.get("/")
  def get() = {
    val messages: Seq[Message] = ctx.run(ctx.query[Message])
    Templates.page(messages).render
  }

  @cask.postJson("/")
  def post(message: Message) = {
    run(query[Message].insert(lift(message)))
  }

  @cask.staticResources("/assets")
  def statics() = "assets"

  initialize()
}
