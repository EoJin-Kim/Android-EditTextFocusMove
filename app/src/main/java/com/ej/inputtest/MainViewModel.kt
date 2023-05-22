package com.ej.inputtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paycoin.global.presentation.util.event.MutableEventFlow
import com.paycoin.global.presentation.util.event.asEventFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    init {
        setInputStatus(MainViewModel.InputStatus.Input4())
    }
    @JvmName("setInputStatus1")
    fun setInputStatus(inputStatus: MainViewModel.InputStatus) {
        this.inputStatus = inputStatus
        viewModelScope.launch {
            _inputEventFlow.emit(inputStatus)
        }
    }
    var inputStatus : MainViewModel.InputStatus = MainViewModel.InputStatus.Input4()
    private var _inputEventFlow = MutableEventFlow<MainViewModel.InputStatus>()
    val inputEvent = _inputEventFlow.asEventFlow()
    
    sealed class InputStatus(){
        class Input1() : MainViewModel.InputStatus()
        class Input2() : MainViewModel.InputStatus()
        class Input3() : MainViewModel.InputStatus()
        class Input4() : MainViewModel.InputStatus()
    }
}