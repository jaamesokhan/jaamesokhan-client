package com.example.jaamebaade_client.model

import androidx.room.Embedded

class PoemWithPoet(
    @Embedded(prefix = "poem_")
    val poem: Poem,
    @Embedded(prefix = "poet_")
    val poet: Poet,
)