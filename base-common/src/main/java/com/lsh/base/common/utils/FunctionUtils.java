package com.lsh.base.common.utils;

import com.google.common.collect.Lists;
import com.lsh.base.common.utils.functional.Func;
import com.lsh.base.common.utils.functional.Reduce;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fuhao on 15/11/25.
 */
public class FunctionUtils {
    public static List<Integer> range(int start, int stop,int step) {
        final List<Integer> list = new ArrayList<Integer>();
        for (int i=start; (step > 0) ? i<stop : i>stop; i+=step) {
            list.add(i);
        }
        return list;
    }

    public static <F,T> T reduce(final Iterable<F> iterable, final Func<F, T> func, T origin){
        return Reduce.reduce(iterable, func, origin);
    }


    public static void main(String args[]){
        List<Integer> res = range(1,5,1);
        System.out.println(res.toString());
        List<Integer> comments = range(1, 3, 1);
        System.out.println(comments.toString());
        res.removeAll(comments);
        System.out.println(res.toString());
        List<Integer> tt = new ArrayList<Integer>();
        res.removeAll(tt);
        System.out.println(res.toString());


        Random random = new Random();
        int randomNum  = random.nextInt(1)%(1+1);
        System.out.println(randomNum);


        List<Integer> testList = Lists.newArrayList(1, 20, 3, 40, 5, 67, 7, 8, 9);
        Integer max = reduce(testList, new Func<Integer, Integer>() {

            public Integer apply(Integer i, Integer origin) {
                return i > origin ? i : origin;
            }
        }, 0);
        System.out.println(max);

        Integer sum = reduce(testList, new Func<Integer, Integer>() {

            public Integer apply(Integer i, Integer origin) {
                return i + origin;
            }
        }, 0);
        System.out.println(sum);

    }
}
