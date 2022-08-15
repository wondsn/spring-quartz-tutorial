package hello.kgs.springquartztutorial.util

import hello.kgs.springquartztutorial.dto.JobRequest
import org.quartz.*
import org.springframework.context.ApplicationContext

object JobUtils {
    fun createJob(jobRequest: JobRequest, jobClass: Class<out Job>, context: ApplicationContext): JobDetail? {
        return JobBuilder.newJob(jobClass)
            .withIdentity(jobRequest.jobName, jobRequest.groupName)
            .setJobData(jobRequest.jobDataMap)
            .storeDurably(false)
            .build()
    }

    fun createTrigger(jobDetail: JobDetail?): Trigger? {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .startNow()
            .withIdentity(jobDetail?.key?.name, jobDetail?.key?.group)
            .build()
    }
}
