package com.sharpershape.scala.metrics

import org.scalamock.scalatest.MockFactory
import org.scalatest.{WordSpec, MustMatchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class MetricsSpec extends WordSpec with MustMatchers with ScalaFutures with MockFactory {
  private val mockMetricsSender     = mock[MetricsSender]
  private val mockCurrentTimeMillis = mockFunction[Long]

  private val metrics = new Metrics {
    override val metricName         = "test" / "thing"
    override val metricsSender      = mockMetricsSender
    override val currentTimeMillis  = mockCurrentTimeMillis
  }
  
  private def mockCurrentTime(millis: Long) = {
    mockCurrentTimeMillis.expects().returns(millis)
  }

  private def mockSendIncrement(metric: MetricName, n: Int) = {
    (mockMetricsSender.increment _).expects(metric, n)
  }
  
  private def mockSendDuration(metric: MetricName, duration: Duration) = {
    (mockMetricsSender.duration _).expects(metric, duration)
  }

  "Increment" should {
    "send increment" in {
      mockSendIncrement("test" / "thing" / "key-1", 1)
      mockSendIncrement("test" / "thing" / "key-2", 2)

      metrics.metrics.increment("key-1")
      metrics.metrics.increment("key-2", 2)
    }
  }

  "Decrement" should {
    "send negative increment" in {
      mockSendIncrement("test" / "thing" / "key-1", -1)
      mockSendIncrement("test" / "thing" / "key-2", -2)

      metrics.metrics.decrement("key-1")
      metrics.metrics.decrement("key-2", 2)
    }
  }

  "Duration" should {
    "send duration" in {
      mockSendDuration("test" / "thing" / "time-1", 10.millis)
      mockSendDuration("test" / "thing" / "time-2", 20.millis)

      metrics.metrics.duration("time-1", 900, 910)
      metrics.metrics.duration("time-2", 20.millis)
    }
  }

  "Timed" should {
    "send duration for given code bock and return block result" in {
      mockCurrentTime(1000)
      mockCurrentTime(1010)
      mockSendDuration("test" / "thing" / "block-1" / "duration", 10.millis)
      mockSendIncrement("test" / "thing" / "block-1" / "success", 1)

      val result = metrics.metrics.timed("block-1") {
        "x"
      }

      result mustEqual "x"
    }
    "handle exceptions" in {
      mockCurrentTime(2000)
      mockCurrentTime(2005)
      mockSendDuration("test" / "thing" / "block-2" / "duration", 5.millis)
      mockSendIncrement("test" / "thing" / "block-2" / "failure", 1)

      an[Exception] must be thrownBy metrics.metrics.timed("block-2") {
        throw new Exception("error")
      }
    }
  }
  
  "Timing" should {
    "send duration for given future" in {
      mockCurrentTime(3000)
      mockCurrentTime(4000)
      mockSendDuration("test" / "thing" / "future-1" / "duration", 1.second)
      mockSendIncrement("test" / "thing" / "future-1" / "success", 1)

      val result = metrics.metrics.timing("future-1") {
        Future { "success" }
      }

      whenReady(result) { _ mustEqual "success" }
    }
    "handle failed futures" in {
      mockCurrentTime(4321)
      mockCurrentTime(4328)
      mockSendDuration("test" / "thing" / "future-2" / "duration", 7.millis)
      mockSendIncrement("test" / "thing" / "future-2" / "failure", 1)

      val result = metrics.metrics.timing("future-2") {
        Future { throw new Exception("error") }
      }

      whenReady(result.failed) { _ mustBe an[Exception] }
    }
  }
}
