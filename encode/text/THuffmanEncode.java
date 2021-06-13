package huffmanencode.text;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileReader;

public class THuffmanEncode
{
	TNode tree;
	ExtraInfo obj;
	private byte temp[],codes[][][]=new byte[CONS.SIZE1][][];
	
	/*it will initialize tree*/
	THuffmanEncode(int totalchr,int[][]freq)
	{
		tree=HuffmanTreeLinkedList.getTree(freq,totalchr);
		int level=(-1)*tree.ch+1;
		temp=new byte[level];
		obj=new ExtraInfo(level,totalchr);
		this.findCode(tree,0);
		obj.endEntry();
		/*obj.print();*/
	}

	public static long main(String fname)throws IOException
	{
		THuffmanEncode encode=null;
		FileIO f=new FileIO(fname);		// (source)
		
		f.readFile();
		encode=new THuffmanEncode(f.totalchr,f.freq);
		f.freq=null;
		
		return f.writeFile(encode.obj.ch,encode.obj.codes,encode.obj.sigcode,encode.codes);
		//System.out.println("min code len : "+encode.obj.codes[0][0].length);		
	}

	/*recursive method for finding code of character*/
	void findCode(TNode curr,int id)
	{
		if(curr.left!=null)
		{
			temp[id]=0;
			findCode(curr.left,id+1);
		}
		if(curr.right!=null)
		{
			temp[id]=1;
			findCode(curr.right,id+1);
		}
		if(curr.left==null&&curr.right==null)
		{
			byte temp1[]=new byte[id];
			int id1,id2;
			id1=(int)(curr.ch/CONS.SIZE2);
			id2=curr.ch-(id1*CONS.SIZE2);
			if(codes[id1]==null)
				{codes[id1]=new byte[CONS.SIZE2][];}
			for(int i=0;i<id;i++)
				{temp1[i]=temp[i];}
			codes[id1][id2]=temp1;
			obj.insert(curr.ch,temp1);
		}
	}
}