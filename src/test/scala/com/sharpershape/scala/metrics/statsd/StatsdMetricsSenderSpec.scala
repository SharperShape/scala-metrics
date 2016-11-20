package com.sharpershape.scala.metrics.statsd

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.sharpershape.scala.metrics.stringToMetricName
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

import scala.concurrent.duration._


class StatsdMetricsSenderSpec extends TestKit(ActorSystem("StatsdMetricsSenderSpec"))
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  private val sender = new StatsdMetricsSender("test", testActor)

  "Increment" should {
    "send count message" in {
      sender.increment("key" / "1", 1)
      expectMsg(Statsd.Count("test.key.1", 1))

      sender.increment("key" / "2", -1)
      expectMsg(Statsd.Count("test.key.2", -1))
    }
  }

  "Duration" should {
    "send duration message" in {
      sender.duration("key" / "1" / "duration", 2.seconds)
      expectMsg(Statsd.Duration("test.key.1.duration", 2.seconds))

      sender.duration("key" / "2" / "duration", 10.millis)
      expectMsg(Statsd.Duration("test.key.2.duration", 10.millis))
    }
  }
}
