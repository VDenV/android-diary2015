/**
 *
 */
package ua.org.sedu.diary.anroiddiary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author voinovdenys
 */
public class DaySchedule implements Serializable, Comparable<DaySchedule> {

    private static final long serialVersionUID = 1L;

    private final int dayOfWeek;

    private final boolean isToday;

    private final String date;

    private final List<DayLesson> dayLessons;

    private DaySchedule(int dayOfWeek, boolean isToday, List<DayLesson> dayLessons, String date) {
        this.dayOfWeek = dayOfWeek;
        this.isToday = isToday;
        this.dayLessons = dayLessons;
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public boolean isToday() {
        return isToday;
    }

    public List<DayLesson> getDayLessons() {
        return dayLessons;
    }

    public void sortLessons() {
        Collections.sort(dayLessons);
    }

    @Override
    public int compareTo(DaySchedule other) {
        return Integer.valueOf(dayOfWeek).compareTo(other.dayOfWeek);
    }

    public static DaySchedule fromJson(JSONObject dayScheduleJson) throws JSONException {
        List<DayLesson> lessons = getLessonsFromJson(dayScheduleJson);
        return new DaySchedule(dayScheduleJson.getInt("dayOfWeek"), dayScheduleJson.getBoolean("today"), lessons, dayScheduleJson.getString("date"));
    }

    public String getDate() {
        return date;
    }

    private static List<DayLesson> getLessonsFromJson(JSONObject dayScheduleJson) throws JSONException {
        List<DayLesson> lessons = new ArrayList<DayLesson>();
        JSONArray lessonJsonArray = dayScheduleJson.getJSONArray("dayLessons");
        for (int i = 0; i < lessonJsonArray.length(); i++) {
            JSONObject dayLessonJson = lessonJsonArray.getJSONObject(i);
            lessons.add(DayLesson.fromJson(dayLessonJson));
        }
        return lessons;
    }

    public static DaySchedule emptySchedule(int dayOfWeek) {
        return new DaySchedule(dayOfWeek, false, Collections.<DayLesson> emptyList(), "");
    }

    public boolean isEmpty() {
        return dayLessons.isEmpty();
    }

}
