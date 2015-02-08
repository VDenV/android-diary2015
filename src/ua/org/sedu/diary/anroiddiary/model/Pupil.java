/**
 *
 */
package ua.org.sedu.diary.anroiddiary.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author voinovdenys
 *
 */
public class Pupil {

	private String id;

	private String name;

	private String classGroupName;

	private Pupil(String id, String name, String classGroupName) {
		this.id = id;
		this.name = name;
		this.classGroupName = classGroupName;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getClassGroupName() {
		return classGroupName;
	}

    public static Pupil fromJson(JSONObject child) throws JSONException {
        return new Pupil(child.getString("id"), child.getString("shortName"), child.getString("classGroup"));
    }
}
