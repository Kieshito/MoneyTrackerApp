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
fun AllTransactions(navController: NavController){
    val viewModel = HomeViewModelFactoty(LocalContext.current).create(HomeViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()


    Surface(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, topBar, list, listTitle, topWalletIcon) = createRefs()

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
                        .clickable {
                            navController.popBackStack()
                        }
                )
                Text(
                    text = "All Transactions",
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
            Image(painter = painterResource(id = R.drawable.ic_start),
                contentDescription = "topBar",
                modifier = Modifier.padding(top=10.dp).size(175.dp).constrainAs(topWalletIcon) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            val state = viewModel.transactions.collectAsState(initial = emptyList())
            Box(modifier = Modifier.fillMaxWidth().padding(top=20.dp, start = 15.dp).constrainAs(listTitle){
                top.linkTo(topWalletIcon.bottom)
                start.linkTo(parent.start)
            }){
                Text(text = "Transaction list: ", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            }
            AllTransactionList(modifier = Modifier.fillMaxWidth().padding(top=5.dp).constrainAs(list) {
                top.linkTo(listTitle.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }, dataList = state.value, viewModel, coroutineScope, navController)
        }
    }
}

@Composable
fun AllTransactionList(modifier: Modifier, dataList: List<TransactionEntity>, viewModel: HomeViewModel, coroutineScope: CoroutineScope, navController: NavController){
    LazyColumn(modifier = modifier
        .padding(horizontal = 16.dp)
        .height(1.dp)){
        item{
        }
        items(dataList){item->
            item.transactionId?.let {
                AllTransactionListItem(
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
fun AllTransactionListItem(title: String,
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
                .padding(8.dp)
                .align(Alignment.CenterEnd)
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
@Preview
fun PreviewAllTransactions(){
    AllTransactions(rememberNavController())
}