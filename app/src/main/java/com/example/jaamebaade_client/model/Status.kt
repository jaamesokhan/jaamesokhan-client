package com.example.jaamebaade_client.model

enum class Status {
    NOT_STARTED,
    LOADING,
    SUCCESS,
    FAILED,
    FINISHED,

    /**
     * Something is in progress, like audio is being played
     */
    IN_PROGRESS,

    /**
     * In audio, it means Paused
     */
    STOPPED,
    ;
}