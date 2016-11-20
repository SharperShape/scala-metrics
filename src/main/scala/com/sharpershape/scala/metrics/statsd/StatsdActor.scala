package com.sharpershape.scala.metrics.statsd

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props, Stash}
import akka.io.{IO, Udp}
import akka.util.ByteString


class StatsdActor (remote: InetSocketAddress) extends Actor with Stash {
  import context.system
  IO(Udp) ! Udp.SimpleSender

  override def receive: Receive = {
    case Udp.SimpleSenderReady =>
      context.become(ready(sender()))
      unstashAll()

    case _ =>
      stash()
  }

  def ready(socket: ActorRef): Receive = {
    case metric: Statsd.Metric =>
      socket ! Udp.Send(ByteString(metric.payload), remote)
  }
}

object StatsdActor {
  def props(remote: InetSocketAddress): Props = Props(new StatsdActor(remote))
}
