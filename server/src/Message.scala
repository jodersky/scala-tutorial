import java.time.Instant

case class Message(
  author: String,
  date: Long, // seconds since epoch
  content: String
)

object Message {

  // A ReadWriter is used to serialize a message to JSON.
  // The call to macroRW will generate a readwriter through compiler-time
  // introspection. By default, the mapping of a case class to JSON is pretty
  // straightforward. A message:
  //   Message("John Smith", 0, "Hello, World!")
  // will get rendered as
  //   {
  //     "author": "John Smith",
  //     "date": 0,
  //     "message": "Hello, World!"
  //   }
  //
  implicit def messageRW: upickle.default.ReadWriter[Message] =
    upickle.default.macroRW[Message]

}
