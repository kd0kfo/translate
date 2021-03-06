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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;

public class LoadDict {

    public static HashMap<String, ArrayList<String> > load(final String[] args){return load(args,false);}

    public static HashMap<String, ArrayList<String> > load(final String[] args, boolean reverse_order)
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
				String key;
				String val;
				if(reverse_order)
				    {
					key = tokens[0].toLowerCase();
					val = tokens[1].toLowerCase();
				    }
				else
				    {
					key = tokens[1].toLowerCase();
					val = tokens[0].toLowerCase();
				    }
				
				if(retval.containsKey(key))
					retval.get(key).add(val);
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
	    
	    Options options = new Options();
	    options.addOption("r", false, "Reverse dictionary search");
	    CommandLineParser parser = new PosixParser();

	    CommandLine cmd = null;
	    try
		{
		    cmd = parser.parse(options, args);
		}
	    catch(org.apache.commons.cli.ParseException pe)
		{
		    System.err.println("There was a problem parsing command line options. Here comes the stack trace...");
		    pe.printStackTrace();
		    System.exit(1);
		}

	    boolean reverse_order = false;
	    if(cmd.hasOption("r"))
		reverse_order = true;
			
	    String[] cmd_args = cmd.getArgs();
	    HashMap<String, ArrayList<String> > dict = load(cmd_args,reverse_order);
	    
		if(dict != null)
		{
			System.out.println("Loaded dictionary with " + dict.size() + " entries.");
			if(cmd_args.length >= 2)
			{
			    String key = cmd_args[1].toLowerCase();
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
