/**
 *
 */
package ua.org.sedu.diary.anroiddiary.activity;

import java.util.ArrayList;
import java.util.List;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.model.DayLesson;
import ua.org.sedu.diary.anroiddiary.util.CollectionUtils;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author voinovdenys
 */
public class DiaryListViewAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private List<DayLesson> daySchedule = new ArrayList<DayLesson>();

    public DiaryListViewAdapter(List<DayLesson> daySchedule, Activity activity) {
        this.daySchedule = daySchedule;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return daySchedule.size();
    }

    @Override
    public Object getItem(int position) {
        return daySchedule.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View dayView = convertView;
        if (dayView == null) {
            dayView = inflater.inflate(R.layout.diary_list_row, null);
        }
        DayLesson lesson = daySchedule.get(position);
        ((TextView) dayView.findViewById(R.id.subject)).setText(CollectionUtils.join(lesson.getSubjects()));
        ((TextView) dayView.findViewById(R.id.marks)).setText(lesson.getMarksForView());

        if (lesson.hasNotification()) {
            View homeTaskIcon = dayView.findViewById(R.id.notificationIcon);
            homeTaskIcon.setTag(lesson.getHomeTask());
            homeTaskIcon.setVisibility(View.VISIBLE);
        }
        return dayView;
    }
}
