package fanstaticapps.week

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews

abstract class BaseWeekWidget : AppWidgetProvider() {
    abstract fun Context.getWeekForWidgetId(appWidgetId: Int): String

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val week = context.getWeekForWidgetId(appWidgetId)

            println("Updating ${this.javaClass.simpleName} with $week")
            appWidgetManager.updateWidget(week, context, appWidgetId)
        }
    }
}

class CalendarWeekWidget : BaseWeekWidget() {
    override fun Context.getWeekForWidgetId(appWidgetId: Int) = getCurrentCalendarWeek().toString()
}

class WeekCounterAppWidget : BaseWeekWidget() {

    override fun Context.getWeekForWidgetId(appWidgetId: Int) = loadCounterPref(this, appWidgetId)

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        appWidgetIds.forEach { deleteCounterPref(context, it) }
    }
}

fun AppWidgetManager.updateWidget(
    week: String,
    context: Context,
    appWidgetId: Int
) {
    val firstNumber = week.first().toString()
    val secondNumber = week.getOrNull(1)
    val visibility2Nr = if (secondNumber == null) View.GONE else View.VISIBLE
    val views = RemoteViews(context.packageName, R.layout.widget_calendar_week)

    views.setTextViewText(R.id.calendarWeekNr1Tv, firstNumber)
    views.setTextViewText(R.id.calendarWeekNr2Tv, secondNumber.toString())
    views.setViewVisibility(R.id.calendarWeekNr2Tv, visibility2Nr)

    updateAppWidget(appWidgetId, views)
}