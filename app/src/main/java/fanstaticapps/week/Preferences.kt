package fanstaticapps.week

import android.content.Context
import androidx.core.content.edit
import java.util.Calendar

private const val PREFS_NAME = "fanstaticapps.week.SemesterWeekAppWidget"
private const val PREF_INITIAL_VALUE_PREFIX_KEY = "appwidget_counter_ini_"
private const val PREF_SAVED_ON_WEEK_PREFIX_KEY = "appwidget_counter_week_"
private val WEEKS_IN_YEAR = Calendar.getInstance().getActualMaximum(Calendar.WEEK_OF_YEAR)

// Write the prefix to the SharedPreferences object for this widget
internal fun saveCounterPref(context: Context, appWidgetId: Int, text: String) {
    context.getSharedPreferences(PREFS_NAME, 0).edit {
        putInt(PREF_INITIAL_VALUE_PREFIX_KEY + appWidgetId, text.toIntOrNull() ?: 0)
        putInt(PREF_SAVED_ON_WEEK_PREFIX_KEY + appWidgetId, getCurrentCalendarWeek())
    }
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadCounterPref(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val initialValue = prefs.getInt(PREF_INITIAL_VALUE_PREFIX_KEY + appWidgetId, 0)
    val currentCalendarWeek = getCurrentCalendarWeek()
    val weekSavedOn = prefs.getInt(PREF_SAVED_ON_WEEK_PREFIX_KEY + appWidgetId, currentCalendarWeek)
    val passedWeeks = if (weekSavedOn > currentCalendarWeek) {
        WEEKS_IN_YEAR - weekSavedOn + currentCalendarWeek
    } else {
        currentCalendarWeek - weekSavedOn
    }
    return "${initialValue + passedWeeks}"
}

internal fun deleteCounterPref(context: Context, appWidgetId: Int) {
    context.getSharedPreferences(PREFS_NAME, 0).edit {
        remove(PREF_INITIAL_VALUE_PREFIX_KEY + appWidgetId)
    }
}

internal fun getCurrentCalendarWeek(): Int = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)