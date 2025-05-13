package com.example.fit5046a4

import android.graphics.Paint.Align
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults


@OptIn(ExperimentalMaterial3Api::class)

//UI SCREEN ONLY
@Composable
fun AddIngredientScreen() {
    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(), // full width so we can center
                        contentAlignment = Alignment.Center // center its content
                    ) {
                        Text(text = "Add Ingredients")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD7DEFB),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),

            ) {

            Spacer(modifier = Modifier.height(20.dp))

            val image: Painter = painterResource(R.drawable.grocery)
            Image(
                painter = image,
                contentDescription = "Fridge Image",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text("Ingredient:")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(10.dp)
            )

            Text("Quantity & Unit")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Qty") },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                    singleLine = true,


                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    }
                )

                OutlinedTextField(
                    value = "Select Unit",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Unit") },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    }
                )
            }

            Text("Unit Price")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("e.g. 2.50") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Text("Category:")
            OutlinedTextField(
                value = "Select Category",
                onValueChange = {}, readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Arrow"
                    )
                }
            )

            Text("Expiry Date:")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("DD/MM/YYYY") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFD0E8FF).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ElevatedButton(
                    onClick = {
                        Toast.makeText(context, "Save Ingredient", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Add Ingredients")
                }
            }
        }
    }
}
