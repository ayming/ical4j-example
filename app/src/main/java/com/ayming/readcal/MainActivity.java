package com.ayming.readcal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.filter.Rule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // read iCal from url
                Calendar calendar = null;
                try {
                    InputStream in = null;
                    in = new URL("http://www.1823.gov.hk/common/ical/en.ics").openStream();
                    CalendarBuilder builder = new CalendarBuilder();
                    calendar = builder.build(in);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserException e) {
                    e.printStackTrace();
                }
                // read iCal from String
                try {
                    String myCalendarString = null;
                    if (calendar != null) {
                        myCalendarString = calendar.toString();
                        StringReader sin = new StringReader(myCalendarString);
                        CalendarBuilder builder = new CalendarBuilder();
                        calendar = builder.build(sin);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserException e) {
                    e.printStackTrace();
                }
                if (calendar != null) {
                    /*Log.i(TAG, "Calendar: " + calendar);
                    PropertyList plist = calendar.getProperties();
                    for (Object object : plist.toArray()) {
                        Log.i(TAG, "prop: " + object);
                    }*/
                    // get the events in calendar
                    ComponentList events = calendar.getComponents(Component.VEVENT);
                    for (Object item : events) {
                        VEvent event = (VEvent) item;
                        Log.i(TAG, "event: "
                                + event.getSummary().getValue()
                                + " (" + event.getStartDate().getDate()
                                + " - " + event.getEndDate().getDate() + ")");
                    }
                    // filter the events with rules
                    Period period = new Period(new DateTime(new Date().getTime()), new Dur(60, 0, 0, 0));
                    Rule[] rules = {new PeriodRule(period)};
                    Filter filter = new Filter(rules, Filter.MATCH_ALL);
                    events = (ComponentList) filter.filter(events);
                    for (Object item : events) {
                        VEvent event = (VEvent) item;
                        Log.i(TAG, "filtered event: "
                                + event.getSummary().getValue()
                                + " (" + event.getStartDate().getDate()
                                + " - " + event.getEndDate().getDate() + ")");
                    }
                }
            }
        }).start();
    }
}
