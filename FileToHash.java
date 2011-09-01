import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

@SuppressWarnings("serial")
public class FileToHash extends HashMap<String, String> {
	//HashMap<String, String> settings;
	public String configfile;
	public FileToHash (String file)
	{
		configfile = file;
		//settings = new HashMap<String, String>();
		
		try {
		    BufferedReader in = new BufferedReader(new FileReader(configfile));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	String[] splitstr = str.split("=");
		        put(splitstr[0], splitstr[1]);
		    }
		    in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String[] toArray()
	{
		String[] result = new String[1];
		result = keySet().toArray(result);
		return result;		
	}
	
	public void writeToFile(String file, HashMap<String, String> newsettings)
	{
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(file));
		    out.write("");
		    String[] keys = new String[1];
		    keys = newsettings.keySet().toArray(keys);
		    for(int i = 0; i < keys.length; i++)
		    {
		    	out.append(keys[i] + "=" + newsettings.get(keys[i]));
		    	out.newLine();
		    	put(keys[i], newsettings.get(keys[i]));
		    }
		    out.close();		    
		} catch (Exception e) {
		}
	}
}
