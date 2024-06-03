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
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.moneytracker.viewmodel.LoginViewModel
import com.example.moneytracker.viewmodel.LoginViewModelFactoty
import com.example.moneytracker.viewmodel.RegistrationViewModel
import com.example.moneytracker.viewmodel.RegistrationViewModelFactoty
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(navController: NavController) {
    val viewModel = RegistrationViewModelFactoty(LocalContext.current).create(RegistrationViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()

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
                    text = "Registration",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
            RegistrationForm(navController, modifier = Modifier
                .padding(top = 50.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onConfirmClick = {
                    coroutineScope.launch {
                        if (viewModel.checkUser(it)){
                            viewModel.registration(it)
                            navController.navigate("/home"){
                                navController.popBackStack()
                            }
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
    }
}

@Composable
fun RegistrationForm(navController: NavController, modifier: Modifier, onConfirmClick: (user: RegisteredUser) -> Unit){
    val Login = remember {
        mutableStateOf("")
    }
    val Password = remember {
        mutableStateOf("")
    }
    val Treatment = remember {
        mutableStateOf("")
    }


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
            value = Login.value,
            onValueChange = {
                Login.value = it
            },
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = "Password", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = Password.value,
            onValueChange = {
                Password.value = it
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = "Preferred Treatment", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = Treatment.value,
            onValueChange = {
                Treatment.value = it
            },
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = {
            val model = RegisteredUser(
                null,
                Login.value,
                Treatment.value,
                Password.value
            )
            onConfirmClick(model)
                         }, //TODO: доделать, чтоб возвращало даже не на стартовый экран, а выкидывало из приложения
            Modifier
                .clip(RoundedCornerShape(2.dp))
                .fillMaxWidth()){
            Text(text = "Confirm", fontSize = 14.sp, color = Color.White)
        }
    }
}

@Composable
@Preview
fun PreviewRegistrationScreen(){
    RegistrationScreen(rememberNavController())
}