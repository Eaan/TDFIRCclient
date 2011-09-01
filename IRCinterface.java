import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
public class IRCinterface implements MouseListener, MouseMotionListener{
	JTextArea console;
	JTextField inputbox;
	JWindow panel;
	JScrollPane sp;
	String name;
	JPanel dragbutton;
	JScrollPane sp2;
	JTextArea userlist;
	int lastx = 0;
	int lasty = 0;
	Vector<String> users = new Vector<String>();

	public IRCinterface(FileToHash s, String configfile, JFrame parent)  {
		// Create dummy parent window

		// Import settings
		name = s.get("username");
		int width = Integer.parseInt(s.get("width"));
		int height = Integer.parseInt(s.get("height"));
		int xpos =  Integer.parseInt(s.get("xpos"));
		int ypos = Integer.parseInt(s.get("ypos"));

		Integer red = Integer.parseInt(s.get("red"));
		Integer green = Integer.parseInt(s.get("green"));
		Integer blue = Integer.parseInt(s.get("blue"));

		Color color;

		if (red != null || green != null || blue != null)
		{
			color = new Color(red, green, blue);

		} else {	
			// If there was no color data in the config file, create it
			color = new Color(0,255,0);
			s.put("red", "0");
			s.put("green", "255");
			s.put("blue", "0");
			s.writeToFile(configfile, s);
		}		

		// Create the JWindow which holds our components
		panel = new JWindow(parent);
		panel.setLayout(null);
		if(width == 0)
		{
			width = 800;
		}
		if(height == 0)
		{
			height = 600;
		}
		panel.setSize(width,height);
		panel.setLocation(xpos,ypos);

		// Initialize the font used
		Font f = new Font("Consolas", Font.PLAIN, 15);

		// Create the scroll pane to make our console scrollable
		sp = new JScrollPane();
		sp.setSize(width-150, height - 22);
		sp.setLocation(0,0);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.getVerticalScrollBar().setPreferredSize (new Dimension(0,0));
		sp.getVerticalScrollBar().setForeground(color);
		sp.getVerticalScrollBar().setBackground(Color.black);
		sp.setBorder(BorderFactory.createLineBorder(color, 5));
		panel.getContentPane().add(sp);

		// Creates the JTextArea which will serve as our main console
		console = new JTextArea();
		console.setSize(width - 150 , height - 22);		
		console.setLocation(5,5);
		console.setText("> Welcome " + name + ", to the TDF IRC client!");
		console.setForeground(color);
		console.setBackground(Color.black);
		console.setFont(f);
		console.setEditable(false);
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		sp.getViewport().add(console);

		// Creates the JTextField where input will be put in
		inputbox = new JTextField();
		inputbox.setSize(width - 45 ,27);
		inputbox.setLocation(0, height - 27);
		inputbox.setForeground(color);
		inputbox.setBackground(Color.black);
		inputbox.setFont(f);
		inputbox.setBorder(BorderFactory.createLineBorder(color, 5));
		panel.getContentPane().add(inputbox);

		dragbutton = new JPanel();
		dragbutton.setSize(50, 27);
		dragbutton.setLocation(width-50, height - 27);
		dragbutton.setForeground(color);
		dragbutton.setBackground(Color.black);
		dragbutton.setBorder(BorderFactory.createLineBorder(color, 5));
		panel.getContentPane().add(dragbutton);
		dragbutton.addMouseListener(this);
		dragbutton.addMouseMotionListener(this);

		sp2 = new JScrollPane();
		sp2.setSize(155, height - 22);
		sp2.setLocation(width - 155, 0);
		sp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp2.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
		sp2.setBorder(BorderFactory.createLineBorder(color, 5));		
		panel.getContentPane().add(sp2);

		userlist = new JTextArea();
		userlist.setSize(width - 26 , height - 32);		
		userlist.setLocation(0,0);
		userlist.setForeground(color);
		userlist.setBackground(Color.black);
		userlist.setFont(f);
		userlist.setEditable(false);
		userlist.setLineWrap(true);
		userlist.setWrapStyleWord(true);
		sp2.getViewport().add(userlist);

		panel.setVisible(true);
		inputbox.requestFocus();		
	}

	public void changeColor(Color color)
	{
		// Set the color of all components
		sp.getVerticalScrollBar().setForeground(color);
		sp.setBorder(BorderFactory.createLineBorder(color, 5));
		sp2.getVerticalScrollBar().setForeground(color);
		sp2.setBorder(BorderFactory.createLineBorder(color, 5));
		console.setForeground(color);
		inputbox.setForeground(color);
		inputbox.setBorder(BorderFactory.createLineBorder(color, 5));
		dragbutton.setBorder(BorderFactory.createLineBorder(color, 5));
	}

	public void addmsg(String msg, String sender)
	{
		// Chat messages
		console.append("\n" + "> " + sender + ": " + msg);
	}
	public void addmsg(String msg)
	{
		// Mostly system messages
		console.append("\n" + "> " + msg);
	}
	
	public void addUser(String user)
	{
		users.add(user);
		updateUserList();
		
	}
	public void removeUser(String user)
	{
		if(users.contains(user))
		{
			users.remove(user);
			updateUserList();
		}
	}
	
	public void updateUserList()
	{
		String[] sortedusers = new String[1];
		sortedusers = users.toArray(sortedusers);
		java.util.Arrays.sort(sortedusers, String.CASE_INSENSITIVE_ORDER);
		String sorteduserlist = "";
		for(int i = 0; i < sortedusers.length; i++)
		{
			sorteduserlist += sortedusers[i] + "\n";
		}
		userlist.setText(sorteduserlist);
	}
	
	public void placeOnScreen(String x, String y, FileToHash s) {
		// get screen size and set the variables
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int xint = 0;
		int yint = 0;
		if (x.equals("center"))
		{
			xint = (dim.width-panel.getWidth())/2;
		} else
		{
			xint = Integer.parseInt(x);
		}

		if (y.equals("center"))
		{
			yint = (dim.height-panel.getHeight())/2;
		} else 
		{
			yint = Integer.parseInt(y);
		}
		panel.setLocation(xint, yint);

		// Save to config
		s.put("xpos", "" + xint);
		s.put("ypos", "" + yint);
		s.writeToFile(s.configfile, s);
	}

	public void setSize(String x, String y, FileToHash s) {
		// Handle special keywords
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		if(x.equals("max"))
		{
			x = "" + dim.width;
		} else if (x.equals("default"))
		{
			x = "" + 800;
		}
		if(y.equals("max"))
		{
			y = "" + dim.height;
		} else if (y.equals("default"))
		{
			y = "" + 600;
		}		

		int width = Integer.parseInt(x);
		int height = Integer.parseInt(y);

		// Set minimum width/height
		if (width < 300)
		{
			width = 300;
		}
		if (height < 300)
		{
			height = 300;
		}

		// Set sizes of everything
		panel.setSize(width,height);
		sp.setSize(width - 150, height - 22);
		console.setSize(width - 150 , height - 22);
		sp2.setSize(155, height - 22);
		userlist.setSize(155, height- 22);
		inputbox.setSize(width - 45 ,27);
		inputbox.setLocation(0, height - 27);
		dragbutton.setLocation(width - 50, height - 27);
		sp2.setLocation(width-155, 0);

		// Save to config
		s.put("width", "" + width);
		s.put("height", "" + height);
		s.writeToFile(s.configfile, s);

		// Reset position on screen
		placeOnScreen("center", "center", s);		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x= 0, y = 0;

		x = panel.getX() + e.getX() - lastx;
		y = panel.getY() + e.getY() - lasty;

		panel.setLocation(x, y);
		panel.repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {	

		if(lastx == 0 && e.getSource() == dragbutton)
		{
			lastx = e.getX();
		}
		if(lasty == 0 && e.getSource() == dragbutton)
		{
			lasty = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		lastx = 0;
		lasty = 0;
		inputbox.requestFocus();
	}
}