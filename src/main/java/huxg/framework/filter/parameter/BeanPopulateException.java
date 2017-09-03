package huxg.framework.filter.parameter;

public class BeanPopulateException extends Exception {

    private static final long serialVersionUID = 6270541814470747346L;

    /**
     * Constructs an <code>IllegalAccessException</code> without a detail
     * message.
     */
    public BeanPopulateException() {
        super();
    }

    /**
     * Constructs an <code>IllegalAccessException</code> with a detail
     * message.
     * 
     * @param s the detail message.
     */
    public BeanPopulateException(String s) {
        super(s);
    }
}
