package com.example.android.storedirectory.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.android.storedirectory.R
import com.example.android.storedirectory.databinding.MainActivityBinding
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection.inject
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var navController: NavController
    private lateinit var binding: MainActivityBinding

    @Inject
    @JvmField
    var networkStateMonitor: NetworkStateMonitor? = null

    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        snackbar = Snackbar.make(binding.root, "No network connection", Snackbar.LENGTH_INDEFINITE)

        networkStateMonitor?.apply {
            lifecycle.addObserver(this)
            connectedStatus.observe(this@MainActivity, Observer {
                if (it) {
                    handleNetworkConnected()
                } else {
                    handleNoNetworkConnection()
                }
            })
        } ?: handleNoNetworkConnection()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun handleNoNetworkConnection() {
        Log.d(TAG, "No network connected")
        snackbar.show()
    }

    private fun handleNetworkConnected() {
        Log.d(TAG, "Network connected")
        snackbar.dismiss()
    }
}
