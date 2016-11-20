package com.sharpershape.scala

import scala.language.implicitConversions


package object metrics {
  implicit def stringToMetricName(key: String): MetricName = MetricName(key)
}
