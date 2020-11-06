package imageView;

import java.awt.image.BufferedImage;
/** TransformAlgorithm类用于傅里叶变换和小波分解**/
public class TransformAlgorithm
{
	public static final int opt = 1; // 光学DFT,直流分量在中间
	public static final int DFT = 1; // 正变换
	public static final int IDA = -1; // 逆变换
	/**
	 * 
	 * @param ori 实部（输入）
	 * @param goal 虚部（输出）
	 * @param len 数组长度
	 * @param flag 标志位
	 * @param sinChg 待改变sin值
	 * @param cosChg 待改变cos值
	 * @param str 标签说明已丢失
	 * @return 无返回值
	 */
	private static void mainFFT(float[] ori, float[] goal, int len, int flag,
                                float[] sinChg, float[] cosChg, float[] str)
	{
		int w, tmp1, tmp2,number, length, times;
		int i,j,k;
		float xo, xg, yo, yg;
		float metric;
		if(opt == 1)
		{
			for(i=1; i<len; i=i+2)
			{
				goal[i] = -goal[i];  
				ori[i] = -ori[i];
			}
		}
		number = 1;
		length = len;
		for(i=0; i<flag; i++)
		{
			length /= 2;
			times = 0;
			for(j=0; j<number; j++)
			{
				w = 0;
				for(k=0; k<length; k++)
				{
					tmp1 = times+k;
					tmp2 = tmp1+length;
					xo = ori[tmp1];
					xg = goal[tmp1];
					yo = ori[tmp2];
					yg = goal[tmp2];
					ori[tmp1] = xo+yo;
					goal[tmp1] = xg+yg;
					xo = xo-yo;
					xg = xg-yg;
					ori[tmp2] = xo*cosChg[w]-xg*sinChg[w];
					goal[tmp2] = xo*sinChg[w]+xg*cosChg[w];
					w += number;
				}
				times += (2*length);
			}
			number *= 2;
		}
		averse(ori, len, flag, str);
		averse(goal, len, flag, str);
		if(opt == 1)
		{
			for(i=1; i<len; i+=2)
			{
				ori[i] = -ori[i];
				goal[i] = -goal[i];
			}
		}
		metric = (float)(1.0/Math.sqrt(len));
		for(i=0; i<len; i++)
		{
			ori[i] *= metric;
			goal[i] *= metric;
		}
	}
	/**
	 * 
	 * @param len 数组长度
	 * @param trans 变换方向
	 * @param sinChg 待改变sin值
	 * @param cosChg 待改变cos值
	 */
	private static void scResult(int len, int trans, float[] sinChg, float[] cosChg)
	{
		float arg;
		float xx = (float) (((-Math.PI) * 2) / len);
		if (trans == IDA)
			xx = -xx;
		int i;
		for (i = 0; i < len; i++)
		{
			arg = i * xx;
			sinChg[i] = (float) Math.sin(arg);
			cosChg[i] = (float) Math.cos(arg);
		}
	}
	private static void averse(float[] num1, int len, int flag, float[] num2)
	{
		int i, j, k, point;
		for (i = 0; i < len; i++)
		{
			for (j = 0, k = i, point = 0;; point <<= 1, k >>= 1)
			{
				point = (k & 1) | point;
				if (++j == flag)
					break;
			}
			num2[i] = num1[point];
		}
		for (i = 0; i < len; i++)
		{
			num1[i] = num2[i];
		}
	}
	private static void transform(float[][] num1, float[][] num2, int size, int leny)
	{
		int i, j;
		for(j = 0; j < leny; j++)
		{
			for(i = 0; i < size; i++)
			{
				num2[i][j] = num1[j][i];
			}
		}
	}
	/** 二维傅里叶快速变化呢
	 * 
	 * @param ori 实部（输入）
	 * @param goal 虚部（输出）
	 * @param trans 变换方向
	 * @param xLen 宽度
	 * @param yLen 高度
	 */
	public static void FFFor2(float[][] ori, float[][] goal, int trans, int xLen, int yLen)
	{
		int pow;
		int i;
		float[][] aim1 = new float[xLen][];
		float[][] aim2 = new float[xLen][];
		for (i = 0; i < xLen; i++)
		{
			aim1[i] = new float[yLen];
			aim2[i] = new float[yLen];
		}
		float[] sinT = new float[xLen];
		float[] cosT = new float[xLen];
		float[] sinTV = new float[yLen];
		float[] cosTV = new float[yLen];
		float[] styx = new float[xLen];
		float[] spry = new float[yLen];
		scResult(xLen, trans, sinT, cosT);
		scResult(yLen, trans, sinTV, cosTV);
		pow = (int) (Math.log(xLen) / Math.log(2));
		for (i = 0; i < yLen; i++)
		{
			mainFFT(ori[i], goal[i], xLen, pow, sinT, cosT, styx);
		}
		transform(ori, aim1, xLen, yLen);
		transform(goal, aim2, xLen, yLen);
		pow = (int) (Math.log(yLen) / Math.log(2));
		for (i = 0; i < xLen; i++)
		{
			mainFFT(aim1[i], aim2[i], yLen, pow, sinTV, cosTV, spry);
		}
		transform(aim1, ori, yLen, xLen);
		transform(aim2, goal, yLen, xLen);
	}
	/** 
	 * 小波变换
	 */
	public static void wavelet(float[] num1, int len1, float[] a, float[] b, int top,
							   float[] num2, float[] wave)
	{
		int i,j,key;
		for(i=0; i<len1/2; i++)
		{
			num2[i] = 0;
			wave[i] = 0;
			for(j=0; j<top; j++)
			{
				key = (j+2*i)%len1;
				num2[i] += a[j]*num1[key];
				wave[i] += b[j]*num1[key];
			}
		}
	}
	/**
	 * 二维小波变换
	 */
	public static void wavelet2d(float[][] num1, int len1, float[][] num2,
								 float[][] Wave, float[][] Have, float[][] Dave)
	{
		int i;
		float[][] numb1 = new float[len1][];
		float[][] wave1 = new float[len1][];
		for(i=0; i<len1; i++)
		{
			numb1[i] = new float[len1/2];
			wave1[i] = new float[len1/2];
		}
		float[][] numb1t = new float[len1/2][];
		float[][] wave1t = new float[len1/2][];
		for(i=0; i<len1/2; i++)
		{
			numb1t[i] = new float[len1];
			wave1t[i] = new float[len1];
		}
		float[][] nut1 = new float[len1/2][];
		float[][] Valet = new float[len1/2][];
		float[][] Havel = new float[len1/2][];
		float[][] Daven = new float[len1/2][];
		for(i=0; i<len1/2; i++)
		{
			nut1[i] = new float[len1/2];
			Valet[i] = new float[len1/2];
			Havel[i] = new float[len1/2];
			Daven[i] = new float[len1/2];
		}
		int top = 4;
		float[] a = new float[] {0.482962913145f, 0.836516303738f,
				0.224143868042f, -0.129409522551f};
		float[] b = new float[4];
		for(i=0; i<top; i++)
			b[i] = (float)Math.pow(-1, i)*a[top-i-1];
		for(i=0; i<len1; i++)
			wavelet(num1[i], len1, a, b, top, numb1[i], wave1[i]);
		transform(numb1, numb1t, len1/2, len1);
		transform(wave1, wave1t, len1/2, len1);
		for(i=0; i<len1/2; i++)
		{
			wavelet(numb1t[i], len1, a, b, top, nut1[i], Havel[i]);
			wavelet(wave1t[i], len1, a, b, top, Valet[i], Daven[i]);
		}
		transform(nut1, num2, len1/2, len1/2);
		transform(Havel, Have, len1/2, len1/2);
		transform(Valet, Wave, len1/2, len1/2);
		transform(Daven, Dave, len1/2, len1/2);
	}
	/**
	 * 小波分解
	 * @param source 图片来源
	 * @return 生成的图片
	 */
	public static BufferedImage decompose(BufferedImage source)
	{
		int i, j;
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		int len1 = Math.min(horizon, vertical);
		BufferedImage resultImage = new BufferedImage(len1, len1, BufferedImage.TYPE_INT_RGB);
		float[] remix = new float[3];
		float[][] num1 = new float[len1][];
		for(i=0; i<len1; i++)
			num1[i] = new float[len1];
		for(i=0; i<len1; i++)
		{
			for(j=0; j<len1; j++)
			{
				Process.convertRGBToRemix(sourceRGBs[i*horizon+j], remix);
				num1[i][j] = remix[0];
			}
		}
		float[][] num2 = new float[len1/2][];
		float[][] Wave = new float[len1/2][];
		float[][] Have = new float[len1/2][];
		float[][] Dave = new float[len1/2][];
		for(i=0; i<len1/2; i++)
		{
			num2[i] = new float[len1/2];
			Wave[i] = new float[len1/2];
			Have[i] = new float[len1/2];
			Dave[i] = new float[len1/2];
		}
		wavelet2d(num1, len1, num2, Wave, Have, Dave);
		float h = 0, l = 500;
		float Vh = 0, Vl = 500;
		float Hh = 0, Hl = 500;
		float Dh = 0, Dl = 500;
		for(i=0; i<len1/2; i++)
		{
			for(j=0; j<len1/2; j++)
			{
				if(h < num2[i][j]) h = num2[i][j];
				if(l > num2[i][j]) l = num2[i][j];
				if(Vh < Wave[i][j]) Vh = Wave[i][j];
				if(Vl > Wave[i][j]) Vl = Wave[i][j];
				if(Hh < Have[i][j]) Hh = Have[i][j];
				if(Hl > Have[i][j]) Hl = Have[i][j];
				if(Dh < Dave[i][j]) Dh = Dave[i][j];
				if(Dl > Dave[i][j]) Dl = Dave[i][j];
			}
		}
		int[] color = new int[3];
		for(i=0; i<len1/2; i++)
		{
			for(j=0; j<len1/2; j++)
			{
				Process.convertRGBToRemix(sourceRGBs[i*2*horizon+j*2], remix);
				remix[0] = (num2[i][j]-l)*255/(h-l);
				resultImage.setRGB(j, i, Process.convertRemixToRGB(remix));
				color[0] = (int)((Have[i][j]-Hl)*255/(Hh-Hl));
				color[1] = color[2] = color[0];
				resultImage.setRGB(j+len1/2, i, Process.mergeColor(color));
				color[0] = (int)((Wave[i][j]-Vl)*255/(Vh-Vl));
				color[1] = color[2] = color[0];
				resultImage.setRGB(j, i+len1/2, Process.mergeColor(color));
				color[0] = (int)((Dave[i][j]-Dl)*255/(Dh-Dl));
				color[1] = color[2] = color[0];
				resultImage.setRGB(j+len1/2, i+len1/2, Process.mergeColor(color));
			}
		}
		return resultImage;
	}
	/**
	 * 函数名称：图像平移 算法描述：给图像平移后的空白地方加入白色。
	 * 相当于扩大图像,多余部分为无色。
	 */
	public static BufferedImage translation(BufferedImage source, int a, int blue)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i, j;
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		for (j = 0; j < vertical; j++)
		{
			for (i = 0; i < horizon; i++)
			{
				int[] color = new int[3];
				color[0] = 255;
				color[1] = 255;
				color[2] = 255;
				resultImage.setRGB(i, j, Process.mergeColor(color));
			}
		}
		for (j = blue; j < vertical; j++)
		{
			for (i = a; i < horizon; i++)
			{
				int tmp = sourceRGBs[(j - blue) * horizon + i - a];
				resultImage.setRGB(i, j, tmp);
			}
		}
		return resultImage;
	}
	/**
	 * 函数名称：水平镜像 算法描述：将所有点存入数组，
	 * 然后通过偏移量offset得到图像镜像。
	 */
	public static BufferedImage horMirror(BufferedImage source)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i;
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		for (i = 0; i < vertical; i++)
		{
			int v = vertical - i - 1;
			resultImage.setRGB(0, v, horizon, 1, sourceRGBs, i * horizon, horizon);
		}
		return resultImage;
	}
	/**
	 * 函数名称：垂直镜像 算法描述：将所有点存入数组，对每一行的像素对互换。
	 */
	public static BufferedImage verMirror(BufferedImage source)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i, j;
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		for (i = 0; i < horizon; i++)
		{
			for (j = 0; j < vertical; j++)
			{
				int tmp = sourceRGBs[j * horizon + horizon - i - 1];
				resultImage.setRGB(i, j, tmp);
			}
		}
		return resultImage;
	}
	/*
	 * 函数名称：图像缩放 
	 * 算法描述：利用与或运算将像素点拆分3种颜色的值的集合，利用与或运算合并像素点。
	 * 本算法利用 二次差值的方法，差值针对像素点的每一种颜色。
	 */
	public static BufferedImage scale(BufferedImage source, float sx, float sy)
	{
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();
		int i, j;
		int[] sourceRGBs = source.getRGB(0, 0, sourceWidth, sourceHeight, null, 0, sourceWidth);
		int fHorizon = Math.round(sourceWidth * sx);
		int fVertical = Math.round(sourceHeight * sy);
		BufferedImage resultImage = new BufferedImage(fHorizon, fVertical, BufferedImage.TYPE_INT_RGB);
		int a, b, c, d;
		float d1, d2, t1, t2;
		int[] point1 = new int[3];
		int[] point2 = new int[3];
		int[] point3 = new int[3];
		int[] point4 = new int[3];
		int[] color = new int[3];
		for (j = 0; j < fVertical; j++)
		{
			d2 = j / sy;
			a = (int) d2;
			t2 = d2 - a;
			b = a + 1 >= sourceHeight ? a : a + 1;
			for (i = 0; i < fHorizon; i++)
			{
				d1 = i / sx;
				c = (int) d1;
				t1 = d1 - c;
				d = c + 1 >= sourceWidth ? c : c + 1;
				Process.getColor(sourceRGBs[a * sourceWidth + c], point1);
				Process.getColor(sourceRGBs[a * sourceWidth + d], point2);
				Process.getColor(sourceRGBs[b * sourceWidth + c], point3);
				Process.getColor(sourceRGBs[b * sourceWidth + d], point4);
				for (int k = 0; k < 3; k++)
				{
					color[k] = Math.round(Process.biLinear(point1[k], point2[k], point3[k], point4[k], t1, t2));
				}
				resultImage.setRGB(i, j, Process.mergeColor(color));
			}
		}
		return resultImage;
	}
	/*
	 * 函数名称：图像旋转 
	 * 算法描述：(1)求最大尺寸分截取匹配、扩大匹配
	 * (2)在使用原坐标点和变换坐标点的函数时候，必须注意坐标系的变化，
	 * 从扩大匹配的坐标系转到截取匹配的坐标系容易理解！
	 * (3)对点进行二次差值，一个一个写入。
	 */
	public static BufferedImage rotate(BufferedImage source, float angle, boolean isResize)
	{
		int i, j;
		int a, b, c, d;
		float d1, d2, t1, t2;
		int[] point1 = new int[3];
		int[] point2 = new int[3];
		int[] point3 = new int[3];
		int[] point4 = new int[3];
		int[] color = new int[3];
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();
		int[] sourceRGBs = source.getRGB(0, 0, sourceWidth, sourceHeight, null, 0, sourceWidth);
		float sAngle = (float) Math.sin(angle);
		float cAngle = (float) Math.cos(angle);
		//求旋转后图像的尺寸
		float fX, fY, sX, bX, sY, bY;
		float[] num1 = new float[3];
		float[] num2 = new float[3];
		if (isResize)
		{
			sX = 0;
			bX = 0;
			sY = 0;
			bY = 0;
			num1[0] = sourceWidth - 1;
			num2[0] = 0;
			num1[1] = 0;
			num2[1] = sourceHeight - 1;
			num1[2] = sourceWidth - 1;
			num2[2] = sourceHeight - 1;
			for (i = 0; i < 3; i++)
			{
				d1 = num2[i] * sAngle + num1[i] * cAngle;
				if (d1 < sX)
					sX = d1;
				if (d1 > bX)
					bX = d1;
				d2 = num2[i] * cAngle - num1[i] * sAngle;
				if (d2 < sY)
					sY = d2;
				if (d2 > bY)
					bY = d2;
			}
			fX = sX;
			fY = sY;
		}
		else
		{
			sX = 0;
			bX = sourceWidth - 1;
			sY = 0;
			bY = sourceHeight - 1;
			num1[0] = (float)(sourceWidth / 2);
			num2[0] = (float)(sourceHeight / 2);
			d1 = num2[0] * sAngle + num1[0] * cAngle;
			d2 = num2[0] * cAngle - num1[0] * sAngle;
			fX = d1 - (float)(sourceWidth / 2);
			fY = d2 - (float)(sourceHeight / 2);
		}
		int fHorizon = Math.round(bX - sX + 1);
		int fVertical = Math.round(bY - sY + 1);
		BufferedImage resultImage = new BufferedImage(fHorizon, fVertical, BufferedImage.TYPE_INT_RGB);
		// 旋转图像
		for (j = 0; j < fVertical; j++)
		{
			for (i = 0; i < fHorizon; i++)
			{
				d2 = (i + fX) * sAngle + (j + fY) * cAngle;
				d1 = (i + fX) * cAngle - (j + fY) * sAngle;
				a = (int) d2;
				c = (int) d1;
				t1 = d1 - c;
				t2 = d2 - a;
				if (a >= 0 && a < sourceHeight && c >= 0 && c < sourceWidth)
				{
					b = a + 1 >= sourceHeight ? a : a + 1;
					d = c + 1 >= sourceWidth ? c : c + 1;
					Process.getColor(sourceRGBs[a * sourceWidth + c], point1);
					Process.getColor(sourceRGBs[a * sourceWidth + d], point2);
					Process.getColor(sourceRGBs[b * sourceWidth + c], point3);
					Process.getColor(sourceRGBs[b * sourceWidth + d], point4);
					for (int k = 0; k < 3; k++)
					{
						color[k] = Math.round(Process.biLinear(point1[k], point2[k], point3[k], point4[k], t1, t2));
					}
				}
				else
				{
					for (int k = 0; k < 3; k++)
						color[k] = 255;
				}
				resultImage.setRGB(i, j, Process.mergeColor(color));
			}
		}
		return resultImage;
	}
}
