package huffmandecode;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

class UserInput extends Frame implements ActionListener
{
	Button b1=new Button("choose file"),b2=new Button("decompress");
	TextField tb=new TextField(50);
	Label oldsize=new Label(),newsize=new Label(),msg=new Label();

	UserInput()
	{
		super("decode");
		setSize(500,500);
		setVisible(true);
		setLayout(new GridLayout(6,1,0,10));

		Panel arr[]={new Panel(),new Panel(),new Panel()};
		arr[0].add(b1);
		arr[1].add(b2);
		arr[2].add(tb);

		Font f=new Font("Arial",Font.BOLD,15);
		oldsize.setFont(f);oldsize.setForeground(Color.RED);
		newsize.setFont(f);newsize.setForeground(Color.RED);

		tb.setEditable(false);
		add(arr[0],0);add(arr[2],1);
		add(oldsize,2);
		add(newsize,3);
		add(arr[1],4);add(msg,5);
		
		b1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				FileDialog fd=new FileDialog(UserInput.this,"choosefile",FileDialog.LOAD);
				fd.setVisible(true);
				String fname=fd.getDirectory()+fd.getFile();

				tb.setText(fname);
				oldsize.setText("");
				newsize.setText("");
				File f=new File(fname);

				if(f.exists()&&f.isFile())
				{oldsize.setText(("file size : "+f.length()/1000.0)+" kb");}
			}
		});
		b2.addActionListener(this);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{dispose();}
		});
	}

	public void actionPerformed(ActionEvent ae)
	{
		String fname=tb.getText();
		File f=new File(fname);
		long size=0;
		newsize.setText("");
		if(!f.exists() || !f.isFile())
			{newsize.setText("invalid file");return;}

		try
		{
			if(isDat(fname))
			{size=FileIO.decode(fname);}
			else
			{newsize.setText("only .dat files are valid");return;}	
		}
		catch(Exception e)
		{newsize.setText("can't decompress file");return;}


		newsize.setText("decompressedfilesize : "+(size/1000.0)+" kb");
	}

	public static void main(String[] args)
	{new UserInput();}

	static boolean isDat(String fname)
	{
		int i=fname.lastIndexOf('.');
		String extention="";
		if(i>0){extention=fname.substring(i+1);}

		if(extention.equals("dat"))
		{return true;}
		return false;
	}
}

