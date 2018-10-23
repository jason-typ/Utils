import akka.actor.{ActorRef, ActorSystem, Cancellable}
import org.joda.time.{DateTime, DateTimeZone, Duration, Interval}

import scala.concurrent.duration._

class Timer(system: ActorSystem, actor: ActorRef, schemes: Seq[Timer.TimerInterval]) {
  implicit val executionContext = system.dispatcher

  val cancelHandlers = schemes.map {
    case Timer.Every(delay, freq, message) => scheduleEvery(delay, freq, message)
    case Timer.Hourly(delay, message) => scheduleHourly(delay, message)
    case Timer.Daily(delay, message) => scheduleDaily(delay, message)
  }

  def scheduleEvery(delay: Duration, freq: Duration, message: AnyRef) = {
    system.scheduler.schedule(delay.getMillis.millis, freq.getMillis.millis, actor, message)
  }

  def scheduleHourly(delay: Duration,  message: AnyRef): Cancellable = {
    val now = DateTime.now()
    val currentHour = TimeUtil.floor(now)
    val nextTick = currentHour.plus(delay.getMillis)

    new Interval(now ,nextTick.plus(1.hour.toMillis))

    val initialDelay =
      if (nextTick.isAfter(now)) new Interval(now, nextTick)
      else new Interval(now, nextTick.plusHours(1))
    system.scheduler.schedule(initialDelay.toDurationMillis.millis, 1 hour, actor, message)
  }

  def scheduleDaily(delay: Duration, mess: Any): Cancellable = {
    val now  = DateTime.now(DateTimeZone.UTC)
    val today = now.withTimeAtStartOfDay()
    val nextTick = today.plus(delay.getMillis)

    val initialDelay =
      if (nextTick.isAfter(now)) new Interval(now, nextTick)
      else new Interval(now, nextTick.plusDays(1))
    system.scheduler.schedule(initialDelay.toDurationMillis.millis, 1 day, actor, mess)
  }

  def  test(delay: Duration) {
    delay.getMillis.millis
  }
}

object Timer {
  sealed abstract class TimerInterval

  case class Every(delay: Duration, freq: Duration, message: AnyRef) extends TimerInterval
  case class Hourly(delay: Duration, message: AnyRef) extends TimerInterval
  case class Daily(delay: Duration, message: AnyRef) extends TimerInterval

  def schedule(schemes: TimerInterval*)(implicit system: ActorSystem, actor: ActorRef) {
    new Timer(system, actor, schemes)
  }
}

object TimeUtil {
  def floor(dt: DateTime, unit: FiniteDuration = 1 hour) = {
    val epoch = dt.getMillis
    val interval = unit.toMillis

    new DateTime((epoch / interval) * interval).withZone(DateTimeZone.UTC)
  }
}
