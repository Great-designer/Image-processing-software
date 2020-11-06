package imageView;

import javax.swing.*;

import java.awt.*;
/** Threshold�����ڼ���ֵ�任 **/
public class Threshold extends JDialog
{
	public Threshold(java.awt.Frame ancestor, boolean successor)
	{
		super(ancestor, successor);
		initComponents();
	}
	/**
	 * initComponents�������ڳ�ʼ�����
	 */
	private void initComponents()
	{
		JLabel JLabel1 = new JLabel();
		JButton trueBtn = new JButton();
		JButton falseBtn = new JButton();
		inputBox = new JTextField();
		getContentPane().setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints meshBoundary;
		JLabel1.setText("��������ֵ(0~255������)");
		meshBoundary = new GridBagConstraints();
		meshBoundary.gridx = 1;
		meshBoundary.gridy = 0;
		meshBoundary.gridwidth = 4;
		meshBoundary.gridheight = 1;
		meshBoundary.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(JLabel1, meshBoundary);
		meshBoundary = new GridBagConstraints();
		meshBoundary.gridx = 1;
		meshBoundary.gridy = 2;
		meshBoundary.gridwidth = 5;
		meshBoundary.gridheight = 1;
		meshBoundary.ipadx = 200;
		meshBoundary.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(inputBox, meshBoundary);
		trueBtn.setText("ȷ��");
		meshBoundary = new GridBagConstraints();
		meshBoundary.gridx = 1;
		meshBoundary.gridy = 4;
		meshBoundary.gridwidth = 2;
		meshBoundary.gridheight = 1;
		meshBoundary.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(trueBtn, meshBoundary);
		trueBtn.addActionListener(evt -> okPerform());
		falseBtn.setText("ȡ��");
		meshBoundary = new GridBagConstraints();
		meshBoundary.gridx = 4;
		meshBoundary.gridy = 4;
		meshBoundary.gridwidth = 2;
		meshBoundary.gridheight = 1;
		meshBoundary.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(falseBtn, meshBoundary);
		falseBtn.addActionListener(evt -> cancelPerform());
		pack();
	}
	private void cancelPerform()
	{
		inputBox.setText("");
		modelResult = JOptionPane.CANCEL_OPTION;
		dispose();
	}
	private void okPerform()
	{
		String Text = inputBox.getText();
		String template = "(\\d?)+(\\d?)+(\\d?)";
		if (Text.matches(template))
		{
			threshold = Integer.parseInt(Text);
			if (threshold > 255)
			{
				String warning = "�벻Ҫ���볬��255�����֣�";
				JOptionPane.showMessageDialog(getContentPane(), warning, "ϵͳ��Ϣ", JOptionPane.WARNING_MESSAGE);
				inputBox.setText("");
			}
			else
			{
				modelResult = JOptionPane.OK_OPTION;
				dispose();
			}
		}
		else
		{
			String warning = "������0~255��������";
			JOptionPane.showMessageDialog(getContentPane(), warning, "ϵͳ��Ϣ", JOptionPane.WARNING_MESSAGE);
			inputBox.setText("");
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

	private JTextField inputBox;
	private int modelResult = JOptionPane.CANCEL_OPTION;
	private int threshold;
}
