package com.neisha.ispalindrom.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neisha.ispalindrom.R
import com.neisha.ispalindrom.view.adapter.UserAdapter
import com.neisha.ispalindrom.view.viewModel.UserViewModel
import com.neisha.ispalindrom.view.viewModel.UserViewModelFactory
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class UserListActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var emptyStateText: TextView
    private var page = 1
    private val perPage = 20
    private var isLoading = false
    private var isRefreshing = false

    private val startForResult: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedUserName = result.data?.getStringExtra("SELECTED_USER_NAME")
            selectedUserName?.let {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)

        val factory = UserViewModelFactory()
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.rvUsers)
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        emptyStateText = findViewById(R.id.emptyStateText)

        userAdapter = UserAdapter { user ->
            val intent = Intent().apply {
                putExtra("SELECTED_USER_NAME", "${user.firstName} ${user.lastName}")
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            isRefreshing = true
            loadUsers(page, perPage) {
                swipeRefreshLayout.isRefreshing = false
                isRefreshing = false
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isRefreshing && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    isLoading = true
                    page++
                    loadUsers(page, perPage)
                }
            }
        })
        loadUsers(page, perPage)

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            onBackButtonClicked(it)
        }
    }

    private fun loadUsers(page: Int, perPage: Int, onComplete: (() -> Unit)? = null) {
        userViewModel.getUsers(page, perPage).observe(this) { response ->
            if (response.data.isEmpty() && userAdapter.itemCount == 0) {
                emptyStateText.visibility = View.VISIBLE
            } else {
                emptyStateText.visibility = View.GONE
                val updatedList = userAdapter.currentList + response.data
                userAdapter.submitList(updatedList)
            }
            isLoading = false
            onComplete?.invoke()
        }
    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
    }
}
