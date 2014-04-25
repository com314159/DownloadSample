package cn.keyshare.utils.File;

import java.io.File;
import java.io.FileFilter;

public class FileExtensionFilter implements FileFilter
{
	private String mFileExtension = "";
	
    public FileExtensionFilter(String fileExtension)
	{
		mFileExtension = fileExtension;
	}
	
	@Override
	public boolean accept(File pathname)
	{
		String filenameString = pathname.getName();
		if(filenameString.endsWith(mFileExtension)) 
		{
			return true;
		}
		return false;
	}

}
