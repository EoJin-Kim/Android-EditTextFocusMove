package com.ej.inputtest

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ej.inputtest.databinding.ActivityMainBinding
import com.paycoin.global.presentation.util.event.repeatOnStarted
import kotlinx.coroutines.flow.catch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()
        setFocusAction()
        setOnclickListeners()
    }

    private fun setOnclickListeners() {
        binding.input4.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                viewModel.inputStatus = MainViewModel.InputStatus.Input4()
            }
        }
        binding.input3.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                viewModel.inputStatus = MainViewModel.InputStatus.Input3()
            }
        }
        binding.input2.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                viewModel.inputStatus = MainViewModel.InputStatus.Input2()
            }
        }
        binding.compeleteBtn.setOnClickListener {
            when (viewModel.inputStatus) {
                is MainViewModel.InputStatus.Input4 -> {
                    viewModel.setInputStatus(MainViewModel.InputStatus.Input3())
                }
                is MainViewModel.InputStatus.Input3 -> {
                    viewModel.setInputStatus(MainViewModel.InputStatus.Input2())
                }
                is MainViewModel.InputStatus.Input2 -> {
                    viewModel.setInputStatus(MainViewModel.InputStatus.Input1())

                }
                is MainViewModel.InputStatus.Input1 -> {
                    binding.input1.clearFocus()
                    val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.input1.windowToken, 0)
                }
            }
        }
        repeatOnStarted {
            viewModel.inputEvent
                .catch { }
                .collect {
                    when (it) {
                        is MainViewModel.InputStatus.Input4 -> {
                            binding.input4.requestFocus()
                        }
                        is MainViewModel.InputStatus.Input3 -> {
                            binding.input3.visibility = View.VISIBLE
                            binding.input3.requestFocus()
                        }
                        is MainViewModel.InputStatus.Input2 -> {
                            binding.input2.visibility = View.VISIBLE
                            binding.input2.requestFocus()

                        }
                        is MainViewModel.InputStatus.Input1 -> {
                            binding.input1.visibility = View.VISIBLE
                            binding.input1.requestFocus()
                        }
                    }
                }
        }
    }

    private fun setFocusAction() {
        binding.input4.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setInputStatus(MainViewModel.InputStatus.Input3())
            }
            false
        }
        binding.input3.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setInputStatus(MainViewModel.InputStatus.Input2())
            }
            false
        }
        binding.input2.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setInputStatus(MainViewModel.InputStatus.Input1())
            }
            false
        }
    }
}