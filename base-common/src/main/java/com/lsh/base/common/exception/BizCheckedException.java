package com.lsh.base.common.exception;

import java.io.Serializable;
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
public class BizCheckedException extends BaseCheckedException implements Serializable{
    private static final long serialVersionUID = -7926728004903781662L;

    private static String defaultDomainMessageCode;

    public BizCheckedException(){super();}

    public BizCheckedException(String code) {
        super(code, defaultDomainMessageCode, (Throwable) null);
    }
    public BizCheckedException(String code, String message) {super(code, message);}

    public BizCheckedException(String code ,Object... args) {
        super(code,args);
    }

    public BizCheckedException(String code, Throwable ex) {
        super(code, defaultDomainMessageCode, ex);
    }

    public BizCheckedException(String code, Map<String, Object> parameters) {
        super(code, defaultDomainMessageCode, parameters);
    }

    public BizCheckedException(String code, Map<String, Object> parameters, Throwable ex) {
        super(code, defaultDomainMessageCode, parameters, ex);
    }


    public static void setDefaultDomainMessageCode(String defaultDomainMessageCode) {
        BizCheckedException.defaultDomainMessageCode = defaultDomainMessageCode;
    }

    public static String getDefaultDomainMessageCode() {
        return BizCheckedException.defaultDomainMessageCode;
    }

    @Override
    public Type getType() {
        return Type.APPLICATION;
    }
}
