import sbt._
import sbt.Keys._
import com.typesafe.sbt.pgp.PgpKeys._

object ProjectBuild extends Build {
  val akkaVersion = sys.props.getOrElse("akka.version", default = "2.4.9")

  val sonatypeCredentials = (sys.env.get("SONATYPE_USER"), sys.env.get("SONATYPE_PASSWORD")) match {
    case (Some(username), Some(password)) =>
      Seq(Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password))

    case _ =>
      Seq.empty
  }

  lazy val root = (project in file(".")).settings(
    pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toArray),
    credentials ++= sonatypeCredentials,
    pomExtra in Global := {
      <url>https://github.com/SharperShape/scala-metrics</url>
      <licenses>
        <license>
          <name>MIT</name>
          <url>https://opensource.org/licenses/MIT</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:git://github.com/SharperShape/scala-metrics</connection>
        <developerConnection>scm:git:ssh://github.com:SharperShape/scala-metrics.git</developerConnection>
        <url>http://github.com/SharperShape/scala-metrics/tree/master</url>
      </scm>
      <developers>
        <developer>
          <name>Markus Sammallahti</name>
          <email>markus.sammallahti@sharpershape.com</email>
          <organization>Sharper Shape</organization>
          <organizationUrl>http://sharpershape.com</organizationUrl>
        </developer>
      </developers>
    }
  )
}
