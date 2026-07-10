package com.example.restaurantmenu.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Static "About Us" screen. Later this content can also be pulled from
 * Firestore (e.g. an "about" document) the same way menu data is.
 */
@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Biz haqimizda",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Restoranimiz mazali va sifatli taomlar bilan mijozlarimizga xizmat " +
                "ko'rsatib kelmoqda. Pizza, lavash, burger, donar va turli ichimliklar " +
                "assortimenti bilan tanishing.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "📍 Manzil: (kiritilishi kerak)\n📞 Telefon: (kiritilishi kerak)\n🕐 Ish vaqti: 09:00 - 23:00",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
