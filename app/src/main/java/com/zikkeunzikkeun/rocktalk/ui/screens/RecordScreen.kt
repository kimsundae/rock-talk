import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import com.zikkeunzikkeun.rocktalk.R
import com.zikkeunzikkeun.rocktalk.api.callGetRecordList
import com.zikkeunzikkeun.rocktalk.api.getUserInfo
import com.zikkeunzikkeun.rocktalk.data.RecordInfoData
import com.zikkeunzikkeun.rocktalk.data.UserInfoData
import com.zikkeunzikkeun.rocktalk.ui.components.CommonProgress
import com.zikkeunzikkeun.rocktalk.ui.components.RecordInfoDialog
import com.zikkeunzikkeun.rocktalk.util.getUserId


@Composable
fun RecordScreen(
    navController: NavController
) {
    var userInfo by remember { mutableStateOf<UserInfoData>(UserInfoData()) }
    var recordList by remember { mutableStateOf<List<RecordInfoData>>(emptyList()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var isLoading by remember { mutableStateOf(false) }

    Log.i("recordscreen", recordList.toString())
    LaunchedEffect(Unit) {
        isLoading = true
        val userId = getUserId()
        if (!userId.isNullOrEmpty()) {
            userInfo = getUserInfo(userId) ?: UserInfoData()
            val rawList = callGetRecordList(userInfo.userId)

            val localRecordList = rawList.map { record ->
                record.copy(createDate = record.getLocalDateString() ?: record.createDate)
            }
            recordList = localRecordList
        }
        isLoading = false
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8F1))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFD5DEB1))
                .padding(top = 32.dp, bottom = 12.dp)
        ) {
            Row(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${userInfo.nickname}님의 기록",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A6953)
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F8F1))
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(5) {
                Box(
                    Modifier
                        .size(18.dp, 10.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFD5DEB1))
                )
            }
        }

        CalendarMonthView(
            userInfo = userInfo,
            currentMonth = currentMonth,
            recordList = recordList,
            stoneRes = R.drawable.rock_icon,
            isLoading = isLoading,
            onPrevMonth = { currentMonth = currentMonth.minusMonths(1) },
            onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
        )
    }
}


@Composable
fun CalendarMonthView(
    userInfo: UserInfoData,
    currentMonth: YearMonth,
    recordList: List<RecordInfoData>,
    stoneRes: Int,
    isLoading: Boolean,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val daysOfWeek = listOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat")
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val totalDays = lastDayOfMonth.dayOfMonth
    val today = LocalDate.now()
    var isOpenRecordInfoModal by remember { mutableStateOf(false) }
    var selectedRecordInfo by remember { mutableStateOf<RecordInfoData?>(null) }
    val cellSize = 60.dp

    // recordList를 날짜별 Map으로 변환 (키: "YYYY-MM-DD")
    val recordInfoMap = remember(recordList) {
        recordList.associateBy { it.createDate.take(10) }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp, bottomStart = 36.dp, bottomEnd = 36.dp),
                ambientColor = Color(0x33000000),
                spotColor = Color(0x44000000)
            )
            .background(Color.White, RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrevMonth, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "이전달",
                    tint = Color(0xFFECB764)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    currentMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH).uppercase(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFECB764)
                )
                Spacer(Modifier.width(10.dp))
                Box(
                    Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFFECB764), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("${currentMonth.year}", fontSize = 16.sp, color = Color(0xFFECB764))
                }
            }
            IconButton(onClick = onNextMonth, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "다음달",
                    tint = Color(0xFFECB764)
                )
            }
        }
        val totalCells = ((startDayOfWeek + totalDays + 6) / 7) * 7
        Column {
            for (week in 0 until totalCells / 7) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(cellSize),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (d in 0..6) {
                        val dayNum = week * 7 + d - startDayOfWeek + 1
                        val date = if (dayNum in 1..totalDays) currentMonth.atDay(dayNum) else null

                        val recordInfoForDay = date?.let {
                            recordInfoMap[it.toString()]
                        }
                        Box(
                            Modifier
                                .size(cellSize)
                                .weight(1f)
                                .padding(vertical = 2.dp)
                                .clickable(enabled = date != null) {
                                    if (date != null) {
                                        selectedRecordInfo = recordInfoForDay ?: RecordInfoData(
                                            centerId = userInfo.centerId,
                                            userId = userInfo.userId ?: "",
                                            nickname = userInfo.nickname,
                                            createDate = date.toString()
                                        )
                                        isOpenRecordInfoModal = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (date != null) {
                                if (recordInfoForDay != null) {
                                    Image(
                                        painter = painterResource(id = stoneRes),
                                        contentDescription = "돌 아이콘",
                                        modifier = Modifier.size(36.dp)
                                    )
                                } else {
                                    Text(
                                        "${date.dayOfMonth}",
                                        fontSize = 14.sp,
                                        color = if (date == today) Color(0xFFF57C6F) else Color(0xFF5A6953),
                                        fontWeight = if (date == today) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    CommonProgress(isLoading = isLoading)
    // 기록 info 전달
    RecordInfoDialog(
        isEdit = selectedRecordInfo?.let { it.recordContent.isNotBlank() } == true,
        userInfo = userInfo,
        recordInfo = selectedRecordInfo,
        isShow = isOpenRecordInfoModal,
        onDismiss = { isOpenRecordInfoModal = false }
    )
}
