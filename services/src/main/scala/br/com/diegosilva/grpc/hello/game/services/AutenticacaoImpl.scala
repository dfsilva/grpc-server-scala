package br.com.diegosilva.grpc.hello.game.services

import akka.actor.{ActorSystem, Props}
import br.com.diegosilva.grpc.hello.game.AutenticacaoGrpc.Autenticacao
import br.com.diegosilva.grpc.hello.game.actors.{AutenticacaoActor, Login}
import br.com.diegosilva.grpc.hello.game.{AutenticacaoRequest, AutenticacaoResponse}

import scala.concurrent.Future


class AutenticacaoImpl(system:ActorSystem) extends Autenticacao {

  override def autenticar(request: AutenticacaoRequest) = {

      val autenticacaoActor = system.actorOf(Props[AutenticacaoActor], "autenticacao-actor")

      autenticacaoActor ! Login(request.usuario)

    
      Future.successful(AutenticacaoResponse("Teste",0))
  }
}
