package com.tastybeans.timer.application.services

import com.tastybeans.timer.api.events.DayHasPassed
import com.tastybeans.timer.api.events.MonthHasPassed
import io.smallrye.mutiny.Multi
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MonthHasPassedGenerator {
    val logger = LoggerFactory.getLogger(MonthHasPassedGenerator::class.java)

    @Incoming("dayHasPassed")
    @Outgoing("monthHasPassed")
    fun handle(msg: Multi<DayHasPassed>): Multi<MonthHasPassed> {
        return msg.filter { evt -> evt.date.dayOfMonth == 1 }
            .map { evt ->
                val yesterday = evt.date.minusDays(1)
                val month = yesterday.monthValue
                val year = yesterday.year

                logger.info("Month has passed {}/{}", year, month)

                MonthHasPassed(year, month)
            }
    }
}