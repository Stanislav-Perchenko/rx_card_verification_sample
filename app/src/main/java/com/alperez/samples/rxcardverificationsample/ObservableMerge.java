package com.alperez.samples.rxcardverificationsample;

import io.reactivex.Observable;

public class ObservableMerge {

    @SafeVarargs
    public static Observable<Boolean> or(Observable<Boolean>... sources) {
        return Observable.combineLatest(sources, values -> {
            for (Object v : values) {
                if ((v instanceof Boolean) && (Boolean) v) {
                    return true;
                }
            }
            return false;
        });
    }

    @SafeVarargs
    public static Observable<Boolean> and(Observable<Boolean>... sources) {
        return Observable.combineLatest(sources, values -> {
            for (Object v : values) {
                if (!(v instanceof Boolean) || !((Boolean) v)) {
                    return false;
                }
            }
            return true;
        });
    }

    public static Observable<Boolean> eq(Observable<Integer> o1, Observable<Integer> o2) {
        return Observable.combineLatest(o1, o2, (v1, v2) -> v1.equals(v2));
    }
}
