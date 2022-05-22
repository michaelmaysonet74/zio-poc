package com.michaelmaysonet74.ziopoc.services

import com.michaelmaysonet74.ziopoc.models.User
import zio.{RIO, Has, Task, ZLayer}

object UserRepository {

  trait Service {
    def insert(user: User): Task[Unit]
  }

  type Env = Has[UserRepository.Service]

  val live: ZLayer[Any, Nothing, Env] = ZLayer.succeed {
    new Service {
      override def insert(user: User): Task[Unit] = Task {
        println(s"[Database] insert into public.user values ('${user.name}')")
      }
    }
  }

  def insert(user: User): RIO[Env, Unit] =
    RIO.accessM(_.get.insert(user))

}
