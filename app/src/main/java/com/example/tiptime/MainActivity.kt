package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontSynthesis.Companion.Weight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    Layout()
                }
            }
        }
    }
}

@VisibleForTesting
internal fun calculateTip(
    amount: Double,
    tipPercentage: Double = 15.00,
    roundUpSelected: Boolean)
    : String {
    var tip = tipPercentage / 100 * amount
    if (roundUpSelected) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getNumberInstance(Locale.FRANCE).format(tip)
}



@Composable
fun Layout () {
    /*Compose will pay attention to any change in state of the tip input*/
    var tipAmountInput by remember { mutableStateOf("") }
    var tipPercentageInput by remember { mutableStateOf("") }
    var roundUpSelected by remember { mutableStateOf(false) }

    /*converting tip which come as a String to Double or Null, if it's null, 0.0*/
    val tipAmount = tipAmountInput.toDoubleOrNull() ?: 0.00
    val tipPercentage = tipPercentageInput.toDoubleOrNull() ?: 0.00

    val tip = calculateTip(tipAmount, tipPercentage, roundUpSelected)

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 20.dp).padding(top= 30.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        TipBoard(tip)

        TipForm(
            tipAmountInput = tipAmountInput,
            handleAmountChange = { tipAmountInput = it },
            tipPercentageInput = tipPercentageInput,
            handlePercentageChange = { tipPercentageInput = it},
            roundUpSelected = roundUpSelected,
            handleRoundUpChange = { roundUpSelected = it }
        )

    }
}


@Composable
fun TipBoard(tip: String) {
    Column(
        Modifier
            .height(150.dp)
            .fillMaxWidth()
            .background(
                Color(0xFFA1D5EF),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text= stringResource(id = R.string.total_tip),
            color= Color(0xFF116A97),
            fontWeight = FontWeight.Medium

        )
        Text(
            fontSize = 24.sp,
            color= Color(0xFF116A97),
            fontWeight = FontWeight.Bold,
            text= stringResource(R.string.tip_amount, tip)
        )

    }
}

@Composable
fun TipForm(
    tipAmountInput: String,
    handleAmountChange: (String) -> Unit,
    tipPercentageInput: String,
    handlePercentageChange: (String) -> Unit,
    roundUpSelected: Boolean,
    handleRoundUpChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 60.dp)
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(size = 5.dp)
            )
    ) {
        /*Bill amount*/
       BillAmountField(
           tipAmountInput = tipAmountInput,
           handleValueChange = handleAmountChange
       )
        /*Tip percentage*/
        TipPercentageField(
            tipPercentageInput = tipPercentageInput,
            handlePercentageChange = handlePercentageChange
        )

        TipRoundUpField(
            roundUpSelected = roundUpSelected,
            handleRoundUpChange = handleRoundUpChange
        )

    }
}

@Composable
fun BillAmountField(
    tipAmountInput:String,
    handleValueChange: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.bill_amount),
            color = Color(0xFF8B9092)
        )
        OutlinedTextField(
            value = tipAmountInput,
            onValueChange = handleValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next ),
            singleLine = true,
            suffix = { Text("€") },
            modifier = Modifier
                .width(120.dp)
        )
    }
}


@Composable
fun TipPercentageField(
    tipPercentageInput: String,
    handlePercentageChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.tip_percentage),
            color = Color(0xFF8B9092)
        )
        OutlinedTextField(
            value = tipPercentageInput,
            onValueChange = handlePercentageChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            singleLine = true,
            suffix = { Text("%")},
            modifier = Modifier.width(120.dp)
        )
    }
}

@Composable
fun TipRoundUpField(
    roundUpSelected: Boolean,
    handleRoundUpChange: (Boolean) -> Unit

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.round_up),
            color = Color(0xFF8B9092)
        )
        Switch(
            checked = roundUpSelected,
            onCheckedChange = handleRoundUpChange
        )
    }
}
@Preview(showBackground = true)
@Composable
fun LayoutPreview() {
    TipTimeTheme {
        Layout()
    }
}