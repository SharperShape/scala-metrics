package com.sharpershape.scala.metrics


case class MetricName(parts: Seq[String]) {
  def /(part: String): MetricName       = MetricName(parts :+ part)         // scalastyle:ignore
  def /(other: MetricName): MetricName  = MetricName(parts ++ other.parts)  // scalastyle:ignore
}

object MetricName {
  def apply(base: String, parts: String*): MetricName = {
    MetricName(base +: parts.toSeq)
  }
}
