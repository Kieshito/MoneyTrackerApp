package com.example.moneytracker

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneytracker.data.model.RegisteredUser
import com.example.moneytracker.data.model.TransactionEntity
import com.example.moneytracker.ui.theme.Zinc
import com.example.moneytracker.viewmodel.AddTransactionViewModel
import com.example.moneytracker.viewmodel.AddTransactionViewModelFactoty
import com.example.moneytracker.viewmodel.LoginViewModel
import com.example.moneytracker.viewmodel.LoginViewModelFactoty
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel = LoginViewModelFactoty(LocalContext.current).create(LoginViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()

    val isUserFounded = remember{
        mutableStateOf(false)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, moneyPicture, card, topBar) = createRefs()
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
                    text = "Login",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
            LoginForm(navController, modifier = Modifier
                .padding(top = 50.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onConfirmClick = {
                    coroutineScope.launch {
                        val id = viewModel.foundUser(it.login, it.password)
                        if (id!=null){
                            viewModel.login(id)
                            navController.navigate("/home"){
                                navController.clearBackStack("/home")
                                navController.popBackStack()
                            }
                        } else {
                            isUserFounded.value = true
                        }
                    }
            })
            Image(painter = painterResource(id = R.drawable.ic_start),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 383.dp, start = 24.dp, end = 16.dp)
                    .size(150.dp)
                    .constrainAs(moneyPicture) {
                        top.linkTo(card.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
        }
        if (isUserFounded.value) {
            AlertDialog(
                onDismissRequest = {
                },
                title = { Text(text = "Wrong data") },
                text = { Text(text = "User is not found, check your data or register new user") },
                confirmButton = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isUserFounded.value = false
                            }
                        }, colors = ButtonDefaults.buttonColors(Zinc)
                    ) {
                        Text(
                            text = "Confirm",
                            color = Color.White,
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun LoginForm(navController: NavController,
              modifier: Modifier,
              onConfirmClick: (user: RegisteredUser) -> Unit,
){
    val login = remember{
        mutableStateOf("")
    }
    val password = remember{
        mutableStateOf("")
    }
    val isWrongDataDialog = remember{
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
    ){
        Text(text = "Login", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = login.value,
            onValueChange = {
                login.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            isError = login.value.isEmpty())
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = "Password", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = login.value.isEmpty())
        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = {
            val model = RegisteredUser(
                null,
                login.value,"",
                password.value
            )
                if (login.value != "" && password.value != ""){
                    isWrongDataDialog.value=false
                    onConfirmClick(model)
                } else {
                    isWrongDataDialog.value = true
                }
            },
            Modifier
                .clip(RoundedCornerShape(2.dp))
                .fillMaxWidth()){
            Text(text = "Confirm", fontSize = 14.sp, color = Color.White)
        }
    }

    if (isWrongDataDialog.value) {
        AlertDialog(
            onDismissRequest = {
            },
            title = { Text(text = "Wrong data") },
            text = { Text(text = "Check your data") },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isWrongDataDialog.value = false
                        }
                    }, colors = ButtonDefaults.buttonColors(Zinc)
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White,
                    )
                }
            }
        )
    }
}



@Composable
@Preview
fun PreviewLoginForm(){
    LoginScreen(rememberNavController())
}