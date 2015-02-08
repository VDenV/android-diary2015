package ua.org.sedu.diary.anroiddiary.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Bogdan Nechyporenko
 */
public class RecordMark implements Serializable, Comparable<RecordMark> {

    private static final long serialVersionUID = 1L;

    private final int ordNumber;
    private final String mark;
    private final String markColor;
    private final EstimationAccuracy accuracy;

    private RecordMark(int ordNumber, String mark, String markColor, EstimationAccuracy accuracy) {
        this.ordNumber = ordNumber;
        this.mark = mark;
        this.markColor = markColor;
        this.accuracy = accuracy;
    }

    public int getOrdNumber() {
        return ordNumber;
    }

    public String getMark() {
        return mark;
    }

    public String getMarkColor() {
        return markColor;
    }

    public EstimationAccuracy getAccuracy() {
        return accuracy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RecordMark that = (RecordMark) o;

        return new EqualsBuilder().append(this.ordNumber, that.ordNumber).append(this.mark, that.mark).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ordNumber).append(mark).toHashCode();
    }

    @Override
    public int compareTo(RecordMark that) {
        return new CompareToBuilder().append(this.ordNumber, that.ordNumber).toComparison();
    }

    public static RecordMark fromJson(JSONObject markJson) throws JSONException {
        EstimationAccuracy accuracy = null;
        if (!StringUtils.isEmpty(markJson.getString("accuracy")) && !markJson.getString("accuracy").equals("null")) {
            accuracy = EstimationAccuracy.valueOf(markJson.getString("accuracy"));
        }
        return new RecordMark(markJson.getInt("ordNumber"), markJson.getString("mark"),
                markJson.getString("markColor"), accuracy);
    }
}
