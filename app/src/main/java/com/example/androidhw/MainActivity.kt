package com.example.androidhw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.androidhw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val navController = (supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment).navController

        binding?.run {
            bnvMain.setupWithNavController(navController)
        }

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.mainFragment,
                R.id.servicesFragment,
                R.id.chatFragment,
                R.id.historyFragment,
                R.id.paymentsFragment
            ),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )

        NavigationUI.setupWithNavController(
            findViewById(androidx.appcompat.R.id.action_bar),
            navController,
            appBarConfiguration
        )
    }
}