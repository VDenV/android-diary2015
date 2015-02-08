/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Redirects List Touch event to the parent activity's onTouchEvent method.
 * 
 * @author voinovdenys
 */
public class TouchEventRedirector implements OnTouchListener {

    private Activity parent;

    public TouchEventRedirector(Activity parent) {
        this.parent = parent;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        parent.onTouchEvent(event);
        return true;
    }
}
