package com.neisha.ispalindrom.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.neisha.ispalindrom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener {
            val sentence = binding.etPalindrome.text.toString()
            if (sentence.isBlank()) {
                showEmptyInputDialog()
            } else {
                val isPalindrome = checkPalindrome(sentence)
                showResultDialog(isPalindrome)
            }
        }

        binding.btnNext.setOnClickListener {
            val name = binding.etName.text.toString()
            val intent = Intent(this, SelectUserActivity::class.java).apply {
                putExtra("USER_NAME", name)
            }
            startActivity(intent)
        }
    }

    private fun checkPalindrome(sentence: String): Boolean {
        val cleanSentence = sentence.replace(Regex("[^A-Za-z0-9]"), "").lowercase()
        return cleanSentence == cleanSentence.reversed()
    }

    private fun showResultDialog(isPalindrome: Boolean) {
        val message = if (isPalindrome) "is palindrome" else "not palindrome"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Result")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showEmptyInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Input Required")
        builder.setMessage("Please, enter data first")
        builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
