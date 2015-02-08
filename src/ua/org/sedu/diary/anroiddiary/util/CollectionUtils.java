/**
 *
 */
package ua.org.sedu.diary.anroiddiary.util;

import java.util.List;

/**
 * @author voinovdenys
 * 
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // restrict instantiation
    }

    public static String join(List<String> collection) {

        int index = 1;
        String retValue = "";
        for (String element : collection) {

            retValue += element;
            if (index != collection.size()) {
                retValue += ",";
            }
            index++;
        }
        return retValue;
    }

}
