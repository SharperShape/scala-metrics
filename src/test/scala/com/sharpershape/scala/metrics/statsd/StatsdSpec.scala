package com.sharpershape.scala.metrics.statsd

import org.scalatest.{MustMatchers, WordSpec}

import scala.concurrent.duration._


class StatsdSpec extends WordSpec with MustMatchers {
  "Count" should {
    "have valid payload" in {
      Statsd.Count("key.1", 1).payload  mustEqual "key.1:1|c"
      Statsd.Count("key.2", 2).payload  mustEqual "key.2:2|c"
      Statsd.Count("key.3", -1).payload mustEqual "key.3:-1|c"
    }
  }

  "Duration" should {
    "have valid payload" in {
      Statsd.Duration("key.1", 5.seconds).payload mustEqual "key.1:5000|ms"
      Statsd.Duration("key.2", 10.millis).payload mustEqual "key.2:10|ms"
    }
  }
}
