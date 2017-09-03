
package huxg.framework.filter.parameter;

/**
 * ParamDescriptor
 * 
 * @author zhangjy
 * @version 1.0, 2009-2-23
 */
public class ParameterDescriptor {

    /** 操作符 */
    public static final char opChar = '.';

    /** array类型参数(request param)的右操作符 */
    public static final char arrayOpChar_L = '[';
    /** array类型参数(request param)的右操作符 */
    public static final char arrayOpChar_R = ']';

    /** list类型(request param)参数的左操作符 */
    public static final char listOpChar_L = '(';
    /** list类型(request param)参数的右操作符 */
    public static final char listOpChar_R = ')';

    private int beanType = BeanWrapper.TypeUndefined;
    private String beanName;
    private String paramName;
    private String prop;
    private int index;

    /**
     * 生成参数描述符<br>
     * 根据参数名，分析出Bean类型和Prop名
     * 
     * @param beanName bean的参数名，如: a
     * @param paramName bean属性的参数全名称，如: a[1].b[2].c
     * @return ParamDescriptor
     */
    public static ParameterDescriptor build(String beanName, String paramName) {
        if (beanName == null || paramName == null || beanName.length() == 0
                || paramName.startsWith(beanName) == false
                || paramName.length() == beanName.length()) {
            return null;
        }
        ParameterDescriptor pd = new ParameterDescriptor();
        pd.setBeanName(beanName);
        pd.setParamName(paramName);
        // 分析Bean类型
        switch (paramName.charAt(beanName.length())) {
        case opChar:
            pd.setBeanType(BeanWrapper.TypePojo);
            analyzeParam(pd);
            break;
        case listOpChar_L:
            pd.setBeanType(BeanWrapper.TypeList);
            analyzeParam(pd);
            if (pd.getIndex() < 0) {
                pd.setBeanType(BeanWrapper.TypeError);
            }
            break;
        case arrayOpChar_L:
            pd.setBeanType(BeanWrapper.TypeArray);
            analyzeParam(pd);
            if (pd.getIndex() < 0) {
                pd.setBeanType(BeanWrapper.TypeError);
            }
            break;
        default:
            // paramName 与 beanName 无附属关系
            return null;
        }
        return pd;
    }

    /**
     * 分析参数名，得到如下信息：
     * <ul>
     * <li>index (参数对应的Bean在结果列表中的index)</li>
     * <li>prop (参数对应的Bean属性名)</li>
     * </ul>
     * <br>
     * 注:
     * <ul>
     * <li>BeanType为array或者list，则index>=0</li>
     * <li>BeanType为pojo，则index=0</li>
     * <li>其他情况，默认index=-1</li>
     * </ul>
     */
    protected static void analyzeParam(ParameterDescriptor pd) {
        // 解析属性名
        int posOpChar = pd.getParamName().indexOf(opChar,
            pd.getBeanName().length());// skip the opChar(list/array)
        if (posOpChar == -1) {
            pd.setIndex(-1);// param error
            return;
        }
        pd.setProp(pd.getParamName().substring(posOpChar + 1));
        // 解析index值
        if (posOpChar == pd.getBeanName().length()) {
            // 只有一个元素时，index设置为0
            pd.setIndex(0);
        } else if (pd.getBeanType() == BeanWrapper.TypeArray
                || pd.getBeanType() == BeanWrapper.TypeList) {
            // 根据'.'操作符的前一个字符判断当前参数是否为indexed，并解析出index值
            switch (pd.getParamName().charAt(posOpChar - 1)) {
            case arrayOpChar_R:
            case listOpChar_R:
                String sIndex = pd.getParamName().substring(
                    pd.getBeanName().length() + 1, posOpChar - 1);
                pd.setIndex(Integer.parseInt(sIndex));
                break;
            default:
                // 不合法
                pd.setIndex(-1);
            }
        } else {// default
            pd.setIndex(-1);
        }
    }

    public String getBeanName() {
        return this.beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public int getBeanType() {
        return this.beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getProp() {
        return this.prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

}
