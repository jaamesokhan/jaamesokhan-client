package com.example.jaamebaade_client.model

enum class Status {
    /**
     * When a task is not started yet
     */
    NOT_STARTED,

    /**
     * When a task is in progress, i.e. an http request is being made
     */
    LOADING,

    /**
     * When a task is successful, i.e. an http request is successful
     */
    SUCCESS,

    /**
     * When a task is failed, i.e. an http request status code is not 2xx
     */
    FAILED,

    /**
     * When a task is finished, i.e. a media has been finished
     */
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