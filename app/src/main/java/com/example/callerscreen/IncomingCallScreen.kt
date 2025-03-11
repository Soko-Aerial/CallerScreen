package com.example.callerscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.airbnb.lottie.compose.*


@Composable
fun CallScreen(navController: NavHostController) {
    RingtoneHandler()
    val coroutineScope = rememberCoroutineScope()
    var showResponses by remember { mutableStateOf(false) }
    val predefinedMessages = listOf(
        "Can't talk now, call you later.",
        "I'm busy, text me.",
        "I'll call you back soon.",
        "In a meeting, talk later."
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {

        Text(
            "ALPHA KILO",
            fontSize = 24.sp,
            color = Color(0xFF9A9A00),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        )

        Lines()

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Decline Button (Left)
            DeclineButton(navController)

            // Central Message Button with Glowing Effect
            LottieMessageButton(onClick = { showResponses = true })

            // Accept Button (Right)
            AcceptButton(navController)

        }

        if (showResponses) {
            PredefinedMessageDialog(
                predefinedMessages,
                onDismiss = { showResponses = false }) { selectedMessage ->
                coroutineScope.launch {
                    // Simulate sending message instantly
                    println("Sent message: $selectedMessage")
                }
                showResponses = false
            }
        }
    }
}

@Composable
fun LottieMessageButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.glow))

    Box(
        modifier = modifier
            .wrapContentSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition,
            modifier = Modifier.size(150.dp),
            iterations = LottieConstants.IterateForever,
            reverseOnRepeat = true
        )
        Image(
            painter = painterResource(R.drawable.circular_message),
            contentDescription = "message",
            modifier = Modifier.size(70.dp)
        )
    }
}

@Composable
fun PredefinedMessageDialog(
    messages: List<String>,
    onDismiss: () -> Unit,
    onMessageSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {},
        text = {
            Column {
                messages.forEach { message ->
                    Text(
                        text = message,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMessageSelected(message) }
                            .padding(8.dp)
                    )
                }
            }
        }
    )
}


@Composable
private fun DeclineButton(navController: NavController) {
    PhoneCallButton(
        text = "Decline",
        color = Color.Red,
        icon = painterResource(R.drawable.call2),
        navController = navController
    )
}


@Composable
private fun AcceptButton(navController: NavController) {
    PhoneCallButton(
        text = "Accept",
        color = Color.Green,
        icon = painterResource(R.drawable.call1),
        navController = navController
    )
}

@Composable
private fun PhoneCallButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    icon: Painter,
    navController: NavController
) {
    Column(verticalArrangement = Arrangement.Center,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false, color = color),
            enabled = true,
            onClick = {
                if (text == "Accept"){
                    navController.navigate("answeredScreen")
                }
                else if(text == "Decline"){
                    navController.navigate("declineScreen")
                }
            }
        )) {
        Image(
            painter = icon,
            contentDescription = text,
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp)

        )
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun Lines() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val lineLength = 50f
        val strokeWidth = 4f
        val color = Color(0xFF9A9A00)

        // Top-left
        drawLine(
            color = color,
            start = Offset(0f, lineLength),
            end = Offset(lineLength, 0f),
            strokeWidth = strokeWidth
        )

        // Top-right
        drawLine(
            color = color,
            start = Offset(size.width - lineLength, 0f),
            end = Offset(size.width, lineLength),
            strokeWidth = strokeWidth
        )

        // Bottom-left
        drawLine(
            color = color,
            start = Offset(0f, size.height - lineLength),
            end = Offset(lineLength, size.height),
            strokeWidth = strokeWidth
        )

        // Bottom-right
        drawLine(
            color = color,
            start = Offset(size.width - lineLength, size.height),
            end = Offset(size.width, size.height - lineLength),
            strokeWidth = strokeWidth
        )
    }
}

