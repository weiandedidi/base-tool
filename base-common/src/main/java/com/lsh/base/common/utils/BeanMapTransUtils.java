package com.lsh.base.common.utils;
import com.lsh.base.common.json.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Project Name: lajin-base
 * Created by fuhao
 * Date: 16/7/6
 * Time: 16/7/6.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.base.common.utils.
 * desc:map转bean,bean转map
 */
public class BeanMapTransUtils {

    // Map --> Bean 2: 利用org.apache.commons.beanutils 工具类实现 Map --> Bean
    /*public static void transMap2Bean2(Map<String, Object> map, Object obj) {
        if (map == null || obj == null) {
            return;
        }
        try {
            BeanUtils.populate(obj, map);
        } catch (Exception e) {
            System.out.println("transMap2Bean2 Error " + e);
        }
    }

    // Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
    public static void transMap2Bean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }

            }

        } catch (Exception e) {
            System.out.println("transMap2Bean Error " + e);
        }

        return;

    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> transBean2Map(Object obj) {

        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;

    }*/


    public static Map Bean2map( Object bean) {
        Map<String, String> map = null;
        try{
            map = BeanUtils.describe(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }



    public static <T> T map2Bean(Map<String, Object> map, Class<T> class1) {
        T bean = null;
        try {
            bean = class1.newInstance();
            BeanUtils.populate(bean, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }


    public static void main(String args[]) throws  Exception {
       /* Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1L);
        map.put("name", "fuhao");


        try {
            User user  = map2Bean(map,User.class);
            System.out.println(JsonUtils.obj2Json(user));
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user1 = new User();
        user1.setId(2L);
        user1.setName("css");
        Map<String, String> map1 = Bean2map(user1);
        System.out.println(JsonUtils.obj2Json(map1));*/

        // map = BeanUtils.describe(user);

    }
}
