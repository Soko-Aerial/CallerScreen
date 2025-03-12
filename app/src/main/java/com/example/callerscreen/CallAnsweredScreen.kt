//package com.example.callerscreen
//
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//
//@Composable
//fun AnswerScreen(navController: NavHostController) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFEFEFEF))
//    ) {
//        // Title Text
//        Text(
//            "ALPHA KILO",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF9A9A00),
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 40.dp)
//        )
//
//        // Corner lines
//        Lines()
//
//        // Buttons on the right
//        Column(
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .padding(end = 20.dp),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            CameraButton()
//
//            EndCall(navController = navController)
//        }
//
//    }
//}
//
//@Composable
//fun CircularButton(icon: Int, backgroundColor: Color, onClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .size(70.dp)
//            .clip(CircleShape)
//            .background(backgroundColor)
//            .clickable { onClick() },
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = painterResource(id = icon),
//            contentDescription = null,
//            modifier = Modifier.size(40.dp)
//        )
//    }
//}
//
//
//@Composable
//fun CameraButton(modifier: Modifier = Modifier) {
//    CircularButton(
//        icon = R.drawable.camera,
//        backgroundColor = Color.LightGray,
//        onClick = {
//
//        })
//}
//
//@Composable
//fun EndCall(modifier: Modifier = Modifier,
//            navController: NavController
//) {
//    CircularButton(
//        icon = R.drawable.call2,
//        backgroundColor = Color.LightGray,
//        onClick = { navController.navigate("declineScreen") })
//}
//
//
