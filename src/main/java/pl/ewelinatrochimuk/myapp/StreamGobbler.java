package pl.ewelinatrochimuk.myapp;

import java.io.*;

public class StreamGobbler extends Thread
{
	InputStream	mInputStream=null;
	String		mType=null;
	boolean		mVerbose=false;

	public StreamGobbler(InputStream is,String type,boolean verbose)
	{
		this.mInputStream=is;
		this.mType=type;
		this.mVerbose=verbose;
	}

	public void run()
	{
		BufferedReader	buffer=null;
		String			line=null;

		try
		{
			buffer=new BufferedReader(new InputStreamReader(this.mInputStream));

			while ((line=buffer.readLine())!=null)
				if (this.mVerbose)
					System.out.println(this.mType + "> " + line);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}	
}
