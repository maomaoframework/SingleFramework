package huxg.framework.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class CurrencyUtils {
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 减法计算
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal jian(BigDecimal b1, BigDecimal b2) {
		return b1.subtract(b2);
	}

	/**
	 * 加法计算
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static BigDecimal jia(BigDecimal b1, BigDecimal b2) {
		return b1.add(b2);
	}

	public static BigDecimal jia(BigDecimal b1, int v2) {
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2);
	}

	/**
	 * 乘法
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal cheng(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2);
	}

	public static BigDecimal cheng(BigDecimal b1, double v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2);
	}

	public static BigDecimal cheng(BigDecimal b1, int v2) {
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2);
	}

	public static BigDecimal chu(BigDecimal v1, int v2) {
		return chu(v1, v2, DEF_DIV_SCALE);
	}

	public static BigDecimal chu(double v1, double v2) {
		return chu(v1, v2, DEF_DIV_SCALE);
	}

	public static BigDecimal chu(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal chu(BigDecimal b1, int v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	public static String keep2BitFloat(BigDecimal b) {
		if (null == b)
			return "0.00";

		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		return df.format(b);
	}

	public static String keep1BitFloat(BigDecimal b) {
		if (null == b)
			return "0.0";

		java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
		return df.format(b);
	}

	public static String toInt(BigDecimal b) {
		return b.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
	}

	public static BigDecimal setScale(BigDecimal amount) {
		if (amount == null) {
			return null;
		}

		int roundingMode = BigDecimal.ROUND_UP;
		return amount.setScale(2, roundingMode);
	}

	public static String keep2BitFloat(double b) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(b);
	}
}
