package com.lsh.base.common.exception;

import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.utils.TemplateUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * Project Name: lsh-wms
 * Created by fuhao
 * Date: 16/7/16
 * Time: 16/7/16.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.wms.api.service.exception.
 * desc:类功能描述
 */
public class BaseCheckedException extends RuntimeException implements IBaseException,Serializable {
    private static final long serialVersionUID = 4540849704711249641L;
    private static String defaultPlatfromMessageCode;

    private String code;

    private String innerCode;

    private String message = defaultPlatfromMessageCode;

    private List<String> serverNames = new ArrayList<String>(3);

    private List<BaseCheckedException> exceptions;

    private transient Map<String, Object> infoParams;

    private Map<String, Object> parameters;

    private String stackInfo;

    private final Map<String, String> logStorage = new HashMap<String, String>(5);

    private final long time = System.currentTimeMillis();

    private static String localServerName;
    private static final String localIP;
    private static final List<String> allIP = new ArrayList<String>();

    static {

        // 获取主机名
        try {
            localServerName = InetAddress.getLocalHost().toString();
        } catch (Exception ex) {
            localServerName = "UnknownHost";
        }

        // 获取本机IP
        byte[] ip = null;
        try {
            ip = InetAddress.getLocalHost().getAddress();
        } catch (Throwable ex) {
            ip = new byte[] { 0, 0, 0, 0 };
        }
        localIP = (ip[0] >= 0 ? ip[0] : ip[0] + 256) + "." + (ip[1] >= 0 ? ip[1] : ip[1] + 256) + "."
                + (ip[2] >= 0 ? ip[2] : ip[2] + 256) + "." + (ip[3] >= 0 ? ip[3] : ip[3] + 256);

        // 获取所有可用网卡IP
        Enumeration<?> allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<?> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress) addresses.nextElement();
                    if (address != null && !address.isLoopbackAddress() && !address.isLinkLocalAddress() && address instanceof Inet4Address) {
                        allIP.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            allIP.add(localIP);
        }
    }


    public static String getLocalIP() {
        return localIP;
    }

    public BaseCheckedException() {
        this.addServerName();
        processCode();
        processMessage();
    }

    public BaseCheckedException(String code) {
        super(code);
        this.code = code;
        this.addServerName();
        processCode();
        processMessage();
    }

    public BaseCheckedException(String code, Throwable ex) {
        super(code);
        initCause(ex);
        this.code = code;
        this.addServerName();
        processCode();
        processMessage();
    }

    public BaseCheckedException(String code, String message) {
        super(code);
        this.code = code;
        this.message = message;
        this.addServerName();
        processCode();
        processMessage();
    }


    public BaseCheckedException(String code ,Object... args) {
        super(code);
        this.code = code;
        this.message = PropertyUtils.getMessage(code,args);// TODO: 16/12/19
        this.addServerName();
        processCode();
        processMessage();
    }

    public BaseCheckedException(String code, String message, Throwable ex) {
        super(code);
        this.code = code;
        this.message = message;
        this.addServerName();
        initCause(ex);
        processCode();
        processMessage();
    }

    public BaseCheckedException(String code, Map<String, Object> infoParams) {
        super(code);
        this.code = code;
        this.infoParams = infoParams;
        this.addServerName();
        processCode();
        processMessage();
    }

    public BaseCheckedException(String code, String message, Map<String, Object> infoParams) {
        super(code);
        this.code = code;
        this.message = message;
        this.infoParams = infoParams;
        this.addServerName();
        processCode();
        processMessage();
    }

    public BaseCheckedException(String code, Map<String, Object> infoParams, Throwable ex) {
        super(code);
        this.code = code;
        this.infoParams = infoParams;
        this.addServerName();
        initCause(ex);
        processCode();
        processMessage();
    }

    public BaseCheckedException(String code, String message, Map<String, Object> infoParams, Throwable ex) {
        super(code);
        this.code = code;
        this.message = message;
        this.infoParams = infoParams;
        this.addServerName();
        initCause(ex);
        processCode();
        processMessage();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        if (cause != null) {
            Exception ex = new Exception(cause.getMessage() + "(" + cause.getClass().getName() + ")", cause.getCause());
            ex.setStackTrace(cause.getStackTrace());
            cause = ex;
        }

        super.initCause(cause);

        return super.getCause();
    }

    private void processCode() {
        if(this.code ==null){
            return ;
        }
        String exceptionMsg = PropertyUtils.getString(this.code,"系统异常");
        // 增加异常提示信息
        if (exceptionMsg == null) {
            exceptionMsg = this.code;
        }
//        else {
//            //exceptionMsg = this.code + ":" + exceptionMsg;
//            exceptionMsg = exceptionMsg;
//        }


        // 处理参数化的异常提示信息
        if (infoParams != null && !infoParams.isEmpty()) {
            exceptionMsg = TemplateUtils.generateString(exceptionMsg, infoParams);
        }

        this.innerCode = exceptionMsg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
        processCode();
        this.stackInfo = null;
    }

    private void processMessage() {
        StringBuilder str = new StringBuilder();
        String exceptionMsg = null;

        if (this.message == null) {
            this.message = this.innerCode;
            return;
        }

        str.append(this.message);

        if (exceptionMsg != null) {
            str.append(":");
            // 处理参数化的异常提示信息
            if (infoParams != null && !infoParams.isEmpty()) {
                exceptionMsg = TemplateUtils.generateString(exceptionMsg, infoParams);
            }
            str.append(exceptionMsg);
        }

        this.message = str.toString();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
        processMessage();
        this.stackInfo = null;
    }

    public String getExceptionStackInfo() {
        if (stackInfo == null) {
            stackInfo = filterStackTrace();
        }
        // TODO:提示信息加密
        return stackInfo;
    }

    // ---------------------------------------------异常堆栈信息过滤----------------------------------------------------------

    public void printStackTrace(PrintWriter writer) {
        if (stackInfo == null) {
            stackInfo = filterStackTrace();
        }
        synchronized (writer) {
            writer.println(stackInfo);
        }
    }

    public void printStackTrace(PrintStream stream) {
        if (stackInfo == null) {
            stackInfo = filterStackTrace();
        }
        synchronized (stream) {
            stream.println(stackInfo);
        }
    }

    /**
     * 异常堆栈过滤规则
     *
     * @return
     */
    String getFilter() {
        return null;
    }

    /**
     * 过滤异常堆栈信息
     *
     * @return
     */
    public String filterStackTrace() {
        if (stackInfo != null) {
            return stackInfo;
        }

        final StringBuffer exMsg = new StringBuffer(this.getClass().getName()).append(": ").append(this.message).append("\r\n")
                .append(this.innerCode);

        //todo 增加会话ID信息
       /* final SwordSession currentSession = SwordServerContext.getSession();
        final String parentSessionId = currentSession.getParentSessionId();
        final String sessionId = currentSession.getSessionId();

        exMsg.append("\r\n本次请求会话ID:").append(sessionId);

        if (parentSessionId != null && parentSessionId != sessionId && !parentSessionId.equals(sessionId)) {
            exMsg.append(",本次请求的父会话ID:").append(parentSessionId);
        }*/

        if (this.exceptions == null || this.exceptions.isEmpty()) {
            getExceptionStackMessage(exMsg);
        } else {
            for (BaseCheckedException ex : exceptions) {
                exMsg.append("\r\n 子异常:").append(ex.filterStackTrace());
            }
        }

        return exMsg.toString();
    }

    /**
     * 获取异常堆栈信息
     *
     * @Description 相关说明
     * @Time 创建时间:2012-3-27下午6:05:45
     * @param exMsg
     * @history 修订历史（历次修订内容、修订人、修订时间等）
     */
    private void getExceptionStackMessage(StringBuffer exMsg) {
        boolean doFilter = false;
        String filter = getFilter();

        // 增加处理服务器信息
        if (!serverNames.isEmpty()) {
            exMsg.append("\r\n当前应用服务器:").append(this.serverNames.get(0));
            exMsg.append("\r\n当前应用服务器ip:").append(getLocalIP());
            exMsg.append("\r\n本次请求经过的应用服务器:");
            for (String serverName : this.serverNames) {
                exMsg.append("\r\n\t").append(serverName);
                String localLogFile = this.logStorage.get(serverName);
                if (localLogFile != null) {
                    exMsg.append("[ ").append(localLogFile).append(" ]");
                }
            }
        }

        exMsg.append("\r\n异常堆栈信息：");

        StackTraceElement[] stes = this.getStackTrace();

        // 判断抛出异常的类是否为在要进行异常过滤的包中
        if (filter != null && stes[0].getClassName().startsWith(filter)) {
            doFilter = true;
        }

        for (StackTraceElement ste : stes) {
            String clazz = ste.getClassName();
            /*if (doFilter && !clazz.startsWith(filter) && !clazz.startsWith("")) {
                continue;
            }*/
            exMsg.append("\r\n\tat ").append(clazz).append(".").append(ste.getMethodName()).append("(").append(ste.getFileName())
                    .append(" : ").append(ste.getLineNumber()).append(")");
        }

        // 处理嵌套异常
        Throwable cause = this.getCause();
        while (cause != null) {
            if (cause instanceof BaseCheckedException) {
                exMsg.append("\r\nCaused by: ").append(((BaseCheckedException) cause).filterStackTrace());
            } else {
                exMsg.append("\r\nCaused by: ").append(cause.getClass().getName()).append(" ").append(cause.getMessage());
                for (StackTraceElement ste : cause.getStackTrace()) {
                    exMsg.append("\r\n\tat ").append(ste.getClassName()).append(".").append(ste.getMethodName()).append("(")
                            .append(ste.getFileName()).append(" : ").append(ste.getLineNumber()).append(")");
                }
            }
            cause = cause.getCause();
        }
    }

    public List<String> getServerNames() {
        return serverNames;
    }

    private void addServerName() {
        this.serverNames.add(localServerName);
    }



    public synchronized void addException(BaseCheckedException ex) {
        if (this.exceptions == null) {
            this.exceptions = new LinkedList<BaseCheckedException>();
        }

        this.exceptions.add(ex);
    }

    public List<BaseCheckedException> getAllExceptions() {
        return this.exceptions;
    }

    public final void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public final Map<String, Object> getParameters() {
        return this.parameters;
    }

    public static void setDefaultPlatformMessageCode(String defaultPlatformMessageCode) {
        BaseCheckedException.defaultPlatfromMessageCode = defaultPlatformMessageCode;
    }

    public static String getDefaultPlatformMessageCode() {
        return BaseCheckedException.defaultPlatfromMessageCode;
    }

    public void reverseInfo() {
        Throwable cause = this.getCause();
        if (cause != null && cause instanceof BaseCheckedException) {
            BaseCheckedException e = (BaseCheckedException) cause;

            // 翻转提示信息
            e.setCode(this.getCode());
            e.setMessage(this.getMessage());

            // 翻转服务器列表
            List<String> serverNames = this.serverNames;
            this.serverNames = ((BaseCheckedException) cause).serverNames;
            ((BaseCheckedException) cause).serverNames = serverNames;
        }
    }

    public void setLogStorage(String logStorage) {
        this.logStorage.put(localServerName, logStorage);
    }


    public Type getType() {
        return Type.SYSTEM;
    }

    public long getTime() {
        return this.time;
    }



    @Deprecated
    public void addThisServerName() {

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseCheckedException)) {
            return false;
        }
        return this.code.equals(((BaseCheckedException) obj).code);
    }

    @Override
    public int hashCode() {
        if (code == null) {
            return -1;
        }
        return code.hashCode();
    }

    public static BaseCheckedException generate(String head, Throwable e) {
        BaseCheckedException ex = new BaseCheckedException(head + e.getMessage());
        ex.setStackTrace(e.getStackTrace());
        return ex;
    }

    public static String getStackTraceMessage( Throwable e){
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.getBuffer().toString();
    }
}
