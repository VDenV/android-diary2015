package ua.org.sedu.diary.anroiddiary.activity;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.exception.MessageDialogException;
import ua.org.sedu.diary.anroiddiary.model.DayLesson;
import ua.org.sedu.diary.anroiddiary.model.DayOfWeek;
import ua.org.sedu.diary.anroiddiary.model.DaySchedule;
import ua.org.sedu.diary.anroiddiary.util.DefaultAsyncTask;
import ua.org.sedu.diary.anroiddiary.util.Logger;
import ua.org.sedu.diary.anroiddiary.util.MessageDialogAsyncCallback;
import ua.org.sedu.diary.anroiddiary.util.TouchEventRedirector;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * @author voinovdenys
 */
public class DiaryScreen extends AbstractDiaryActivity {

    public static final String PUPIL_NAME = "pupilName";
    public static final String CHILD_ID = "childId";
    public static final String PUPIL_CLASS = "pupilClass";
    public static final int INITIAL_YEAR = 2012;

    private int selectedWeek;
    private int selectedYear;

    private ViewFlipper viewFlipper;

    private View diaryScreen;
    private View diaryScreen2;

    private DiaryGestureListener diaryGestureListener;
    private GestureDetector detector;

    private List<DaySchedule> weekSchedule;
    private Map<String, String> weeks;

    private String currentWeek;

    @Override
    protected void init() {
        resetWeeksMap();
        resetWeekSchedule();

        initGestureListener();

        initScreens();
        initPupilFieldsForScreens();
        addTouchListeners();

        initYearSpinner(diaryScreen);
        initYearSpinner(diaryScreen2);

        loadWeeks(years().get(selectedYear));
    }

    private void resetWeeksMap() {
        weeks = new LinkedHashMap<String, String>();
    }

    private void resetWeekSchedule() {
        weekSchedule = new ArrayList<DaySchedule>();
    }

    private void initGestureListener() {
        diaryGestureListener = new DiaryGestureListener(this);
        detector = new GestureDetector(this, diaryGestureListener);
    }

    private void loadWeeks(final String year) {
        new DefaultAsyncTask(createProgressBar(), new MessageDialogAsyncCallback<String>(this) {

            @Override
            public String doInBackground() throws MessageDialogException {
                return getRemoteCallService().getWeeks(year);
            }

            @Override
            public void onSuccess(String weeksJson) {
                populateWeeksMap(weeksJson);
                initWeekSpinnerForScreens();
            }

        }).execute();
    }

    private void populateWeeksMap(String weeksJson) {
        resetWeeksMap();
        try {
            JSONArray weeksJsonArray = getJsonDataArray(weeksJson);
            for (int i = 0; i < weeksJsonArray.length(); i++) {
                JSONObject week = weeksJsonArray.getJSONObject(i);
                weeks.put(week.getString("weekLabel"), week.getString("weekNumber"));
                markCurrentWeek(i, week);
            }
        } catch (JSONException e) {
            showJsonErrorDialog(e);
        }
    }

    private void markCurrentWeek(int weekIndex, JSONObject week) throws JSONException {
        if (week.getBoolean("current")) {
            selectedWeek = weekIndex;
        }
    }

    private void initScreens() {
        viewFlipper = getViewFlipper(R.id.diaryViewSwitcher);
        diaryScreen = findViewById(R.id.diaryScreen);
        diaryScreen2 = findViewById(R.id.diaryScreen2);
    }

    private static int getCurrentEduYear() {
        return getEduYear(new DateTime().getYear());
    }

    private static int getEduYear(int realYear) {
        DateTime dateTime = new DateTime().withYear(realYear);

        int year = dateTime.getYear();
        if (dateTime.getMonthOfYear() >= DateTimeConstants.SEPTEMBER) {
            return year;
        }

        return year - 1;
    }

    private void initWeekSpinnerForScreens() {
        initWeekSpinner(diaryScreen);
        initWeekSpinner(diaryScreen2);

//        initYearSpinner(diaryScreen);
//        initYearSpinner(diaryScreen2);
    }

    private void initWeekSpinner(View screen) {
        Spinner spinner = getSpinner(screen, R.id.weekSelection);
        weekListener(spinner, createWeeksAdapter());
        spinner.setSelection(selectedWeek);
    }

    private void initYearSpinner(View screen) {
        Spinner spinner = getSpinner(screen, R.id.yearSelection);
        yearListener(spinner, createYearsAdapter());
        spinner.setSelection(selectedYear);
    }

    private ArrayAdapter<String> createWeeksAdapter() {
        return createSimpleAdapter(getWeeks());
    }

    private ArrayAdapter<String> createYearsAdapter() {
        return createSimpleAdapter(new ArrayList<String>(years().values()));
    }

    private Map<Integer, String> years() {
        Map<Integer, String> yearsMap = new HashMap<Integer, String>();
        int step = 0;
        for (int year = getCurrentEduYear(); year >= INITIAL_YEAR; year--) {
            yearsMap.put(step++, String.valueOf(year));
        }
        return yearsMap;
    }

    private void weekListener(final Spinner spinner, final ArrayAdapter<String> adapter) {
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weekComboSelected(adapter, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    private void yearListener(final Spinner spinner, final ArrayAdapter<String> adapter) {
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearComboSelected(adapter, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    private ArrayList<String> getWeeks() {
        return new ArrayList<String>(weeks.keySet());
    }

    private void weekComboSelected(final ArrayAdapter<String> adapter, int position) {
        selectedWeek = position;
        String week = adapter.getItem(position);
        if (!StringUtils.equals(currentWeek, week)) {
            loadWeekSchedule(week);
        }
    }

    private void yearComboSelected(final ArrayAdapter<String> adapter, int position) {
        if (position != selectedYear) {
            selectedYear = position;
            String year = adapter.getItem(position);
            loadWeeks(year);
        }
    }

    private void initPupilFieldsForScreens() {
        setPupilNameAndClass(diaryScreen);
        setPupilNameAndClass(diaryScreen2);
    }

    private void setPupilNameAndClass(View screen) {
        setPupilField(screen, R.id.pupilName, PUPIL_NAME);
        setPupilField(screen, R.id.pupilClass, PUPIL_CLASS);
    }

    private void setPupilField(View screen, int fieldId, String fieldValueConstant) {
        TextView pupilField = getTextView(screen, fieldId);
        String pupilFieldValue = getIntentParam(fieldValueConstant);
        pupilField.setText(pupilFieldValue);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.diary_view_switcher;
    }

    private void loadWeekSchedule(final String weekLabel) {
        new DefaultAsyncTask(createProgressBar(), new MessageDialogAsyncCallback<String>(this) {

            @Override
            public String doInBackground() throws MessageDialogException {
                Logger.info("Loading week schedule for week: " + weekLabel);
                return getChildWeekScheduleJson(weekLabel);
            }

            @Override
            public void onSuccess(String weekScheduleJson) {
                populateWeekSchedule(weekScheduleJson);
                DayOfWeek currentDayOfWeek = getCurrentDayOfWeek();
                diaryGestureListener.updateCurrentDay(currentDayOfWeek);
                selectDaySchedule(viewFlipper.getCurrentView(), currentDayOfWeek);
                currentWeek = weekLabel;
            }

        }).execute();
    }

    private String getChildWeekScheduleJson(String weekLabel) throws MessageDialogException {
        Logger.info("Loading child data.");
        return getRemoteCallService().getChildData(getIntentParam(CHILD_ID), weeks.get(weekLabel), years().get(selectedYear));
    }

    private void populateWeekSchedule(String weekScheduleJson) {
        resetWeekSchedule();
        populateParsedWeekSchedule(weekScheduleJson);
    }

    private void populateParsedWeekSchedule(String weekScheduleJson) {
        try {
            Logger.info("Populating week schedule.");
            JSONArray weekScheduleJsonArray = getJsonDataArray(weekScheduleJson);
            for (int i = 0; i < weekScheduleJsonArray.length(); i++) {
                addParsedDaySchedule(weekScheduleJsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            showJsonErrorDialog(e);
        }
    }

    private void addParsedDaySchedule(JSONObject dayScheduleJson) throws JSONException {
        DaySchedule daySchedule = DaySchedule.fromJson(dayScheduleJson);
        weekSchedule.add(daySchedule);
    }

    private DayOfWeek getCurrentDayOfWeek() {
        Logger.info("Getting current day of week.");
        for (DaySchedule daySchedule : weekSchedule) {
            if (daySchedule.isToday()) {
                return DayOfWeek.get(daySchedule.getDayOfWeek());
            }
        }
        return DayOfWeek.MONDAY;
    }

    private void addTouchListeners() {
        TouchEventRedirector touchEventRedirector = new TouchEventRedirector(this);
        getViewFromParent(diaryScreen, R.id.daySchedule).setOnTouchListener(touchEventRedirector);
        getViewFromParent(diaryScreen2, R.id.daySchedule).setOnTouchListener(touchEventRedirector);
    }

    private void selectDaySchedule(View screen, DayOfWeek selectedDay) {
        DaySchedule daySchedule = getDaySchedule(selectedDay);

        addDayScheduleToListView(screen, daySchedule.getDayLessons());

        updateDayOfWeekLabel(screen, selectedDay, daySchedule);

        if (daySchedule.isEmpty()) {
            showDialog(R.string.NO_SCHEDULE_FOUND, R.string.NO_SCHEDULE_MSG);
        }
    }

    private DaySchedule getDaySchedule(DayOfWeek selectedDay) {
        int index = selectedDay.getIndex() - 2;
        if (weekSchedule.size() > index) {
            return weekSchedule.get(index);
        }
        return DaySchedule.emptySchedule(selectedDay.getIndex());
    }

    private void addDayScheduleToListView(View screen, List<DayLesson> dayLessons) {
        ListView dayScheduleView = getListView(screen, R.id.daySchedule);
        dayScheduleView.setAdapter(new DiaryListViewAdapter(dayLessons, this));
    }

    private void updateDayOfWeekLabel(View screen, DayOfWeek selectedDay, DaySchedule daySchedule) {
        TextView dayOfWeek = getTextView(screen, R.id.dayOfWeek);
        dayOfWeek.setText(getDayAndDateLabel(selectedDay, daySchedule));
    }

    private String getDayAndDateLabel(DayOfWeek selectedDay, DaySchedule daySchedule) {
        return getString(selectedDay.getShortNameId()) + getDateLabel(daySchedule.getDate());
    }

    private String getDateLabel(String date) {
        return StringUtils.isNotEmpty(date) ? ", " + date : "";
    }

    public void switchView(DayOfWeek selectedDay) {
        View nextView = findViewById(getNextDiaryViewId());
        selectDaySchedule(nextView, selectedDay);
        setSelectedWeek(nextView);
        setSelectedYear(nextView);
        viewFlipper.showNext();
    }

    private int getNextDiaryViewId() {
        return viewFlipper.getCurrentView() == diaryScreen ? R.id.diaryScreen2 : R.id.diaryScreen;
    }

    private void setSelectedWeek(View screen) {
        Spinner spinner = getSpinner(screen, R.id.weekSelection);
        spinner.setSelection(selectedWeek);
    }

    private void setSelectedYear(View screen) {
        Spinner spinner = getSpinner(screen, R.id.yearSelection);
        spinner.setSelection(selectedYear);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        detector.onTouchEvent(me);
        getViewFromParent(viewFlipper.getCurrentView(), R.id.daySchedule).onTouchEvent(me);
        return super.onTouchEvent(me);
    }

    /**
     * Event when home task button is pressed.
     */
    public void showHomeTask(View homeTaskImage) {
        showDialog(R.string.HOME_TASK_WINDOW_TITLE, (String) homeTaskImage.getTag(), R.drawable.footer_homework);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                getLoginService().logout();
                break;
        }
        return true;
    }

    @Override
    protected Integer getOptionsMenuLayoutId() {
        return R.menu.diary_screen_menu;
    }

}
