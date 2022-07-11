package fanstaticapps.week

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import fanstaticapps.week.databinding.WeekCounterAppWidgetConfigureBinding
import java.util.*

/**
 * The configuration screen for the [WeekCounterAppWidget] AppWidget.
 */
class WeekCounterAppWidgetConfigureActivity : Activity() {
    private lateinit var binding: WeekCounterAppWidgetConfigureBinding

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        binding = WeekCounterAppWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Find the widget id from the intent.
        val appWidgetId = loadWidgetId()
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        binding.appwidgetText.setText(loadCounterPref(this, appWidgetId))

        binding.updateButton.setOnClickListener {
            updateWidget(appWidgetId)
        }
    }

    private fun updateWidget(appWidgetId: Int) {
        // When the button is clicked, store the string locally
        val widgetText = binding.appwidgetText.text.toString()
        saveCounterPref(this, appWidgetId, widgetText)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(this)
        updateCounterWidget(this, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        setResult(
            RESULT_OK,
            Intent().apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId) })
        finish()
    }

    private fun loadWidgetId(): Int {
        val extras = intent.extras
        return extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID
    }

}

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