package com.lsh.base.common.utils.functional;

/**
 * Created by fuhao on 15/11/26.
 */
public interface Func<F,T> {

    T apply(F currentElement, T origin);
}