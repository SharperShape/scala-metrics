package com.sharpershape.scala.metrics

import org.scalatest.{WordSpec, MustMatchers}


class MetricNameSpec extends WordSpec with MustMatchers {
  private val name1 = MetricName("test", "name")
  private val name2 = MetricName("other", "thing")

  "/" when {
    "string" should {
      "append string to name" in {
        name1 / "added" mustEqual MetricName("test", "name", "added")
        name1 / "extra" mustEqual MetricName("test", "name", "extra")
        name2 / "key"   mustEqual MetricName("other", "thing", "key")
      }
    }
    "metric name" should {
      "append name" in {
        name1 / name2 mustEqual MetricName("test", "name", "other", "thing")
        name2 / name1 mustEqual MetricName("other", "thing", "test", "name")
      }
    }
  }
}
