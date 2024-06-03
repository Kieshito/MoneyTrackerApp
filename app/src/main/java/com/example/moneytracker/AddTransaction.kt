package com.example.moneytracker

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneytracker.data.model.TransactionEntity
import com.example.moneytracker.viewmodel.AddTransactionViewModel
import com.example.moneytracker.viewmodel.AddTransactionViewModelFactoty
import kotlinx.coroutines.launch


private val incomeList = listOf("None", "Salary", "Transfer", "Selling", "Gift", "Scholarship", "Investment")
private val expenseList = listOf("None", "Shopping", "Hobby", "Health", "Bank payment", "Rent", "Transport")
private val typeList = listOf("None", "Income", "Expense")
@Composable
fun AddExpense(navController: NavController) {
    val viewModel = AddTransactionViewModelFactoty(LocalContext.current).create(AddTransactionViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = "topBar",
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { navController.popBackStack() }
                )
                Text(
                    text = "Add Transaction",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_dotsmenu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            DataForm(modifier = Modifier
                .padding(top = 60.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onAddTransactionClick = {
                    coroutineScope.launch {
                        if (viewModel.addTransaction(it)){
                            //ShowMessage(message = "Error")
                            navController.popBackStack()
                        }
                    }
                }, viewModel
            )
        }
    }
}


@Composable
fun DataForm(modifier: Modifier,
             onAddTransactionClick: (model: TransactionEntity) ->Unit,
             viewModel: AddTransactionViewModel){
    val name = remember{
        mutableStateOf("")
    }

    var amount = remember{
        mutableStateOf("")
    }

    var description = remember{
        mutableStateOf("")
    }

    val date = remember{
        mutableLongStateOf(0L)
    }

    val dateDialogVisability = remember {
        mutableStateOf(false)
    }

    val type = remember {
        mutableStateOf("")
    }

    var isValidDescription by remember {
        mutableStateOf(true)
    }

    var isValidAmount by remember {
        mutableStateOf(true)
    }

    Column(modifier = modifier
        .shadow(16.dp)
        .padding(16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
    ){
        //Main type (Income or Expense)
        Text(text = "Type", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        TypeDropDown(listOfItems = typeList,
            onItemSelected = {
                type.value = it
            })
        Spacer(modifier = Modifier.size(16.dp))

        //Category of Income/Expense
        Text(text = "Category", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
//        OutlinedTextField(value = name.value, onValueChange = {name.value=it}, modifier = Modifier.fillMaxWidth())
        if (type.value =="Income"){
            TransactionDropDown(listOfItems = incomeList,
                onItemSelected = {
                    name.value = it
                })
        } else if (type.value == "Expense"){
            TransactionDropDown(listOfItems = expenseList,
                onItemSelected = {
                    name.value = it
                })
        } else if (type.value == "None" || type.value == ""){
            OutlinedTextField(
                value = "In first, you should select type",
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(Color.Gray),
                //color = Color.Gray,
                modifier = Modifier.fillMaxWidth())
        }
        Spacer(modifier = Modifier.size(16.dp))

        //Money amount
        Text(text = "Amount", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = {
                amount.value=it
                            try{
                                if (it.toDouble().isNaN()) isValidAmount = true
                            } catch (e: NumberFormatException){
                                isValidAmount = false
                            }
                            },
            isError = !isValidAmount,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.size(16.dp))

        //Date of transaction
        Text(text = "Date", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = if (date.value==0L)"" else Utils.formatDateToReadableForm(date.longValue),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisability.value = true },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Black,
                disabledTextColor = Color.Black
            )
        )
        if (dateDialogVisability.value){
            TransactionDatePickerDialog(
                onDateSelected = {
                date.value = it
                dateDialogVisability.value=false
            }, onDismiss = {
                dateDialogVisability.value=false
                })
        }
        Spacer(modifier = Modifier.size(16.dp))

        //Description of transaction
        Text(text = "Description", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(value = description.value, onValueChange = {
            description.value = it
            isValidDescription = it.isNotBlank()
        },
            isError = !isValidDescription,
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = {
            val model = TransactionEntity(
                null,
                MainActivity.enteredUserId,
                name.value,
                amount.value.toDoubleOrNull() ?: 0.0,
                Utils.formatDateToReadableForm(date.value),
                description.value,
                type.value
            )
            onAddTransactionClick(model)
        },
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .fillMaxWidth()){
            Text(text = "Add transaction", fontSize = 14.sp, color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDatePickerDialog(
    onDateSelected:(date:Long)->Unit,
    onDismiss:()->Unit
){
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?:0L
    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = { TextButton (onClick = { onDateSelected(selectedDate) }) {
            Text(text = "Confirm")
        }
        },
        dismissButton = {
            TextButton(onClick = { onDateSelected(selectedDate)}) {
                Text(text = "Cancel")
            }
        }
        ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDropDown(listOfItems: List<String>, onItemSelected: (item: String) -> Unit){
    val expanded = remember{
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf(listOfItems[0])
    }

    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = it}) {
        TextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            })
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { /*TODO*/ }) {
            listOfItems.forEach {
                if (it!=""){
                    DropdownMenuItem(text = { Text(text = it) }, onClick = {
                        selectedItem.value = it
                        onItemSelected(selectedItem.value)
                        expanded.value = false
                    })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropDown(listOfItems: List<String>, onItemSelected: (item: String) -> Unit){
    val expanded = remember{
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf(listOfItems[0])
    }

    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = it}) {
        TextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            })
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { /*TODO*/ }) {
            listOfItems.forEach {
                DropdownMenuItem(text = { Text(text = it) }, onClick = {
                    selectedItem.value = it
                    onItemSelected(selectedItem.value)
                    expanded.value = false
                })
            }
        }
    }
}

@Composable
fun ShowMessage(message:String){
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}



@Composable
@Preview(showBackground = true)
fun AddExpensePreview(){
    AddExpense(rememberNavController())
}