package huxg.framework.filter.parameter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

public class MyMultipartHttpServletRequest extends StandardMultipartHttpServletRequest {

	private Map multipartParameters;
	/**
	 * <p>
	 * Cache: the parameters(all) for this multipart request
	 * </p>
	 */
	protected Map parameters = null;
	protected Set parameterNames = null;

	public MyMultipartHttpServletRequest(HttpServletRequest request) {
		super(request);
		this.parameters = new HashMap();
	}

	public MyMultipartHttpServletRequest(HttpServletRequest request, MultiValueMap multipartFiles, Map multipartParameters) {
		this(request);
		setMultipartFiles(multipartFiles);
		setMultipartParameters(multipartParameters);
	}

	public String getParameter(String name) {
		String[] values = (String[]) getParameterValues(name);
		if (values != null) {
			return (values.length > 0 ? values[0] : null);
		}
		return null;
	}

	public String[] getParameterValues(String name) {
		if (parameters.containsKey(name)) {
			return (String[]) parameters.get(name);
		}
		String[] values = mergeParemeterValues(name);
		parameters.put(name, values);
		return values;
	}

	/**
	 * 
	 * 
	 * @param name
	 * @return
	 */
	protected final String[] mergeParemeterValues(String name) {
		String[] pvalues = super.getParameterValues(name);
		String[] mvalues = (String[]) getMultipartParameters().get(name);
		if (mvalues == null || mvalues.length == 0) {
			return pvalues;
		} else if (pvalues == null || pvalues.length == 0) {
			return mvalues;
		}

		// merge
		String[] values = new String[mvalues.length + pvalues.length];
		System.arraycopy(pvalues, 0, values, 0, pvalues.length);
		System.arraycopy(mvalues, 0, values, pvalues.length, mvalues.length);

		return values;
	}

	public MultipartFile getFile(String name) {
		Object multipartFiles = getMultipartFiles().get(name);
		if (multipartFiles instanceof List) {
			List lstFiles = (List) multipartFiles;
			return (MultipartFile) (lstFiles.size() > 0 ? lstFiles.get(0) : null);
		}
		return (MultipartFile) multipartFiles;
	}

	public Enumeration getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	}

	public Map getParameterMap() {
		if (parameterNames == null) {
			parameterNames = new HashSet();
			parameterNames.addAll(super.getParameterMap().keySet());
			parameterNames.addAll(getMultipartParameters().keySet());

		}
		if (parameters.size() < parameterNames.size()) {
			Iterator iter = parameterNames.iterator();
			while (iter.hasNext()) {
				String name = (String) iter.next();
				if (parameters.containsKey(name)) {
					continue;
				}
				String[] values = mergeParemeterValues(name);
				parameters.put(name, values);
			}
		}

		return parameters;
	}

	/**
	 * Set a Map with parameter names as keys and String array objects as
	 * values. To be invoked by subclasses on initialization.
	 */
	protected final void setMultipartParameters(Map multipartParameters) {
		this.multipartParameters = multipartParameters;
	}

	/**
	 * Obtain the multipart parameter Map for retrieval, lazily initializing it
	 * if necessary.
	 * 
	 * @see #initializeMultipart()
	 */
	protected Map getMultipartParameters() {
		if (this.multipartParameters == null) {
			initializeMultipart();
		}
		return this.multipartParameters;
	}

	public void setRequest(ServletRequest request) {
		// reset
		this.multipartParameters = null;
		this.parameters = new HashMap();
		this.parameterNames = null;

		super.setRequest(request);
		ParameterWrapper.fireRebuild();
	}

}
