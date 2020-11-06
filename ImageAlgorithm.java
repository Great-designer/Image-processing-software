package imageView;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ImageAlgorithm
{
	/**
	 * grayScale：灰度变换_灰度化
	 */
	public static BufferedImage grayScale(BufferedImage source)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i, j;
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		int[] color = new int[3];
		for (i = 0; i < horizon; i++)
		{
			for (j = 0; j < vertical; j++)
			{
				int tmp = j * horizon + i;
				color[0] = Process.getBrightness(sourceRGBs[tmp]);
				color[1] = color[2] = color[0];
				resultImage.setRGB(i, j, Process.mergeColor(color));
			}
		}
		return resultImage;
	}
	/**
	 * linearTransformation：灰度变换_线性变换
	 */
	public static BufferedImage linearTransformation(BufferedImage source, int a, int b, int c, int d)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i, j;
		int[] sourceRGBs = grayScale(source).getRGB(0, 0, horizon, vertical, null, 0, horizon);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		int[] color = new int[3];
		for (i = 0; i < horizon; i++)
		{
			for (j = 0; j < vertical; j++)
			{
				int tmp = j * horizon + i;
				if (Process.getColor(sourceRGBs[tmp]) <= b & Process.getColor(sourceRGBs[tmp]) >= a)
				{
					color[0] = Process.linearFunction(a, b, c, d, Process.getColor(sourceRGBs[tmp]));
				}
				else
				{
					color[0] = sourceRGBs[tmp];
				}
				color[1] = color[2] = color[0];
				resultImage.setRGB(i, j, Process.mergeColor(color));
			}
		}
		return resultImage;
	}
	/**
	 * NotSegmentationLinearTransformation：线性变换_分段线性变换
	 */
	public static BufferedImage NotSegmentationLinearTransformation(BufferedImage source, int a, int b, int c, int d)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i, j;
		int[] sourceRGBs = grayScale(source).getRGB(0, 0, horizon, vertical, null, 0, horizon);
		int max = Process.getMaxGray(sourceRGBs);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		int[] color = new int[3];
		for (i = 0; i < horizon; i++)
		{
			for (j = 0; j < vertical; j++)
			{
				int tmp = j * horizon + i;
				if (Process.getColor(sourceRGBs[tmp]) < a)
				{
					color[0] = (c / a) * Process.getColor(sourceRGBs[tmp]);
					color[1] = color[2] = color[0];
					resultImage.setRGB(i, j, Process.mergeColor(color));
				}
				else if (Process.getColor(sourceRGBs[tmp]) < b & Process.getColor(sourceRGBs[tmp]) >= a)
				{
					color[0] = Process.linearFunction(a, b, c, d, Process.getColor(sourceRGBs[tmp]));
					color[1] = color[2] = color[0];
					resultImage.setRGB(i, j, Process.mergeColor(color));
				}
				else if (Process.getColor(sourceRGBs[tmp]) < max - 1
						& Process.getColor(sourceRGBs[tmp]) >= b)
				{
					color[0] = Process.linearFunction(b, max - 1, d, max - 1,
							Process.getColor(sourceRGBs[tmp]));
					color[1] = color[2] = color[0];
					resultImage.setRGB(i, j, Process.mergeColor(color));
				}
				else
				{
					resultImage.setRGB(i, j, sourceRGBs[tmp]);
				}
			}
		}
		return resultImage;
	}
	/**
	 * getHistInfo：直方图修正_绘制直方图
	 */
	public static int [] getHistInfo(BufferedImage source, int [] histArray)
	{
		if (histArray == null)
			histArray = new int[256];
		int i, j;
		for (i = 0; i < 256; i++)
			histArray[i] = 0;
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		float[] remix = new float[3];
		for (i = 0; i < horizon; i++)
		{
			for (j = 0; j < vertical; j++)
			{
				int tmp = j * horizon + i;
				Process.convertRGBToRemix(sourceRGBs[tmp], remix);
				int graph = Math.round(remix[0]);
				graph = Math.max(graph, 0);
				graph = Math.min(graph, 255);
				histArray[graph]++;
			}
		}
		return histArray;
	}
	/**
	 * medianFiltering：中值滤波
	 */
	public static BufferedImage medianFiltering(BufferedImage source)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i, j;
		int[] sourceRGBs = new int[(vertical + 2) * (horizon + 2)];
		source.getRGB(0, 0, horizon, vertical, sourceRGBs, horizon + 3, horizon + 2);
		for (i = 1; i < horizon; i++)
		{
			sourceRGBs[i] = sourceRGBs[i + horizon + 2];
			sourceRGBs[i + (vertical + 1) * (horizon + 2)] = sourceRGBs[i + vertical * (horizon + 2)];
		}
		for (i = 0; i < vertical + 2; i++)
		{
			sourceRGBs[i * (horizon + 2)] = sourceRGBs[i * (horizon + 2) + 1];
			sourceRGBs[i * (horizon + 2) + horizon + 1] = sourceRGBs[i * (horizon + 2) + horizon];
		}
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		int[] color = new int[3];
		int[] sRed = new int[9];
		int[] sGreen = new int[9];
		int[] sBlue = new int[9];
		for (j = 1; j <= vertical; j++)
		{
			for (i = 1; i <= horizon; i++)
			{
				int tmp1 = (j - 1) * (horizon + 2) + i;
				Process.getColor(sourceRGBs[tmp1 - 1], color);
				sRed[0] = color[0];
				sGreen[0] = color[1];
				sBlue[0] = color[2];
				Process.getColor(sourceRGBs[tmp1], color);
				sRed[1] = color[0];
				sGreen[1] = color[1];
				sBlue[1] = color[2];
				Process.getColor(sourceRGBs[tmp1 + 1], color);
				sRed[2] = color[0];
				sGreen[2] = color[1];
				sBlue[2] = color[2];
				int tmp2 = (j) * (horizon + 2) + i;
				Process.getColor(sourceRGBs[tmp2 - 1], color);
				sRed[3] = color[0];
				sGreen[3] = color[1];
				sBlue[3] = color[2];
				Process.getColor(sourceRGBs[tmp2], color);
				sRed[4] = color[0];
				sGreen[4] = color[1];
				sBlue[4] = color[2];
				Process.getColor(sourceRGBs[tmp2 + 1], color);
				sRed[5] = color[0];
				sGreen[5] = color[1];
				sBlue[5] = color[2];
				int tmp3 = (j + 1) * (horizon + 2) + i;
				Process.getColor(sourceRGBs[tmp3 - 1], color);
				sRed[6] = color[0];
				sGreen[6] = color[1];
				sBlue[6] = color[2];
				Process.getColor(sourceRGBs[tmp3], color);
				sRed[7] = color[0];
				sGreen[7] = color[1];
				sBlue[7] = color[2];
				Process.getColor(sourceRGBs[tmp3 + 1], color);
				sRed[8] = color[0];
				sGreen[8] = color[1];
				sBlue[8] = color[2];
				Arrays.sort(sRed);
				Arrays.sort(sGreen);
				Arrays.sort(sBlue);
				color[0] = sRed[4];
				color[1] = sGreen[4];
				color[2] = sBlue[4];
				resultImage.setRGB(i - 1, j - 1, Process.mergeColor(color));
			}
		}
		return resultImage;
	}
	/**
	 * gaussianSmoothing：高斯平滑
	 */
	public static BufferedImage gaussianSmoothing(BufferedImage source)
	{
		float[] keep = new float[] { 1.0f / 16, 2.0f / 16, 1.0f / 16, 2.0f / 16, 4.0f / 16, 2.0f / 16, 1.0f / 16,
				2.0f / 16, 1.0f / 16 };
		return Process.convolve(source, keep);
	}
	/**
	 * fieldAverage：领域平均
	 */
	public static BufferedImage fieldAverage(BufferedImage source)
	{
		float[] keep = new float[] { 1.0f / 9, 1.0f / 9, 1.0f / 9, 1.0f / 9, 1.0f / 9, 1.0f / 9, 1.0f / 9, 1.0f / 9,
				1.0f / 9 };
		return Process.convolve(source, keep);
	}
	/**
	 * laplacian：拉普拉斯变换
	 */
	public static BufferedImage laplacian(BufferedImage source)
	{
		float[] keep = new float[] { -1, -1, -1, -1, 8, -1, -1, -1, -1 };
		return Process.convolve(source, keep);
	}
	/**
	 * laplacianHiBoostFiltering：拉普拉斯高增滤波
	 */
	public static BufferedImage laplacianHiBoostFiltering(BufferedImage source)
	{
		float[] keep = new float[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
		return Process.convolve(source, keep);
	}
	/**
	 * gaussianHiBoostFiltering：高斯平滑高增滤波
	 */
	public static BufferedImage gaussianHiBoostFiltering(BufferedImage source)
	{
		float[] keep = new float[] { -3.0f / 80, -6.0f / 80, -3.0f / 80, -6.0f / 80, 29f / 20, -6.0f / 80,
				-3.0f / 80, -6.0f / 80, -3.0f / 80 };
		return Process.convolve(source, keep);
	}
	/**
	 * findLine：查找直线
	 */
	public static void findLine(BufferedImage source)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i;
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		int dur1 = 0, dur2 = 0, dur3, dur4 = 0;
		for (i = 0; i < sourceRGBs.length; i++)
		{
			dur1 = sourceRGBs[i] == 0xff000000 ? dur1 + 1 : dur1;
		}
		int[] srcBlackX = new int[dur1];
		int[] srcBlackY = new int[dur1];
		for (i = 0; i < sourceRGBs.length; i++)
		{
			if (sourceRGBs[i] == 0xff000000)
			{
				srcBlackX[dur2] = i % horizon;
				srcBlackY[dur2] = i / horizon;
				dur2++;
			}
		}
		int[] graNum = new int[90];// 存储斜率的个数。
		int[] graX = new int[90];// 记录斜率的第一个X值
		int[] graY = new int[90];// 记录斜率的第一个Y值
		for (i = 0; i < graX.length; i++)
		{
			graX[i] = graY[i] = -1;
			graNum[i] = 0;
		}
		for (i = 0; i < srcBlackX.length; i++)
		{
			int random = (int) (Math.random() * dur1);
			dur3 = (int) ((srcBlackX[i] - srcBlackX[random]) == 0 ? 45
					: Math.atan(Math.abs((double)(srcBlackY[i] - srcBlackY[random])) / Math.abs((double)(srcBlackX[i] - srcBlackX[random])))
							* 90 / Math.PI);
			graNum[dur3] = graNum[dur3] + 1;
			if (graX[dur3] == -1)
			{
				graX[dur3] = srcBlackX[i];
			}
			if (graY[dur3] == -1)
			{
				graY[dur3] = srcBlackY[i];
			}
		}
		int arg = 0;
		int[] result = new int[90];
		for (i = 0; i < result.length; i++)
		{
			result[i] = -1;
		}
		for (i = 0; i < graNum.length; i++)
		{
			arg = graNum[i] + arg;
		}
		arg = arg / graNum.length;
		for (i = 0; i < graNum.length; i++)
		{
			if (graNum[i] > 5 * arg)
			{
				result[dur4] = graX[i];
				dur4++;
				result[dur4] = graY[i];
				dur4++;
				result[dur4] = graNum[i];
				dur4++;
			}
		}
		for (i = 0; i < result.length; i++)
		{
			if (result[i] != -1)
			{
				System.out.println(result[i]);
			}
		}
	}
	/**
	 * threshold：简单阈值分割
	 */
	public static BufferedImage threshold(BufferedImage source, int t)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i;
		int[] sColors = ImageAlgorithm.grayScale(source).getRGB(0, 0, horizon, vertical, null, 0, horizon);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		for (i = 0; i < sColors.length; i++)
		{
			if ((sColors[i] & 0x000000ff) <= t)
			{
				sColors[i] = 0xff000000;
			}
			else
			{
				sColors[i] = 0xffffffff;
			}
		}
		resultImage.setRGB(0, 0, horizon, vertical, sColors, 0, horizon);
		return resultImage;
	}
	/**
	 * iterative：迭代阈值分割
	 */
	public static BufferedImage iterative(BufferedImage source, int x1, int x2)
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int x = 0, t1 = 0, t2 = 0, num1 = 0, num2 = 0, i;
		boolean check = true;
		int[] sColors = ImageAlgorithm.grayScale(source).getRGB(0, 0, horizon, vertical, null, 0, horizon);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		while (check)
		{
			for (i = 0; i < sColors.length; i++)
			{
				if ((sColors[i] & 0x000000ff) < x2)
				{
					t1 = t1 + (sColors[i] & 0x000000ff);
					num1++;
				}
				else
				{
					t2 = t2 + (sColors[i] & 0x000000ff);
					num2++;
				}
			}
			int result1 = Math.round((float)(t1 / num1));
			int result2 = Math.round((float)(t2 / num2));
			if (Math.abs((result2 - result1) / 2 - x2) <= x1)
			{
				check = false;
			}
			else
			{
				x2 = x;
			}
		}
		for (i = 0; i < sColors.length; i++)
		{
			if ((sColors[i] & 0x000000ff) <= x2)
			{
				sColors[i] = 0xff000000;
			}
			else
			{
				sColors[i] = 0xffffffff;
			}
		}
		resultImage.setRGB(0, 0, horizon, vertical, sColors, 0, horizon);
		return resultImage;
	}
	/**
	 * oahu：Oahu法
	 */
	public static BufferedImage oahu(BufferedImage source)
	{
		// A:拿到所有需要的数据,首先定义所有数据
		int[] graph;// 直方图数组
		int avg, avg0, avg1; // 全图平均灰度值μT
		double p0, p1; // 第一个区间的概率ω1
		int threshold = 0, t = 0, t1 = 0; // 最终确定的阈值
		int i,j;
		double t2, t3 = 0; // 中间变量
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		// ①：利用以前写过的函数对直方图数组其赋值
		graph = ImageAlgorithm.getHistInfo(source, null);
		// ②：计算平均灰度值
		for (i = 0; i < graph.length; i++)
		{
			t = t + graph[i] * i;
		}
		avg = t / (horizon * vertical);
		// B:计算最合适阈值
		t = 0;
		for (i = 0; i < graph.length; i++)
		{
			for (j = 0; j < i; j++)
			{
				t = t + graph[j] * j;
				t1 = t1 + graph[j];
			}
			avg0 = t1 == 0 ? 0 : t / t1;
			p0 = (double)(t1 / horizon) * vertical;
			t2 = p0 * (avg0 - avg) * (avg0 - avg);
			t = t1 = 0;
			for (j = i; j < graph.length; j++)
			{
				t = t + graph[j] * j;
				t1 = t1 + graph[j];
			}
			avg1 = t1 == 0 ? 0 : t / t1;
			p1 = (double)(t1 / horizon) * vertical;
			t2 = t2 + p1 * (avg1 - avg) * (avg1 - avg);
			if (t3 < t2) {
				t3 = t2;
				threshold = i;
			}
		}
		// C:进行阈值分割
		return threshold(source, threshold);
	}
	/**
	 * dynamic：动态阈值分割法
	 */
	public static BufferedImage dynamic(BufferedImage source)
	{
		// A：截图成为长宽为4的倍数，直接对数组操作
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int j,i;
		final int i1 = (horizon - horizon % 4) * (vertical - vertical % 4);
		int[] fColors = new int[i1];
		int[] sColors = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		for (i = 0; i < vertical - vertical % 4; i++)
		{
			for (j = 0; j < horizon - horizon % 4; j++)
			{
				fColors[i * (horizon - horizon % 4) + j] = sColors[i * horizon + j];
			}
		}
		// B:分成16个小图像,并且进行oahu阈值分割
		for (i = 0; i < vertical - vertical % 4; i = i + (vertical - vertical % 4) / 4)
		{
			for (j = 0; j < horizon - horizon % 4; j = j + (horizon - horizon % 4) / 4)
			{
				BufferedImage tempImage = new BufferedImage((horizon - horizon % 4) / 4, (vertical - vertical % 4) / 4,
						BufferedImage.TYPE_INT_RGB);
				int[] tColors = new int[i1 / 16];
				for (int k = 0; k < (vertical - vertical % 4) / 4; k++)
				{
					for (int l = 0; l < (horizon - horizon % 4) / 4; l++)
					{
						tColors[k * (horizon - horizon % 4) / 4 + l] = fColors[(k + i) * (horizon - horizon % 4) + j
								+ l];
					}
				}
				tempImage.setRGB(0, 0, (horizon - horizon % 4) / 4, (vertical - vertical % 4) / 4, tColors, 0,
						(horizon - horizon % 4) / 4);
				tempImage = oahu(tempImage);
				tColors = tempImage.getRGB(0, 0, (horizon - horizon % 4) / 4, (vertical - vertical % 4) / 4, null, 0,
						(horizon - horizon % 4) / 4);
				for (int k = 0; k < (vertical - vertical % 4) / 4; k++)
				{
					for (int l = 0; l < (horizon - horizon % 4) / 4; l++)
					{
						fColors[(k + i) * (horizon - horizon % 4) + j + l] = tColors[k * (horizon - horizon % 4) / 4
								+ l];
					}
				}
			}
		}
		// C:返回新图像
		BufferedImage resultImage = new BufferedImage((horizon - horizon % 4), (vertical - vertical % 4),
				BufferedImage.TYPE_INT_RGB);
		resultImage.setRGB(0, 0, horizon - horizon % 4, vertical - vertical % 4, fColors, 0, horizon - horizon % 4);
		return resultImage;
	}
}
