package imageView;

import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.swing.filechooser.*;
/** IFileFilter类表示图片格式的相关筛选 **/
public class IFileFilter extends FileFilter
{
	private Hashtable<String, IFileFilter> filters;
    private String description = null;
    private String fullDescription = null;
	public IFileFilter()
	{
		this.filters = new Hashtable<>();
    }
    public IFileFilter(String extension, String description)
	{
		this();
		if(extension!=null) addExtension(extension);
	 	if(description!=null) setDescription(description);
    }
    public IFileFilter(String [] filters, String description)
	{
		this();
		for (String filter : filters) {
			// add filters one by one
			addExtension(filter);
		}
	 	if(description!=null) setDescription(description);
    }
    public boolean accept(File f)
	{
		if(f != null)
		{
		    if(f.isDirectory())
		    {
				return true;
		    }
		    String extension = getExtension(f);
			return extension != null && filters.get(getExtension(f)) != null;
		}
		return false;
    }
    public String getExtension(File f)
	{
		if(f != null)
		{
		    String filename = f.getName();
		    int i = filename.lastIndexOf('.');
		    if(i>0 && i<filename.length()-1)
		    {
				return filename.substring(i+1).toLowerCase();
		    }
		}
		return null;
    }
    public void addExtension(String extension)
	{
		if(filters == null)
		{
		    filters = new Hashtable<>(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
    }
    public String getDescription()
	{
		if(fullDescription == null)
		{
			fullDescription = description==null ? "(" : description + " (";
			// build the description from the extension list
			Enumeration<String> extensions = filters.keys();
			if(extensions != null)
			{
				fullDescription += "." + extensions.nextElement();
				StringBuilder full=new StringBuilder();
				while (extensions.hasMoreElements())
				{
					full.append(", .").append(extensions.nextElement());
				}
				fullDescription +=full.toString();
			}
			fullDescription += ")";
		}
		return fullDescription;
    }
    public void setDescription(String description)
	{
		this.description = description;
		fullDescription = null;
    }
}