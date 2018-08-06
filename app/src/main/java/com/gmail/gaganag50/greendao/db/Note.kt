package com.gmail.gaganag50.greendao.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
data class Note(
        @Id var id: Long = 0,
        var text: String = "",
        var comment: String = "",
        var date: Date = Date()



)