package com.michaelmaysonet74.ziopoc.services

import zio.{RIO, Has, Task, ZLayer}
import com.michaelmaysonet74.ziopoc.models.User

object UserSubscription {

  class Service(notifier: UserEmailer.Service, userRepo: UserDb.Service) {

    def subscribe(user: User): Task[User] = {
      for {
        _ <- userRepo.insert(user)
        _ <- notifier.notify(user, s"Welcome, ${user.name}!")
      } yield user
    }

  }

  type Env = Has[UserSubscription.Service]

  val live: ZLayer[UserEmailer.Env with UserDb.Env, Nothing, Env] =
    ZLayer.fromServices[UserEmailer.Service, UserDb.Service, UserSubscription.Service]((emailer, db) =>
      new Service(emailer, db)
    )

  def subscribe(user: User): RIO[Env, User] =
    RIO.accessM(_.get.subscribe(user))

}
