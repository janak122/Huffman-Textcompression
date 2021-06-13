package huffmanencode.bin;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileReader;

public class BHuffmanEncode
{
	TNode tree;
	ExtraInfo obj;
	private byte temp[],nullcode[],codes[][]=new byte[CONS.SIZE+1][];

	/*it will initialize tree*/
	BHuffmanEncode(int[]freq,int totalchr)
	{
		tree=HuffmanTreeLinkedList.getTree(freq,totalchr);		
		int level=(-1)*tree.ch+1;
		temp=new byte[totalchr];
		obj=new ExtraInfo(level,totalchr);
		this.findCode(tree,0);
		obj.endEntry();
		/*obj.print();*/
	}

	public static long main(String fname)throws IOException
	{
		BHuffmanEncode encode=null;
		FileIO f=new FileIO(fname);// (source)
		
		f.readFile();
		encode=new BHuffmanEncode(f.freq,f.totalchr);
		f.freq=null;

		return f.writeFile(encode.obj.ch,encode.obj.codes,encode.obj.sigcode,encode.codes);
		//System.out.println("min code len : "+encode.obj.codes[0][0].length);
	}

	/*recursive method for finding code of character*/
	void findCode(TNode curr,int id)
	{
		if(curr.left!=null){
			temp[id]=0;
			findCode(curr.left,id+1);
		}
		if(curr.right!=null){
			temp[id]=1;
			findCode(curr.right,id+1);
		}
		if(curr.left==null&&curr.right==null){
			byte temp1[]=new byte[id];
			for(int i=0;i<id;i++)
				{temp1[i]=temp[i];}

			codes[curr.ch]=temp1;
			obj.insert(curr.ch,temp1);
		}
	}
}