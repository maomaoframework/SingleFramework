package huxg.framework.exception;

import java.util.ArrayList;
import java.util.List;

public class Status {
	private boolean success = false;
	private List<StatusElement> elements = new ArrayList<StatusElement>();
	private String data;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<StatusElement> getElements() {
		return elements;
	}

	public void setElements(List<StatusElement> elements) {
		this.elements = elements;
	}
	
	public void addMessage(String field, String message) {
		StatusElement se = new StatusElement();
		se.setElement(field);
		se.setMessage(message);
		this.elements.add(se);
	}
	
	public void addMessage(String field, String message, String redirect) {
		StatusElement se = new StatusElement();
		se.setElement(field);
		se.setMessage(message);
		se.setRedirect(redirect);
		this.elements.add(se);
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public void empty(){
		this.elements.clear();
		this.data = null;
	}
}
