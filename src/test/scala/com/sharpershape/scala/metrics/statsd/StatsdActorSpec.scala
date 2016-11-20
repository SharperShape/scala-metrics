package com.sharpershape.scala.metrics.statsd

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Stash}
import akka.io.{IO, Udp}
import akka.testkit.TestKit
import org.scalatest.concurrent.{IntegrationPatience, Eventually}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration._


class StatsdActorSpec extends TestKit(ActorSystem("StatsdActorSpec"))
  with WordSpecLike
  with MustMatchers
  with Eventually
  with IntegrationPatience
  with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  private val address = new InetSocketAddress("localhost", 10000)

  "Receive" should {
    "send metrics to statsd server" in {
      val buffer    = ArrayBuffer.empty[String]
      val listener  = system.actorOf(TestListener.props(address, buffer))
      val statsd    = system.actorOf(StatsdActor.props(address))

      statsd ! Statsd.Count("key1", 1)
      statsd ! Statsd.Count("key2", 2)
      statsd ! Statsd.Duration("key3", 3.seconds)
      statsd ! Statsd.Duration("key4", 10.millis)

      eventually {
        buffer.toArray must contain only ("key1:1|c", "key2:2|c", "key3:3000|ms", "key4:10|ms")
      }
    }
  }
}


class TestListener(address: InetSocketAddress, buffer: ArrayBuffer[String]) extends Actor with Stash {
  import context.system
  IO(Udp) ! Udp.Bind(self, address)

  override def receive: Receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
      unstashAll()

    case _ =>
      stash()
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      buffer += data.utf8String

    case Udp.Unbind  =>
      socket ! Udp.Unbind

    case Udp.Unbound =>
      context.stop(self)
  }
}

object TestListener {
  def props(address: InetSocketAddress, buffer: ArrayBuffer[String]): Props = Props(new TestListener(address, buffer))
}
