package harrison;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.io.*; // file IO
public class APP_summ3 extends JFrame {

	JTextArea tA;
	JScrollPane tAScroll;
	JTextArea whatyouwanted;
	int angle1 = 0;
	int angle2 = 0;
	int angle3 = 0;
	int dialAreaWidth = 800;
	int dialAreaHeight = 500;

	//======================================================== constructor
	public APP_summ3 ()
	{
		// 1... Create/initialize components

		// 2... Create content pane, set layout
		JPanel left = new JPanel ();                                   	// Create a content pane
		left.setLayout (new BorderLayout ());                         	// Use BorderLayout for panel

		JPanel right = new JPanel ();
		right.setLayout (new BoxLayout (right,BoxLayout.Y_AXIS));     	// flow downwards

		JPanel boxbottom = new JPanel ();
		//JPanel tfPanel = new JPanel ();                                 	// panel for tf
		int fontSize = 25;
		tA = new JTextArea ("", 25-fontSize/2, 35-fontSize/2); 	// the tf without scroll    
		tA.setEditable(false);
		tA.setLineWrap(true);
		Font sideFont = new Font("Anonymous", Font.PLAIN, fontSize);  
		tA.setFont(sideFont);

		int pitch;
		int yaw;
		int roll;
		int voltage;
		tA.setText("Roll    Pitch      Yaw     Voltage");
		tAScroll = new JScrollPane(tA);                    	// adding scroll
		tAScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		JScrollBar sb = tAScroll.getVerticalScrollBar();
		sb.setValue( sb.getMaximum() );
		revalidate();
		
		JTextArea textArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		tAScroll.setSize(800, 800);

		whatyouwanted = new JTextArea("", 13-fontSize/2, 35-fontSize/2);
		whatyouwanted.setEditable(false);
		whatyouwanted.setLineWrap(true);
		whatyouwanted.setFont(sideFont);
		updateTitle(0,0,0,0);
		// 3... Add the components to the input area.

		// LEFT (text/log) AREA
		left.add(whatyouwanted, "North");
		left.add(tAScroll, "West");

		// RIGHT AREA (graphics should be empty)
		DrawArea graphicsArea = new DrawArea(dialAreaWidth,dialAreaHeight);    	// draw the graph for the first time
		right.add(graphicsArea);
		right.add(boxbottom);

		// MAIN PANEL
		JPanel mainWindow = new JPanel ();        	// Create a main pane
		mainWindow.setLayout (new FlowLayout()); 	// left to right
		mainWindow.add(left);
		mainWindow.add(right);

		// 4... Set this window's attributes.
		setContentPane (mainWindow);
		//pack ();
		setTitle ("Flight Control");
		pack();
		//setSize (1200, 800);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null);  	 
	}

	public void actionPerformed (ActionEvent e)
	{

	}

	void drawHorizontalLine(Graphics g, int origin_x, int origin_y, int radius, int degreeTilt) { // tilt is CCW from x+
		int end_x = (int) (origin_x + radius * Math.cos(Math.toRadians(degreeTilt)));
		int end_y = (int) (origin_y - radius * Math.sin(Math.toRadians(degreeTilt)));
		int start_x =   (int) (origin_x - radius * Math.cos(Math.toRadians(degreeTilt)));
		int start_y =   (int) (origin_y + radius * Math.sin(Math.toRadians(degreeTilt)));
		g.drawLine(start_x, start_y, end_x, end_y);   // flat horizontal line
		g.fillOval(origin_x - 2, origin_y - 2, 4, 4);
		drawArrowLine(g, end_x, end_y, radius/4, degreeTilt - 35);
		drawArrowLine(g, end_x, end_y, radius/4, degreeTilt + 35);
	}

	void drawArrowLine(Graphics g, int start_x, int start_y, int radius, int degreeTilt) { // tilt is CCW from x+
		int end_x =   (int) (start_x - radius * Math.cos(Math.toRadians(degreeTilt)));
		int end_y =   (int) (start_y + radius * Math.sin(Math.toRadians(degreeTilt)));
		g.drawLine(start_x, start_y, end_x, end_y);   // flat horizontal line
	}

	public String printSpaces(int spaces) {
		String returnStr = "";
		for (int x = 0 ; x < spaces; x += 1) {
			returnStr += " ";
		}
		return returnStr;
	}
	void updateTitle(int pitchAng, int rollAng, int yawAng, double volt) {
		whatyouwanted.setText("Roll     Pitch     Yaw     Voltage\n");
		whatyouwanted.append(Integer.toString(pitchAng));
		whatyouwanted.append(  printSpaces(10 - Integer.toString(pitchAng).length())  );

		whatyouwanted.append(Integer.toString(rollAng));
		whatyouwanted.append(  printSpaces(13 - Integer.toString(rollAng).length())  );
		
		whatyouwanted.append(Integer.toString(yawAng));
		whatyouwanted.append(  printSpaces(11 - Integer.toString(yawAng).length())  );

		whatyouwanted.append(Double.toString(volt));
	}
	
	void drawArrow(Graphics g, int origin_x, int origin_y, int radius, int angle, String angleName){
		int bah = (int)((radius+10) / Math.sqrt(2));
		g.setColor(Color.red);
		g.drawString(angleName, origin_x-radius-30, origin_y-radius-35);
		g.drawString(angle + "", origin_x-radius-30, origin_y-radius-5);
		if(angle/100 >= 1){
			g.drawOval(origin_x-radius+15, origin_y-radius-22, 8, 8);
		}
		else if(angle/10 >= 1){
			g.drawOval(origin_x-radius, origin_y-radius-22, 8, 8);
		}
		else{
			g.drawOval(origin_x-radius-10, origin_y-radius-22, 8, 8);
		}

		g.drawOval(origin_x-radius-10, origin_y-radius-10, 2*radius+20, 2*radius+20);

		g.drawLine(origin_x-radius-13, origin_y, origin_x-radius-3, origin_y);//first circle dashes
		g.drawLine(origin_x+radius+13, origin_y, origin_x+radius+3, origin_y);
		g.drawLine(origin_x, origin_y-radius-3, origin_x, origin_y-radius-13);
		g.drawLine(origin_x, origin_y+radius+13, origin_x, origin_y+radius+3);

		g.drawLine(origin_x+bah+2, origin_y-bah-2, origin_x+bah-4, origin_y-bah+4);
		g.drawLine(origin_x+bah+2, origin_y+bah+2, origin_x+bah-4, origin_y+bah-4);
		g.drawLine(origin_x-bah-2, origin_y-bah-2, origin_x-bah+4, origin_y-bah+4);
		g.drawLine(origin_x-bah-2, origin_y+bah+2, origin_x-bah+4, origin_y+bah-4);

		drawHorizontalLine(g, origin_x, origin_y, radius, angle);
	}

	// method to generate graph
	public void drawGraphics(Graphics g) {
		Font sideFont = new Font("American Typewriter", Font.PLAIN, 25);  
		g.setFont(sideFont);

		drawArrow(g,dialAreaWidth/5,220,7*dialAreaWidth/100,angle1,"Roll");
		drawArrow(g,dialAreaWidth/2,220,7*dialAreaWidth/100,angle2,"Pitch");
		drawArrow(g,4*dialAreaWidth/5,220,7*dialAreaWidth/100,angle3,"Yaw");
	}

	public void appendText(String appending) {
		tA.setText(tA.getText() + appending);
	}

	//subclass of JPanel with paintComponent method to display drawn items
	class DrawArea extends JPanel{
		public DrawArea (int w, int h){
			this.setPreferredSize(new Dimension(w, h)); //set preferred size
		}
		public void paintComponent (Graphics g){
			super.paintComponent(g);
			revalidate();
			drawGraphics(g);   // call draw graph method
		}
	}



	//======================================================== method main
	public static void main (String[] args)
	{
		APP_summ3 window = new APP_summ3 ();
		window.setVisible (true);
		while(true){
			for(int i = 0; i < 720; i++){
				window.angle1 = i/2;
				window.angle2 = 720-i;
				window.angle3 = i;
				
				window.revalidate();
				window.repaint();
			
				try {
					Thread.sleep(20);
				}
				catch (InterruptedException e) {}
			}
		}
	}
}





