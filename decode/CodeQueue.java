package huffmandecode;

class CodeQueue
{
	private byte code[][][],q[];
	private char chr[][],ch;
	private int count,front=-1,rear=-1;
	private final int MINLEN;
	/*create queue of given size and get code[][][] and chr[][]*/
	CodeQueue(int size,byte[][][]code,char chr[][])
	{
		this.code=code;this.chr=chr;
		MINLEN=code[0][0].length;
		q=new byte[size];
	}

	/*insert given byte[] into CircularQueue*/
	boolean insertByte(byte ... datas)
	{
		/*System.out.print("{");*/
		for(byte data : datas)
		{
			if(rear==q.length-1){rear=0;}
			else {rear+=1;}
			if(front==rear){
				if(rear==0){rear=(q.length-1);}
				else {rear-=1;}
				return false;
			}
			count++;
			q[rear]=data;
			if(front==-1){front=0;}
			/*System.out.print(q[rear]);*/
		}	
		/*System.out.print("}");*/
		return true;
	}
	/*to get element present at given position from front*/
	byte peep(int idx)
	{
		if(idx>=count){return -128;}
		int pos=front+idx;
		if(pos>=q.length)
			{pos=pos-q.length;}
		return q[pos];
	}
	/* first it will check for 3 bit then 4 bit then 5 bit 
	 * and so on.....
	 * if matched code found then it will return currosponding
	 * char other wise it will return -1
	 */
	int removeChar()
	{
		if(count<MINLEN){return -1;}
		int len=0;

		for(int idx=0;idx<chr.length;idx++)
		{
			/*finding matched code*/
			for(int id=0;id<chr[idx].length;id++)
			{
				len=0;
				for(int i=0;i<code[idx][id].length;i++){
					if(code[idx][id][i]==peep(i))
						{len++;}
					else break;
				}
				/*if matched code found then removeChar those many bits
				 *from CircularQueue*/
				if(len==code[idx][id].length)
				{
					for(int i=0;i<code[idx][id].length;i++)
					{
						if(rear==front)
						{front=rear=-1;}
						else if(front==q.length-1){front=0;}
						else{front++;}
						count--;
					}
					/*System.out.print("{"+chr[idx][id]+"}");
					 */
					return chr[idx][id];
				}
			}
		}
		return -1;
	}
	/*return number of element present inside queue*/
	int getCount()
	{return count;}	

	void print()
	{
		for(char[]arr:chr){
			for(char c:arr)
				{System.out.print("("+(int)c+")");}
			System.out.println();
		}
		for(byte[][]byt:code){
			for(byte[]by:byt){
				for(byte b:by)
					{System.out.print(b);}
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	void printQ()
	{
		int visit=0;
		for(int i=front;visit<count;visit++,i++)
		{
			System.out.print(q[i]);
			if(i==q.length-1)i=-1;
		}
		//System.out.println();
	}
}

