package br.com.diegosilva.hello.grpc

import java.util.logging.Logger
import io.grpc.{Server, ServerBuilder}
import scala.concurrent.{ExecutionContext, Future}

object GameApp extends App {
  private val logger = Logger.getLogger(classOf[GameApp].getName)
  private val port = 50051

  val server = new GameApp(ExecutionContext.global)
  server.start()
  server.blockUntilShutdown()

}

class GameApp(executionContext: ExecutionContext) { self =>
  private[this] var server: Server = null

  private def start(): Unit = {
    server = ServerBuilder.forPort(GameApp.port).addService(GreeterGrpc.bindService(new GreeterImpl, executionContext)).build.start

    GameApp.logger.info("Server started, listening on " + GameApp.port)

    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("*** server shut down")
    }
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }



}
