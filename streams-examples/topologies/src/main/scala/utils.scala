import scala.concurrent.duration.FiniteDuration

package object utils {
  def duration2JavaDuration(d: FiniteDuration): java.time.Duration =
    java.time.Duration.ofNanos(d.toNanos)
}
