package com.example.moneytracker

import android.annotation.SuppressLint
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneytracker.data.model.RegisteredUser
import com.example.moneytracker.ui.theme.Zinc
import com.example.moneytracker.viewmodel.EditProfileViewModel
import com.example.moneytracker.viewmodel.EditProfileViewModelFactory
import com.example.moneytracker.viewmodel.LoginViewModel
import com.example.moneytracker.viewmodel.LoginViewModelFactoty
import com.example.moneytracker.viewmodel.RegistrationViewModel
import com.example.moneytracker.viewmodel.RegistrationViewModelFactoty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EditProfile(navController: NavController) {
    val viewModel = EditProfileViewModelFactory(LocalContext.current).create(EditProfileViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()
    val isUpdated = remember {
        mutableStateOf(false)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, card, topBar) = createRefs()
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
                    text = "Edit profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
            EditProfileForm(navController, modifier = Modifier
                .padding(top = 50.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onConfirmClick = {
                coroutineScope.launch {
                    isUpdated.value = viewModel.updateUser(it)
                    if (isUpdated.value){
                        navController.navigate("/home"){
                            MainActivity.enteredName = it.preferredTreatment
                            MainActivity.enteredLogin = it.login
                            navController.popBackStack()
                        }
                    }
                }
            })
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EditProfileForm(navController: NavController, modifier: Modifier, onConfirmClick: (user: RegisteredUser) -> Unit){
    val coroutineScope = rememberCoroutineScope()
    val Login = remember {
        mutableStateOf(MainActivity.enteredLogin)
    }
    val Password = remember {
        mutableStateOf("")
    }
    val Treatment = remember {
        mutableStateOf(MainActivity.enteredName)
    }
    val isWrongDataDialog = remember{
        mutableStateOf(false)
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
            isError = Login.value.isEmpty(),
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
            isError = Password.value.isEmpty(),
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = "Preferred Treatment", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.size(4.dp))
        OutlinedTextField(
            value = Treatment.value,
            onValueChange = {
                Treatment.value = it
            },
            isError = Treatment.value.isEmpty(),
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = {
            val model = RegisteredUser(
                MainActivity.enteredUserId,
                Login.value,
                Treatment.value,
                Password.value
            )
            if (Password.value!=""&&Login.value!=""&&Treatment.value!=""){
                onConfirmClick(model)
            } else {
                isWrongDataDialog.value=true
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
fun PreviewEditProfileScreen(){
    EditProfile(rememberNavController())
    TODO("заменить treatment на просто name")
}

