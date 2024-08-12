package com.neisha.ispalindrom.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.neisha.ispalindrom.databinding.ActivitySelectUserBinding

class SelectUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectUserBinding
    private var userName: String? = null

    private val startForResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedUserName = result.data?.getStringExtra("SELECTED_USER_NAME")
            binding.tvSelectedUserName.text = selectedUserName ?: "No user selected"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("USER_NAME")
        Log.d("SelectUserActivity", "Received user name from MainActivity: $userName")

        binding.tvName.text = userName ?: "User Name"

        binding.tvSelectedUserName.text = "Selected User Name"

        binding.btnChooseUser.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startForResult.launch(intent)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tvName.text = userName ?: "User Name"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("USER_NAME", userName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userName = savedInstanceState.getString("USER_NAME")
        binding.tvName.text = userName ?: "User Name"
    }
}
