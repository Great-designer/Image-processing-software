package imageView;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
/** NotLinearTransformation�����ڷ����Ա任 **/
public class NotLinearTransformation extends JDialog
{
	private BufferedImage source;
	private BufferedImage linearImg;
	Panel imgPanel;
	JScrollPane scrollPane;
	JButton trueBtn;
	// ����һ�����ε�������λ��
	private int p1x, p1y, p2x, p2y;
	// �Ƿ�Ϊ�ڶ��������
	private int isSecond = 0; 
	// ����ȷ�ϰ�ť������������Ӧ�������ж�
	private int trueOption = 0;
	// �����С�ҶȰٷֱȣ��ٷֱ�ʾ
	private int low, high; 
	private final int LMargin = 40;
	private final int TMargin = 20;
	public NotLinearTransformation(java.awt.Frame ancestor, boolean successor)
	{
		super(ancestor, successor);
		initComponents();
		setTitle("������2");
	}
	/**
	 * initComponents�������ڳ�ʼ�����
	 */
	void initComponents()
	{
		Container wholePane = getContentPane();
		imgPanel = new Panel(linearImg);
		scrollPane = new JScrollPane(imgPanel);
		trueBtn = new JButton("ȷ��");
		imgPanel.add(trueBtn, BorderLayout.EAST);
		wholePane.add(scrollPane, BorderLayout.CENTER);
		imgPanel.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				Graphics graph1 = imgPanel.getGraphics();
				if (e.getClickCount() == 1)
				{
					isSecond = isSecond + 1;
				}
				if (isSecond == 1)
				{
					p1x = e.getX();
					p1y = e.getY();
					graph1.drawOval(p1x - 5, p1y - 5, 10, 10);
				}
				if (isSecond == 2)
				{
					p2x = e.getX();
					p2y = e.getY();
					graph1.drawOval(p2x - 5, p2y - 5, 10, 10);
					graph1.drawLine(p1x, p1y, p2x, p2y);
					int tmp1 = low * 2 + 69;
					int tmp2 = high * 2 + 69;
					graph1.drawOval(low * 2 + 40 - 5, 600 - tmp1 - 5, 10, 10);
					graph1.drawOval(high * 2 + 40 - 5, 600 - tmp2 - 5, 10, 10);
					graph1.drawLine(low * 2 + 40, 600 - tmp1, p1x, p1y);
					graph1.drawLine(p2x, p2y, high * 2 + 40, 600 - tmp2);
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
		return 256 * 2;
	}
	int getLinearHeight()
	{
		return 256 * 2;
	}
	// ȷ������x��
	int drawLineX(int lineX)
	{
		int check1 = 2;
		return LMargin + lineX * check1;
	}
	// ȷ�����ߵ�y��
	int drawLineY(int lineY)
	{
		// �Ҷ�/����
		int check2 = 2;
		return TMargin + (high - lineY) * check2;
	}
	//�������Ա任��Ϣ
	void calculateLinearInfo()
	{
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int i;
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		high = low = 0;
		for (i = 0; i < sourceRGBs.length; i++)
		{
			if (Process.getBrightness(sourceRGBs[i]) > high)
				high = Process.getBrightness(sourceRGBs[i]);
			if (Process.getBrightness(sourceRGBs[i]) < low)
				low = Process.getBrightness(sourceRGBs[i]);
		}
		int BMargin = 30;
		int RMargin = 20;
		linearImg = new BufferedImage(getLinearWidth() + LMargin + RMargin, getLinearHeight() + TMargin + BMargin,
				BufferedImage.TYPE_INT_RGB);
		imgPanel.setImage(linearImg);
	}
	//����
	void drawLinear()
	{
		Graphics graph = linearImg.getGraphics();
		graph.setColor(Color.white);
		graph.fillRect(0, 0, linearImg.getWidth(), linearImg.getHeight());
		drawCoordinate(graph);
	}
	// ��������Ҫ��ͼ��
	void drawCoordinate(Graphics graph)
	{
		int P1x, P1y;
		int i;
		graph.setColor(Color.black);
		// x��ͼ�ͷ
		P1x = drawLineX(256) + 15;
		P1y = drawLineY(0);
		graph.drawLine(drawLineX(0), P1y, P1x, P1y);
		graph.drawLine(P1x - 5, P1y - 5, P1x, P1y);
		graph.drawLine(P1x - 5, P1y + 5, P1x, P1y);
		// y��ͼ�ͷ
		P1x = drawLineX(0);
		P1y = drawLineY(256) - 15;
		graph.drawLine(P1x, drawLineY(0), P1x, P1y);
		graph.drawLine(P1x - 5, P1y + 5, P1x, P1y);
		graph.drawLine(P1x + 5, P1y + 5, P1x, P1y);
		// y��ĺ��ߣ���ʶ�Ҷȼ�վ�ı���
		graph.setColor(Color.gray);
		for (i = 5; i < 256; i += 5)
		{
			graph.drawLine(drawLineX(0), drawLineY(i), drawLineX(256), drawLineY(i));
			graph.drawLine(drawLineX(i), drawLineY(0), drawLineX(i), drawLineY(256));
		}
		// ��װ��
		graph.setColor(Color.red);
		graph.drawLine(drawLineX(low), drawLineY(low), drawLineX(low), drawLineY(high));
		graph.drawLine(drawLineX(low), drawLineY(low), drawLineX(high), drawLineY(low));
		graph.drawLine(drawLineX(low), drawLineY(high), drawLineX(high), drawLineY(high));
		graph.drawLine(drawLineX(high), drawLineY(high), drawLineX(high), drawLineY(low));
		// y�ᵥλ����
		for (i = 20; i <= 256; i += 20)
		{
			P1x = drawLineX(0);
			P1y = drawLineY(i);
			graph.setColor(Color.black);
			graph.drawLine(P1x, P1y, P1x - 5, P1y);
			String str = String.valueOf(i);
			graph.drawString(str, P1x - 25, P1y + 4);
		}
		// x�ᵥλ����
		for (i = 0; i < 256; i += 20)
		{
			P1x = drawLineX(i);
			P1y = drawLineY(0);
			graph.drawLine(P1x, P1y, P1x, P1y + 5);
			String str = String.valueOf(i);
			int strWidth = graph.getFontMetrics().stringWidth(str);
			graph.drawString(str, P1x - strWidth / 2, P1y + 20);
		}
	}
	public void setImage(BufferedImage img)
	{
		source = img;
		calculateLinearInfo();
		drawLinear();
	}
	public int getP1x()
	{
		return p1x;
	}
	public int getP2x()
	{
		return p2x;
	}
	public int getP1y()
	{
		return p1y;
	}
	public int getP2y()
	{
		return p2y;
	}
	public int getTrueOption()
	{
		return trueOption;
	}
}
