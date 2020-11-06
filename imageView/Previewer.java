package imageView;

import java.io.File;
import java.awt.*;
import javax.swing.*;

import java.beans.*;
import javax.imageio.*;
/** PreviewerÀàÓÃÓÚÍ¼ÏñÔ¤ÀÀ **/
class Previewer extends JComponent implements PropertyChangeListener
{
	ImageIcon indexImage = null;
	public Previewer(JFileChooser fc)
	{
	    setPreferredSize(new Dimension(100, 50));
	    fc.addPropertyChangeListener(this);
	}
	public void loadImage(File f)
	{
        if (f == null)
        {
            indexImage = null;
        } 
        else
        {
			ImageIcon tmpIcon = null;
			try
			{
				tmpIcon = new ImageIcon(ImageIO.read(f));
			}
			catch(Exception ignored){}
			if(tmpIcon == null) return ;
			if(tmpIcon.getIconWidth() > 120)
			{
			    indexImage = new ImageIcon(
					tmpIcon.getImage().getScaledInstance(120, -1, Image.SCALE_DEFAULT));
			} 
			else
			{
			    indexImage = tmpIcon;
			}
	    }
	}
	public void propertyChange(PropertyChangeEvent e)
	{
	    String prop = e.getPropertyName();
	    if(prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))
	    {
			if(isShowing())
			{
	            loadImage((File) e.getNewValue());
			    repaint();
			}
	    }
	}
	public void paint(Graphics g)
	{
	    if(indexImage != null)
	    {
			int x = getWidth()/2 - indexImage.getIconWidth()/2;
			int y = getHeight()/2 - indexImage.getIconHeight()/2;
			if(y < 0)
			{
				y = 0;
			}
			if(x < 5)
			{
				x = 5;
			}
			indexImage.paintIcon(this, g, x, y);
	    }
	}
}