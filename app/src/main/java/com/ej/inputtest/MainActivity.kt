package com.ej.inputtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ej.inputtest.databinding.ActivityMainBinding
import com.paycoin.global.presentation.util.event.repeatOnStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        repeatOnStarted {
            viewModel.inputEvent
                .catch {  }
                .collect{
                    when (it) {
                        is MainViewModel.InputStatus.Input4 -> {
                            binding.input4.requestFocus()
                        }
                        is MainViewModel.InputStatus.Input3 -> {
                            binding.input3.requestFocus()
                        }
                        is MainViewModel.InputStatus.Input2 -> {
                            binding.input2.requestFocus()

                        }
                        is MainViewModel.InputStatus.Input1 -> {
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
//                binding.input3.requestFocus()
            }
            false
        }
        binding.input3.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setInputStatus(MainViewModel.InputStatus.Input2())
//                binding.input2.requestFocus()
            }
            false
        }
        binding.input2.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.setInputStatus(MainViewModel.InputStatus.Input1())
//                binding.input1.requestFocus()
            }
            false
        }
    }
}