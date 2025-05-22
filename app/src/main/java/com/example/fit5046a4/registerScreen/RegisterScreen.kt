package com.example.fit5046a4.registerScreen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.fit5046a4.R
import com.example.fit5046a4.firebaseAuth.AuthViewModel
import com.example.fit5046a4.firebaseAuth.GoogleSignInUtils
import com.example.fit5046a4.loginScreen.DividerWithText

@Composable
fun RegisterScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTerms: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isTermsAccepted by remember { mutableStateOf(false) }
    var context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
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
            // App title section
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
            }

            // Registration form section
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

                // Email input
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

                // Password input with validation feedback
                LabelWithAsterisk("Password")
                PasswordInputField(
                    password,
                    passwordVisible,
                    { password = it },
                    { passwordVisible = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm password field with match check
                LabelWithAsterisk("Confirm Password")
                ConfirmPasswordInputField(
                    confirmPassword = confirmPassword,
                    password = password,
                    visible = passwordVisible,
                    onValueChange = { confirmPassword = it },
                    onToggleVisibility = { passwordVisible = it }
                )
            }

            // Checkbox for Terms of Service
            TermsCheckbox(
                checked = isTermsAccepted,
                onCheckedChange = { isTermsAccepted = it },
                onNavigateToTerms = onNavigateToTerms,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Register button with validation logic
            FilledTonalButton(
                onClick = {
                    val passwordRules = validatePassword(password)
                    val isPasswordValid = passwordRules.all { it.isValid }

                    when {
                        email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            AppUtil.showToast(context, "Please fill in all required fields")
                        }
                        !isPasswordValid -> {
                            AppUtil.showToast(context, "Password does not meet requirements")
                        }
                        password != confirmPassword -> {
                            AppUtil.showToast(context, "Passwords do not match")
                        }
                        !isTermsAccepted -> {
                            AppUtil.showToast(context, "You must accept the Terms of Service")
                        }
                        else -> {
                            authViewModel.register(email, password) { success, errorMessage ->
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
                Text("Register", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing before divider

            DividerWithText("OR")

            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing after divider

            // Google Sign-In button
            RegisterGoogle(onSuccess = onNavigateToMain)

            Spacer(modifier = Modifier.height(16.dp)) // Added bottom spacing

            // Navigation to login screen
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

// Helper class to store and display password validation results
data class PasswordRule(val description: String, val isValid: Boolean)

// Rules: length, case, digit, symbol
fun validatePassword(password: String): List<PasswordRule> {
    return listOf(
        PasswordRule("At least 8 characters", password.length >= 8),
        PasswordRule("At least one uppercase letter", password.any { it.isUpperCase() }),
        PasswordRule("At least one lowercase letter", password.any { it.isLowerCase() }),
        PasswordRule("At least one digit", password.any { it.isDigit() }),
        PasswordRule("At least one special character", password.any { !it.isLetterOrDigit() })
    )
}

// Composable to add red asterisk to required labels
@Composable
fun LabelWithAsterisk(label: String) {
    Text(buildAnnotatedString {
        append(label)
        withStyle(SpanStyle(color = Color.Red)) { append(" *") }
    })
}

// Password input field with live rule checking
@Composable
fun PasswordInputField(
    value: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: (Boolean) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val passwordRules = validatePassword(value)
    val isValid = passwordRules.all { it.isValid }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Enter your password") },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        isError = isFocused && !isValid,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onToggleVisibility(!visible) }) {
                Icon(
                    painter = painterResource(
                        if (visible) R.drawable.ic_visibility
                        else R.drawable.ic_visibility_off
                    ),
                    contentDescription = if (visible) "Hide password" else "Show password"
                )
            }
        }
    )

    if (isFocused) {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            passwordRules.forEach { rule ->
                Text(
                    text = (if (rule.isValid) "✅ " else "❌ ") + rule.description,
                    color = if (rule.isValid) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Confirm password field with match validation
@Composable
fun ConfirmPasswordInputField(
    confirmPassword: String,
    password: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: (Boolean) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val doPasswordsMatch = confirmPassword == password && confirmPassword.isNotEmpty()

    OutlinedTextField(
        value = confirmPassword,
        onValueChange = onValueChange,
        label = { Text("Confirm your password") },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        isError = isFocused && !doPasswordsMatch,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onToggleVisibility(!visible) }) {
                Icon(
                    painter = painterResource(
                        if (visible) R.drawable.ic_visibility
                        else R.drawable.ic_visibility_off
                    ),
                    contentDescription = if (visible) "Hide password" else "Show password"
                )
            }
        }
    )

    if (isFocused && !doPasswordsMatch) {
        Text(
            text = "❌ Passwords do not match",
            color = Color(0xFFD32F2F),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// Google Sign-In button logic
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

// Terms of Service checkbox with navigation
@Composable
fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onNavigateToTerms: () -> Unit,
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
            modifier = Modifier.clickable {
                onCheckedChange(!checked)
                onNavigateToTerms()
            }
        )
    }
}