import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class LoadDict {

	public static HashMap<String, ArrayList<String> > load(final String[] args)
	{
		String encoding = "UTF-8";
		if(args.length == 0)
			return null;
		String filename = args[0];
		FileInputStream fis;
		InputStreamReader in;
		try 
		{
			fis = new FileInputStream(filename);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		try 
		{
			in = new InputStreamReader(fis, encoding);
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		BufferedReader input = new BufferedReader(in);
		
		String line = null;
		HashMap<String, ArrayList<String> > retval = new HashMap<String, ArrayList<String> >();
		try
		{
			while((line = input.readLine()) != null)
			{
				line = line.trim();
				if(line.charAt(0) == '#')
					continue;
				String[] tokens = line.split("\t");
				if(tokens.length != 2)
				{
					System.err.println("Broken line: " + line);
					continue;
				}
				String key = tokens[1];
				String val = tokens[0];
				if(retval.containsKey(key))
					retval.get(tokens[1]).add(val);
				else
				{
					ArrayList<String> new_list = new ArrayList<String>();
					new_list.add(val);
					retval.put(key, new_list);
				}
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			return null;
		}
		
		return retval;
		
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HashMap<String, ArrayList<String> > dict = load(args);
		if(dict != null)
		{
			System.out.println("Loaded dictionary with " + dict.size() + " entries.");
			if(args.length >= 2)
			{
				String key = args[1];
				System.out.println("Looking up " + key);
				if(dict.containsKey(key))
				{
					ArrayList<String> vals = dict.get(key);
					Iterator<String> it = vals.iterator();
					while(it.hasNext())
						System.out.println(it.next());
				}
				else
				{
					System.out.println("Searching for partial matches.");
					Set<String> keys = dict.keySet();
					Iterator<String> it = keys.iterator();
					String full_key = "";
					ArrayList<String> vals = null;
					while(it.hasNext())
					{
						full_key = it.next();
						if(full_key.contains(key))
						{
							vals = dict.get(full_key);
							Iterator<String> vals_it = vals.iterator();
							while(vals_it.hasNext())
								System.out.println(full_key + " => " + vals_it.next());
						}
						
					}
					
				}
			}
		}
		else
			System.err.println("Could not load dictionary.");
		
		
	}

}
