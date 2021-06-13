package huffmandecode;
import java.io.*;

class FileIO
{
	static long decode(String source) throws IOException,ClassNotFoundException
	{
		File f=new File(source);
		if(!f.exists()||!f.isFile())
			{throw new IOException("file not found");}

		int i=source.lastIndexOf('.');
		String extention="";
		if(i>0){extention=source.substring(i+1);}
		if(!extention.equals("dat"))
			{throw new IOException("invalid file for decoding");}

		try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(source)))
		{
			extention=(String)ois.readObject();

			String arr[]={"txt","java","py","c"};
			for(String str:arr){
				if(str.equals(extention))
				{
					return generateTextFile(ois,extention);
				}
			}
			return generateBinFile(ois,extention);
		}

	}

	static long generateTextFile(ObjectInputStream ois,String extention)throws IOException,ClassNotFoundException
	{
		try(BufferedWriter bw=new BufferedWriter(new FileWriter("output."+extention)))
		{
			int SIG=ois.readChar();
			char [][]extra_info_ch=(char[][])ois.readObject();
			byte [][][]extra_info_code=(byte[][][])ois.readObject();

			int maxcodelen=extra_info_code[extra_info_code.length-1][0].length;
			CodeQueue q=new CodeQueue(maxcodelen+(8*2),extra_info_code,extra_info_ch);

			int ch;
			out : while(true)
			{
				int temp;
				while(q.getCount()<maxcodelen)
				{
					temp=ois.readByte();
					if(temp<0){temp+=256;}
					q.insertByte(binCode(temp));//printing bin code		
				}

				/*q.printQ();*/
				if((ch=q.removeChar())==SIG)
				{
					System.out.println("SIGNAL CODE FOUND");
					break;
				}
				/*q.printQ();*/
				bw.write(ch);
			}
			return new File("output."+extention).length();
		}
	}

	static long generateBinFile(ObjectInputStream ois,String extention)throws IOException,ClassNotFoundException
	{
		try(BufferedOutputStream bis=new BufferedOutputStream(new FileOutputStream("output."+extention)))
		{
			int SIG=ois.readChar();
			char [][]extra_info_ch=(char[][])ois.readObject();
			byte [][][]extra_info_code=(byte[][][])ois.readObject();

			int maxcodelen=extra_info_code[extra_info_code.length-1][0].length;
			CodeQueue q=new CodeQueue(maxcodelen+(8*2),extra_info_code,extra_info_ch);

			int ch;
			out : while(true)
			{
				int temp;
				while(q.getCount()<maxcodelen)
				{
					temp=ois.readByte();
					if(temp<0){temp+=256;}
					q.insertByte(binCode(temp));//printing bin code		
				}

				/*q.printQ();*/
				if((ch=q.removeChar())==SIG)
				{
					System.out.println("SIGNAL CODE FOUND");
					break;
				}
				/*q.printQ();*/
				bis.write(ch);
			}
			//System.out.println("*");
			return new File("output."+extention).length();
		}
	}

	/*it will return binary of given num in 8 bit*/
	static byte[] binCode(int num)
	{
		BitIOStack stk=new BitIOStack(8);
		while(num!=0)
		{
			stk.push(num%2);
			num=num/2;
		}
		byte code[] =stk.popArr();
		return code;
	}
}

class BitIOStack
{
	private byte BitIOStack[],top=-1,bin[];

	BitIOStack(int size)
	{BitIOStack=new byte[size];}

	boolean push(int data)
	{
		if(top==BitIOStack.length-1)return false;
		BitIOStack[++top]=(byte)data;
		return true;
	}
	byte[] popArr()
	{
		while(push(0));
		bin=new byte[BitIOStack.length];
		for(int i=BitIOStack.length-1,j=0;i>=0&&j<BitIOStack.length;i--,j++)
		{
			bin[j]=BitIOStack[i ];
		}
		return bin;
	}
}