package huffmanencode.bin;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;
class HuffmanTreeLinkedList
{
	private static class ListNode
	{
		TNode data;
		ListNode next;
		ListNode(){}
		public ListNode(TNode data,ListNode next)
		{
			this.data=data;
			this.next=next;
		}
	}
	/*it will create linklist which contain tree nodes*/
	static ListNode fillLL(int[]freq,int totalchr)
	{
		Stack<ListNode> avail1;
		Stack<TNode> avail2;
		avail1=new Stack<>();
		for(int i=0;i<=totalchr;i++)
			{avail1.push(new ListNode());}
		avail2=new Stack<>();
		for(int i=0;i<=totalchr;i++)
			{avail2.push(new TNode());}
		ListNode head=null,temp;
		
/********************************************************************/
/************** TIME CONSUMING PLESE FIND ALTERNATIVE ***************/
		
        for(int i=0;i<freq.length-1;i++)
		{
			if(freq[i]==0)
				{continue;}
			temp=avail1.pop();
			temp.data=avail2.pop();
			temp.data.freq=freq[i];
			temp.data.ch=i;
			temp.next=head;
			head=temp;			
		}
		temp=avail1.pop();
		temp.data=avail2.pop();
		temp.data.freq=0;
		temp.data.ch=CONS.SIG;
		temp.next=head;
		head=temp;
/******************************************************************/	
         return head;
	}

	/*it will create tree from given linklist*/
	static TNode makeTree(ListNode head,int totalchr)
	{
        TNode tree=null;
		if(totalchr==0)return tree;
		if(totalchr==1)
		{tree=head.data;return tree;}

		ListNode prvT,prvMin,prvSmin,min=null,smin=null,t;
		int chvalue;
		TNode temp;
		while(head.next!=null)
		{
			//printLL();
			 
			prvT=head.next;
			t=prvT.next;

			if((head.data.freq)<(head.next.data.freq)){
				min=head;smin=head.next;
				prvMin=null;prvSmin=min;
			}
			else{
				min=head.next;smin=head;
				prvMin=smin;prvSmin=null;
			}
			if(head.next.next==null)break;
			while(t!=null)
			{
				if(t.data.freq<=min.data.freq){
					smin=min;prvSmin=prvMin;
					min=t;prvMin=prvT;
				}
				else if(t.data.freq<=smin.data.freq){
					smin=t;
					prvSmin=prvT;
				}
				prvT=t;
				t=t.next;
			}
			
			if(min.data.ch>=0 && smin.data.ch>=0)
				{chvalue=-1;}
			else if(min.data.ch<smin.data.ch)
				{chvalue=min.data.ch-1;}
			else
				{chvalue=smin.data.ch-1;}
			temp=new TNode(chvalue,min.data.freq+smin.data.freq);
			temp.left=min.data;
			temp.right=smin.data;
/********************VARY DENGEROUS CODDE*************************/
/*******BE CAREFUL THIS MAY GENERATE NULLPOINTEREXCEPTION*********/
			if(min.next==smin)
			{
				if(prvMin!=null)
					{prvMin.next=smin.next;}
				else {head=smin.next;}
			}
			else if(smin.next==min)
			{
					if(prvSmin!=null)
						{prvSmin.next=min.next;}
					else {head=min.next;}
			}
			else{
				if(prvMin!=null)
					{prvMin.next=min.next;}
				else
					{head=head.next;}
				if(prvSmin!=null)
					{prvSmin.next=smin.next;}
				else
					{head=head.next;}
			}
/******************************************************************/
			t=new ListNode(temp,head);
			head=t;
		}

		if(min.data.ch>=0 && smin.data.ch>=0)
			{chvalue=-1;}
		else if(min.data.ch<smin.data.ch)
			{chvalue=min.data.ch-1;}
		else
			{chvalue=smin.data.ch-1;}
		temp=new TNode(chvalue,head.data.freq+head.next.data.freq);
		temp.left=min.data;temp.right=smin.data;
		tree=temp;
         return tree;       
	}
        
    static void printTree(TNode root)
    {
        if(root==null)
    		{return;}
        int level=(-1)*root.ch+1;
        TNode arr[][]=new TNode[level][];
        for(int i=0;i<level;i++)
            {arr[i]=new TNode[(int)Math.pow(2,i)];}
          
        Queue<TNode> nodeq=new LinkedList<>();
        Queue<Integer> idxq=new LinkedList<>();
        TNode newline=new TNode(-1,-1),prev=null,curr;
            
        int plevel=1,idx,left,right;
        nodeq.add(root);nodeq.add(newline);
        idxq.add(0);
        arr[0][0]=root;
        while(true)
        {
            curr=nodeq.remove();
            if(curr==newline&&prev==newline){break;}
            prev=curr;
            if(curr==newline)
            {
                nodeq.add(newline);
                plevel++;continue;
            }
            
            idx=idxq.remove();
            left=idx*2;
            right=left+1;
             
            if(curr.left!=null){
            	nodeq.add(curr.left);
            	idxq.add(left);
            	arr[plevel][left]=curr.left;
            }
            if(curr.right!=null){
                nodeq.add(curr.right);
                idxq.add(right);
                arr[plevel][right]=curr.right;
            }
        }  
		drawTree(arr);         
    }
    static void drawTree(TNode arr[][])
	{
		try(BufferedWriter writer=new BufferedWriter(new FileWriter("TreeRepresentation.txt")))
		{
			int level=arr.length;
			System.out.println(level);
			int constrain1=(int)(Math.pow(2,level)-2)/2,
				constraint2=constrain1;
			for(int row=0;row<level;row++)
			{
				for(int space=0;space<constrain1;space++)
				{
					for(int i=0; i<3; i++)
					{
						//System.out.print(" ");
						writer.write(' ');
					}
				}
				for(int col=0;col<Math.pow(2,row);col++)
				{
					if(arr[row][col]==null)
					{
						for(int i=0;i<3;i++)
						{
							//System.out.print("_");
							writer.write(' ');
						}
					}
					else
					{
						String str=String.valueOf(arr[row][col].freq);
						if(str.length()<3)
						{
							for(int loop=0; loop<str.length(); loop++)
								writer.write(' ');
							if(arr[row][col].left==null && arr[row][col].right==null)
							{
								//System.out.printf("%03d",arr[row][col].ch);
								writer.write(arr[row][col].ch);	
							}
							else
							{
								//System.out.printf("%03d",arr[row][col].freq);
								writer.write(str);
							}
						}
						else	
						{
							//System.out.printf("%03d",arr[row][col].freq);
							writer.write(String.valueOf(arr[row][col].freq));
						}
					}
					if(col==Math.pow(2,row)-1)
						break;
					for(int i=0;i<constraint2;i++)
					{
						for(int j=0; j<3; j++)
						{
							//System.out.print(" ");
							writer.write(' ');
						}
					}
				}
				constraint2=constrain1;
				constrain1=(constrain1-1)/2;
				//System.out.println("\n");
				writer.newLine();
			}
		}
		catch(IOException io){}
	}
	static void printLL(ListNode head)
	{
		ListNode temp=head;
		int i=0;
		System.out.println();
		while(temp!=null){
			System.out.print("("+temp.data.ch+"|"+temp.data.freq+")");
			temp=temp.next;i++;
		}
		System.out.print("{"+i+"}");
	}
	/*it will return huffman tree*/
	static TNode getTree(int[]freq,int totalchr)
	{
		ListNode head=fillLL(freq,totalchr);
        TNode tree=makeTree(head,totalchr);
        
		//printTree(tree);
		return tree;
	}
}