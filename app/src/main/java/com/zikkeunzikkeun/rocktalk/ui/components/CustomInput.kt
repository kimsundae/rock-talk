package com.zikkeunzikkeun.rocktalk.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zikkeunzikkeun.rocktalk.R

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(vertical = 4.dp)
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
        Column(modifier = Modifier.weight(7f)) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) Text(label, color = Color.Gray)
                        innerTextField()
                    }
                }
            )
            Divider(
                color = Color(0xFFBBBBBB),
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun InputFieldWithIcon(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(48.dp)
            .padding(vertical = 4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rock_icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,  // 또는 ContentScale.Inside
            modifier = Modifier.size(24.dp).weight(1f)
        )
        Text(
            label,
            modifier = Modifier
                .weight(2f)   // 1 / (1+1+8) = 10%
                .padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.fillMaxWidth().weight(7f)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 40.dp), // 오른쪽 아이콘 영역 확보
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
