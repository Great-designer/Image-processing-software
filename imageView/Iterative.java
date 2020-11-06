package imageView;

import javax.swing.*;

import java.awt.*;
/** Iterator类用于迭代阈值分割 **/
public class Iterative extends JDialog
{
    public Iterative(java.awt.Frame ancestor, boolean successor)
    {
        super(ancestor, successor);
        initComponents();
    }
    /**
	 * initComponents方法用于初始化组件
	 */
    private void initComponents()
    {
        JLabel JLabel1 = new JLabel();
        JLabel jLb2 = new JLabel();
        JButton trueBtn = new JButton();
        JButton falseBtn = new JButton();
    	inputBox1 = new JTextField();
    	inputBox2 = new JTextField();
    	getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints meshBoundary;
        JLabel1.setText("请输入初始阈值T1(0~255的整数)");
        meshBoundary = new GridBagConstraints();
        meshBoundary.gridx = 1;
        meshBoundary.gridy = 1;
        meshBoundary.gridwidth = 5;
        meshBoundary.gridheight = 1;
        meshBoundary.insets = new Insets(5,5,5,5);
        getContentPane().add(JLabel1, meshBoundary);
        meshBoundary = new GridBagConstraints();
        meshBoundary.gridx = 1;
        meshBoundary.gridy = 2;
        meshBoundary.gridwidth = 5;
        meshBoundary.gridheight = 1;
        meshBoundary.ipadx = 200;
        meshBoundary.insets = new Insets(5,5,5,5);
        getContentPane().add(inputBox1, meshBoundary);
        jLb2.setText("请输入预定正数T0(0~255的整数)");
        meshBoundary = new GridBagConstraints();
        meshBoundary.gridx = 1;
        meshBoundary.gridy = 3;
        meshBoundary.gridwidth = 5;
        meshBoundary.gridheight = 1;
        meshBoundary.insets = new Insets(5,5,5,5);
        getContentPane().add(jLb2, meshBoundary);
        meshBoundary = new GridBagConstraints();
        meshBoundary.gridx = 1;
        meshBoundary.gridy = 4;
        meshBoundary.gridwidth = 5;
        meshBoundary.gridheight = 1;
        meshBoundary.ipadx = 200;
        meshBoundary.insets = new Insets(5,5,5,5);
        getContentPane().add(inputBox2, meshBoundary);
        trueBtn.setText("确定");
        meshBoundary = new GridBagConstraints();
        meshBoundary.gridx = 1;
        meshBoundary.gridy = 6;
        meshBoundary.gridwidth = 2;
        meshBoundary.gridheight = 1;
        meshBoundary.insets = new Insets(5,5,5,5);
        getContentPane().add(trueBtn, meshBoundary);
        trueBtn.addActionListener(e -> okPerform());
        falseBtn.setText("取消");
        meshBoundary = new GridBagConstraints();
        meshBoundary.gridx = 4;
        meshBoundary.gridy = 6;
        meshBoundary.gridwidth = 2;
        meshBoundary.gridheight = 1;
        meshBoundary.insets = new Insets(5,5,5,5);
        getContentPane().add(falseBtn, meshBoundary);
        falseBtn.addActionListener(e -> cancelPerform());
        pack();
    }
	private void cancelPerform()
    {
		inputBox1.setText("");
        modelResult = JOptionPane.CANCEL_OPTION;
		dispose();
	}
	//点击OK后，可能出现的情况
	private void okPerform()
    {
		String s = inputBox1.getText();
		String s1 = inputBox1.getText();
		String sPattern = "(\\d?)+(\\d?)+(\\d?)";
		if(s.matches(sPattern)&s1.matches(sPattern))
		{
			threshold = Integer.parseInt(s);
			judgement = Integer.parseInt(s1);
			if(threshold > 255)
			{
				JOptionPane.showMessageDialog(getContentPane()
						, "请不要输入超过255的数字！","系统信息",JOptionPane.WARNING_MESSAGE);
				inputBox1.setText("");
			}
			else
			{
				modelResult = JOptionPane.OK_OPTION;
				dispose();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(getContentPane()
					, "请输入0~255的整数！","系统信息",JOptionPane.WARNING_MESSAGE);
			inputBox1.setText("");
			inputBox2.setText("");
		}
	}
	public int getModelResult()
    {
		return modelResult;
	}
	public int getThreshold()
    {
		return threshold;
	}
	public int getJudgement()
    {
		return judgement;
	}
    private JTextField inputBox1 , inputBox2;
    private int modelResult = JOptionPane.CANCEL_OPTION;
    private int threshold;
    private int judgement;
}
