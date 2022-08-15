package hello.kgs.springquartztutorial.service

import hello.kgs.springquartztutorial.dto.JobRequest
import hello.kgs.springquartztutorial.dto.JobResponse
import hello.kgs.springquartztutorial.util.JobUtils
import org.quartz.*
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val schedulerFactoryBean: SchedulerFactoryBean,
    private val applicationContext: ApplicationContext
) {

    fun executeJob(jobRequest: JobRequest, jobClass: Class<out Job>): JobResponse {
        var jobKey: JobKey? = null
        val jobDetail: JobDetail?
        val trigger: Trigger?

        try {
            jobDetail = JobUtils.createJob(jobRequest, jobClass, applicationContext)
            trigger = JobUtils.createTrigger(jobDetail)
            jobKey = jobDetail?.key
            schedulerFactoryBean.scheduler.scheduleJob(jobDetail, trigger)
            return JobResponse(jobRequest.jobName, jobRequest.groupName, "STARTED")
        } catch (e: SchedulerException) {
            jobKey?.let{ println("error occurred while scheduling with jobKey : $jobKey") }
            e.printStackTrace()
        }
        return JobResponse(jobRequest.jobName, jobRequest.groupName, "FAILED")
    }

    fun checkJob(jobKey: JobKey): JobResponse? {
        val jobResponse: JobResponse?
        try {
            jobResponse = JobResponse(
                groupName = jobKey.group,
                jobName = jobKey.name
            )
            jobResponse.jobStatus = if (isJobRunning(jobKey)) "RUNNING" else getJobState(jobKey)
        } catch (e: SchedulerException) {
            return null
        }
        return jobResponse
    }

    private fun isJobRunning(jobKey: JobKey): Boolean {
        try {
            val currentJobs = schedulerFactoryBean.scheduler.currentlyExecutingJobs ?: throw SchedulerException()
            for (jobCtx in currentJobs) {
                if (jobKey.name.equals(jobCtx.jobDetail.key.name)) {
                    return true
                }
            }
        } catch (_: SchedulerException) {
        }
        return false
    }

    private fun getJobState(jobKey: JobKey): String {
        try {
            val scheduler = schedulerFactoryBean.scheduler
            val jobDetail = scheduler.getJobDetail(jobKey)
            val triggers = scheduler.getTriggersOfJob(jobDetail.key)
            if (triggers == null || triggers.size == 0) {
                throw SchedulerException()
            }
            for (trigger in triggers) {
                val triggerState = scheduler.getTriggerState(trigger!!.key)
                return if (Trigger.TriggerState.NORMAL == triggerState) {
                    "SCHEDULED"
                } else triggerState.name.toUpperCase()
            }
        } catch (_: SchedulerException) {
        }
        return "UNDEFINED"
    }
}