package hello.kgs.springquartztutorial.controller

import hello.kgs.springquartztutorial.dto.JobRequest
import hello.kgs.springquartztutorial.dto.JobResponse
import hello.kgs.springquartztutorial.job.SimpleJob
import hello.kgs.springquartztutorial.service.SchedulerService
import org.quartz.JobDataMap
import org.quartz.JobKey
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/scheduler")
class SchedulerController(
    private val schedulerService: SchedulerService
) {

    @PostMapping("/job")
    fun executeJob(): ResponseEntity<JobResponse> {
        val jobName = UUID.randomUUID().toString()
        val jobRequest = JobRequest("test-batch", jobName, JobDataMap())
        val jobResponse = schedulerService.executeJob(jobRequest, SimpleJob::class.java)
        return ResponseEntity.ok(jobResponse)
    }

    @GetMapping("/job/{groupName}/{jobName}")
    fun checkJob(@PathVariable groupName: String, @PathVariable jobName: String): ResponseEntity<JobResponse> {
        val jobKey = JobKey.jobKey(jobName, groupName)
        val response = schedulerService.checkJob(jobKey)
        return ResponseEntity.ok(response)
    }
}