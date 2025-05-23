package com.learn.ultimate_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val areaViewModel = ViewModelProvider(this)[AreaCalculatorViewModel::class.java]
        val calculatorViewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]


        enableEdgeToEdge()


        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "HomeCal") {

                composable("HomeCal") {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        HomeCalculator(
                            viewModel = areaViewModel,
                            navController = navController
                        )
                    }
                }

                composable("AreaCal") {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AreaCalculator(
                            viewModel = areaViewModel,
                            navController = navController
                        )
                    }
                }

                composable("SciCal") {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        SciCal(
                            viewModel = calculatorViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

}
