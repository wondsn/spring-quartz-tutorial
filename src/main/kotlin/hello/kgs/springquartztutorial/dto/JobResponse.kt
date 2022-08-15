package hello.kgs.springquartztutorial.dto

data class JobResponse(
    val jobName: String,
    val groupName: String,
    var jobStatus: String? = "UNDEFINED",
)
