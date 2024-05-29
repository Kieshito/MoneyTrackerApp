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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.moneytracker.ui.theme.Zinc


@Composable
fun HomeScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()){
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
                        text = "User Name",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            CardItem(modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            TransactionList(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                })
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
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("/add")
                    }
            )
        }
    }
}


@Composable
fun CardItem(modifier: Modifier){
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
                Text(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                Text(
                    text = "$5000",
                    fontSize = 24.sp,
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
                amount ="$3,349",
                image = R.drawable.ic_income)
            CardRowItem(modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount ="$1,651",
                image = R.drawable.ic_expenses)
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
fun TransactionList(modifier: Modifier){
    Column(modifier = modifier
        .padding(horizontal = 16.dp)
        .height(150.dp)){
        Box(modifier = Modifier.fillMaxWidth()){
            Text(text = "Recent Transactions", fontSize = 20.sp)
            Text(text = "See all",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        TransactionListItem(
            title = "Upwork",
            amount = "+$200.00",
            icon = R.drawable.ic_temporary_all_transaction_icon,
            date = "Today",
            color = Zinc
        )
        TransactionListItem(
            title = "Shopping",
            amount = "-$200.00",
            icon = R.drawable.ic_temporary_all_transaction_icon,
            date = "Today",
            color = Color.Red
        )
        TransactionListItem(
            title = "Transfer",
            amount = "-$85.00",
            icon = R.drawable.ic_temporary_all_transaction_icon,
            date = "Today",
            color = Color.Red
        )
        TransactionListItem(
            title = "Hobby",
            amount = "-$200.00",
            icon = R.drawable.ic_temporary_all_transaction_icon,
            date = "Today",
            color = Color.Red
        )
    }

}


@Composable
fun TransactionListItem(title: String, amount: String, icon: Int, date:String, color: Color){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)){
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
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
@Preview (showBackground = true)
fun PreviewHomeScreen(){
    HomeScreen(rememberNavController())
}