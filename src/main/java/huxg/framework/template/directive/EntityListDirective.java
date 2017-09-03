package huxg.framework.template.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import huxg.framework.service.BaseService;
import huxg.framework.util.FreemarkerUtils;
import huxg.framework.util.SpringUtils;
import huxg.framework.util.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("entityListDirective")
public class EntityListDirective extends BaseDirective {
	/** 变量名称 */
	private static final String VARIABLE_NAME = "entitys";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String entity = FreemarkerUtils.getParameter("entity", String.class, params);
		BaseService baseService = SpringUtils.getBean(entity + "ServiceImpl", BaseService.class);
		List list = new ArrayList();
		try {
			String where = getString("where", params);
			String limit = getString("limit", params);
			String orderby = getString("orderby", params);
			String var = getString("var", params);
			String sql = getString("sql", params);

			String query;
			if (StringUtils.isEmptyAfterTrim(sql)) {
				query = baseService.makeSelectQuery();
			} else {
				query = sql;
			}

			if (!StringUtils.isEmptyAfterTrim(where)) {
				query += " where " + where;
			}

			if (!StringUtils.isEmptyAfterTrim(orderby)) {
				query += " order by " + orderby;
			}

			if (StringUtils.isEmptyAfterTrim(limit)) {
				list = baseService.findList(query, null);
			} else {
				int start = 0, max = 1;
				if (limit.contains(",")) {
					String[] s = limit.split(",");
					try {
						start = Integer.parseInt(s[0].trim());
					} catch (Exception e) {
					}
					try {
						max = Integer.parseInt(s[1].trim());
					} catch (Exception e) {

					}
				} else {
					start = 0;
					max = Integer.parseInt(limit.trim());
				}
				list = baseService.findList(query, null, start, max);
			}

			// List<Filter> filters = getFilters(params,
			// Class.forName(entityPackage+firstLetterToUpper(entity)));
			// List<Order> orders = getOrders(params);
			// list = baseService.findList(null, filters, orders);
			setLocalVariable(var, list, env, body);
		} catch (Exception e) {
			e.printStackTrace();
			setLocalVariable(VARIABLE_NAME, list, env, body);
		}
	}

	public static String firstLetterToUpper(String str) {
		char[] array = str.toCharArray();
		array[0] -= 32;
		return String.valueOf(array);
	}
}