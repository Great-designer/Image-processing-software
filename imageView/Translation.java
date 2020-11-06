package imageView;

import javax.swing.*;
/** Translation类用于图像平移 **/
public class Translation extends javax.swing.JDialog
{
	public Translation(java.awt.Frame ancestor, boolean successor)
	{
		super(ancestor, successor);
		initComponents();
	}
	/**
	 * initComponents方法用于初始化组件
	 */
	private void initComponents()
	{
		JSlider1 = new javax.swing.JSlider();
		JLabel JLabel1 = new JLabel();
		chgWidth = new javax.swing.JTextField();
		JLabel JLabel2 = new JLabel();
		chgHeight = new javax.swing.JTextField();
		JButton trueBtn = new JButton();
		JButton falseBtn = new JButton();
		getContentPane().setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints meshBoundary = new java.awt.GridBagConstraints();
		JSlider1.setMaximum(200);
		JSlider1.setValue(100);
		meshBoundary.gridx = 0;
		meshBoundary.gridy = 0;
		meshBoundary.gridwidth = 2;
		meshBoundary.fill = java.awt.GridBagConstraints.HORIZONTAL;
		meshBoundary.insets = new java.awt.Insets(15, 10, 15, 1);
		getContentPane().add(JSlider1, meshBoundary);
		JSlider1.addChangeListener(evt -> translationChanged());
		JLabel1.setText("图像高度");
		meshBoundary = new java.awt.GridBagConstraints();
		meshBoundary.gridx = 0;
		meshBoundary.gridy = 2;
		meshBoundary.insets = new java.awt.Insets(3, 20, 3, 20);
		meshBoundary.anchor = java.awt.GridBagConstraints.WEST;
		getContentPane().add(JLabel1, meshBoundary);
		meshBoundary = new java.awt.GridBagConstraints();
		meshBoundary.gridx = 1;
		meshBoundary.gridy = 2;
		meshBoundary.ipadx = 80;
		meshBoundary.insets = new java.awt.Insets(3, 20, 3, 20);
		meshBoundary.anchor = java.awt.GridBagConstraints.EAST;
		getContentPane().add(chgWidth, meshBoundary);
		JLabel2.setText("图像宽度");
		meshBoundary = new java.awt.GridBagConstraints();
		meshBoundary.gridx = 0;
		meshBoundary.gridy = 3;
		meshBoundary.insets = new java.awt.Insets(3, 20, 10, 20);
		meshBoundary.anchor = java.awt.GridBagConstraints.WEST;
		getContentPane().add(JLabel2, meshBoundary);
		meshBoundary = new java.awt.GridBagConstraints();
		meshBoundary.gridx = 1;
		meshBoundary.gridy = 3;
		meshBoundary.ipadx = 80;
		meshBoundary.insets = new java.awt.Insets(3, 20, 10, 20);
		meshBoundary.anchor = java.awt.GridBagConstraints.EAST;
		getContentPane().add(chgHeight, meshBoundary);
		trueBtn.setText("确定(O)");
		trueBtn.setMnemonic('O');
		meshBoundary = new java.awt.GridBagConstraints();
		meshBoundary.gridx = 2;
		meshBoundary.gridy = 0;
		meshBoundary.insets = new java.awt.Insets(10, 10, 1, 20);
		getContentPane().add(trueBtn, meshBoundary);
		trueBtn.addActionListener(e -> {
			trueCheck1 = true;
			okPerformed();
		});
		falseBtn.setText("取消(C)");
		falseBtn.setMnemonic('C');
		meshBoundary = new java.awt.GridBagConstraints();
		meshBoundary.gridx = 2;
		meshBoundary.gridy = 1;
		meshBoundary.insets = new java.awt.Insets(1, 10, 1, 20);
		getContentPane().add(falseBtn, meshBoundary);
		falseBtn.addActionListener(e -> {
			trueCheck1 = false;
			cancelPerformed();
		});
		pack();
	}
	private void okPerformed()
	{
		result = JOptionPane.OK_OPTION;
		dispose();

	}
	private void cancelPerformed()
	{
		result = JOptionPane.CANCEL_OPTION;
		dispose();
	}
	private void translationChanged()
	{
		removing = JSlider1.getValue();
		chgWidth.setText(String.valueOf(Math.round(removing + picWidth)));
		chgHeight.setText(String.valueOf(Math.round(removing + picHeight)));
	}
	public int showSuccessor()
	{
		setVisible(true);
		return result;
	}
	public int getRemoving()
	{
		return removing;
	}
	public void septicWidth(int horizon)
	{
		this.picWidth = horizon;
		chgWidth.setText(String.valueOf(horizon));
	}
	public void septicHeight(int vertical)
	{
		this.picHeight = vertical;
		chgHeight.setText(String.valueOf(vertical));
	}
	public boolean getOkFlag()
	{
		return trueCheck1;
	}
	private javax.swing.JSlider JSlider1;
	private javax.swing.JTextField chgWidth, chgHeight;
	private int result = JOptionPane.CANCEL_OPTION;
	private int removing = 0;
	private int picWidth, picHeight;
	public static boolean trueCheck1 = false;
}
