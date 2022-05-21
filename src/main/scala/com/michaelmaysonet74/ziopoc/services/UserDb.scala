package com.michaelmaysonet74.ziopoc.services

import zio.{RIO, Has, Task, ZLayer}
import com.michaelmaysonet74.ziopoc.models.User

object UserDb {

  trait Service {
    def insert(user: User): Task[Unit]
  }

  type Env = Has[UserDb.Service]

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
