package huffmanencode.bin;
class ExtraInfo
{
	byte codes[][][],sigcode[];
	char ch[][];
	/*MINIMUM LENGTH OF CODE IS 1 !*/
	final int MINLEN=1;
	private int endch[],totalchr,len1;
	/*endch[i] point to lastelement+1 for each character's code*/
	ExtraInfo(int level,int totalchr){
		ch=new char[level-1][];
		endch=new int[level-1];
		codes=new byte[level-1][][];
		this.totalchr=totalchr;
	}

	boolean insert(int ch,byte ... code)
	{
		if(ch==CONS.SIG){sigcode=code;}
		int dim1=code.length-MINLEN;
		if(codes[dim1]==null){
			int value=(int)Math.pow(2,dim1+1);
			int dim2=(value<totalchr)?value:totalchr;
			this.ch[dim1]=new char[dim2];
			codes[dim1]=new byte[dim2][];
			len1++;
		}
		this.ch[dim1][endch[dim1]]=(char)ch;
		codes[dim1][endch[dim1]]=code;
		endch[dim1]++;
		return true;
	}

	boolean endEntry()
	{
		char resizech[][]=new char[len1][];
		byte resizecodes[][][]=new byte[len1][][];
		int countid=0;

		for(int i=0;i<ch.length;i++)
		{
			if(ch[i]==null){continue;}
			resizech[countid]=new char[endch[i]];
			resizecodes[countid]=new byte[endch[i]][];
			for(int j=0;j<endch[i];j++){
				resizech[countid][j]=ch[i][j];
				resizecodes[countid][j]=codes[i][j];
			}
			countid++;
		}
		ch=resizech;
		codes=resizecodes;
		return true;
	}

	void print()
	{
		for(int r=0;r<ch.length;r++){
			for(int c=0;c<ch[r].length;c++){
				System.out.print(ch[r][c]+" ");
				if(codes[r][c]!=null)
				for(int i=0;i<codes[r][c].length;i++){
					System.out.print(codes[r][c][i]);
				}
				System.out.println();
			}
		}
	}
}