/**
 *
 */
package ua.org.sedu.diary.anroiddiary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.org.sedu.diary.anroiddiary.util.CollectionUtils;

/**
 * @author voinovdenys
 */
public class DayLesson implements Serializable, Comparable<DayLesson> {

    private static final long serialVersionUID = 1L;

    private final int lessonNum;

    private final List<String> subjects;

    private final List<RecordMark> marks;

    private final String homeTask;

    private DayLesson(int lessonNum, List<String> subjects, String homeTask, List<RecordMark> marks) {
        this.lessonNum = lessonNum;
        this.subjects = subjects;
        this.homeTask = homeTask;
        this.marks = marks;
    }

    public int getLessonNum() {
        return lessonNum;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<RecordMark> getMarks() {
        return marks;
    }

    public String getHomeTask() {
        return homeTask;
    }

    @Override
    public int compareTo(DayLesson lesson) {
        return Integer.valueOf(lessonNum).compareTo(lesson.getLessonNum());
    }

    public static DayLesson fromJson(JSONObject dayLessonJson) throws JSONException {
        List<String> subjects = getSubjectsFromJson(dayLessonJson);
        List<RecordMark> marks = getMarksFromJson(dayLessonJson);
        return new DayLesson(dayLessonJson.getInt("lessonNum"), subjects, dayLessonJson.getString("homeTask"), marks);
    }

    private static List<RecordMark> getMarksFromJson(JSONObject dayLessonJson) throws JSONException {
        List<RecordMark> marks = new ArrayList<RecordMark>();
        JSONArray marksJsonArray = dayLessonJson.getJSONArray("marks");
        for (int i = 0; i < marksJsonArray.length(); i++) {
            JSONObject markJson = marksJsonArray.getJSONObject(i);
            marks.add(RecordMark.fromJson(markJson));
        }
        Collections.sort(marks);
        return marks;
    }

    public String getMarksForView() {
        List<String> marksForView = new ArrayList<String>();
        for (RecordMark mark : marks) {
            marksForView.add(mark.getMark());
        }
        return CollectionUtils.join(marksForView);
    }

    private static List<String> getSubjectsFromJson(JSONObject dayLessonJson) throws JSONException {
        List<String> subjects = new ArrayList<String>();
        JSONArray subjectsJsonArray = dayLessonJson.getJSONArray("subjects");
        for (int i = 0; i < subjectsJsonArray.length(); i++) {
            subjects.add(subjectsJsonArray.getString(i));
        }
        return subjects;
    }

    public boolean hasNotification() {
        return !homeTask.equals("null") && !StringUtils.isEmpty(homeTask);
    }
}
