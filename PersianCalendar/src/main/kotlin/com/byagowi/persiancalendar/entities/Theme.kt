package com.byagowi.persiancalendar.entities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.PowerManager
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.byagowi.persiancalendar.PREF_THEME
import com.byagowi.persiancalendar.R
import com.byagowi.persiancalendar.utils.appPrefs

enum class Theme(val key: String, @StringRes val title: Int, @StyleRes private val styleRes: Int) {
    SYSTEM_DEFAULT("SystemDefault", R.string.theme_default, R.style.LightTheme),
    LIGHT("LightTheme", R.string.theme_light, R.style.LightTheme),
    DARK("DarkTheme", R.string.theme_dark, R.style.DarkTheme),
    MODERN("ClassicTheme"/*legacy*/, R.string.theme_modern, R.style.ModernTheme),
    BLUE("BlueTheme", R.string.theme_blue, R.style.BlueTheme),
    BLACK("BlackTheme", R.string.theme_black, R.style.BlackTheme);

    companion object {
        private val SharedPreferences?.theme
            get() = this?.getString(PREF_THEME, null) ?: SYSTEM_DEFAULT.key

        fun apply(activity: AppCompatActivity) = activity.setTheme(getCurrent(activity).styleRes)

        fun getCurrent(context: Context): Theme {
            val key = context.appPrefs.theme
            return values().find { it.key == key }?.takeIf { it != SYSTEM_DEFAULT } ?: when {
                isPowerSaving(context) -> BLACK
                isNightModeEnabled(context) -> DARK
                else -> LIGHT
            }
        }

        fun isNonDefault(appPrefs: SharedPreferences?) = appPrefs.theme != SYSTEM_DEFAULT.key

        fun isNightModeEnabled(context: Context): Boolean =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        private fun isPowerSaving(context: Context): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    context.getSystemService<PowerManager>()?.isPowerSaveMode == true
        }
    }
}
