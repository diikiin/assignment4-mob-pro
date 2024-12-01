package com.dikin.assignment4.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dikin.assignment4.retrofit.ApiResponse
import com.dikin.assignment4.retrofit.Product
import com.dikin.assignment4.ui.theme.Assignment4Theme
import com.dikin.assignment4.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = viewModel()
) {
    val productsState by productViewModel.productsState.observeAsState(ApiResponse.Loading)
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    when (productsState) {
        is ApiResponse.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is ApiResponse.Success -> {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = "Tasks List",
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )

                Button(
                    onClick = {
                        showDialog = true
                        selectedProduct = null
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Add Task")
                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items((productsState as ApiResponse.Success<List<Product>>).data) { product ->
                        ProductItem(
                            product = product,
                            onUpdate = {
                                selectedProduct = it
                                showDialog = true
                            },
                            onDelete = {
                                productViewModel.delete(it)
                            }
                        )
                    }
                }
            }

            if (showDialog) {
                AddOrUpdateProductDialog(
                    product = selectedProduct,
                    onCreate = { product ->
                        productViewModel.create(product)
                        showDialog = false
                    },
                    onUpdate = { task ->
                        productViewModel.update(task)
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }

        is ApiResponse.Error -> {
            Text(
                text = (productsState as ApiResponse.Error).message,
                color = Color.Red,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onUpdate: (Product) -> Unit,
    onDelete: (Product) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { showDialog = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    fontSize = 20.sp,
                    color = Color.Black,
                )
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Settings")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onUpdate(product)
                        },
                        text = { Text("Update") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onDelete(product)
                        },
                        text = { Text("Delete") }
                    )
                }
            }
        }
    }

    if (showDialog) {
        ProductDetailDialog(product = product, onDismiss = { showDialog = false })
    }
}

@Composable
fun ProductDetailDialog(product: Product, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Task Details") },
        text = {
            Column {
                Text(text = "Id: ${product.id}", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Title: ${product.title}", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Description: ${product.description}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Category: ${product.category}", fontSize = 16.sp)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun AddOrUpdateProductDialog(
    product: Product?,
    onCreate: (Product) -> Unit,
    onUpdate: (Product) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(product?.title ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Add/Update Task", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            if (title.isNotBlank() && description.isNotBlank()) {
                                if (product != null) {
                                    onUpdate(
                                        Product(
                                            product.id,
                                            title,
                                            description,
                                            product.category,
                                            product.price,
                                            product.discountPercentage,
                                            product.rating
                                        )
                                    )
                                } else {
                                    onCreate(Product(title = title, description = description))
                                }
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    Assignment4Theme {
        ProductListScreen()
    }
}
