package com.learn.ultimate_calculator



import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.learn.ultimate_calculator.ui.theme.Cyan1
import com.learn.ultimate_calculator.ui.theme.Orange
import com.learn.ultimate_calculator.ui.theme.Purple

val Scibutton = listOf(
    "C", "AC", "(", ")", "+",
    "1", "2", "3", "4", "-",
    "5", "6", "7", "8", "×",
    "9", "0", ".", "π", "÷",
    "√", "tan", "cos", "sin", "%",
    "^", "log", "ln", "!", "+/-"
)

@Composable
fun SciCal(
    viewModel: CalculatorViewModel,
    navController: NavController,
) {
    var tDTSize by remember { mutableStateOf(20.sp) }
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
            Spacer(modifier = Modifier.height(16.dp))



            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.2f)
                    .background(color = Color.LightGray),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
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
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp, 8.dp, 0.dp, 0.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Calculator", fontSize = tDTSize) },
                            onClick = {
                                navController.navigate(route = "HomeCal")
                                expanded = false
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        DropdownMenuItem(
                            text = { Text("AreaCal", fontSize = tDTSize) },
                            onClick = {
                                Toast.makeText(context, "test was a W's", Toast.LENGTH_SHORT).show()
                                expanded = false
                                navController.navigate(route = "AreaCal")
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
                        text = "Scientific Calculator",
                        style = TextStyle(
                            fontSize = 27.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 0.dp)
                    )
                }
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
                        fontSize = 30.sp,
                        textAlign = TextAlign.End,
                        color = if (viewModel.result.value == "Error") Color.Red else Gray
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
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
                            fontSize = 30.sp,
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
                columns = GridCells.Fixed(5),
                modifier = Modifier.weight(1.5f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Scibutton) { button ->
                    SciCalculatorButton(
                        text = button,
                        onClick = {
                            when (button) {
                                "×" -> viewModel.onButtonClick("*")
                                "÷" -> viewModel.onButtonClick("/")
                                else -> viewModel.onButtonClick(button)
                            }
                        },
                        isSpecial = listOf(
                            "C",
                            "AC",
                            "/",
                            "*",
                            "+",
                            "-",
                            "%",
                            "√",
                            "tan",
                            "cos",
                            "sin",
                            "(",
                            ")",
                            "^",
                            "log",
                            "ln",
                            "!",
                            "+/-"
                        ).contains(button),
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
                    text = if (viewModel.isRadiansMode.value) {
                        "RAD"
                    } else {
                        "DEG"
                    },
                    onClick = {
                        viewModel.toggleAngleMode()
                        if (viewModel.isRadiansMode.value) {
                            Toast.makeText(context, "You are in RAD mode", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "You are in DEG mode", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    isSpecial = true,
                    modifier = Modifier.weight(1f),
                    buttonColor = Orange

                )

                CalculatorButton(
                    text = "=",
                    onClick = { viewModel.calculateResult() },
                    isSpecial = false,
                    modifier = Modifier.weight(2f),
                    buttonColor = Purple
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SciCalculatorButton(
    text: String,
    onClick: () -> Unit,
    isSpecial: Boolean = false,
    modifier: Modifier = Modifier,
    buttonColor: Color? = null,
) {
    val defaultColor = if (isSpecial) Cyan1 else Gray
    val finalColor = buttonColor ?: defaultColor
    val textColor =
        if (isSpecial) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(finalColor)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 30.sp,
                color = textColor
            )
        )
    }
}

