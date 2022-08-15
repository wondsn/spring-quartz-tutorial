package hello.kgs.springquartztutorial.job

import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class SimpleJob: QuartzJobBean() {

    private val log = LoggerFactory.getLogger(SimpleJob::class.java)

    override fun executeInternal(context: JobExecutionContext) {
        val jobKey = context.jobDetail.key
        val currentThread = Thread.currentThread()
        log.info("============================================================================");
        log.info("SimpleJob started :: jobKey : {} - {}", jobKey, currentThread.name);
        Thread.sleep(30000)
        log.info("SimpleJob ended :: jobKey : {} - {}", jobKey, currentThread.name);
        log.info("============================================================================");
    }
}