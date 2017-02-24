package com.lsh.base.common.utils.functional;

import java.util.Iterator;

/**
 * Created by fuhao on 15/11/26.
 */
public class Reduce {
    private Reduce() {

    }

    public static <F,T> T reduce(final Iterable<F> iterable, final Func<F, T> func, T origin) {

        for (Iterator iterator = iterable.iterator(); iterator.hasNext(); ) {
            origin = func.apply((F)(iterator.next()), origin);
        }

        return origin;
    }
}
