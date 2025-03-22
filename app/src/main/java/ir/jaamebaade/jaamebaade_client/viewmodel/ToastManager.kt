package ir.jaamebaade.jaamebaade_client.viewmodel

import ir.jaamebaade.jaamebaade_client.view.components.toast.ToastType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object ToastManager {
    private val _toastMessage = MutableStateFlow(0)
    val toastMessageId: StateFlow<Int> = _toastMessage

    private val _showMessage = MutableStateFlow(false)
    val showMessage: StateFlow<Boolean> = _showMessage

    private val _type = MutableStateFlow(ToastType.SUCCESS)
    val type: StateFlow<ToastType> = _type

    fun showToast(messageId: Int, isSuccess: ToastType) {
        _toastMessage.update { messageId }
        _type.update { isSuccess }
        _showMessage.update { true }
    }

    fun hideToast() {
        _showMessage.update { false }
        _toastMessage.update { 0 }
    }
}
