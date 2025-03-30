package com.example.kot_trip.utils

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object GeminiHelper {

    fun fetchDailyTipForCountry(country: String, callback: (String) -> Unit) {
        val model = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = ""

        )

        val prompt = "אני נוסע ל-$country. תן לי אטרקציה שחובה לראות או טיפים שאסור לפספס, במשפט אחד."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = model.generateContent(prompt = prompt)
                val answer = response.text ?: "לא התקבלה תשובה"
                withContext(Dispatchers.Main) {
                    callback(answer)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback("שגיאה: ${e.message}")
                }
            }
        }
    }
}
