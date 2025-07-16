package com.zikkeunzikkeun.rocktalk.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zikkeunzikkeun.rocktalk.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xFF81C784) else Color(0xFFDDDDDD),
        label = "borderColor"
    )

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 6.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rock_icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,  // 또는 ContentScale.Inside
            modifier = Modifier.weight(1f)
        )
        Text(
            label,
            modifier = Modifier
                .weight(2f)   // 1 / (1+1+8) = 10%
                .padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .height(48.dp)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .weight(7f)
                .background(Color(0xFFFFFFFF), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged { focusState -> isFocused = focusState.isFocused },
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = androidx.compose.ui.Alignment.CenterStart,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = label.ifEmpty { label },
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun InputFieldWithIcon(label: String, value: String, onValueChange: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = if (isFocused) Color(0xFF81C784) else Color(0xFFDDDDDD)

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .padding(vertical = 6.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rock_icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,  // 또는 ContentScale.Inside
            modifier = Modifier.weight(1f)
        )
        Text(
            label,
            modifier = Modifier
                .weight(2f)   // 1 / (1+1+8) = 10%
                .padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(7f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFFFF), RoundedCornerShape(8.dp))) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    modifier = Modifier
                        .fillMaxSize()
                        .onFocusChanged { focusState -> isFocused = focusState.isFocused },
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(Modifier.fillMaxWidth()) {
                            if (value.isEmpty()) Text(label, color = Color.Gray)
                            innerTextField()
                        }
                    }
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .size(24.dp)
                )
            }
            Divider(
                color = Color(0xFFBBBBBB),
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CommonRadioGroup(
    items: List<String>,
    selectedItem: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rock_icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.weight(1f)
        )
        Text(
            label,
            modifier = Modifier
                .weight(2f)
                .padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier.weight(6f)
        ) {
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onSelect(item) }
                ) {
                    RadioButton(
                        selected = (item == selectedItem),
                        onClick = { onSelect(item) }
                    )
                    Text(text = item)
                }
            }
        }
    }
}
