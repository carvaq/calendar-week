package fanstaticapps.week

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class CalendarWeekWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        // There may be multiple widgets active, so update all of them
        val week = getCurrentCalendarWeek().toString()
        for (appWidgetId in appWidgetIds) {
            println("Updating calendar week widget with $week")
            updateAppWidget(context, appWidgetManager, appWidgetId, week)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        week: String
    ) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.widget_calendar_week)
        views.setTextViewText(R.id.calendarWeekNrTv, week)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

internal fun getCurrentCalendarWeek(): Int = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)