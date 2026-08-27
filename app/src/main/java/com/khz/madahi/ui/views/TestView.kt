package com.khz.madahi.ui.views

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.R
import com.khz.madahi.ui.theme.MadahiTheme
import com.khz.madahi.ui.theme.PrimaryGreenDark

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TestView() {
    Scaffold(
        topBar = {
        
        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = {},
                containerColor = Color(0xFF074634),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(70.dp)
                    .border(
                        width = 3.dp,
                        color = Color(0xFFD4AF37),  // طلایی برای حاشیه
                        shape = CircleShape
                    )
            ) {
                Image(
                    painter = painterResource(
                        R.drawable.ic_fab_border // ✅ تصویر دارک
                    ),
                    contentDescription = "Splash Background",
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.FillBounds

                )
                Icon(
                    imageVector = Icons.Default.Add  // 🤍 خالی
                    ,
                    contentDescription = "افزودن به علاقه‌مندی‌ها",
                    modifier = Modifier.size(36.dp),
                    tint = Color.White
                )
            }

        },
    ) {

    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TestViewPreviewDark() {
    MadahiTheme(darkTheme = true) {
        TestView()
    }
}