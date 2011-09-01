import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class TDFIRC extends JFrame implements ActionListener {
	String username;
	Networking net;
	IRCinterface gui;
	FileToHash s;
	
	JTextField adduser;
	JButton adduserbutton;
	JButton removeuserbutton;
	JButton settingsbutton;
	
	public static final String configfile = "config.ini";

	public TDFIRC()
	{		
		super("TDF IRC Client");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Create the main window and its components		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(5, 5);
		setSize(300, 300);
		setIconImage(new ImageIcon("tdfirc.ico").getImage());
		setLayout(null);
		setResizable(false);
		setVisible(true);
		
		settingsbutton = new JButton("Settings");
		settingsbutton.setSize(100, 40);
		settingsbutton.setLocation(190, 228);
		add(settingsbutton);
		settingsbutton.addActionListener(this);
		
		
		adduser = new JTextField();
		adduser.setSize(150, 20);
		adduser.setLocation(5, 5);
		add(adduser);
		
		adduserbutton = new JButton("Add");
		adduserbutton.setSize(70, 30);
		adduserbutton.setLocation(5, 35);
		add(adduserbutton);
		adduserbutton.addActionListener(this);
		
		removeuserbutton = new JButton("Del");
		removeuserbutton.setSize(70, 30);
		removeuserbutton.setLocation(85, 35);
		add(removeuserbutton);
		removeuserbutton.addActionListener(this);
		
		
		// Import settings
		s = new FileToHash(configfile);
		username = s.get("username");

		// Initialise networking and gui
		net = new Networking();
		gui = new IRCinterface(s, configfile, this);
		gui.inputbox.addActionListener(this);
		gui.addUser(username);
		repaint();
		
		
	}

	public static void main(String[] args) 
	{
		new TDFIRC();
	}

	public void handleSlashCommands(String text)
	{
		// Remove the leading '/' and create a comparestring consisting of the word up until the first space or end of line
		text = text.substring(1);
		int compareend = text.indexOf(" ");
		if (compareend == -1)
		{
			compareend = text.length();
		}
		String comparestring = text.substring(0, compareend).toLowerCase();

		if (comparestring.equals("qqq") || comparestring.equals("exit") || comparestring.equals("quit"))
		{
			// Exit command
			System.exit(0);
		} else if (comparestring.equals("username"))
		{
			// Change username and save it to config
			username = text.substring(text.indexOf(" ") + 1, text.length());
			s.put("username", username);
			s.writeToFile(configfile, s);
			gui.addmsg("Your new username is: " + username);
		} else if (comparestring.equals("color"))
		{			
			// Change the color of the window and save it to config
			// Try clause to avoid ArrayOutOfBoundsExceptions if used incorrectly

			try
			{
				String[] colors = text.split(" ");
				int red = Integer.parseInt(colors[1]);
				int green = Integer.parseInt(colors[2]);
				int blue = Integer.parseInt(colors[3]);
				gui.changeColor(new Color(red, green, blue));
				s.put("red", "" + red);
				s.put("green", "" + green);
				s.put("blue", "" + blue);
				s.writeToFile(configfile, s);

			} catch (Exception e)
			{
				gui.addmsg("Usage: /color red green blue");
				gui.addmsg("Where red, green and blue are 8-bit rbg values.");
				gui.addmsg("Example: /color 0 255 120");
			}

		} else if (comparestring.equals("pos"))
		{
			// Change position of the window and save to config

			String[] pos = text.split(" ");
			String x = pos[1].toLowerCase();
			String y = pos[2].toLowerCase();
			if ((x.equals("center") || isIntNumber(x)) && (y.equals("center") || isIntNumber(y)))
			{
				gui.placeOnScreen(x, y, s);
			}
		} else if (comparestring.equals("size"))
		{
			// Change the size of the window and save to config

			String[] pos = text.split(" ");
			String x = pos[1].toLowerCase();
			String y = pos[2].toLowerCase();
			if ((x.equals("max") || isIntNumber(x)) && (y.equals("max") || isIntNumber(y)))
			{
				gui.setSize(x, y, s);
			}
		}
	}

	public boolean isIntNumber(String num){
		try{
			Integer.parseInt(num);
		} catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(gui.inputbox))
		{
			String text = gui.inputbox.getText();
			if (!text.equals(""))
			{
				if (text.substring(0,1).equals("/"))
				{
					// If a slashcommand is entered
					handleSlashCommands(text);
				} else 
				{
					// TODO this should go through the net class, echoing on screen for now
					gui.addmsg(text, username);
				}
				// Clear the inputbox
				gui.inputbox.setText("");			
			}
		} else if (e.getSource().equals(adduserbutton))
		{
			String user = adduser.getText();
			if(user != "")
			{
				gui.addUser(user);
			}
		}  else if (e.getSource().equals(removeuserbutton))
		{
			String user = adduser.getText();
			if(user != "")
			{
				gui.removeUser(user);
			}
		}  else if (e.getSource().equals(settingsbutton))
		{
			SettingsWindow sw = new SettingsWindow(s, this);
		}

	}
}