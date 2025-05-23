package com.learn.ultimate_calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.learn.ultimate_calculator.ui.theme.Purple

val Areabutton = listOf(
    "C", "(", ")", "/",
    "1", "2", "3", "*",
    "4", "5", "6", "+",
    "7", "8", "9", "-",
    "AC", "0", ".", "π"
)

@Composable
fun AreaCalculator(
    viewModel: AreaCalculatorViewModel,
    navController: NavController
) {
    var tDTSize by remember { mutableStateOf(20.sp) }
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.2f)
                    .background(color = Color.LightGray),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                //from

                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth()
                        .weight(1f)

                ) {

                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            modifier = Modifier.fillMaxSize()
                                .padding(
                                    0.dp,
                                    8.dp,
                                    0.dp,
                                    0.dp
                                )
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Calculator", fontSize = tDTSize,

                                    )
                            },
                            onClick = {
                                navController.navigate(route = "HomeCal")
                                expanded = false
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Scientific Calculator",
                                    fontSize = tDTSize
                                )
                            },
                            onClick = {
                                navController.navigate(route = "SciCal")

                                expanded = false
                            }
                        )


                    }
                }
                Box(
                    modifier = Modifier
                        .weight(5f)
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Area Calculator",
                        style = TextStyle(
                            fontSize = 35.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(
                            0.dp,
                            8.dp,
                            0.dp,
                            0.dp
                        )
                    )
                }
//too
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {

                Text(
                    text = viewModel.result.value.ifEmpty { "0" },
                    style = TextStyle(
                        fontSize = 35.sp,
                        textAlign = TextAlign.End
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp * 3)
                ) {
                    Text(
                        text = viewModel.expression.value.ifEmpty { "0" },
                        style = TextStyle(
                            fontSize = 38.sp,
                            textAlign = TextAlign.End
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }


            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.weight(1.5f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Areabutton) { button ->
                    CalculatorButton(
                        text = button,
                        onClick = { viewModel.onButtonClick(button) },
                        isSpecial = listOf("C", "AC", "/", "*", "+", "-", "π").contains(button),
                        modifier = Modifier
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
                    .weight(0.3f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "=",
                    onClick = { viewModel.calculateResult() },
                    isSpecial = false,
                    modifier = Modifier.weight(2f),
                    buttonColor = Purple
                )
                CalculatorButton(
                    text = "+/-",
                    onClick = { viewModel.toggleSign() },
                    isSpecial = false,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}





