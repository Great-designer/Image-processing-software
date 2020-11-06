package imageView;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;

class Panel extends JComponent
{
	// 缩放比例
	protected final float scale = 1;  
	// 需要显示的图像
	protected Image image = null;       
	public Panel(Image image)
	{
		setImage(image);
	}
	public void setImage(Image image)
	{
		this.image = image;
		setSize(getPreferredSize());
	}
	/** 获取需要显示图像缩放后的尺寸 **/
	protected Dimension getImageSize()
	{
		if(image != null)
		{
			return new Dimension(Math.round(image.getWidth(null)*scale), Math.round(image.getHeight(null)*scale));
		}
		else return new Dimension(0, 0);
	}
	/** 获取控件首选尺寸，由于没有边框，它等于getImageSize() **/
	public Dimension getPreferredSize()
	{
		return getImageSize();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(image == null) return ;
		Dimension destDim = getImageSize();
		g.drawImage(image, 0, 0, destDim.width, destDim.height,
				0, 0, image.getWidth(null), image.getHeight(null), null);
	}
}

class FFTImage extends Panel
{
	int a, b;
	public FFTImage(Image image)
	{
		super(image);
	}
	public void setA(int a)
	{
		this.a = a;
		repaint();
	}
	public void setB(int b)
	{
		this.b = b;
		repaint();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int x0 = image.getWidth(null)/2;
		int y0 = image.getHeight(null)/2;
		g.setClip(0, 0, 2*x0, 2*y0);
		if(a > 0)
		{
			g.setColor(Color.red);
			g.drawOval(x0-a, y0-a, 2*a, 2*a);
		}
		if(b > 0)
		{
			g.setColor(Color.green);
			g.drawOval(x0-b, y0-b, 2*b, 2*b);
		}
	}
}

public class FFTFilter extends javax.swing.JDialog
{
    /** 创建FFTFilter **/
    public FFTFilter(java.awt.Frame ancestor, boolean successor)
    {
        super(ancestor, successor);
        initComponents();
    }
    /**
	 * initComponents方法用于初始化组件
	 */
    private void initComponents()
    {
    	imgPanel = new FFTImage(fftImage);
        // Variables declaration - do not modify
        JScrollPane scrollPane = new JScrollPane(imgPanel);
        JPanel jPanel1 = new JPanel();
        innerSlider = new javax.swing.JSlider();
        outerSlider = new javax.swing.JSlider();
        JButton falseBtn = new JButton();
        JButton trueBtn = new JButton();
        JLabel JLabel1 = new JLabel();
        JLabel JLabel2 = new JLabel();
        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
        jPanel1.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints meshBoundary;
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 1;
        meshBoundary.gridy = 0;
        meshBoundary.insets = new java.awt.Insets(15, 3, 3, 10);
        jPanel1.add(innerSlider, meshBoundary);
        innerSlider.addChangeListener(evt -> innerChanged());
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 1;
        meshBoundary.gridy = 1;
        meshBoundary.insets = new java.awt.Insets(3, 3, 15, 10);
        jPanel1.add(outerSlider, meshBoundary);
        outerSlider.addChangeListener(evt -> outerChanged());
        falseBtn.setText("取消(C)");
        falseBtn.setMnemonic('C');
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 2;
        meshBoundary.gridy = 1;
        meshBoundary.insets = new java.awt.Insets(0, 0, 10, 15);
        jPanel1.add(falseBtn, meshBoundary);
        falseBtn.addActionListener(e -> cancelPerformed());
        trueBtn.setText("确定(O)");
        trueBtn.setMnemonic('O');
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 2;
        meshBoundary.gridy = 0;
        meshBoundary.insets = new java.awt.Insets(12, 0, 0, 15);
        jPanel1.add(trueBtn, meshBoundary);
        trueBtn.addActionListener(e -> okPerformed());
        JLabel1.setText("内半径a");
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 0;
        meshBoundary.gridy = 0;
        meshBoundary.insets = new java.awt.Insets(15, 15, 0, 3);
        jPanel1.add(JLabel1, meshBoundary);
        JLabel2.setText("外半径b");
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 0;
        meshBoundary.gridy = 1;
        meshBoundary.insets = new java.awt.Insets(3, 15, 15, 3);
        jPanel1.add(JLabel2, meshBoundary);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        setSize(new Dimension(640, 480));
        setTitle("傅立叶带通滤波");
    }
    private void okPerformed()
    {
        result = JOptionPane.OK_OPTION;
        resultImage = filterImage();
		dispose();
    }
    private void cancelPerformed()
    {
        result = JOptionPane.CANCEL_OPTION;
		dispose();
    }
    private void innerChanged()
    {
    	int a = innerSlider.getValue();
    	imgPanel.setA(a);
    }
    private void outerChanged()
    {
    	int b = outerSlider.getValue();
    	imgPanel.setB(b);		
    }
    public int showSuccessor()
    {
		setVisible(true);
    	return result;
    }
    public BufferedImage getDestImage()
    {
    	return resultImage;
    }
    public void setImage(BufferedImage image)
    {
    	source = image;
    	fftImage = createFFTImage();
    	int a = 0;
    	int b = (int)Math.round(Math.sqrt((double)(lenx * lenx /4)+ (double)(leny * leny /4)))+2;
    	innerSlider.setMaximum(b);
    	innerSlider.setValue(a);
    	outerSlider.setMaximum(b);
    	outerSlider.setValue(b);
    	imgPanel.setImage(fftImage);
    	imgPanel.setA(a);
    	imgPanel.setB(b);
    }
    private BufferedImage createFFTImage()
    {
		int i, j;
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		lenx = 1;
		//傅里叶处理的是2的倍数，处理的时候截取部分图像
		for(i=horizon/2; i>0; i/=2) lenx *= 2;
		lenx = lenx > 1 ? lenx : 0;
		leny = 1;
		for(i=vertical/2; i>0; i/=2) leny *= 2;
		leny = leny > 1 ? leny : 0;
		BufferedImage fftImage = new BufferedImage(lenx, leny, BufferedImage.TYPE_INT_RGB);
		//得到处理的点
		int[] sourceRGBs = source.getRGB(0, 0, lenx, leny, null, 0, lenx);
		//实部数组
		ori = new float[leny][];
	    //虚部数组
		goal = new float[leny][];
		float[][] gra = new float[leny][];
		for(i=0; i< leny; i++)
		{
			ori[i] = new float[lenx];
			goal[i] = new float[lenx];
			//最后的值数组
			gra[i] = new float[lenx];
		}
		int[] color = new int[3];
		float[] remix = new float[3];
		for(i=0; i< leny; i++)
		{
			for(j=0; j< lenx; j++)
			{
				Process.convertRGBToRemix(sourceRGBs[i* lenx +j], remix);
				//灰度化
				ori[i][j] = remix[0];
				goal[i][j] = 0;
			}
		}
		TransformAlgorithm.FFFor2(ori, goal, TransformAlgorithm.DFT, lenx, leny);
		//正无穷大
		float min = Float.POSITIVE_INFINITY;
		//负无穷大
		float max = Float.NEGATIVE_INFINITY;
		for(i=0; i< leny; i++)
		{
			for(j=0; j< lenx; j++)
			{
				//取得能量谱
				float norm = ori[i][j]*ori[i][j]+goal[i][j]*goal[i][j];
				if(norm != 0.0) norm = (float)Math.log(norm)/2;
				else norm = 0.0f;
				gra[i][j] = norm;
				if(gra[i][j] > max) max = gra[i][j];
				if(gra[i][j] < min) min = gra[i][j];
			}
		}
		for(i=0; i< leny; i++)
		{
			for(j=0; j< lenx; j++)
			{
				//换算成灰度值
				color[0] = (int)((gra[i][j]-min)*255/(max-min));
				color[1] = color[2] = color[0];
				fftImage.setRGB(j, i, Process.mergeColor(color));
			}
		}
		return fftImage;
	}
	private BufferedImage filterImage()
    {
		float a = innerSlider.getValue();
		float b = outerSlider.getValue();
		int horizon = source.getWidth();
		int vertical = source.getHeight();
		int[] sourceRGBs = source.getRGB(0, 0, horizon, vertical, null, 0, horizon);
		BufferedImage resultImage = new BufferedImage(horizon, vertical, BufferedImage.TYPE_INT_RGB);
		resultImage.setRGB(0, 0, horizon, vertical, sourceRGBs, 0, horizon);
		for(int i = 0; i< leny; i++)
		{
			for(int j = 0; j< lenx; j++)
			{
				float r = (float)Math.sqrt((double)((j- lenx /2)*(j- lenx /2))+(double)((i- leny /2)*(i- leny /2)));
				if(r < a || r > b)
				{
					ori[i][j] = 0;
					goal[i][j] = 0;
				}
			}
		}
		TransformAlgorithm.FFFor2(ori, goal, TransformAlgorithm.IDA, lenx, leny);
		float max = 0, min = 500;
		for(int i = 0; i< leny; i++)
		{
			for(int j = 0; j< lenx; j++)
			{
				if(max < ori[i][j]) max = ori[i][j];
				if(min > ori[i][j]) min = ori[i][j];
			}
		}
		float[] remix = new float[3];
		for(int i = 0; i< leny; i++)
		{
			for(int j = 0; j< lenx; j++)
			{
				Process.convertRGBToRemix(sourceRGBs[i*horizon+j], remix);
				remix[0] = (ori[i][j]-min)*255/(max-min);
				resultImage.setRGB(j, i, Process.convertRemixToRGB(remix));
			}
		}
		return resultImage;
	}
    FFTImage imgPanel;
    private javax.swing.JSlider innerSlider;
    private javax.swing.JSlider outerSlider;
    // End of variables declaration
    private int result = JOptionPane.CANCEL_OPTION;
    private BufferedImage source;
    private BufferedImage resultImage;
    private BufferedImage fftImage;
    private int lenx, leny;
    float[][] ori;
    float[][] goal;
}


