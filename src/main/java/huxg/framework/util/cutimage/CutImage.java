package huxg.framework.util.cutimage;

public class CutImage {
	private String left;
	private String top;
	private String width;
	private String height;
	private String standardWidth;
	private String standardHeight;
	private String src;
	
	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getStandardWidth() {
		return standardWidth;
	}

	public void setStandardWidth(String standardWidth) {
		this.standardWidth = standardWidth;
	}

	public String getStandardHeight() {
		return standardHeight;
	}

	public void setStandardHeight(String standardHeight) {
		this.standardHeight = standardHeight;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
	
	
	public int getWidthInt(){
		String h = this.width.trim();
		if (h.endsWith("px")) 
			h = h.substring(0, h.length() - 2);
		
		return Integer.parseInt(h);
	}
	
	public int getHeightInt(){
		String h = this.height.trim();
		if (h.endsWith("px")) 
			h = h.substring(0, h.length() - 2);
		
		return Integer.parseInt(h);
	}
	
	public int getLeftInt(){
		String h = this.left.trim();
		if (h.endsWith("px")) 
			h = h.substring(0, h.length() - 2);
		
		return Math.abs(Integer.parseInt(h));
	}
	
	public int getTopInt(){
		String h = this.top.trim();
		if (h.endsWith("px")) 
			h = h.substring(0, h.length() - 2);
		
		return Math.abs(Integer.parseInt(h));
	}
	
	public int getStandardWidthInt(){
		String h = this.standardWidth.trim();
		if (h.endsWith("px")) 
			h = h.substring(0, h.length() - 2);
		
		return Integer.parseInt(h);
	}
	
	public int getStandardHeightInt(){
		String h = this.standardHeight.trim();
		if (h.endsWith("px")) 
			h = h.substring(0, h.length() - 2);
		
		return Integer.parseInt(h);
	}
}
