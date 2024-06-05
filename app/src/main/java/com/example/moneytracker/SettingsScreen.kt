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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneytracker.ui.theme.Zinc
import com.example.moneytracker.ui.theme.grayBackground
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController){
    Surface(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, topBar, profilePicture, nameUnderPicture, logout, changePassword) = createRefs()
            var isLogoutDialogVisible = remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

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
                    text = "Settings",
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
            Image(
                painterResource(R.drawable.ic_profile_picture),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(profilePicture) {
                        top.linkTo(nameRow.bottom)
                    }
                    .padding(start = 120.dp, top = 75.dp)
                    .size(150.dp)
            )
            Text(
                text = MainActivity.enteredName,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .constrainAs(nameUnderPicture) {
                        top.linkTo(profilePicture.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(top = 15.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp).constrainAs(changePassword){
                top.linkTo(nameUnderPicture.bottom)
            }.padding(top=250.dp).clickable { navController.navigate("/edit_profile") }){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(40.dp)
                    .background(grayBackground, shape = RoundedCornerShape(20.dp))){
                    Row{
                        Text(
                            text = "Edit profile",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 120.dp, top=5.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp).constrainAs(logout){
                top.linkTo(changePassword.bottom)
            }.padding(top=10.dp).clickable { isLogoutDialogVisible.value = true }){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(40.dp)
                    .background(grayBackground, shape = RoundedCornerShape(20.dp))){
                    Row{
                        Text(
                            text = "Logout",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 140.dp, top=5.dp)
                        )
                    }
                }
            }

            if (isLogoutDialogVisible.value) {
                AlertDialog(
                    onDismissRequest = {
                        isLogoutDialogVisible.value = false
                    },
                    title = { Text(text = "Logout confirmation") },
                    text = { Text(text = "Are you sure you want to logout?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    MainActivity.enteredUserId = 0
                                    MainActivity.enteredName = ""
                                    MainActivity.enteredLogin = ""
                                    navController.navigate("/start_screen"){
                                        navController.popBackStack()
                                        navController.clearBackStack("/start_screen")
                                    }
                                }
                                isLogoutDialogVisible.value = false
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
                                isLogoutDialogVisible.value = false
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
}

@Preview
@Composable
fun PreviewSettinsgScreen(){
    SettingsScreen(rememberNavController())
}
