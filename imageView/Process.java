package imageView;

import java.awt.image.BufferedImage;
// Process类是设计时用到的数学方法和工具函数
public class Process
{
	/** 取得三色 **/
	public static void getColor(int color, int [] mix)
	{
		if (mix == null)
			mix = new int[3];
		mix[0] = (color & 0x00ff0000) >> 16;
		mix[1] = (color & 0x0000ff00) >> 8;
		mix[2] = (color & 0x000000ff);
	}
	/** 取得单色 **/
	public static int getColor(int color)
	{
		return (color & 0x000000ff);
	}
	/** 合并三色 **/
	public static int mergeColor(int[] mix)
	{
		return (255 << 24) | (mix[0] << 16) | (mix[1] << 8) | mix[2];
	}
	/** 灰度化计算 **/
	public static int getBrightness(int color)
	{
		int red = (color & 0x00ff0000) >> 16;
		int green = (color & 0x0000ff00) >> 8;
		int blue = (color & 0x000000ff);
		int result = Math.round(0.3f * red + 0.59f * green + 0.11f * blue);
		result = Math.max(result, 0);
		result = Math.min(result, 255);
		return result;
	}
	/** 取得灰度最大值 **/
	public static int getMaxGray(int [] sourceRGBs)
	{
		int max = 0;
        for (int sourceRGB : sourceRGBs)
        {
            if (sourceRGB > max)
            {
                max = sourceRGB;
            }
        }
		return max;
	}
	/** RGB_TO_Remix **/
	public static void convertRGBToRemix(int color, float [] remix)
	{
		if (remix == null)
			remix = new float[3];
		int red = (color & 0x00ff0000) >> 16;
		int green = (color & 0x0000ff00) >> 8;
		int blue = (color & 0x000000ff);
		remix[0] = (float) (0.3 * red + 0.59 * green + 0.11 * blue);
		double tmp1 = 0.7 * red - 0.59 * green - 0.11 * blue;
		double tmp2 = -0.3 * red - 0.59 * green + 0.89 * blue;
		remix[2] = (float) Math.sqrt(tmp1 * tmp1 + tmp2 * tmp2);
		if (remix[2] < 0.005)
			remix[1] = 0;
		else
		{
			remix[1] = (float) Math.atan2(tmp1, tmp2);
			if (remix[1] < 0)
				remix[1] += (float) Math.PI * 2;
		}
	}
	/** Remix_TO_RGB **/
	public static int convertRemixToRGB(float[] remix)
	{
		double tmp1 = remix[2] * Math.sin(remix[1]);
		double tmp2 = remix[2] * Math.cos(remix[1]);
		int red = (int) Math.round(remix[0] + tmp1);
		red = Math.max(red, 0);
		red = Math.min(red, 255);
		int green = (int) Math.round(remix[0] - 0.3 * tmp1 / 0.9 - 0.11 * tmp2 / 0.59);
		green = Math.max(green, 0);
		green = Math.min(green, 255);
		int blue = (int) Math.round(remix[0] + tmp2);
		blue = Math.max(blue, 0);
		blue = Math.min(blue, 255);
		return (255 << 24) | (red << 16) | (green << 8) | blue;
	}
	/** 灰度线性变换计算**/
	public static int linearFunction(int n1 ,int n2, int n3, int n4,int n5)
	{
		return(n4-n3)*(n5-n1)/(n2-n1)+n3;
	}
	/** 双线性插值*/
	public static float biLinear(int p0, int p1, int p2, int p3,
								 float m, float n)
	{
		return (1-n)*( (1-m)*p0+m*p1 ) + n*( (1-m)*p2+m*p3 );
	}
	/** 函数名称：卷积计算  **/
	public static BufferedImage convolve(BufferedImage source, float[] num)
	{
		int vertical = source.getHeight();
		int horizon = source.getWidth();
		int i,j;
		int[] sourceRGBs = new int[(vertical+2)*(horizon+2)];
		source.getRGB(0, 0, horizon, vertical, sourceRGBs, horizon+3, horizon+2);
		//给四周的空白赋值
		for(i=1; i<horizon; i++)
		{
			sourceRGBs[i] = sourceRGBs[i+horizon+2];
			sourceRGBs[i+(vertical+1)*(horizon+2)] = sourceRGBs[i+vertical*(horizon+2)];
		}
		for(j=0; j<vertical+2; j++)
		{
			sourceRGBs[j*(horizon+2)] = sourceRGBs[j*(horizon+2)+1];
			sourceRGBs[j*(horizon+2)+horizon+1] = sourceRGBs[j*(horizon+2)+horizon];
		}
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		float[] floor = new float[3];
		int[] color = new int[3];
		int[][] colors = new int[9][];
		for(j=0; j<9; j++) colors[j] = new int[3];
		for(i=1; i<=vertical; i++)
		{
			for(j=1; j<=horizon; j++)
			{
				int tmp1 = (i-1)*(horizon+2)+j;
				Process.getColor(sourceRGBs[tmp1-1], colors[0]);
				Process.getColor(sourceRGBs[tmp1], colors[1]);
				Process.getColor(sourceRGBs[tmp1+1], colors[2]);
				int tmp2 = (i)*(horizon+2)+j;
				Process.getColor(sourceRGBs[tmp2-1], colors[3]);
				Process.getColor(sourceRGBs[tmp2], colors[4]);
				Process.getColor(sourceRGBs[tmp2+1], colors[5]);
				int tmp3 = (i+1)*(horizon+2)+j;
				Process.getColor(sourceRGBs[tmp3-1], colors[6]);
				Process.getColor(sourceRGBs[tmp3], colors[7]);
				Process.getColor(sourceRGBs[tmp3+1], colors[8]);
				floor[0] = floor[1] = floor[2] = 0;
				//卷积运算
				for(int k=0; k<9; k++)
				{
					floor[0] += num[k]*colors[k][0];
					floor[1] += num[k]*colors[k][1];
					floor[2] += num[k]*colors[k][2];
				}
				color[0] = (int)floor[0];
				color[0] = Math.max(color[0], 0);
				color[0] = Math.min(color[0], 255);
				color[1] = (int)floor[1];
				color[1] = Math.max(color[1], 0);
				color[1] = Math.min(color[1], 255);
				color[2] = (int)floor[2];
				color[2] = Math.max(color[2], 0);
				color[2] = Math.min(color[2], 255);
				resultImage.setRGB(j-1, i-1, Process.mergeColor(color));
			}
		}
		return resultImage;
	}
}
