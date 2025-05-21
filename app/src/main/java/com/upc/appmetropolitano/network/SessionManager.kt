package com.upc.appmetropolitano.network

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFS_NAME       = "app_session"
    private const val KEY_USER_ID      = "user_id"
    private const val KEY_EMAIL    = "user_email"
    private const val KEY_FIRTS_NAME    = "user_firtsname"
    private const val KEY_FULL_NAME    = "user_fullname"
    private const val KEY_DOCUMENT_NUMBER    = "user_document"
    private const val KEY_PHONE    = "user_phone"
    private const val KEY_DEFAULT_CARD = "default_card_id"
    private const val KEY_CARD_NUMBER = "card_number"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(
        ctx: Context,
        userId: Int,
        email: String,
        firtsName: String,
        fullName: String,
        documentNumber: String,
        phone: String,
        cardNumber: String,
        defaultCardId: Int
    ) {
        prefs(ctx).edit()
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_EMAIL, email)
            .putString(KEY_FIRTS_NAME, firtsName)
            .putString(KEY_FULL_NAME, fullName)
            .putString(KEY_DOCUMENT_NUMBER, documentNumber)
            .putString(KEY_PHONE, phone)
            .putString(KEY_CARD_NUMBER, cardNumber)
            .putInt(KEY_DEFAULT_CARD, defaultCardId)
            .apply()
    }

    fun clearSession(ctx: Context) {
        prefs(ctx).edit().clear().apply()
    }

    val isLoggedIn: Boolean
        get() = false

    fun getUserId(ctx: Context) = prefs(ctx).getInt(KEY_USER_ID, -1)
    fun getEmail(ctx: Context) = prefs(ctx).getString(KEY_EMAIL, "") ?: ""
    fun getFirtsName(ctx: Context) = prefs(ctx).getString(KEY_FIRTS_NAME, "") ?: ""
    fun getFullName(ctx: Context) = prefs(ctx).getString(KEY_FULL_NAME, "") ?: ""
    fun getDocumentNumber(ctx: Context) = prefs(ctx).getString(KEY_DOCUMENT_NUMBER, "") ?: ""
    fun getPhone(ctx: Context) = prefs(ctx).getString(KEY_PHONE, "") ?: ""
    fun getCardNumber(ctx: Context) = prefs(ctx).getString(KEY_CARD_NUMBER, "") ?: ""
    fun getDefaultCardId(ctx: Context) = prefs(ctx).getInt(KEY_DEFAULT_CARD, -1)
}
