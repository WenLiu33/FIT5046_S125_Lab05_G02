package com.example.fit5046a4.loginScreen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.fit5046a4.firebaseAuth.AuthViewModel
import com.example.fit5046a4.firebaseAuth.GoogleSignInUtils
import com.example.fit5046a4.R
import com.example.fit5046a4.registerScreen.LabelWithAsterisk
import com.example.fit5046a4.registerScreen.validatePassword

@Composable
fun LoginScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToRegister: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    var context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                // Clear focus when tapping outside input fields
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
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
                    .weight(1f)
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

                Image(
                    painter = painterResource(R.drawable.loginicon),
                    contentDescription = "Google logo",
                    modifier = Modifier.size(180.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 24.dp),
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text="Welcome Back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )

                Text(
                    text="Login to your account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Email Field
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

                // Password Field
                LabelWithAsterisk("Password")
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = { Text("Enter your password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.ic_visibility
                                    else R.drawable.ic_visibility_off
                                ),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Forgot Password Link
                Text(
                    text = "Forgot Password?",
                    color = Color(0xFF415F91),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                        .clickable { showForgotPasswordDialog = true }
                )
                // Dialog for password reset
                if (showForgotPasswordDialog) {
                    ForgotPasswordDialog(
                        onDismiss = { showForgotPasswordDialog = false },
                        onResetClicked = { emailInput ->
                            authViewModel.resetPassword(emailInput) { success, message ->
                                showForgotPasswordDialog = false
                                if (success) {
                                    AppUtil.showToast(context, "Reset email sent!")
                                } else {
                                    AppUtil.showToast(context, message ?: "Oops, something went wrong")
                                }
                            }
                        }
                    )
                }
            }

            // Login Button
            FilledTonalButton(
                onClick = {
                    val passwordRules = validatePassword(password)
                    val isPasswordValid = passwordRules.all { it.isValid }

                    when {
                        email.isBlank() || password.isBlank() -> {
                            AppUtil.showToast(context, "Please fill in all required fields")
                        }
                        else -> {
                            authViewModel.login(email, password) { success, errorMessage ->
                                if (success) {
                                    onNavigateToMain()
                                } else {
                                    AppUtil.showToast(context, errorMessage ?: "Something went wrong")
                                }
                            }
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
                Text("Login", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing before divider

            DividerWithText("OR")

            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing after divider

            // Google Login
            LoginGoogle(onSuccess = onNavigateToMain)

            Spacer(modifier = Modifier.height(16.dp)) // Added bottom spacing


            TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        append("Don't have an account? ")
                        withStyle(SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )) {
                            append("Register")
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
fun DividerWithText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.Gray.copy(alpha = 0.3f)
        )
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color.Gray
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.Gray.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun LoginGoogle(
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
fun ForgotPasswordDialog(
    onDismiss: () -> Unit,
    onResetClicked: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Forgot Password", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        error = null
                    },
                    label = { Text("Enter your email") },
                    isError = error != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                if (error != null) {
                    Text(text = error!!, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (email.isBlank()) {
                    error = "Email cannot be empty"
                } else {
                    onResetClicked(email)
                }
            }) {
                Text("Reset Password")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
