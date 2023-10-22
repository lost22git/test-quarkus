package lost.test.quarkus.common;

import io.hypersistence.utils.hibernate.type.range.Range;

import java.time.ZonedDateTime;

public class Ranges {

    public static <T extends Comparable<? super T>> Range<T> union(Range<T> t1,
                                                                   Range<T> t2) {
        if (t1.contains(t2)) return t1;
        if (t2.contains(t1)) return t2;

        T lower = null;
        var lowerClosed = false;
        T upper = null;
        var upperClosed = false;
        if (t1.lower().compareTo(t2.lower()) < 0) {
            lower = t1.lower();
            lowerClosed = t1.isLowerBoundClosed();
            upper = t2.upper();
            upperClosed = t2.isUpperBoundClosed();
        } else {
            lower = t2.lower();
            lowerClosed = t2.isLowerBoundClosed();
            upper = t1.upper();
            upperClosed = t1.isUpperBoundClosed();
        }
        if (lowerClosed) {
            if (upperClosed) {
                return Range.closed(lower, upper);
            } else {
                return Range.closedOpen(lower, upper);
            }
        } else {
            if (upperClosed) {
                return Range.openClosed(lower, upper);
            } else {
                return Range.open(lower, upper);
            }
        }

//            ZonedDateTime lower = null;
//            boolean lowerClosed = false;
//            if (t1.lower().compareTo(t2.lower()) < 0) {
//                lower = t1.lower();
//                lowerClosed = t1.isLowerBoundClosed();
//            } else if (t1.lower().compareTo(t2.lower()) > 0) {
//                lower = t2.lower();
//                lowerClosed = t2.isLowerBoundClosed();
//            } else {
//                lower = t1.lower();
//                lowerClosed = t1.isLowerBoundClosed() || t2.isLowerBoundClosed();
//            }
//
//            ZonedDateTime upper = null;
//            boolean upperClosed = false;
//            if (t1.upper().compareTo(t2.upper()) > 0) {
//                upper = t1.upper();
//                upperClosed = t1.isUpperBoundClosed();
//            } else if (t1.upper().compareTo(t2.upper()) < 0) {
//                upper = t2.upper();
//                upperClosed = t2.isUpperBoundClosed();
//            } else {
//                upper = t1.upper();
//                upperClosed = t1.isUpperBoundClosed() || t2.isUpperBoundClosed();
//            }
    }
}
