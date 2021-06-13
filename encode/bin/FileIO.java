package huffmanencode.bin;
import java.io.*;

interface CONS
{
	int SIZE=256, SIG=256, MINFILESIZE=3;
}

class FileIO
{
	int freq[]=new int[CONS.SIZE+1],totalchr;
	private String source;

	FileIO(String source)
	{this.source=source;}

	void readFile()throws IOException
	{
		totalchr=FileInput.scanBinFile(source,freq);
		freq[CONS.SIZE]=1;
		totalchr++;
	}
	/***************IT WILL GENERATE BINARY FILE*******************/
	long writeFile(char extra_info_ch[][],byte extra_info_code[][][],byte sigcode[], byte codes[][]) throws IOException
	{
		return FileOutput.writeCompressedBin(extra_info_ch,extra_info_code,sigcode,codes,source);
	}
}
class FileInput
{
	public static int scanBinFile(String source,int []freq) throws IOException 
	{
		File f=new File(source);
		if(!f.exists()||!f.isFile())
			{throw new IOException("file not found");}
		if(source.contains("compress.dat"))
			{throw new IOException("invalid input file");}
		int c,totalchr=0;
		try(BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source)))
		{
			/*int remember=bis.read();
			ch[remember]=remember;*/

			while((c=bis.read() )!= -1)//range of c = 0-255
			{
				if(freq[c]==0){
					totalchr++;
					freq[c]++;
				}
				else{freq[c]++;}
			}
		}
		if(totalchr<CONS.MINFILESIZE)
			{throw new IOException("small file not allowed");}
		return totalchr;
	}
}
class FileOutput
{
	/*it will generate binary file*/
	
	static long writeCompressedBin(char extra_info_ch[][],byte extra_info_code[][][],byte sigcode[],byte codes[][],String source)throws IOException
	{
		File f=new File(source);
		if(!f.exists()||!f.isFile())
			{throw new IOException("file not found");}
		int i=source.lastIndexOf('.');
		String extention="";
		if(i>0)
			{extention=source.substring(i+1);}

		int c;
		int maxcodelen=extra_info_code[extra_info_ch.length-1][0].length;
		BitIOQ q=new BitIOQ(maxcodelen*3);

		try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("compress.dat")))
		{
			oos.writeObject(extention);
			oos.writeChar(CONS.SIG);
			oos.writeObject(extra_info_ch);
			oos.writeObject(extra_info_code);

			try(BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source)))
			{
				out: while(true)
				{
					while(q.getCount()<8)
					{
						if((c=bis.read())!=-1)
						{
							q.insertCode(codes[c]);
						}
						else{
							/*insert signalcode at end of file*/
							q.insertCode(sigcode);
							while(q.getCount()>0)
							{oos.write(q.removeByte());}
							System.out.println("SIGNAL CODE ENTERED");
							break out;
						}
					}
					oos.write(q.removeByte());
				}
			}
		}
		return new File("compress.dat").length();
	}
}
/*use for creating binary file*/
class BitIOQ
{
	private byte q[],count=0;
	int front=-1,rear=-1;
	BitIOQ(int size)
	{q=new byte[size];}

	boolean insertCode(byte ... datas)
	{
		for(byte data : datas)
		{
			if(rear==q.length-1){rear=0;}
			else {rear+=1;}
			if(front==rear){
				if(rear==0){rear=(byte)(q.length-1);}
				else {rear-=1;}
				return false;
			}
			count++;
			q[rear]=data;
			if(front==-1){front=0;}
		}	
		return true;
	}

	int removeByte()
	{
		int value=0,rdx=128;
		byte data;

		for(int i=0;i<8;i++)
		{
			if(front==-1){data=0;}
			else{
				data=q[front];
				count--;
			}
			
			if(front==rear)
			{front=rear=-1;}
			else if(front==q.length-1){front=0;}
			else {front+=1;}

			value+=data*rdx;
			rdx/=2;
		}	
		return value;
	}
	byte getCount(){return count;}
}