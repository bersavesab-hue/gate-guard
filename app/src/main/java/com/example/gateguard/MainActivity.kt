package com.example.gateguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GateGuardPrototype()
        }
    }
}

private val Ink = Color(0xFF2D241C)
private val Paper = Color(0xFFF3E6C9)
private val PaperDark = Color(0xFFE3CF9E)
private val GateBrown = Color(0xFF5A3526)
private val GateRed = Color(0xFF8A3324)
private val Muted = Color(0xFF6F6256)

@Composable
fun GateGuardPrototype() {
    val traveler = remember {
        Traveler(
            name = "沈三郎",
            age = 31,
            origin = "河南府洛阳县",
            occupation = "药材商",
            passValid = true,
            wanted = false,
            hasContraband = false,
            baggageSummary = "干姜×2包、陈皮×1包、短刀×1"
        )
    }

    var searched by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("先观察，再决定是否询问或搜查。") }
    var resolved by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Paper)
                .verticalScroll(rememberScrollState())
        ) {
            Header()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DutyStrip()
                TravelerPanel(traveler, searched)
                PassPanel(traveler)
                StatusPanel(message)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        enabled = !resolved,
                        onClick = {
                            message = "沈三郎：小人从洛阳来，赶着午时前进城交药材。"
                        }
                    ) {
                        Text("询问")
                    }
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        enabled = !resolved,
                        onClick = {
                            searched = true
                            message = "你完成了基础搜查。注意：现在还没有自动提示对错。"
                        }
                    ) {
                        Text("搜查")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        enabled = !resolved,
                        colors = ButtonDefaults.buttonColors(containerColor = GateBrown),
                        onClick = {
                            resolved = true
                            message = if (DecisionEngine.expectedDecision(traveler) == Decision.ALLOW) {
                                "判定正确：沈三郎可以放行。第一轮测试通过。"
                            } else {
                                "判定错误：这个人其实不该放行。"
                            }
                        }
                    ) {
                        Text("放行")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        enabled = !resolved,
                        colors = ButtonDefaults.buttonColors(containerColor = GateRed),
                        onClick = {
                            resolved = true
                            message = if (DecisionEngine.expectedDecision(traveler) == Decision.ARREST) {
                                "判定正确：已将可疑人员控制。"
                            } else {
                                "判定错误：你抓了一个目前没有违规证据的人。"
                            }
                        }
                    ) {
                        Text("抓捕")
                    }
                }

                if (resolved) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            searched = false
                            resolved = false
                            message = "先观察，再决定是否询问或搜查。"
                        }
                    ) {
                        Text("重新测试这个NPC")
                    }
                }

                Text(
                    text = "骨架版 v0.1：暂时不放广告、不放图片、不做随机NPC。下一步只加素材与第二个NPC。",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GateBrown)
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Text(
            text = "城门值守",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
        Text(
            text = "南城门 · 辰时二刻 · 今日第 1 人",
            color = Color(0xFFECDCCB),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun DutyStrip() {
    Card(
        colors = CardDefaults.cardColors(containerColor = PaperDark),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MiniStat("治安", "72")
            MiniStat("功勋", "0")
            MiniStat("深查", "3/3")
            MiniStat("俸钱", "0文")
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, color = Ink, fontSize = 17.sp)
        Text(text = label, color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun TravelerPanel(traveler: Traveler, searched: Boolean) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E9)),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(width = 92.dp, height = 118.dp)
                        .background(Color(0xFFD8C39A), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFB89B68), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NPC\n立绘占位",
                        textAlign = TextAlign.Center,
                        color = Muted,
                        fontSize = 13.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(traveler.name, color = Ink, fontWeight = FontWeight.Bold, fontSize = 23.sp)
                    InfoLine("年龄", "${traveler.age}岁")
                    InfoLine("籍贯", traveler.origin)
                    InfoLine("身份", traveler.occupation)
                }
            }

            if (searched) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text("搜查所得", fontWeight = FontWeight.Bold, color = Ink)
                Text(
                    traveler.baggageSummary,
                    color = Muted,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row {
        Text("$label：", color = Muted, fontSize = 14.sp)
        Text(value, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PassPanel(traveler: Traveler) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8EBCF)),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.border(1.dp, Color(0xFFC5A870), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text("路引", fontWeight = FontWeight.Bold, color = GateRed, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("姓名：${traveler.name}", color = Ink)
            Text("籍贯：${traveler.origin}", color = Ink)
            Text("身份：${traveler.occupation}", color = Ink)
            Text("入城事由：交售药材", color = Ink)
            Text("印验：清晰", color = Ink)
        }
    }
}

@Composable
private fun StatusPanel(message: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9DEC5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = message,
            color = Ink,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(14.dp)
        )
    }
}
