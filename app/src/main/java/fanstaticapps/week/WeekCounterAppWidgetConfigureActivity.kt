package fanstaticapps.week

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import fanstaticapps.week.databinding.ActivityWeekCounterAppWidgetConfigureBinding

/**
 * The configuration screen for the [WeekCounterAppWidget] AppWidget.
 */
class WeekCounterAppWidgetConfigureActivity : Activity() {
    private lateinit var binding: ActivityWeekCounterAppWidgetConfigureBinding

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        binding = ActivityWeekCounterAppWidgetConfigureBinding.inflate(layoutInflater)
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
        appWidgetManager.updateWidget(widgetText, this, appWidgetId)

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