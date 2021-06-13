package huffmanencode.text;
import java.io.*;

class CONS
{
	public static final int SIZE1=512, SIZE2=128, SIG=65535, MINFILESIZE=3;
}

class FileIO
{
	int freq[][]=new int[CONS.SIZE1][], totalchr;
	String source;

	FileIO(String source)
	{this.source=source;}

	void readFile()throws IOException
	{
		if(isText(source))
			{totalchr=FileInput.scanTextFile(source,freq);}
		else
			{throw new IOException("Invalid text file");}
	}
	/***************IT WILL GENERATE BINARY FILE*******************/
	long writeFile(char extra_info_ch[][],byte extra_info_code[][][],byte sigcode[], byte codes[][][]) throws IOException
	{
		if(isText(source))
			{return FileOutput.writeCompressedText(extra_info_ch,extra_info_code,sigcode,codes,source);}
		else
			{throw new IOException("Invalid text file");}
	}

	static boolean isText(String fname)
	{
		String arr[]={"txt","java","py","c"};
		int i=fname.lastIndexOf('.');
		String extention="";
		if(i>0)
			{extention=fname.substring(i+1);}
		for(String str:arr){
			if(str.equals(extention))
				{return true;}
		}
		return false;
	}
}
class FileInput
{
	public static int scanTextFile(String source,int [][]freq) throws IOException 
	{
		File f=new File(source);
		if(!f.exists()||!f.isFile())
			{throw new IOException("file not found");}
		int c,id1,id2,totalchr=0;
		/* Counts the frequency of Characters from the File */
		try(BufferedReader br=new BufferedReader(new FileReader(source)))
		{
			while((c=br.read())!=-1)
			{
				id1=(int)(c/CONS.SIZE2);
				id2=c-(id1*CONS.SIZE2);
				if(freq[id1]==null)
				{
					freq[id1]=new int[CONS.SIZE2];
				}
				if(freq[id1][id2]==0)
				{
					totalchr++;
				}
				freq[id1][id2]++;
			}
		}
		if(totalchr<CONS.MINFILESIZE)
			{throw new IOException("small file not allow");}
		return totalchr;
	}
	/*IT IS FOR PRINTING ARRAY OF INT[][] AND CHAR[][]*/
	/*void print()
	{
		int count=0;
		for(int i=0;i<ch.length;i++)
		{	if(ch[i]==null){continue;}
			for(int j=0;j<ch[i].length;j++)
			{
				if(freq[i][j]!=0){
					System.out.print("["+(int)ch[i][j]+"|"+freq[i][j]+"]");
					count++;
					if(count%10==0||count%10==5)
					System.out.println();
				}
			}
		}
	}*/
}
class FileOutput
{
	/*it will generate binary file*/
	static long writeCompressedText(char extra_info_ch[][],byte extra_info_code[][][],byte sigcode[],byte codes[][][],String source)throws IOException
	{
		File f=new File(source);
		if(!f.exists()||!f.isFile())
			{throw new IOException("file not found");}
		if(source.contains("compress.dat"))
			{throw new IOException("invalid input file");}
		int i=source.lastIndexOf('.');
		String extention="";
		if(i>0)
			{extention=source.substring(i+1);}

		int c;
		int maxcodelen=extra_info_code[extra_info_ch.length-1][0].length,id1,id2;
		BitIOQ q=new BitIOQ(maxcodelen*3);

		try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("compress.dat")))
		{
			oos.writeObject(extention);
			oos.writeChar(CONS.SIG);
			oos.writeObject(extra_info_ch);
			oos.writeObject(extra_info_code);
			
			try(BufferedReader br=new BufferedReader(new FileReader(source)))
			{
				out: while(true)
				{
					while(q.getCount()<8)
					{
						if((c=br.read())!=-1)
						{
							id1=(int)(c/CONS.SIZE2);
							id2=c-(id1*CONS.SIZE2);
							//System.out.print("<"+c+">");
							q.insertCode(codes[id1][id2]);
						}
						else
						{
							/*insert signalcode at end of file*/
							q.insertCode(sigcode);
							while(q.getCount()>0)
							{oos.write(q.removeByte());}
							System.out.println("SIGNAL CODE ENTERED");
							break out;
						}
					}
					//int temp=q.removeByte();
					oos.write(q.removeByte());
					//System.out.print("<"+temp+">");
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