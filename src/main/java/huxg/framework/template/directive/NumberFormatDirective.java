package huxg.framework.template.directive;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Component;

import huxg.framework.util.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("NumberFormatDirective")
public class NumberFormatDirective extends BaseDirective {
	/** 变量名称 */
	private static final String VARIABLE_NAME = "currency";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		try {
			Object value = getObject("value", params);
			String pattern = getString("pattern", params);
			if (StringUtils.isEmptyAfterTrim(pattern)) {
				pattern = "0.00";
			}

			String text = "";
			java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
			try {
				if (value == null) {
					text = df.format(BigDecimal.ZERO);
				} else if (value instanceof BigDecimal) {
					text = df.format((BigDecimal) value);
				} else {
					text = df.format(new BigDecimal(value.toString()));
				}
			} catch (Exception e) {
			}

			env.getOut().write(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String firstLetterToUpper(String str) {
		char[] array = str.toCharArray();
		array[0] -= 32;
		return String.valueOf(array);
	}
}