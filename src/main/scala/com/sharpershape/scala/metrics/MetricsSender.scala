package com.sharpershape.scala.metrics

import scala.concurrent.duration.Duration


trait MetricsSender {
  def increment(metric: MetricName, n: Int): Unit
  def duration(metric: MetricName, duration: Duration): Unit
}

