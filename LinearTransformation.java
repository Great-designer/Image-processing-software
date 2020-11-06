package imageView;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class LinearTransformation extends JDialog
{
	private BufferedImage source;
	private BufferedImage linearImg;
	Panel imgPanel;
    JScrollPane scrollPane;
    JButton trueBtn;
    //鼠标第一、二次单击坐标位置
    private int xValue,yValue,xValue1,yValue1;
    //是否为第二个鼠标点击
    private int isSecond = 0;   
    //代替确认按钮，在主界面响应函数中判断
    private int trueOption = 0;
    //最大最小灰度百分比，百分表示
	private int min, max;	    
	private final int leftMargin = 40;
	private final int topMargin = 20;
	public LinearTransformation(java.awt.Frame ancestor, boolean successor)
	{
        super(ancestor,successor);      
        initComponents();
        setTitle("坐标框架");
    }
	/**
	 * initComponents方法用于初始化组件
	 */
    void initComponents()
	{
    	Container contentPane = getContentPane();
    	imgPanel = new Panel(linearImg);
        scrollPane = new JScrollPane(imgPanel);
        trueBtn = new JButton("确定");
        imgPanel.add(trueBtn,BorderLayout.EAST);
    	contentPane.add(scrollPane,BorderLayout.CENTER);
        imgPanel.addMouseListener(new MouseListener()
		{
            @Override 
            public void mouseClicked(MouseEvent e)
			{
        		Graphics g1 = imgPanel.getGraphics();
                if(e.getClickCount() == 1)
                {
                	isSecond = isSecond+1;
                }
            	if(isSecond == 1)
            	{
                    xValue = e.getX();
                    yValue = e.getY();
            	    g1.drawOval(xValue-5, yValue-5, 10, 10);
            	}
            	if(isSecond == 2)
            	{
            		xValue1 = e.getX();
            		yValue1 = e.getY();
            	    g1.drawOval(xValue1-5, yValue1-5, 10, 10);
            	    g1.drawLine(xValue, yValue, xValue1, yValue1);
            	    trueOption = 1;
            	}
            }
            public void mousePressed(MouseEvent e){}
            @Override 
            public void mouseReleased(MouseEvent e){}
            @Override 
            public void mouseEntered(MouseEvent e){}
            @Override 
            public void mouseExited(MouseEvent e){}
		});
    	setSize(new Dimension(800, 600));
    }
	int getLinearWidth()
	{
    	return 256*2;
    }
    int getLinearHeight()
	{
    	return 256*2;
    }
    //确定画线x点
    int drawLineX(int lineX)
	{
		//灰度/像素
		int scaleX = 2;
		return leftMargin+lineX* scaleX;
    }
    //确定画线的y点
    int drawLineY(int lineY)
	{
		//百分比/像素
		int scaleY = 2;
		return topMargin+(max-lineY)* scaleY;
    }
    //计算线性变换信息
    void calculateLinearInfo()
	{
    	int horizon = source.getWidth(); 
		int vertical = source.getHeight();
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
	    max = min = 0;
        for (int sourceRGB : sourceRGBs)
        {
            max = Math.max(Process.getBrightness(sourceRGB), max);
            min = Math.min(Process.getBrightness(sourceRGB), min);
        }
		int BMargin = 30;
		int RMargin = 20;
		linearImg = new BufferedImage(getLinearWidth()+leftMargin+ RMargin,
    								  getLinearHeight()+topMargin+ BMargin,
    								  BufferedImage.TYPE_INT_RGB);
    	imgPanel.setImage(linearImg);
    }
    //画线
    void drawLinear()
	{
   		Graphics g = linearImg.getGraphics();
   		g.setColor(Color.white);
   		g.fillRect(0, 0, linearImg.getWidth(), linearImg.getHeight());
   		drawCoordinate(g);
    }
    //坐标轴需要的图形
    void drawCoordinate(Graphics g)
	{
    	int P1x, P1y;
    	g.setColor(Color.black);
    	//x轴和箭头
    	P1x = drawLineX(256)+15;
    	P1y = drawLineY(0);
    	g.drawLine(drawLineX(0), P1y, P1x, P1y);
    	g.drawLine(P1x-5, P1y-5, P1x, P1y);
    	g.drawLine(P1x-5, P1y+5, P1x, P1y);
    	//y轴和箭头
    	P1x = drawLineX(0);
    	P1y = drawLineY(256)-15;
    	g.drawLine(P1x, drawLineY(0), P1x, P1y);
    	g.drawLine(P1x-5, P1y+5, P1x, P1y);
    	g.drawLine(P1x+5, P1y+5, P1x, P1y);
    	//y轴的横线，标识灰度级站的比例
    	g.setColor(Color.gray);
    	for(int i=5; i<256; i+=5)
    	{
    		g.drawLine(drawLineX(0), drawLineY(i), drawLineX(256), drawLineY(i));	
    		g.drawLine(drawLineX(i), drawLineY(0), drawLineX(i), drawLineY(256));
    	}
    	//封装线
    	g.setColor(Color.red);
    	g.drawLine(drawLineX(min), drawLineY(min), drawLineX(min), drawLineY(max));
    	g.drawLine(drawLineX(min), drawLineY(min), drawLineX(max), drawLineY(min));
    	g.drawLine(drawLineX(min), drawLineY(max), drawLineX(max), drawLineY(max));
    	g.drawLine(drawLineX(max), drawLineY(max), drawLineX(max), drawLineY(min));
        //y轴单位长度
    	for(int i=20; i<=256; i+=20)
    	{
    		P1x = drawLineX(0);
    		P1y = drawLineY(i);
    		g.setColor(Color.black);
    		g.drawLine(P1x, P1y, P1x-5, P1y);
    		String str = String.valueOf(i);
    		g.drawString(str, P1x-25, P1y+4);		
    	}
    	//x轴单位长度
    	for(int i=0; i<256; i+=20)
    	{
    		P1x = drawLineX(i);
    		P1y = drawLineY(0);
    		g.drawLine(P1x, P1y, P1x, P1y+5);
    		String str = String.valueOf(i);
    		int strWidth = g.getFontMetrics().stringWidth(str);
    		g.drawString(str, P1x-strWidth/2, P1y+20);	
    	}
    }
    public void setImage(BufferedImage image)
	{
    	source = image;
    	calculateLinearInfo();
    	drawLinear();
    }
    public int getValue()
	{
		return xValue;
	}
    public int getValue1()
	{
    	return xValue1;
    }
    public int getYValue()
	{
    	return yValue;
    }
    public int getYValue1()
	{
    	return yValue1;
    }
	public int getTrueOption()
	{
		return trueOption;
	}
}
