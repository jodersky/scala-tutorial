
import io.getquill.{SnakeCase, SqliteJdbcContext}
import scala.collection.mutable
import com.typesafe.config.ConfigFactory
import java.nio.file.Files
import cask.endpoints.WsChannelActor
import cask.util.Ws

object Main extends cask.MainRoutes {

  object ctx extends SqliteJdbcContext(SnakeCase, ConfigFactory.parseString(
    // This config uses the HOCON syntax https://github.com/lightbend/config/blob/master/HOCON.md
    // It is usually read from a file.
    s"""{driverClassName = "org.sqlite.JDBC", jdbcUrl = "jdbc:sqlite:messages.db"}"""
  ))
  import ctx._ // this import allows us to construct SQL queries in a nice DSL

  object Templates extends Templates(scalatags.Text)

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
    LiveMessages.broadcast(message)
  }

  object LiveMessages {
    val channels = mutable.WeakHashMap.empty[WsChannelActor, Unit]

    // This actor receives incoming messages from websockets. Since this
    // example project is only interested in broadcasting, we ignore
    // all messages received.
    val incoming = cask.WsActor {
      case _ =>
    }

    def broadcast(message: Message): Unit = channels.keySet.foreach(channel =>
      channel.send(Ws.Text(upickle.default.write(message)))
    )
  }

  @cask.websocket("/feed")
  def messageFeed(): cask.WebsocketResult = {
    cask.WsHandler{ channel =>
      LiveMessages.channels += channel -> ()
      LiveMessages.incoming
    }
  }

  @cask.staticResources("/assets")
  def statics() = "assets"

  initialize()
}
