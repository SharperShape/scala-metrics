package com.sharpershape.scala.metrics

import scala.language.implicitConversions


package object statsd {
  implicit def metricNameToString(name: MetricName): String = name.parts.mkString(".")
}
