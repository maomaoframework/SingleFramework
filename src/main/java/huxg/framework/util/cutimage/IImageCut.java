package huxg.framework.util.cutimage;

public interface IImageCut {
	public String cutImage(int x, int y, int w, int h, String imgLocation, String sufix, int constaint)  throws Exception;

	public void cutImage(String imagePath, String targetPath, int x, int y, int w, int h, String ext, int constraint)  throws Exception;
}
