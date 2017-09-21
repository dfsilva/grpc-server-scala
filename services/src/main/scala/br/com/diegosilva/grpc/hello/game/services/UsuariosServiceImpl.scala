package br.com.diegosilva.grpc.hello.game.services

import br.com.diegosilva.grpc.hello.game.{SairRequest, SairResponse, Usuario}
import br.com.diegosilva.grpc.hello.game.UsuariosGrpc.Usuarios
import io.grpc.stub.StreamObserver

import scala.concurrent.Future

class UsuariosServiceImpl extends Usuarios{

  override def listarUsuarios(request: Usuario, responseObserver: StreamObserver[Usuario]): Unit = {

  }

  override def sair(request: SairRequest): Future[SairResponse] = {
    Future.successful(SairResponse)
  }
}
