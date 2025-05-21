package com.example.fit5046a4.registerScreen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit5046a4.AppUtil
import com.example.fit5046a4.R
import com.example.fit5046a4.firebaseAuth.AuthViewModel
import com.example.fit5046a4.firebaseAuth.GoogleSignInUtils
import com.example.fit5046a4.loginScreen.DividerWithText

@Composable
fun RegisterScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isTermsAccepted by remember { mutableStateOf(false) }
    var context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FF)),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column (
                modifier = Modifier
//                    .weight(1f)
                    .fillMaxWidth()
                    .padding(32.dp, 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "FridgeFairy",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

//                Image(
//                    painter = painterResource(R.drawable.fridge),
//                    contentDescription = "Google logo",
//                    modifier = Modifier.size(50.dp)
//                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 24.dp),
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text="Create your account",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )

                Text(
                    text="To get started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                LabelWithAsterisk("Email address")
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Enter your email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LabelWithAsterisk("Password")
                PasswordInputField(password, passwordVisible, { password = it }, { passwordVisible = it })

                Spacer(modifier = Modifier.height(16.dp))

                LabelWithAsterisk("Confirm Password")
                PasswordInputField(confirmPassword, passwordVisible, { confirmPassword = it }, { passwordVisible = it })

            }


            TermsCheckbox(
                checked = isTermsAccepted,
                onCheckedChange = { isTermsAccepted = it },
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Register Button //onClick = onNavigateToMain,
            FilledTonalButton(
                onClick = {
                    authViewModel.register(email, password) {success, errorMessage ->
                        if(success) {
                            onNavigateToMain()
                        }else{
                            AppUtil.showToast(context, errorMessage?:"Something went wrong")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF415F91),
                    contentColor = Color.White
                )
            ) {
                Text("Register", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing before divider

            DividerWithText("OR")

            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing after divider

            RegisterGoogle(onSuccess = onNavigateToMain)

            Spacer(modifier = Modifier.height(16.dp)) // Added bottom spacing


            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        append("Already have an account? ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Login")
                        }
                    },
                    fontSize = 14.sp
                )
            }

        }
    }
}

@Composable
fun LabelWithAsterisk(label: String) {
    Text(buildAnnotatedString {
        append(label)
        withStyle(SpanStyle(color = Color.Red)) { append(" *") }
    })
}

@Composable
fun PasswordInputField(
    value: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Enter your password") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onToggleVisibility(!visible) }) {
                Icon(
                    painter = painterResource(
                        if (visible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    ),
                    contentDescription = if (visible) "Hide password" else "Show password"
                )
            }
        }
    )
}

@Composable
fun RegisterGoogle(
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            GoogleSignInUtils.doGoogleSignIn(
                context = context,
                scope = coroutineScope,
                launcher = null,
                login = onSuccess
            )
        }
    }

    Box() {
        ElevatedButton(
            onClick = {
                GoogleSignInUtils.doGoogleSignIn(
                    context = context,
                    scope = coroutineScope,
                    launcher = googleSignInLauncher,
                    login = onSuccess
                ) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD6E3FF),
                contentColor = Color(0xFF415F91)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.google),
                    contentDescription = "Google logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Continue with Google",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = buildAnnotatedString {
                append("By ticking this, you agree to our ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("Terms of Service")
                }
            },
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.clickable { onCheckedChange(!checked) }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RegisterPreview() {
//    RegisterScreen()
//}