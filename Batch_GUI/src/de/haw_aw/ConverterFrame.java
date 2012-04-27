package de.haw_aw;

import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

public class ConverterFrame extends Frame{
	private static final long serialVersionUID = 1L;
	protected String os;
	protected String[] files;
	protected Label fileLabel;
	protected TextArea progressArea;
	protected boolean isNative;
	public ConverterFrame(String os,String[] files,boolean nativ) {
		super("Batch-GUI: "+os);
		isNative=nativ;
		this.os=os;
		this.files=files;
		this.setSize(500, 500);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		this.setLayout(null);
		//GUI-Elemente
		Label l=new Label("Welcome to the batch-GUI!\n\nI use following configurations:");
		int xpos=50;
		int ypos=50;
		l.setBounds(xpos, ypos, this.getWidth()-xpos*2, 50);
		this.add(l);
		//l.setVisible(true);
		
		ypos+=60;
		String tools="";
		for (String s : files) tools += s+"\n";
		TextArea f=new TextArea(tools,5,5,TextArea.SCROLLBARS_VERTICAL_ONLY);
		f.setBounds(xpos, ypos, this.getWidth()-xpos*2, 100);
		
		this.add(f);
		
		ypos+=110;
		Button b= new Button("Start!");
		b.setBounds(xpos, ypos, 150, 30);
		final ConverterFrame me=this;
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isNative)
					nativeChooser();
				else
					swingChooser();
			}
			protected void nativeChooser(){
				FileDialog fd= new FileDialog(me, "Choose a file");
				fd.setVisible(true);
				String sFile=fd.getFile();
				String pfad=fd.getDirectory()+sFile;
				if (sFile!=null){
					me.convertAll(pfad);
				}
			}
			protected void swingChooser(){
				
				JFileChooser fd = new JFileChooser();
				fd.setDialogTitle( "Coose file or folder!");
				fd.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);

				int returnVal = fd.showDialog(me, "File or folder");
				if( returnVal == JFileChooser.APPROVE_OPTION) {
				File sel = fd.getSelectedFile();
				String pfad = sel.getAbsolutePath();
				String sFile=sel.getName();
				
			        if (sFile!=null){
			        	me.fileLabel.setText(sFile);
			        	if (sel.isDirectory()) {
			        		File[] files = sel.listFiles();
			        		for (File f : files){
			        			if (!f.isDirectory()){
			        				pfad=f.getAbsolutePath();
			        				me.convertAll(pfad);
			        			}
			        		}
			        	}else{
			        		me.convertAll(pfad);
			        	}
			        }
				}
			}
		});
		this.add(b);
		
		ypos+=40;
		fileLabel=new Label("No File choosen.");
		fileLabel.setBounds(xpos, ypos, this.getWidth()-xpos*2, 25);
		this.add(fileLabel);
		
		ypos+=35;
		progressArea=new TextArea("Batch process not started.",5,5,TextArea.SCROLLBARS_BOTH);
		progressArea.setBounds(xpos, ypos, this.getWidth()-xpos*2, 100);
		this.add(progressArea);
		
		this.setVisible(true);
	}
	protected void convertAll(String pfad){
		progressArea.setText("");
		for (String f : files) {
			String[] fi=f.split(",");
			call(fi[0],pfad,fi[1],fi[2]);
		}
	}
	protected void call(String tool, String arg,String opt,String ext){
		String[] prog=tool.split(" ");
		String[] opts=opt.split(" ");
		
		prog[0]=System.getProperty("user.dir")+System.getProperty("file.separator")+prog[0];
		    
		String[] command=new String[prog.length+opts.length+2];
		System.arraycopy(prog, 0, command, 0, prog.length);
		System.arraycopy(opts,0,command,prog.length+1,opts.length);
		command[prog.length]=arg;
		command[command.length-1]=arg+ext;
		//Starten
		try {
			out(command);
			Process proc = Runtime.getRuntime().exec(command);
			proc.waitFor();
			for (String i  : command) System.out.println(i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out(new String[]{e.getMessage()});
		}

	}
	protected void out(String[] txt){
		String text="";
		for (String t : txt)
			text+=t+" ";
		progressArea.setText(progressArea.getText()+text+"\n");
		
	}

}
