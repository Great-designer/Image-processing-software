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
    //����һ�����ε�������λ��
    private int xValue,yValue,xValue1,yValue1;
    //�Ƿ�Ϊ�ڶ��������
    private int isSecond = 0;   
    //����ȷ�ϰ�ť������������Ӧ�������ж�
    private int trueOption = 0;
    //�����С�ҶȰٷֱȣ��ٷֱ�ʾ
	private int min, max;	    
	private final int leftMargin = 40;
	private final int topMargin = 20;
	public LinearTransformation(java.awt.Frame ancestor, boolean successor)
	{
        super(ancestor,successor);      
        initComponents();
        setTitle("������");
    }
	/**
	 * initComponents�������ڳ�ʼ�����
	 */
    void initComponents()
	{
    	Container contentPane = getContentPane();
    	imgPanel = new Panel(linearImg);
        scrollPane = new JScrollPane(imgPanel);
        trueBtn = new JButton("ȷ��");
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
    //ȷ������x��
    int drawLineX(int lineX)
	{
		//�Ҷ�/����
		int scaleX = 2;
		return leftMargin+lineX* scaleX;
    }
    //ȷ�����ߵ�y��
    int drawLineY(int lineY)
	{
		//�ٷֱ�/����
		int scaleY = 2;
		return topMargin+(max-lineY)* scaleY;
    }
    //�������Ա任��Ϣ
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
    //����
    void drawLinear()
	{
   		Graphics g = linearImg.getGraphics();
   		g.setColor(Color.white);
   		g.fillRect(0, 0, linearImg.getWidth(), linearImg.getHeight());
   		drawCoordinate(g);
    }
    //��������Ҫ��ͼ��
    void drawCoordinate(Graphics g)
	{
    	int P1x, P1y;
    	g.setColor(Color.black);
    	//x��ͼ�ͷ
    	P1x = drawLineX(256)+15;
    	P1y = drawLineY(0);
    	g.drawLine(drawLineX(0), P1y, P1x, P1y);
    	g.drawLine(P1x-5, P1y-5, P1x, P1y);
    	g.drawLine(P1x-5, P1y+5, P1x, P1y);
    	//y��ͼ�ͷ
    	P1x = drawLineX(0);
    	P1y = drawLineY(256)-15;
    	g.drawLine(P1x, drawLineY(0), P1x, P1y);
    	g.drawLine(P1x-5, P1y+5, P1x, P1y);
    	g.drawLine(P1x+5, P1y+5, P1x, P1y);
    	//y��ĺ��ߣ���ʶ�Ҷȼ�վ�ı���
    	g.setColor(Color.gray);
    	for(int i=5; i<256; i+=5)
    	{
    		g.drawLine(drawLineX(0), drawLineY(i), drawLineX(256), drawLineY(i));	
    		g.drawLine(drawLineX(i), drawLineY(0), drawLineX(i), drawLineY(256));
    	}
    	//��װ��
    	g.setColor(Color.red);
    	g.drawLine(drawLineX(min), drawLineY(min), drawLineX(min), drawLineY(max));
    	g.drawLine(drawLineX(min), drawLineY(min), drawLineX(max), drawLineY(min));
    	g.drawLine(drawLineX(min), drawLineY(max), drawLineX(max), drawLineY(max));
    	g.drawLine(drawLineX(max), drawLineY(max), drawLineX(max), drawLineY(min));
        //y�ᵥλ����
    	for(int i=20; i<=256; i+=20)
    	{
    		P1x = drawLineX(0);
    		P1y = drawLineY(i);
    		g.setColor(Color.black);
    		g.drawLine(P1x, P1y, P1x-5, P1y);
    		String str = String.valueOf(i);
    		g.drawString(str, P1x-25, P1y+4);		
    	}
    	//x�ᵥλ����
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
