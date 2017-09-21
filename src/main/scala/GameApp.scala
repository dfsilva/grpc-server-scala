package br.com.diegosilva.hello.grpc

import java.util.logging.Logger

import akka.actor.{ActorSystem, Props}
import br.com.diegosilva.grpc.hello.game.{AutenticacaoGrpc, UsuariosGrpc}
import br.com.diegosilva.grpc.hello.game.AutenticacaoGrpc.Autenticacao
import br.com.diegosilva.grpc.hello.game.actors.AutenticacaoActor
import br.com.diegosilva.grpc.hello.game.services.{AutenticacaoImpl, UsuariosServiceImpl}
import com.typesafe.config.ConfigFactory
import io.grpc.{Server, ServerBuilder}

import scala.concurrent.{ExecutionContext}

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

    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + 2552).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)
    system.actorOf(Props[AutenticacaoActor], name = "clusterListener")

    server = ServerBuilder.forPort(GameApp.port)
      .addService(AutenticacaoGrpc.bindService(new AutenticacaoImpl(system), executionContext))
      .build()

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
