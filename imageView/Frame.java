package imageView;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

public class Frame extends JFrame
{
	JMenuBar mb;
	JMenu fileMenu;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem exitItem;
	JMenu editMenu;
	JMenuItem undoItem;
	JMenuItem redoItem;
	JMenu geoMenu;
	JMenuItem translationItem;
	JMenuItem horMirrorItem;
	JMenuItem verMirrorItem;
	JMenuItem scaleItem;
	JMenuItem rotateItem;
	JMenu enhanceMenu;
	JMenuItem grayScaleItem;
	JMenu grayTransformation;
	JMenu linearTransformation;
	JMenuItem liNotSegmentation;
	JMenuItem liSegmentation;
	JMenu imageSmoothing;
	JMenuItem medianFiltering;
	JMenuItem gaussianSmoothingItem;
	JMenuItem fieldAverageItem;
	JMenu imageSharpening;
	JMenuItem laplacian;
	JMenuItem laplacianHiBoostFiltering;
	JMenuItem gaussianHiBoostFiltering;
	JMenu imageSegmentation;
	JMenu thresholdSeg;
	JMenuItem simpleThreshold;
	JMenuItem iterativeThreshold;
	JMenuItem oahuThreshold;
	JMenuItem dynamicThreshold;
	JMenu frequencyDomainProcessing;
	JMenuItem fftItem;
	JMenuItem decomposeItem;
	Panel imgPanel;
	JScrollPane scrollPane;
	BufferedImage image;
	JFileChooser chooser;
	Previewer imagePreviewer;
	IFileFilter bmpFilter;
	IFileFilter jpgFilter;
	IFileFilter gifFilter;
	IFileFilter bothFilter;
	final LinkedList<BufferedImage> undoList;
	final LinkedList<BufferedImage> redoList;
	final LinkedList<BufferedImage> allList;
	int newImage = 0;
	boolean okFlag = false;
	private final static int MAX_UNDO_COUNT = 10;
	private final static int MAX_REDO_COUNT = 10;
	private final static int MAX_ALL_COUNT = 50;
	public Frame()
	{
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				exit();
			}
		});
		undoList = new LinkedList<>();
		redoList = new LinkedList<>();
		allList = new LinkedList<>();
		initComponents();
	}
	/**
	 * initComponents方法用于初始化组件
	 */
	private void initComponents()
	{
		Container contentPane = getContentPane();
		imgPanel = new Panel(image);
		scrollPane = new JScrollPane(imgPanel);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		chooser = new JFileChooser();
		imagePreviewer = new Previewer(chooser);
		bmpFilter = new IFileFilter("bmp", "BMP Image Files");
		jpgFilter = new IFileFilter("jpg", "JPEG Compressed Image Files");
		gifFilter = new IFileFilter("gif", "GIF Image Files");
		bothFilter = new IFileFilter(new String[] { "bmp", "jpg", "gif" }, "BMP, JPEG and GIF Image Files");
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.addChoosableFileFilter(bmpFilter);
		chooser.addChoosableFileFilter(gifFilter);
		chooser.addChoosableFileFilter(bothFilter);
		chooser.setAccessory(imagePreviewer);
		chooser.setAcceptAllFileFilterUsed(false);
		Icon openIcon = new ImageIcon("open.gif");
		Icon saveIcon = new ImageIcon("save.gif");
		Icon exitIcon = new ImageIcon("exit.gif");
		Icon undoIcon = new ImageIcon("images/undo.gif");
		Icon redoIcon = new ImageIcon("images/redo.gif");
		// ----菜单条------------------------------------------------------------
		mb = new JMenuBar();
		setJMenuBar(mb);
		// ----File菜单----------------------------------------------------------
		fileMenu = new JMenu("文件(D)");
		fileMenu.setMnemonic('D');
		mb.add(fileMenu);

		openItem = new JMenuItem("打开(O)", openIcon);
		openItem.setMnemonic('O');
		openItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
		openItem.addActionListener(e ->
		{
			openFile();
			if(okFlag)
			{
				newImage++;
				okFlag = false;
			}
		});
		saveItem = new JMenuItem("保存(S)", saveIcon);
		saveItem.setMnemonic('S');
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		saveItem.addActionListener(e -> saveFile());
		exitItem = new JMenuItem("退出(X)", exitIcon);
		exitItem.setMnemonic('X');
		exitItem.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK));
		exitItem.addActionListener(e -> exitSystem());
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		// ----Edit菜单----------------------------------------------------------
		editMenu = new JMenu("编辑(E)");
		editMenu.setMnemonic('E');
		mb.add(editMenu);

		undoItem = new JMenuItem("撤销(U)", undoIcon);
		undoItem.setMnemonic('U');
		undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
		undoItem.addActionListener(e -> undo());
		redoItem = new JMenuItem("重做(R)", redoIcon);
		redoItem.setMnemonic('R');
		redoItem.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
		redoItem.addActionListener(e -> redo());
		editMenu.add(undoItem);
		editMenu.add(redoItem);

		// ----Geo菜单----------------------------------------------------------
		geoMenu = new JMenu("几何变换(G)");
		geoMenu.setMnemonic('G');
		mb.add(geoMenu);

		translationItem = new JMenuItem("图像平移(T)");
		translationItem.setMnemonic('T');
		translationItem.addActionListener(e ->
		{
			translation();

			okFlag = false;
		});
		horMirrorItem = new JMenuItem("水平镜象(H)");
		horMirrorItem.setMnemonic('H');
		horMirrorItem.addActionListener(e -> horMirror());
		verMirrorItem = new JMenuItem("垂直镜象(V)");
		verMirrorItem.setMnemonic('V');
		verMirrorItem.addActionListener(e -> verMirror());
		scaleItem = new JMenuItem("比例缩放(S)");
		scaleItem.setMnemonic('S');
		scaleItem.addActionListener(e ->
		{
			scale();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		rotateItem = new JMenuItem("旋转(R)");
		rotateItem.setMnemonic('R');
		rotateItem.addActionListener(e ->
		{
			rotate();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		geoMenu.add(translationItem);
		geoMenu.add(horMirrorItem);
		geoMenu.add(verMirrorItem);
		geoMenu.add(scaleItem);
		geoMenu.add(rotateItem);
		// ----Enhance菜单-------------------------------------------------------
		enhanceMenu = new JMenu("图像增强(E)");
		enhanceMenu.setMnemonic('E');
		mb.add(enhanceMenu);

		grayTransformation = new JMenu("灰度变换(T)");
		grayTransformation.setMnemonic('T');
		grayScaleItem = new JMenuItem("图像灰度化(G)");
		grayScaleItem.setMnemonic('G');
		grayScaleItem.addActionListener(e -> grayScale());
		linearTransformation = new JMenu("线性变换(L)");
		linearTransformation.setMnemonic('L');
		liNotSegmentation = new JMenuItem("不分段线性(N)");
		liNotSegmentation.setMnemonic('N');
		liNotSegmentation.addActionListener(e ->
		{
			liNotSegmentation();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		liSegmentation = new JMenuItem("分段线性(S)");
		liSegmentation.setMnemonic('S');
		liSegmentation.addActionListener(e ->
		{
			liSegmentation();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		imageSmoothing = new JMenu("图像平滑(S)");
		imageSmoothing.setMnemonic('S');
		medianFiltering = new JMenuItem("中值滤波(F)");
		medianFiltering.setMnemonic('F');
		medianFiltering.addActionListener(e -> medianFiltering());
		gaussianSmoothingItem = new JMenuItem("高斯平滑(G)");
		gaussianSmoothingItem.setMnemonic('G');
		gaussianSmoothingItem.addActionListener(e -> gaussianSmoothing());
		fieldAverageItem = new JMenuItem("领域平均(F)");
		fieldAverageItem.setMnemonic('F');
		fieldAverageItem.addActionListener(e -> fieldAverage());
		imageSharpening = new JMenu("图像锐化(S)");
		imageSharpening.setMnemonic('S');
		laplacian = new JMenuItem("拉普拉斯(L)");
		laplacian.setMnemonic('L');
		laplacian.addActionListener(e -> laplacian());
		laplacianHiBoostFiltering = new JMenuItem("拉斯高增滤波(H)");
		laplacianHiBoostFiltering.setMnemonic('H');
		laplacianHiBoostFiltering.addActionListener(e -> laplacianHiBoostFiltering());
		gaussianHiBoostFiltering = new JMenuItem("高斯高增滤波(G)");
		gaussianHiBoostFiltering.setMnemonic('G');
		gaussianHiBoostFiltering.addActionListener(e -> gaussianHiBoostFiltering());
		enhanceMenu.add(grayTransformation);
		grayTransformation.add(grayScaleItem);
		grayTransformation.add(linearTransformation);
		linearTransformation.add(liNotSegmentation);
		linearTransformation.add(liSegmentation);
		enhanceMenu.add(imageSmoothing);
		imageSmoothing.add(medianFiltering);
		imageSmoothing.add(gaussianSmoothingItem);
		imageSmoothing.add(fieldAverageItem);
		enhanceMenu.add(imageSharpening);
		imageSharpening.add(laplacian);
		imageSharpening.add(laplacianHiBoostFiltering);
		imageSharpening.add(gaussianHiBoostFiltering);
		// ----图像分割----------------------------------------------------------
		imageSegmentation = new JMenu("图像分割(I)");
		imageSegmentation.setMnemonic('I');
		mb.add(imageSegmentation);

		thresholdSeg = new JMenu("全局阈值分割(T)");
		thresholdSeg.setMnemonic('T');
		simpleThreshold = new JMenuItem("简单阈值(S)");
		simpleThreshold.setMnemonic('S');
		simpleThreshold.addActionListener(e ->
		{
			threshold();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		iterativeThreshold = new JMenuItem("迭代阈值(I)");
		iterativeThreshold.setMnemonic('I');
		iterativeThreshold.addActionListener(e ->
		{
			iterative();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		oahuThreshold = new JMenuItem("M=2的OAHU法(O)");
		oahuThreshold.setMnemonic('O');
		oahuThreshold.addActionListener(e -> oahu());
		dynamicThreshold = new JMenuItem("动态阈值分割(D)");
		dynamicThreshold.setMnemonic('D');
		dynamicThreshold.addActionListener(e -> dynamicThreshold());
		imageSegmentation.add(thresholdSeg);
		thresholdSeg.add(simpleThreshold);
		thresholdSeg.add(iterativeThreshold);
		thresholdSeg.add(oahuThreshold);
		imageSegmentation.add(dynamicThreshold);
		// ----频域处理----------------------------------------------------------
		frequencyDomainProcessing = new JMenu("频域处理(F)");

		frequencyDomainProcessing.setMnemonic('F');
		mb.add(frequencyDomainProcessing);
		fftItem = new JMenuItem("傅里叶频谱(F)");
		fftItem.setMnemonic('F');
		fftItem.addActionListener(e ->
		{
			fft();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		decomposeItem = new JMenuItem("小波分解(D)");
		decomposeItem.setMnemonic('D');
		decomposeItem.addActionListener(e -> decompose());
		frequencyDomainProcessing.add(fftItem);
		frequencyDomainProcessing.add(decomposeItem);
	}
	//----面板主函数到此结束---------------------------------------
	private void exit()
	{
		System.exit(0);
	}
	/** 打开图片 **/
	void openFile()
	{
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		if(chooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				image = ImageIO.read(chooser.getSelectedFile());
			}
			catch(Exception ex)
			{
				return;
			}
			imgPanel.setImage(image);
			imgPanel.repaint();
			undoList.clear();
			redoList.clear();
			saveRedoInfo(image);
			saveAllInfo(image);
			okFlag = true;
		}
	}
	/** 退出系统 **/
	void exitSystem()
	{
		System.exit(0);
	}
	/** 保存文件 **/
	void saveFile()
	{
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		int index = chooser.showDialog(null, "保存文件");
		if(index == JFileChooser.APPROVE_OPTION)
		{
			File f = chooser.getSelectedFile();
			try
			{
				if(!f.createNewFile())
				{
					System.out.println("File already exists.");
				}
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
			String fileName = chooser.getName(f);
			String writePath = chooser.getCurrentDirectory().getAbsolutePath() + "\\" + fileName;
			File filePath = new File(writePath);
			try
			{
				ImageIO.write(image, "jpg", filePath);
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
	/** 保存撤销内容 **/
	void saveUndoInfo(BufferedImage image)
	{
		if(undoList.size() == MAX_UNDO_COUNT)
		{
			undoList.removeLast();
		}
		undoList.addFirst(image);
	}
	/** 保存重做内容 **/
	void saveRedoInfo(BufferedImage image)
	{
		if(redoList.size() == MAX_REDO_COUNT)
		{
			redoList.removeLast();
		}
		redoList.addFirst(image);
	}
	/** 保存全部内容 **/
	void saveAllInfo(BufferedImage image)
	{
		if(allList.size() == MAX_ALL_COUNT)
		{
			allList.removeFirst();
		}
		allList.addLast(image);
	}
	/** 撤销 **/
	void undo()
	{
		if(undoList.size() > 0)
		{
			image = undoList.get(0);
			imgPanel.setImage(image);
			imgPanel.repaint();
			undoList.remove(0);
		}
	}
	/** 重做 **/
	void redo()
	{
		if(redoList.size() > 0)
		{
			saveUndoInfo(image);
			image = redoList.get(0);
			imgPanel.setImage(image);
			imgPanel.repaint();
		}
	}
	/** 变换 **/
	void translation()
	{
		Translation translationDlg = new Translation(this, true);
		translationDlg.setLocationRelativeTo(this);
		translationDlg.septicWidth(image.getWidth());
		translationDlg.septicHeight(image.getHeight());
		if(translationDlg.showSuccessor() == JOptionPane.OK_OPTION)
		{
			saveUndoInfo(image);
			image = TransformAlgorithm.translation(image, translationDlg.getRemoving(), translationDlg.getRemoving());
			imgPanel.setImage(image);
			imgPanel.repaint();
			saveAllInfo(image);
			okFlag = translationDlg.getOkFlag();
		}
	}
	/** 水平镜面 **/
	void horMirror()
	{
		saveUndoInfo(image);
		image = TransformAlgorithm.horMirror(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 垂直镜面 **/
	void verMirror()
	{
		saveUndoInfo(image);
		image = TransformAlgorithm.verMirror(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 比例缩放 **/
	void scale()
	{
		Scope scaleDlg = new Scope(this, true);
		scaleDlg.setLocationRelativeTo(this);
		scaleDlg.setPicWidth(image.getWidth());
		scaleDlg.setPicHeight(image.getHeight());
		if(scaleDlg.showSuccessor() == JOptionPane.OK_OPTION)
		{
			saveUndoInfo(image);
			image = TransformAlgorithm.scale(image, scaleDlg.getScale(), scaleDlg.getScale());
			imgPanel.setImage(image);
			imgPanel.repaint();
			saveAllInfo(image);
			okFlag = true;
		}
	}
	/** 旋转 **/
	void rotate()
	{
		Rotate rotateDlg = new Rotate(this, true);
		rotateDlg.setLocationRelativeTo(this);
		if(rotateDlg.showSuccessor() == JOptionPane.OK_OPTION)
		{
			saveUndoInfo(image);
			image = TransformAlgorithm.rotate(image, rotateDlg.getJSlider1(), rotateDlg.getChkBox1());
			imgPanel.setImage(image);
			imgPanel.repaint();
			saveAllInfo(image);
			okFlag = true;
		}
	}
	/** 灰度变换 **/
	void grayScale()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.grayScale(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 非线性变换 **/
	void liNotSegmentation()
	{
		LinearTransformation latrena = new LinearTransformation(this, true);
		latrena.setImage(image);
		latrena.setLocationRelativeTo(this);
		latrena.setVisible(true);
		if(latrena.getTrueOption() == 1)
		{
			saveUndoInfo(image);
			image = ImageAlgorithm.linearTransformation(image, Math.round((float)(latrena.getValue() / 2)) - 20,
					Math.round((float)(latrena.getValue1() / 2)) - 20, 300 - Math.round((float)(latrena.getYValue() / 2)) - 15,
					300 - Math.round((float)(latrena.getYValue1() / 2)) - 15);
		}
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
		okFlag = true;
		latrena.dispose();
	}
	/** 线性变换 **/
	void liSegmentation()
	{
		NotLinearTransformation noLatrena = new NotLinearTransformation(this, true);
		noLatrena.setImage(image);
		noLatrena.setLocationRelativeTo(this);
		noLatrena.setVisible(true);
		if (noLatrena.getTrueOption() == 1)
		{
			saveUndoInfo(image);
			image = ImageAlgorithm.NotSegmentationLinearTransformation(image,
					Math.round((float)(noLatrena.getP1x() / 2)) - 20, Math.round((float)(noLatrena.getP2x() / 2)) - 20,
					300 - Math.round((float)(noLatrena.getP1y() / 2)) - 15,
					300 - Math.round((float)(noLatrena.getP2y() / 2)) - 15);
		}
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
		okFlag = true;
		noLatrena.dispose();
	}
	/** 中值滤波 **/
	void medianFiltering()
	{
		saveUndoInfo(image);
		ImageAlgorithm.findLine(image);
		image = ImageAlgorithm.medianFiltering(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 高斯滤波 **/
	void gaussianSmoothing()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.gaussianSmoothing(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 领域平均 **/
	void fieldAverage()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.fieldAverage(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 拉普拉斯变换 **/
	void laplacian()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.laplacian(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 高斯变换 **/
	void gaussianHiBoostFiltering()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.gaussianHiBoostFiltering(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 拉斯高增滤波 **/
	void laplacianHiBoostFiltering()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.laplacianHiBoostFiltering(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);

	}
	/** 简单阈值 **/
	public void threshold()
	{
		Threshold thresholdDag = new Threshold(this, true);
		thresholdDag.setLocationRelativeTo(this);
		thresholdDag.setVisible(true);
		if(thresholdDag.getModelResult() == JOptionPane.OK_OPTION)
		{
			saveUndoInfo(image);
			image = ImageAlgorithm.threshold(image, thresholdDag.getThreshold());
			imgPanel.setImage(image);
			imgPanel.repaint();
			saveAllInfo(image);
			okFlag = true;
		}
	}
	/** 迭代阈值 **/
	public void iterative()
	{
		Iterative iterativeDlg = new Iterative(this, true);
		iterativeDlg.setLocationRelativeTo(this);
		iterativeDlg.setVisible(true);
		if(iterativeDlg.getModelResult() == JOptionPane.OK_OPTION)
		{
			saveUndoInfo(image);
			image = ImageAlgorithm.iterative(image, iterativeDlg.getJudgement(), iterativeDlg.getThreshold());
			imgPanel.setImage(image);
			imgPanel.repaint();
			saveAllInfo(image);
			okFlag = true;
		}
	}
	/** oahu法 **/
	public void oahu()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.oahu(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** 动态阈值 **/
	void dynamicThreshold()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.dynamic(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** FFT变换 **/
	void fft()
	{
		FFTFilter fftDlg = new FFTFilter(this, true);
		fftDlg.setImage(image);
		fftDlg.setLocationRelativeTo(this);
		if(fftDlg.showSuccessor() == JOptionPane.OK_OPTION)
		{
			saveUndoInfo(image);
			image = fftDlg.getDestImage();
			imgPanel.setImage(image);
			imgPanel.repaint();
			saveAllInfo(image);
			okFlag = true;
		}
	}
	/** 小波分解 **/
	void decompose()
	{
		saveUndoInfo(image);
		image = TransformAlgorithm.decompose(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	public static void main(String[] args)
	{
		Font defaultFont = new Font("System", Font.PLAIN, 12);
		UIManager.put("Button.font", defaultFont);
		UIManager.put("CheckBox.font", defaultFont);
		UIManager.put("RadioButton.font", defaultFont);
		UIManager.put("ToolTip.font", defaultFont);
		UIManager.put("ComboBox.font", defaultFont);
		UIManager.put("Label.font", defaultFont);
		UIManager.put("List.font", defaultFont);
		UIManager.put("Table.font", defaultFont);
		UIManager.put("TableHeader.font", defaultFont);
		UIManager.put("MenuBar.font", defaultFont);
		UIManager.put("Menu.font", defaultFont);
		UIManager.put("MenuItem.font", defaultFont);
		UIManager.put("PopupMenu.font", defaultFont);
		UIManager.put("Tree.font", defaultFont);
		UIManager.put("ToolBar.font", defaultFont);
		Frame mainFrame = new Frame();
		mainFrame.setTitle("数字图像测试");
		mainFrame.setSize(800, 600);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
}
