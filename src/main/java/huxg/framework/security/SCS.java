package huxg.framework.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import huxg.framework.filter.parameter.ParameterWrapper;

public class SCS extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("image/jpeg");

		// 验证码边框的长高
		int width = 60;
		int height = 20;
		// 用RGB模式输出图像区域
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// 定义画笔
		Graphics graph = image.getGraphics();
		
		// 设置验证码框背景色0-255
		graph.setColor(Color.WHITE);
		
		// 填充矩形
		graph.fillRect(0, 0, width, height);
		
		// 产生1000-9999之间的随机数
		Random rnd = new Random();
		int rndNum = rnd.nextInt(8999) + 1000;

		// 此处为何转换为String型的用int型的效果一样？
		String rndStr = String.valueOf(rndNum);

		String ecsid = (String) request.getParameter("ec_sid");
		HttpSession session = request.getSession();

		session.setAttribute("rndStr", rndNum);
		
		// 设置矩形区域中随机数颜色及干扰点的颜色
		graph.setColor(new Color(243, 34, 34));
		
		// 设置随机数的字体大小
		graph.setFont(new Font("", Font.PLAIN, 20));
		
		// 在已有的矩形区域中绘制随机数
		graph.drawString(rndStr, 8, 17);
//		// 随机产生100个干扰点
//		for (int i = 0; i < 100; i++) {
//			int x = rnd.nextInt(width);
//			int y = rnd.nextInt(height);
//			// 设置干扰点的位置长宽
//			graph.drawOval(x, y, 1, 1);
//		}
		// 将图像输出到页面上
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 清空缓冲区
		// PrintWriter out = response.getWriter();
		// out.clear();
		// out = pageContext.pushBody();
	}

	public static boolean isRightCode(String checkcode) {
		HttpSession session = ParameterWrapper.getWrapper().getRequest().getSession();
		Integer rndStr = (Integer) session.getAttribute("rndStr");
		try {
			int i;
			try {
				i = Integer.parseInt(checkcode);
			} catch (Exception e) {
				throw new Exception("验证码有误，请重新输入");
			}
			if (i != rndStr.intValue()) {
				throw new Exception("验证码有误，请重新输入");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
