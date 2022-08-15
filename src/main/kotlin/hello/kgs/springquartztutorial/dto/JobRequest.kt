package hello.kgs.springquartztutorial.dto

import org.quartz.JobDataMap

data class JobRequest(
    val groupName: String,
    val jobName: String,
    val jobDataMap: JobDataMap?
)
