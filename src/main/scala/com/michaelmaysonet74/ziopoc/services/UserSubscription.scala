package com.michaelmaysonet74.ziopoc.services

import com.michaelmaysonet74.ziopoc.models.User
import zio.{RIO, Has, Task, ZLayer}
import zio.console._

object UserSubscription {

  class Service(
    notifier: UserEmailer.Service,
    userRepo: UserRepo.Service,
    console: Console.Service
  ) {

    def subscribe(): Task[User] = {
      for {
        _ <- console.putStrLn("What's your Name?")
        name <- console.getStrLn

        _ <- console.putStrLn("What's your Email?")
        email <- console.getStrLn

        user = User(name, email)

        _ <- userRepo.insert(user)
        _ <- notifier.notify(user, s"Welcome, ${user.name}!")
      } yield user
    }

  }

  type Env = Has[UserSubscription.Service]

  val live: ZLayer[UserEmailer.Env with UserRepo.Env with Console, Nothing, Env] =
    ZLayer.fromServices[UserEmailer.Service, UserRepo.Service, Console.Service, UserSubscription.Service](
      (emailer, db, console) => new Service(emailer, db, console)
    )

  def subscribe(): RIO[Env, User] =
    RIO.accessM(_.get.subscribe())

}
