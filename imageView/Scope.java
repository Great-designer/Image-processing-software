package imageView;

import javax.swing.*;
/** Scope类用于图像缩放 **/
public class Scope extends javax.swing.JDialog
{
    public Scope(java.awt.Frame ancestor, boolean successor)
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
        JLabel jLabel1 = new JLabel();
        chgBox = new javax.swing.JTextField();
        JLabel JLabel1 = new JLabel();
        chgWidth = new javax.swing.JTextField();
        JLabel JLabel2 = new JLabel();
        chgHeight = new javax.swing.JTextField();
        JButton trueBtn = new JButton();
        JButton falseBtn = new JButton();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints meshBoundary;
        JSlider1.setMaximum(200);
        JSlider1.setValue(100);
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 0;
        meshBoundary.gridy = 0;
        meshBoundary.gridwidth = 2;
        meshBoundary.fill = java.awt.GridBagConstraints.HORIZONTAL;
        meshBoundary.insets = new java.awt.Insets(15, 10, 15, 1);
        getContentPane().add(JSlider1, meshBoundary);
        JSlider1.addChangeListener(evt -> scaleChanged());
        jLabel1.setText("缩放比例");
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 0;
        meshBoundary.gridy = 1;
        meshBoundary.insets = new java.awt.Insets(3, 20, 3, 20);
        meshBoundary.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel1, meshBoundary);
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 1;
        meshBoundary.gridy = 1;
        meshBoundary.ipadx = 80;
        meshBoundary.insets = new java.awt.Insets(3, 20, 3, 20);
        meshBoundary.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(chgBox, meshBoundary);
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
        trueBtn.addActionListener(evt -> okPerformed());
        falseBtn.setText("取消(C)");
        falseBtn.setMnemonic('C');
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 2;
        meshBoundary.gridy = 1;
        meshBoundary.insets = new java.awt.Insets(1, 10, 1, 20);
        getContentPane().add(falseBtn, meshBoundary);
        falseBtn.addActionListener(evt -> cancelPerformed());
        pack();
        chgBox.setText("1");
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
    private void scaleChanged()
    {
    	int v = JSlider1.getValue();
    	if(v <= 100) 
    		scope = 0.1f+v*0.009f;
    	else 
    		scope = 1+0.01f*(v-100);
    	scope = (int)(scope*100)/100.0f;
    	chgBox.setText(String.valueOf(scope));
    	chgWidth.setText(String.valueOf(Math.round(scope*picWidth)));
    	chgHeight.setText(String.valueOf(Math.round(scope*picHeight)));
    }
    public int showSuccessor()
    {
        setVisible(true);
    	return result;
    }
    public float getScale()
    {
    	return scope;
    }
    public void setPicWidth(int horizon)
    {
    	this.picWidth = horizon;
    	chgWidth.setText(String.valueOf(horizon));
    }
    public void setPicHeight(int vertical)
    {
    	this.picHeight = vertical;
    	chgHeight.setText(String.valueOf(vertical));
    }
    private javax.swing.JSlider JSlider1;
    private javax.swing.JTextField chgBox,chgWidth,chgHeight;
    private int result = JOptionPane.CANCEL_OPTION;
	private float scope = 1;
	private int picWidth,picHeight;
}


