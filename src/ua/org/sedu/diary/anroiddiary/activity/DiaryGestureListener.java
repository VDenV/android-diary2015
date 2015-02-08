/**
 *
 */
package ua.org.sedu.diary.anroiddiary.activity;

import ua.org.sedu.diary.anroiddiary.model.DayOfWeek;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * @author voinovdenys
 */
public class DiaryGestureListener implements OnGestureListener {

    private static final int MOVE_SENSITIVITY_Y = 500;

    private static final int MOVE_SENSITIVITY_X = 50;

    private DayOfWeek currentDay;

    private final DiaryScreen parentActivity;

    public DiaryGestureListener(DiaryScreen parentActivity) {
        this.currentDay = DayOfWeek.MONDAY;
        this.parentActivity = parentActivity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (isDayMoveCorrect(velocityX, velocityY)) {
            boolean slideRight = isDayMoveRight(velocityX);
            currentDay = slideRight ? nextDay() : previousDay();
            parentActivity.switchView(currentDay);
        }
        return true;
    }

    private boolean isDayMoveCorrect(float velocityX, float velocityY) {
        return !(isWrongMondayMove(velocityX) || isWrongFridayMove(velocityX)) && isMoveSensitivityReached(velocityX, velocityY);
    }

    private boolean isMoveSensitivityReached(float velocityX, float velocityY) {
        return Math.abs(velocityX) > MOVE_SENSITIVITY_X && Math.abs(velocityY) < MOVE_SENSITIVITY_Y;
    }

    private boolean isWrongFridayMove(float velocityX) {
        return currentDay == DayOfWeek.FRIDAY && isDayMoveRight(velocityX);
    }

    private boolean isWrongMondayMove(float velocityX) {
        return currentDay == DayOfWeek.MONDAY && isDayMoveLeft(velocityX);
    }

    private boolean isDayMoveRight(float velocityX) {
        return velocityX < 0;
    }

    private boolean isDayMoveLeft(float velocityX) {
        return velocityX > 0;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // do nothing
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // do nothing
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    public DayOfWeek nextDay() {
        return DayOfWeek.nextDay(currentDay);
    }

    public DayOfWeek previousDay() {
        return DayOfWeek.previousDay(currentDay);
    }

    public void updateCurrentDay(DayOfWeek dayOfWeek) {
        this.currentDay = dayOfWeek;
    }

}
