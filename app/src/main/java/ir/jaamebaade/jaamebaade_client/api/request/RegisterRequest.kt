package ir.jaamebaade.jaamebaade_client.api.request

data class RegisterRequest(
    val username: String,
    val email: String = "",
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
)