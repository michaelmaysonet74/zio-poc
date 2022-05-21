package com.michaelmaysonet74.ziopoc

import com.michaelmaysonet74.ziopoc.models.User
import com.michaelmaysonet74.ziopoc.services.{UserDb, UserEmailer, UserSubscription}
import zio.{ZIO, ZLayer, ExitCode}

object Main extends zio.App {

  override def run(args: List[String]) = {
    val michael = User("Michael", "michaelmaysonet74@gmail.com")

    val userSubscriptionLayer: ZLayer[Any, Throwable, UserSubscription.Env] =
      (UserDb.live ++ UserEmailer.live) >>> UserSubscription.live

    UserSubscription
      .subscribe(michael)
      .provideLayer(userSubscriptionLayer)
      .catchAll(t => ZIO.succeed(t.printStackTrace()).map(_ => ExitCode.failure))
      .map { user =>
        println(s"Registered user: $user")
        ExitCode.success
      }
  }

}
