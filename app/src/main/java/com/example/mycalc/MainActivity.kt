package com.example.mycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.mycalc.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.Expression
import java.lang.ArithmeticException
import java.lang.Exception



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot =false
    private lateinit var  expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun OnAllClearClick(view: View) {
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.resultTv.visibility = View.GONE
    }

    fun OnDigitClick(view: View) {
        if(stateError){
            binding.dataTv.text= (view as Button).text
            stateError =false
        } else {
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric= true
        onEqual()
    }

    fun OnEqualClick(view: View) {
        onEqual()
        binding.dataTv.text = binding.resultTv.text.toString().drop(1)
    }

    fun OnOperatorClick(view: View) {
        if (!stateError && lastNumeric) {
            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun OnBackClick(view: View) {
        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)
        try {
            val lastChar = binding.dataTv.text.toString().last()
            if (lastChar.isDigit()){
                onEqual()
            }
        }catch (e: Exception){
            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("Last char error", e.toString())
        }
    }

    fun OnClearClick(view: View) {
        binding.dataTv.text = ""
        lastNumeric = false
    }
    fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = binding.dataTv.text.toString()
            expression = Expression(txt)
            try {
                val result = expression.calculate()
                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = "=" + result.toString()
            } catch (ex: ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.resultTv.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}