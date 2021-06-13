package huffmanencode.text;

class TNode
{
	int freq,ch;
	TNode left,right;
	TNode(){}
	public TNode(int ch,int freq)
	{
		this.ch=ch;
		this.freq=freq;
	}
}