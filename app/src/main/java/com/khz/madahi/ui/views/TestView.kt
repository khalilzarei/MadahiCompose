package com.khz.madahi.ui.views

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khz.madahi.R
import com.khz.madahi.ui.common.DecorativeCorners
import com.khz.madahi.ui.theme.Gold
import com.khz.madahi.ui.theme.MadahiTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TestView(
    isDialog: Boolean
) {
    Scaffold(
        topBar = {
            Text("Top")
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

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xff212121)),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(
                            R.drawable.item_back // ✅ تصویر دارک
                        ),
                        contentDescription = "Splash Background",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp)
                            .padding(horizontal = 25.dp),
                    ) {
                        Row {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = "title",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Gold,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                )

                                Text(
                                    text = "title",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Gold,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ModeEdit,
                                        contentDescription = "ویرایش",
                                        tint = Gold,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "حذف",
                                        tint = Gold,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }


            // تعریف رنگ‌ها
            val Gold = Color(0xFFD4AF37)
            val Background = Color(0xFF1A1A2E)
            val TitleColor = Color(0xFFFFFFFF)
            val BodyColor = Color(0xFFB8B8B8)
            val RedColor = Color(0xFFE74C3C)
            val DarkPurple = Color(0xFF2D2D44)

            val canManage = true

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 5.dp,
                        bottom = 5.dp
                    )
                    .height(92.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Background
                ),
                border = BorderStroke(
                    1.5.dp,
                    Gold
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { }) {
                    // گوشه‌های تزئینی طلایی
                    DecorativeCorners()

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // دایره کتاب
                        Box(
                            modifier = Modifier.size(42.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // نقطه‌های تزئینی در ۴ جهت
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Gold)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Gold)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Gold)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Gold)
                            )

                            // دایره اصلی
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(DarkPurple)
                                    .border(
                                        width = 2.dp,
                                        color = Gold,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_book),
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }

                        // عنوان و توضیحات
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp),
                        ) {
                            Text(
                                text = "category.title",
                                style = MaterialTheme.typography.titleLarge,
                                color = TitleColor,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                            )
                            if (true) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "category.description",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BodyColor,
                                    maxLines = 1,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // دکمه‌های ویرایش/حذف
                        if (canManage) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ModeEdit,
                                        contentDescription = "ویرایش",
                                        tint = Gold,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "حذف",
                                        tint = RedColor,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.width(40.dp))
                        }
                    }
                }
            }
        }
    }
}

//@Preview(
//    name = "Delete Dialog - Light",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun TestViewPreviewLight() {
//    MadahiTheme(darkTheme = false) {
//        TestView(isDialog = true)
//    }
//}
//
@Preview(
    name = "Delete Dialog - Light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TestViewPreviewDark() {
    MadahiTheme(darkTheme = false) {
        TestView(isDialog = false)
    }
}