package cn.keyshare.utils.File;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

public class FileRegExFilter implements FileFilter
{
	private String mRegEx ;
	private Pattern mPattern;
	
    public FileRegExFilter(String regEx)
	{
    	mRegEx = regEx;
    	mPattern = Pattern.compile(mRegEx);
	}
	
	@Override
	public boolean accept(File pathname)
	{
		String filenameString = pathname.getName();
		if(mPattern.matcher(filenameString).matches()) 
		{
			return true;
		}
		return false;
	}

}
