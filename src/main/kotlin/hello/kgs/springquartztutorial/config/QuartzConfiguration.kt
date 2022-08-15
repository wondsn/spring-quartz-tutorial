package hello.kgs.springquartztutorial.config

import org.springframework.boot.autoconfigure.quartz.QuartzProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import java.util.*


@Configuration
class QuartzConfiguration(
    private val quartzProperties: QuartzProperties
) {

    @Bean
    fun schedulerFactoryBean(applicationContext: ApplicationContext): SchedulerFactoryBean {
        val schedulerFactoryBean = SchedulerFactoryBean()

        val jobFactory = AutowiringSpringBeanJobFactory()
        jobFactory.setApplicationContext(applicationContext)
        schedulerFactoryBean.setJobFactory(jobFactory)

        schedulerFactoryBean.setApplicationContext(applicationContext)

        val properties = Properties()
        properties.putAll(quartzProperties.properties)

        // schedulerFactoryBean.setGlobalTriggerListeners(triggersListener)
        // schedulerFactoryBean.setGlobalJobListeners(jobsListener)
        schedulerFactoryBean.setOverwriteExistingJobs(true)
        schedulerFactoryBean.setQuartzProperties(properties)
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true)
        return schedulerFactoryBean
    }
}