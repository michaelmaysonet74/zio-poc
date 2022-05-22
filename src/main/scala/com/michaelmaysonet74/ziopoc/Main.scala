package com.michaelmaysonet74.ziopoc

import com.michaelmaysonet74.ziopoc.models.User
import com.michaelmaysonet74.ziopoc.services.{UserEmailer, UserRepository, UserSubscription}
import zio.{ZIO, ZLayer, ExitCode}
import zio.console._
import zio.magic._

object Main extends zio.App {

  override def run(args: List[String]) =
    UserSubscription
      .subscribe()
      .inject(
        UserRepository.live,
        UserEmailer.live,
        Console.live,
        UserSubscription.live
      )
      .catchAll(t => ZIO.succeed(t.printStackTrace()).map(_ => ExitCode.failure))
      .map { user =>
        println(s"Registered user: $user")
        ExitCode.success
      }

}
