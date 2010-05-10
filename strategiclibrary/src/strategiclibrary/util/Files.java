package strategiclibrary.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Files {
	/**
	 * Copies all the files from one directory to another.  Note that the from directory itself is not copied, only
	 * its contents.
	 * @param fromDir
	 * @param toDir
	 */
	public static void copyFiles(String fromDir,String toDir) throws IOException
	{
		copyFiles(fromDir,toDir,false);
	}
	
	/**
	 * Copies all the files from one directory to another.  Note that the from directory itself is not copied, only
	 * its contents.
	 * @param fromDir
	 * @param toDir
	 * @param recurse If true, will copy all files and directories recursively.
	 */
	public static void copyFiles(String fromDir,String toDir,boolean recurse) throws IOException
	{
		copyFiles(new File(fromDir),new File(toDir),recurse);
	}
	
	/**
	 * Copies all the files from one directory to another.  Note that the from directory itself is not copied, only
	 * its contents.
	 * @param fromDir
	 * @param toDir
	 * @param recurse If true, will copy all files and directories recursively.
	 */
	public static void copyFiles(File fromDir,File toDir,boolean recurse) throws IOException
	{
		if(fromDir != null && fromDir.exists() && toDir != null)
		{
			if(!toDir.exists()) toDir.mkdirs();
		}
		for(File f : fromDir.listFiles())
		{
			if(f.isFile()) copyFile(f,new File(toDir,f.getName()));
			else if(recurse) copyFiles(f,new File(toDir,f.getName()),true);
		}
	}
	
	public static void copyFile(File fromFile,File toFile) throws IOException
	{
		if(fromFile != null && fromFile.exists() && toFile != null)
		{
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(fromFile));
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(toFile));
			try {
			copy(in,out);	
			}finally
			{
				try {
					if(in != null)
						in.close();
				}finally {
					if(out != null)
						out.close();
				}
			}
		}
	}
	
	/**
	 * Copies an inputstream to an outputstream.  The calling method is responsible for closing the streams.
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static long copy(InputStream in,OutputStream out) throws IOException
	{
		byte[] buf = new byte[Short.MAX_VALUE];
		int numBytesRead = -1;
		long sum = 0;
		while((numBytesRead = in.read(buf)) > -1)
		{
			out.write(buf,0,numBytesRead);
			sum += numBytesRead;
		}
		return sum;
	}
	
	public static void deleteTree(String directory)
	{
		deleteTree(new File(directory));
	}
	
	public static void deleteTree(File directory)
	{
		if(directory != null && directory.exists())
		{
			if(directory.delete()) return;
			else
			{
				for(File f : directory.listFiles())
				{
					if(f.isFile()) f.delete();
					else deleteTree(f);
				}
			}
			directory.delete();
		}
	}

	public static String hackOffExtension(File f) {
		return hackOffExtension(f.getName());
	}

	public static String hackOffExtension(String name) {
		int index = name.lastIndexOf(".");
		if (index > -1)
			return name.substring(0, index);
		else
			return name;
	}
	
	public static String getNameOnly(String filename)
	{
		String temp = filename.replaceAll("\\\\","/");
		String[] path = filename.split("/");
		return path[path.length-1];
	}
	
	public static String getExtension(String name)
	{
		int index = name.lastIndexOf(".");
		if (index > -1 && index < name.length() - 1)
			return name.substring(index + 1);
		else
			return "";
	}

	public static String getExtension(File f) {
		return getExtension(f.getName());
	}
	
	public static long getSize(File dir)
	{
		if(dir.isFile())
		{
			return dir.length();
		}
		else
		{
			long sum = 0;
			File[] list = dir.listFiles();
			for(File f : list)
			{
				sum += getSize(f);
			}
			return sum;
		}
	}
}
