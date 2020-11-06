package imageView;

import javax.swing.*;
/**Rotate 用于图像旋转**/
public class Rotate extends javax.swing.JDialog
{
    private javax.swing.JSlider JSlider1;
    private javax.swing.JTextField chgAngle;
    private javax.swing.JCheckBox ChkBox1;
	private int result = JOptionPane.CANCEL_OPTION;
    public Rotate(java.awt.Frame ancestor, boolean successor)
    {
        super(ancestor, successor);
        initComponents();
    }
    private void initComponents()
    {
        JSlider1 = new javax.swing.JSlider();
        JLabel jLabel1 = new JLabel();
        chgAngle = new javax.swing.JTextField();
        ChkBox1 = new javax.swing.JCheckBox();
        JButton trueBtn = new JButton();
        JButton falseBtn = new JButton();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints meshBoundary;
        JSlider1.setMaximum(360);
        JSlider1.setMinimum(-360);
        JSlider1.setValue(0);
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 0;
        meshBoundary.gridy = 0;
        meshBoundary.gridwidth = 2;
        meshBoundary.fill = java.awt.GridBagConstraints.HORIZONTAL;
        meshBoundary.insets = new java.awt.Insets(15, 10, 15, 1);
        getContentPane().add(JSlider1, meshBoundary);
        JSlider1.addChangeListener(evt -> angleChanged());
        jLabel1.setText("旋转角度");
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
        getContentPane().add(chgAngle, meshBoundary);
        ChkBox1.setSelected(true);
        ChkBox1.setText("Resize");
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 0;
        meshBoundary.gridy = 2;
        meshBoundary.gridwidth = 2;
        meshBoundary.insets = new java.awt.Insets(3, 20, 10, 20);
        meshBoundary.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(ChkBox1, meshBoundary);
        trueBtn.setText("确定(O)");
        trueBtn.setMnemonic('O');
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 2;
        meshBoundary.gridy = 0;
        meshBoundary.insets = new java.awt.Insets(10, 10, 1, 20);
        getContentPane().add(trueBtn, meshBoundary);
        trueBtn.addActionListener(e -> okPerformed());
        falseBtn.setText("取消(C)");
        falseBtn.setMnemonic('C');
        meshBoundary = new java.awt.GridBagConstraints();
        meshBoundary.gridx = 2;
        meshBoundary.gridy = 1;
        meshBoundary.insets = new java.awt.Insets(1, 10, 1, 20);
        getContentPane().add(falseBtn, meshBoundary);
        falseBtn.addActionListener(e -> cancelPerformed());
        pack();
        chgAngle.setText("0");
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
    private void angleChanged()
    {
    	chgAngle.setText(String.valueOf(JSlider1.getValue()));	
    }
    public int showSuccessor()
    {
        setVisible(true);
    	return result;
    }
    public float getJSlider1()
    {
    	return (float)(JSlider1.getValue()*Math.PI/180);
    }
    public boolean getChkBox1()
    {
    	return ChkBox1.isSelected();
    }
}
