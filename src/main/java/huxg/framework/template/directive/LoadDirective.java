/*
 * Copyright 2005-2020 Top Team All rights reserved.
 * Support: 
 * License: top team license
 */
package huxg.framework.template.directive;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;

import huxg.framework.service.BaseService;
import huxg.framework.util.FreemarkerUtils;
import huxg.framework.util.SpringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - load(find entity by id)
 * 
 * @author Top Team（ ）
 * @version 1.0
 */
@Component("loadDirective")
public class LoadDirective extends BaseDirective {

	/** 变量名称 */
	private static final String VARIABLE_NAME = "object";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String entity = FreemarkerUtils.getParameter("entity", String.class, params);
		Long id = FreemarkerUtils.getParameter("id", Long.class, params);
		BaseService baseService = SpringUtils.getBean(entity + "ServiceImpl", BaseService.class);
		Object object = new Object();
		if (baseService != null) {
			object = baseService.get(id);
		}
		setLocalVariable(VARIABLE_NAME, object, env, body);
	}

}