package com.sharpershape.scala.metrics

import scala.concurrent.duration.Duration


class NullMetricsSender extends MetricsSender {
  override def increment(metric: MetricName, n: Int): Unit = ()
  override def duration(metric: MetricName, duration: Duration): Unit = ()
}
