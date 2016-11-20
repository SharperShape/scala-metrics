package com.sharpershape.scala.metrics.statsd

import akka.actor.ActorRef
import com.sharpershape.scala.metrics._

import scala.concurrent.duration.Duration


class StatsdMetricsSender(prefix: String, actor: ActorRef) extends MetricsSender {
  override def increment(metric: MetricName, n: Int): Unit = {
    actor ! Statsd.Count(prefix / metric, n)
  }

  override def duration(metric: MetricName, duration: Duration): Unit = {
    actor ! Statsd.Duration(prefix / metric, duration)
  }
}
