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
	 * initComponents�������ڳ�ʼ�����
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
		// ----�˵���------------------------------------------------------------
		mb = new JMenuBar();
		setJMenuBar(mb);
		// ----File�˵�----------------------------------------------------------
		fileMenu = new JMenu("�ļ�(D)");
		fileMenu.setMnemonic('D');
		mb.add(fileMenu);

		openItem = new JMenuItem("��(O)", openIcon);
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
		saveItem = new JMenuItem("����(S)", saveIcon);
		saveItem.setMnemonic('S');
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		saveItem.addActionListener(e -> saveFile());
		exitItem = new JMenuItem("�˳�(X)", exitIcon);
		exitItem.setMnemonic('X');
		exitItem.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_DOWN_MASK));
		exitItem.addActionListener(e -> exitSystem());
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		// ----Edit�˵�----------------------------------------------------------
		editMenu = new JMenu("�༭(E)");
		editMenu.setMnemonic('E');
		mb.add(editMenu);

		undoItem = new JMenuItem("����(U)", undoIcon);
		undoItem.setMnemonic('U');
		undoItem.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
		undoItem.addActionListener(e -> undo());
		redoItem = new JMenuItem("����(R)", redoIcon);
		redoItem.setMnemonic('R');
		redoItem.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
		redoItem.addActionListener(e -> redo());
		editMenu.add(undoItem);
		editMenu.add(redoItem);

		// ----Geo�˵�----------------------------------------------------------
		geoMenu = new JMenu("���α任(G)");
		geoMenu.setMnemonic('G');
		mb.add(geoMenu);

		translationItem = new JMenuItem("ͼ��ƽ��(T)");
		translationItem.setMnemonic('T');
		translationItem.addActionListener(e ->
		{
			translation();

			okFlag = false;
		});
		horMirrorItem = new JMenuItem("ˮƽ����(H)");
		horMirrorItem.setMnemonic('H');
		horMirrorItem.addActionListener(e -> horMirror());
		verMirrorItem = new JMenuItem("��ֱ����(V)");
		verMirrorItem.setMnemonic('V');
		verMirrorItem.addActionListener(e -> verMirror());
		scaleItem = new JMenuItem("��������(S)");
		scaleItem.setMnemonic('S');
		scaleItem.addActionListener(e ->
		{
			scale();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		rotateItem = new JMenuItem("��ת(R)");
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
		// ----Enhance�˵�-------------------------------------------------------
		enhanceMenu = new JMenu("ͼ����ǿ(E)");
		enhanceMenu.setMnemonic('E');
		mb.add(enhanceMenu);

		grayTransformation = new JMenu("�Ҷȱ任(T)");
		grayTransformation.setMnemonic('T');
		grayScaleItem = new JMenuItem("ͼ��ҶȻ�(G)");
		grayScaleItem.setMnemonic('G');
		grayScaleItem.addActionListener(e -> grayScale());
		linearTransformation = new JMenu("���Ա任(L)");
		linearTransformation.setMnemonic('L');
		liNotSegmentation = new JMenuItem("���ֶ�����(N)");
		liNotSegmentation.setMnemonic('N');
		liNotSegmentation.addActionListener(e ->
		{
			liNotSegmentation();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		liSegmentation = new JMenuItem("�ֶ�����(S)");
		liSegmentation.setMnemonic('S');
		liSegmentation.addActionListener(e ->
		{
			liSegmentation();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		imageSmoothing = new JMenu("ͼ��ƽ��(S)");
		imageSmoothing.setMnemonic('S');
		medianFiltering = new JMenuItem("��ֵ�˲�(F)");
		medianFiltering.setMnemonic('F');
		medianFiltering.addActionListener(e -> medianFiltering());
		gaussianSmoothingItem = new JMenuItem("��˹ƽ��(G)");
		gaussianSmoothingItem.setMnemonic('G');
		gaussianSmoothingItem.addActionListener(e -> gaussianSmoothing());
		fieldAverageItem = new JMenuItem("����ƽ��(F)");
		fieldAverageItem.setMnemonic('F');
		fieldAverageItem.addActionListener(e -> fieldAverage());
		imageSharpening = new JMenu("ͼ����(S)");
		imageSharpening.setMnemonic('S');
		laplacian = new JMenuItem("������˹(L)");
		laplacian.setMnemonic('L');
		laplacian.addActionListener(e -> laplacian());
		laplacianHiBoostFiltering = new JMenuItem("��˹�����˲�(H)");
		laplacianHiBoostFiltering.setMnemonic('H');
		laplacianHiBoostFiltering.addActionListener(e -> laplacianHiBoostFiltering());
		gaussianHiBoostFiltering = new JMenuItem("��˹�����˲�(G)");
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
		// ----ͼ��ָ�----------------------------------------------------------
		imageSegmentation = new JMenu("ͼ��ָ�(I)");
		imageSegmentation.setMnemonic('I');
		mb.add(imageSegmentation);

		thresholdSeg = new JMenu("ȫ����ֵ�ָ�(T)");
		thresholdSeg.setMnemonic('T');
		simpleThreshold = new JMenuItem("����ֵ(S)");
		simpleThreshold.setMnemonic('S');
		simpleThreshold.addActionListener(e ->
		{
			threshold();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		iterativeThreshold = new JMenuItem("������ֵ(I)");
		iterativeThreshold.setMnemonic('I');
		iterativeThreshold.addActionListener(e ->
		{
			iterative();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		oahuThreshold = new JMenuItem("M=2��OAHU��(O)");
		oahuThreshold.setMnemonic('O');
		oahuThreshold.addActionListener(e -> oahu());
		dynamicThreshold = new JMenuItem("��̬��ֵ�ָ�(D)");
		dynamicThreshold.setMnemonic('D');
		dynamicThreshold.addActionListener(e -> dynamicThreshold());
		imageSegmentation.add(thresholdSeg);
		thresholdSeg.add(simpleThreshold);
		thresholdSeg.add(iterativeThreshold);
		thresholdSeg.add(oahuThreshold);
		imageSegmentation.add(dynamicThreshold);
		// ----Ƶ����----------------------------------------------------------
		frequencyDomainProcessing = new JMenu("Ƶ����(F)");

		frequencyDomainProcessing.setMnemonic('F');
		mb.add(frequencyDomainProcessing);
		fftItem = new JMenuItem("����ҶƵ��(F)");
		fftItem.setMnemonic('F');
		fftItem.addActionListener(e ->
		{
			fft();
			if (okFlag)
			{
				okFlag = false;
			}
		});
		decomposeItem = new JMenuItem("С���ֽ�(D)");
		decomposeItem.setMnemonic('D');
		decomposeItem.addActionListener(e -> decompose());
		frequencyDomainProcessing.add(fftItem);
		frequencyDomainProcessing.add(decomposeItem);
	}
	//----������������˽���---------------------------------------
	private void exit()
	{
		System.exit(0);
	}
	/** ��ͼƬ **/
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
	/** �˳�ϵͳ **/
	void exitSystem()
	{
		System.exit(0);
	}
	/** �����ļ� **/
	void saveFile()
	{
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		int index = chooser.showDialog(null, "�����ļ�");
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
	/** ���泷������ **/
	void saveUndoInfo(BufferedImage image)
	{
		if(undoList.size() == MAX_UNDO_COUNT)
		{
			undoList.removeLast();
		}
		undoList.addFirst(image);
	}
	/** ������������ **/
	void saveRedoInfo(BufferedImage image)
	{
		if(redoList.size() == MAX_REDO_COUNT)
		{
			redoList.removeLast();
		}
		redoList.addFirst(image);
	}
	/** ����ȫ������ **/
	void saveAllInfo(BufferedImage image)
	{
		if(allList.size() == MAX_ALL_COUNT)
		{
			allList.removeFirst();
		}
		allList.addLast(image);
	}
	/** ���� **/
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
	/** ���� **/
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
	/** �任 **/
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
	/** ˮƽ���� **/
	void horMirror()
	{
		saveUndoInfo(image);
		image = TransformAlgorithm.horMirror(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** ��ֱ���� **/
	void verMirror()
	{
		saveUndoInfo(image);
		image = TransformAlgorithm.verMirror(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** �������� **/
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
	/** ��ת **/
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
	/** �Ҷȱ任 **/
	void grayScale()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.grayScale(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** �����Ա任 **/
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
	/** ���Ա任 **/
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
	/** ��ֵ�˲� **/
	void medianFiltering()
	{
		saveUndoInfo(image);
		ImageAlgorithm.findLine(image);
		image = ImageAlgorithm.medianFiltering(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** ��˹�˲� **/
	void gaussianSmoothing()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.gaussianSmoothing(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** ����ƽ�� **/
	void fieldAverage()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.fieldAverage(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** ������˹�任 **/
	void laplacian()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.laplacian(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** ��˹�任 **/
	void gaussianHiBoostFiltering()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.gaussianHiBoostFiltering(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** ��˹�����˲� **/
	void laplacianHiBoostFiltering()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.laplacianHiBoostFiltering(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);

	}
	/** ����ֵ **/
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
	/** ������ֵ **/
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
	/** oahu�� **/
	public void oahu()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.oahu(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** ��̬��ֵ **/
	void dynamicThreshold()
	{
		saveUndoInfo(image);
		image = ImageAlgorithm.dynamic(image);
		imgPanel.setImage(image);
		imgPanel.repaint();
		saveAllInfo(image);
	}
	/** FFT�任 **/
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
	/** С���ֽ� **/
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
		mainFrame.setTitle("����ͼ�����");
		mainFrame.setSize(800, 600);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
}
