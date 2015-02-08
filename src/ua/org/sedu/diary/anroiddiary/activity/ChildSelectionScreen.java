/**
 *
 */
package ua.org.sedu.diary.anroiddiary.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.org.sedu.diary.anroiddiary.R;
import ua.org.sedu.diary.anroiddiary.model.Pupil;
import ua.org.sedu.diary.anroiddiary.util.Msg;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author voinovdenys
 */
public class ChildSelectionScreen extends Activity {

    public final static String CHILDREN_JSON = "childrenJson";
    private Map<String, Pupil> childrenMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_selection_screen);

        loadChildren();
        initChildrenView();
    }

    private void initChildrenView() {
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout childSelectionPanel = (LinearLayout) findViewById(R.id.child_selection_layout);

        populateChildrenPanel(inflator, childSelectionPanel);
    }

    private void populateChildrenPanel(LayoutInflater inflator, LinearLayout childSelectionPanel) {
        for (Pupil child : childrenMap.values()) {

            View childSelectionView = inflator.inflate(R.layout.child_selection_row, null);
            setChildName(child, childSelectionView);
            initializeButton(child, childSelectionView);
            childSelectionPanel.addView(childSelectionView);
        }
    }

    private void initializeButton(Pupil child, View childSelectionView) {
        ImageButton button = (ImageButton) childSelectionView.findViewById(R.id.childImage);
        button.setTag(child.getId());
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String selectedChildId = (String) ((ImageButton) v).getTag();
                showDiaryForChild(selectedChildId);
            }
        });
    }

    private void setChildName(Pupil child, View singleChildSelectionView) {
        TextView dataText = (TextView) singleChildSelectionView.findViewById(R.id.childName);
        dataText.setText(child.getName());
    }

    private void loadChildren() {
        JSONArray childrenJsonArray = getChildrenJsonArray();
        childrenMap = new HashMap<String, Pupil>();
        try {
            populateChildrenMap(childrenJsonArray);
        } catch (JSONException e) {
            Msg.showErrorDialog(this, getString(R.string.ERROR_READING_JSON_RESPONSE_MSG));
            throw new AndroidRuntimeException(e);
        }
    }

    private void populateChildrenMap(JSONArray childrenJsonArray) throws JSONException {
        for (int i = 0; i < childrenJsonArray.length(); i++) {
            JSONObject child = childrenJsonArray.getJSONObject(i);
            childrenMap.put(child.getString("id"), Pupil.fromJson(child));
        }
    }

    private JSONArray getChildrenJsonArray() {
        try {
            String childrenJson = (String) getIntent().getExtras().get(CHILDREN_JSON);
            return new JSONArray(childrenJson);
        } catch (JSONException e) {
            Msg.showErrorDialog(this, getString(R.string.ERROR_READING_JSON_RESPONSE_MSG));
            throw new AndroidRuntimeException(e);
        }
    }

    private void showDiaryForChild(String selectedChildId) {
        Intent intent = new Intent(this, DiaryScreen.class);
        intent.putExtra(DiaryScreen.CHILD_ID, selectedChildId);
        Pupil pupil = childrenMap.get(selectedChildId);
        intent.putExtra(DiaryScreen.PUPIL_NAME, pupil.getName());
        intent.putExtra(DiaryScreen.PUPIL_CLASS, pupil.getClassGroupName());
        startActivity(intent);
    }
}
