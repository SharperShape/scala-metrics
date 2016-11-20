package com.sharpershape.scala.metrics.statsd

object Statsd {
  trait Metric {
    def payload: String
  }

  case class Count(key: String, n: Int) extends Metric {
    override val payload: String = s"$key:$n|c"
  }

  case class Duration(key: String, duration: scala.concurrent.duration.Duration) extends Metric {
    override val payload: String = s"$key:${duration.toMillis}|ms"
  }
}
