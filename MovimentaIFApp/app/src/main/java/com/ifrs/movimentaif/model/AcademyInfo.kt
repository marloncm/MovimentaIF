package com.ifrs.movimentaif.model

import java.util.Date

data class AcademyInfo(
    var academyId: String = "",
    var startDate: Date = Date(),
    var endDate: Date = Date(),
    var openHour: String = "",
    var closeHour: String = "",
    var additionalInfo: String = ""
)
