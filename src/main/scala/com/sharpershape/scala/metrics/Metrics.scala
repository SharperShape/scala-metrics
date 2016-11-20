package com.sharpershape.scala.metrics

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}


trait Metrics {
  def metricsSender: MetricsSender
  def metricName: MetricName
  def currentTimeMillis: () => Long = System.currentTimeMillis

  private def diff(startMillis: Long, endMillis: Long) = (endMillis - startMillis).millis

  private def blockResult(name: MetricName, result: String, startMillis: Long) = {
    metricsSender.duration(metricName / name / "duration", diff(startMillis, currentTimeMillis()))
    metricsSender.increment(metricName / name / result, 1)
  }

  object metrics { // scalastyle:ignore
    def increment(name: MetricName, n: Int = 1): Unit = {
      metricsSender.increment(metricName / name, n)
    }

    def decrement(name: MetricName, n: Int = 1): Unit = {
      metricsSender.increment(metricName / name, n * -1)
    }

    def duration(name: MetricName, startMillis: Long, endMillis: Long): Unit = {
      duration(name, diff(startMillis, endMillis))
    }

    def duration(name: MetricName, duration: Duration): Unit = {
      metricsSender.duration(metricName / name, duration)
    }

    def timed[A](name: MetricName)(block: => A): A = {
      val start = currentTimeMillis()

      Try(block) match {
        case Success(result) =>
          blockResult(name, "success", start)
          result

        case Failure(exception) =>
          blockResult(name, "failure", start)
          throw exception
      }
    }

    def timing[A](name: MetricName)(block: => Future[A])(implicit context: ExecutionContext): Future[A] = {
      val start   = currentTimeMillis()
      val future  = block

      future.onComplete {
        case Success(result) =>
          blockResult(name, "success", start)

        case Failure(exception) =>
          blockResult(name, "failure", start)
      }

      future
    }
  }
}
