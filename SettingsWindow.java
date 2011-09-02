import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



@SuppressWarnings("serial")
public class SettingsWindow extends JFrame implements ActionListener  {

	JButton save;
	JButton close;
	
	FileToHash settings;
	TDFIRC parentframe;
	
	Vector<JTextField> fields = new Vector<JTextField>();
	Vector<JLabel> labels = new Vector<JLabel>();

	public SettingsWindow(FileToHash s, TDFIRC parent)
	{
		super("Settings");
		settings = s;
		parentframe = parent;
		setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		String[] sa = s.toArray();
		Font f = new Font("Consolas", Font.PLAIN, 15);
		
		for(int i = 0; i < sa.length; i++)
		{
			fields.add(new JTextField(s.get(sa[i])));
			fields.get(i).setSize(140, 22);
			fields.get(i).setLocation(150, (5 + i*27));
			fields.get(i).setFont(f);
			add(fields.get(i));
			
			labels.add(new JLabel(sa[i]));
			labels.get(i).setSize(130, 22);
			labels.get(i).setLocation(10, (5 + i*27));
			labels.get(i).setFont(f);
			labels.get(i).setHorizontalAlignment(SwingConstants.RIGHT);
			add(labels.get(i));
		}
		
		setSize(300, sa.length * 27 + 87);
		
		save = new JButton("Save");
		save.setSize(140, 50);
		save.setLocation(10, getHeight() - 82);
		add(save);
		save.addActionListener(this);
		
		close = new JButton("Cancel");
		close.setSize(140, 50);
		close.setLocation(150, getHeight() - 82);
		add(close);
		close.addActionListener(this);
		
		setLocation((dim.width-getWidth())/2, (dim.height-getHeight())/2);
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(save))
		{
			for(int i = 0; i < fields.size(); i++)
			{
				settings.put(labels.get(i).getText(), fields.get(i).getText());
				settings.writeToFile(settings.configfile, settings);

			}
		} else if (arg0.getSource().equals(close))
		{
			parentframe.sw = null;
			this.setVisible(false);
		}
		
	}
}
