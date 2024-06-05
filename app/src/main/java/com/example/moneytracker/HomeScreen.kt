package com.example.moneytracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneytracker.data.model.TransactionEntity
import com.example.moneytracker.ui.theme.Zinc
import com.example.moneytracker.ui.theme.grayOne
import com.example.moneytracker.viewmodel.HomeViewModel
import com.example.moneytracker.viewmodel.HomeViewModelFactoty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = HomeViewModelFactoty(LocalContext.current).create(HomeViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()


    Surface(modifier = Modifier.fillMaxSize()){
        navController.clearBackStack("/home")

        ConstraintLayout(modifier = Modifier.fillMaxSize()){
            val(nameRow, list, card, topBar, addExpensive)= createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = "topBar",
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                Column {
                    Text(
                        text = "Greetings",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = MainActivity.enteredName,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(25.dp)
                        .clickable {
                            navController.navigate("/settings")
                        }
                )
            }

            val state = viewModel.transactions.collectAsState(initial = emptyList())
            val expenses = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            CardItem(modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, expenses, income, balance)
            TransactionList(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }, dataList = state.value, viewModel, coroutineScope, navController)
            Image(
                painter = painterResource(id = R.drawable.ic_addexpensive),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(addExpensive) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .size(100.dp)
                    //.clip(CircleShape)
                    .clickable {
                        navController.navigate("/add")
                    }
            )
        }
    }
}


@Composable
fun CardItem(modifier: Modifier, expenses: String, income: String, balance: String){
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Zinc)
        .padding(16.dp)
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Column (modifier = Modifier.align(Alignment.CenterStart)){
                Text(text = "Total Balance", fontSize = 20.sp, color = Color.White)
                Text(
                    text = balance,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_dotsmenu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Box (modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            CardRowItem(modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.ic_income)
            CardRowItem(modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expenses,
                image = R.drawable.ic_expense)
        }
    }
}

@Composable
fun CardRowItem(modifier: Modifier,title: String, amount: String, image: Int){
    Column(modifier = modifier) {
        Row{
            Image(painter = painterResource(id = image), contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = title, fontSize = 16.sp, color = Color.White)
        }
        Text(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun TransactionList(modifier: Modifier, dataList: List<TransactionEntity>, viewModel: HomeViewModel, coroutineScope: CoroutineScope, navController: NavController){
    LazyColumn(modifier = modifier
        .padding(horizontal = 16.dp)
        .height(1.dp)){
        item{
            Box(modifier = Modifier.fillMaxWidth()){
                Text(text = "Recent Transactions", fontSize = 20.sp)
                Text(text = "See all",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterEnd).clickable {
                        navController.navigate("/all_transactions"){
                            navController.clearBackStack("/all_transactions")
                        }
                    }
                )
            }
        }
        items(dataList.takeLast(5)){item->
            item.transactionId?.let {
                TransactionListItem(
                    title = item.name,
                    amount = item.amount.toString(),
                    icon = viewModel.getItemIcon(item),
                    date = item.date,
                    color = if (item.type=="Income") Zinc else Color.Red,
                    itemId = it,
                    viewModel,
                    coroutineScope
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }

}


@Composable
fun TransactionListItem(title: String,
                        amount: String,
                        icon: Int,
                        date:String,
                        color: Color,
                        itemId: Int,
                        viewModel: HomeViewModel,
                        coroutineScope: CoroutineScope){

    var isDeleteDialogVisible = remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(grayOne, shape = RoundedCornerShape(16.dp))
        .padding(vertical = 8.dp)
        .padding(start = 6.dp)
        .clickable { TODO("information about selected transaction") }){
        Row{
            Image(painterResource(id = icon), contentDescription = null, modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Column{
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(text = date, fontSize = 12.sp)
            }
        }
        Text(
            text = amount,
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 50.dp),
            color = color,
            fontWeight = FontWeight.SemiBold
        )
        Image(
            painterResource(id = R.drawable.ic_delete),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(8.dp)
                .size(30.dp)
                .clickable {
                    coroutineScope.launch {
                        isDeleteDialogVisible.value = true
                    }
                })
        if (isDeleteDialogVisible.value) {
            AlertDialog(
                onDismissRequest = {
                    isDeleteDialogVisible.value = false
                },
                title = { Text(text = "Delete confirmation") },
                text = { Text(text = "Are you sure you want to delete this transaction?") },
                confirmButton = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                               viewModel.removeTransaction(itemId)
                            }
                            isDeleteDialogVisible.value = false
                        }, colors = ButtonDefaults.buttonColors(Zinc)
                    ) {
                        Text(
                            text = "Confirm",
                            color = Color.White,
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            isDeleteDialogVisible.value = false
                        }, colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.White
                        )
                    }
                },
            )
        }
    }
}

@Composable
@Preview (showBackground = true)
fun PreviewHomeScreen(){
    HomeScreen(rememberNavController())
}