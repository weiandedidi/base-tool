package com.lsh.base.common.exception;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/16
 * Time: 16/7/16.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.exception.
 * desc:类功能描述
 */
public interface IBaseException extends Serializable {

    public enum Type {
        SYSTEM, APPLICATION, OTHERS
    }

    /**
     * 获取异常描述信息
     */
    public String getMessage();

    /**
     * 设置通过异常对象携带返回的对象
     */
    public void setParameters(Map<String, Object> parameters);

    /**
     * 获取异常参数信息
     *
     * @return
     */
    public Map<String, Object> getParameters();

    /**
     * 获取抛出异常的机器名
     *
     * @return
     */
    public List<String> getServerNames();

    /**
     * 获取异常堆栈信息
     *
     * @return
     */
    public String getExceptionStackInfo();

    /**
     * 增加子异常
     *
     * @param ex
     */
    public void addException(BaseCheckedException ex);

    /**
     * 获取所有的子Exception
     *
     * @return
     */
    public List<BaseCheckedException> getAllExceptions();

    @Deprecated
    public void addThisServerName();

    /**
     * 翻转内部异常信息
     *  因中间件处理远程异常对象造成异常自动包装算法，引起内部异常和外部异常相关信息反过来的问题
     *  中间件处理远程异常对象的算法上。 <br>
     *  因为异常对象的initCause()方法只能调用一次, 所以中间件将远程异常对象和本地包装异常对象的线程堆栈信息进行了调换，<br>
     *  但没有调换自定义异常对象内部的信息
     */
    public void reverseInfo();

    /**
     * 获取异常类型
     *
     * @return
     */
    public Type getType();

    /**
     * 当前服务器处理该请求消耗时间
     */
    public long getTime();
}
