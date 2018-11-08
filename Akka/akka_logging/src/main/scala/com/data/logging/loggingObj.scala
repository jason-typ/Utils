package com.data.logging

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.Logging

object loggingObj {
  class MyActor1 extends Actor {
    val log = Logging(context.system, this)

    override def receive: Receive = {
      case "hello" =>
        log.info("receive message: [hello]")
      case msg: String =>
        log.info("receive message: [{}]", msg)
      case x =>
        log.error("unknown msg: [{}]", x)
    }
  }

  class MyActor2 extends Actor with ActorLogging {
    override def preStart(): Unit = {
      log.info("preStart")
    }
    override def receive: Receive = {
      case "hello" =>
        log.info("receive message: [hello]")
      case msg: String =>
        log.info("receive message: [{}]", msg)
      case x =>
        log.error("unknown msg: [{}]", x)
    }
  }

  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem("system")

    val actor1 = actorSystem.actorOf(Props(classOf[MyActor1]), "actor1")
    actor1 ! "world"

    val actor2 = actorSystem.actorOf(Props(classOf[MyActor2]), "actor2")
    actor2 ! "world"
  }
}
