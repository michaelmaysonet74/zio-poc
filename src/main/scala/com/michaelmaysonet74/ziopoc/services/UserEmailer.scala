package com.michaelmaysonet74.ziopoc.services

import com.michaelmaysonet74.ziopoc.models.User
import zio.{RIO, Has, Task, ZLayer}

object UserEmailer {

  trait Service {
    def notify(user: User, message: String): Task[Unit]
  }

  type Env = Has[UserEmailer.Service]

  val live: ZLayer[Any, Nothing, Env] = ZLayer.succeed(
    new Service {
      override def notify(user: User, message: String): Task[Unit] =
        Task {
          println(s"Sending '$message' to ${user.email}")
        }
    }
  )

  def notify(user: User, message: String): RIO[Env, Unit] =
    RIO.accessM(_.get.notify(user, message))

}
