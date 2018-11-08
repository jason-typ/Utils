package com.data

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.routing.ConsistentHashingRouter.ConsistentHashable
import akka.routing.FromConfig

object routerObj {
  case class Msg(key: String) extends ConsistentHashable {
    override def consistentHashKey: String = key
  }

  class MyActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case Msg(message) =>
        Thread.sleep(5000)
        log.info("{} with message [{}] done", this, message)
      case message =>
        log.error("unknown message: {}", message)
    }
  }

  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem("mySystem")
    val hashingRouter = actorSystem.actorOf(FromConfig.props(Props(classOf[MyActor])), "hashingRouter")
    val smallestRouter = actorSystem.actorOf(FromConfig.props(Props(classOf[MyActor])), "smallestRouter")

    while (true) {
      // hashingRouter ! Msg("hello")
      smallestRouter ! Msg("hello")
      Thread.sleep(1000)
    }
  }
}
