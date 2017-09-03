/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.template.method;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import huxg.framework.util.CurrencyUtils;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 模板方法 - 货币格式化
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
@Component("currencyMethod")
public class CurrencyMethod implements TemplateMethodModel {

	@SuppressWarnings("rawtypes")
	public Object exec(List arguments) throws TemplateModelException {
		if (arguments != null && !arguments.isEmpty() && arguments.get(0) != null && StringUtils.isNotEmpty(arguments.get(0).toString())) {
			boolean showSign = false;
			boolean showUnit = false;
			if (arguments.size() == 2) {
				if (arguments.get(1) != null) {
					showSign = Boolean.valueOf(arguments.get(1).toString());
				}
			} else if (arguments.size() > 2) {
				if (arguments.get(1) != null) {
					showSign = Boolean.valueOf(arguments.get(1).toString());
				}
				if (arguments.get(2) != null) {
					showUnit = Boolean.valueOf(arguments.get(2).toString());
				}
			}

			BigDecimal amount = new BigDecimal(arguments.get(0).toString());
			String price = CurrencyUtils.setScale(amount).toString();
			if (showSign) {
				price = "￥" + price;
			}
			if (showUnit) {
				price += "元";
			}
			return new SimpleScalar(price);
		}
		return null;
	}

}