/**
 *
 */
package ua.org.sedu.diary.anroiddiary.model;

import ua.org.sedu.diary.anroiddiary.R;

/**
 * @author voinovdenys
 */
public enum DayOfWeek {

    MONDAY(2, R.string.MONDAY_SHORT),
    TUESDAY(3, R.string.TUESDAY_SHORT),
    WEDNESDAY(4, R.string.WEDNESDAY_SHORT),
    THURSDAY(5, R.string.THURSDAY_SHORT),
    FRIDAY(6, R.string.FRIDAY_SHORT),
    SATURDAY(7, R.string.SATURDAY_SHORT);

    private int index;
    private int shortNameId;

    private DayOfWeek(int index, int nameId) {
        this.index = index;
        this.shortNameId = nameId;
    }

    public int getIndex() {
        return index;
    }

    public int getShortNameId() {
        return shortNameId;
    }

    public static DayOfWeek nextDay(DayOfWeek currentDay) {
        return getByIndex(currentDay.getIndex() + 1);
    }

    public static DayOfWeek previousDay(DayOfWeek currentDay) {
        return getByIndex(currentDay.getIndex() - 1);
    }

    private static DayOfWeek getByIndex(int index) {
        for (DayOfWeek dayOfWeek : values()) {
            if (dayOfWeek.index == index) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException("No such index found for the day of week: " + index);
    }

    public static DayOfWeek get(int dayOfWeekIndex) {
        for (DayOfWeek dayOfWeek : values()) {
            if (dayOfWeek.index == dayOfWeekIndex) {
                return dayOfWeek;
            }
        }
        return null;
    }
}
