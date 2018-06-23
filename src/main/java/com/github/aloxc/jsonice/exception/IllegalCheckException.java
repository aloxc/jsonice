package com.github.aloxc.jsonice.exception;

/**
 * Created by aloxc on 2018/6/23 in project jsonice.
 */

public class IllegalCheckException extends RuntimeException {

    /**
     * @Fields serialVersionUID : 用来封装项目里验证错误参数的异常类
     */

    private static final long serialVersionUID = 5432584489152259168L;

    /**
     * Constructs an IllegalCheckException with no detail message.
     * A detail message is a String that describes this particular exception.
     */
    public IllegalCheckException() {
        super();
    }

    /**
     * Constructs an IllegalCheckException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     */
    public IllegalCheckException(String s) {
        super(s);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link Throwable#getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <tt>null</tt> value
     *         is permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since 1.5
     */
    public IllegalCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.5
     */
    public IllegalCheckException(Throwable cause) {
        super(cause);
    }

}
