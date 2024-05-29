package com.example.moneytracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneytracker.ui.theme.Zinc
import com.example.moneytracker.ui.theme.blueText

@Composable
fun StartScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (titleRow, login, card, topBar, registrating, taglineRow) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = "topBar",
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 400.dp)
                    .constrainAs(titleRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }){
                Text(
                    text = "Money Tracker",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc,
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 460.dp)
                    .constrainAs(taglineRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }){
                Text(
                    text = "Spend smarter",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = blueText
                )
            }
            Image(painter = painterResource(id = R.drawable.ic_start),
                contentDescription = null,
                modifier = Modifier.padding(top = 175.dp, start = 16.dp, end = 16.dp).size(200.dp).constrainAs(card){
                    top.linkTo(topBar.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Image(
                painter = painterResource(id = R.drawable.bt_registration),
                contentDescription = null,
                modifier = Modifier. padding(top = 100.dp, start = 24.dp, end = 24.dp)
                    .constrainAs(registrating) {
                        top.linkTo(card.bottom)
                        end.linkTo(parent.end)
                    }
                    .size(350.dp)
                    .clickable {
                        navController.navigate("/registration")
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.bt_login),
                contentDescription = null,
                modifier = Modifier.padding(top = 175.dp, start = 24.dp, end = 24.dp)
                    .constrainAs(login) {
                        top.linkTo(card.bottom)
                        end.linkTo(parent.end)
                    }
                    .size(350.dp)
                    .clickable {
                        navController.navigate("/login")
                    }
            )
        }
    }
}

@Composable
@Preview (showBackground = true)
fun PreviewStartScreen(){
    StartScreen(rememberNavController())
}
