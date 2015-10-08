package com.kmvrt.Unlived;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;

public class MagicFactory {
	// create and store spells
	
	private static String TAG = MagicFactory.class.getName();
	
	private static boolean initialized = false;

	private static HashMap<String, Spell> spellBook;
		// store the spells

// constructor -----------------------------------------------------------------------------------------------
	private MagicFactory() {
		// make it a static class
	}

	public static void init() {
		// initialise the spell book
	
		if(!initialised) {
			initialised = true;

			// read and use the data from the magic files **********
			/* every spell has an extension ".spell" 
			 * and is stored in "local/magic/spells/" */
			FileHandle[] spells = Gdx.files.local("magic/spells/").list(".spell");
			BufferedReader file;
				// a (reader to a) file
			for(FileHandle spell : spells) {
				// read every ".spell" files

				ArrayList<int> acts = new ArrayList<int>();
					// the acts
				ArrayList<int> actPeriods = new ArrayList<int>();
					// the actPeriods

				file = new BufferedReader(new InputStreamReader(spell.read()));
				while((String line = file.readLine().trim()) != null) {
					// keep looping until it reach the end of the file

					if(line.isEmpty()) {
						// if the line is empty, 
						// move to the next line
						continue;
					}
					String[] args = line.split("\\s");
					boolean initMagic = false;
					boolean actStated = false;

					for(String arg : args) {
						if(arg.equals(Constants.ARG_MAGIC_INIT)
								&& !initMagic) {
							initMagic = true;

						} else if(arg.equals(Constants.ARG_MAGIC_INITEND)
								&& initMagic) {
							initMagic = false;
						
						} else if(initMagic) {
							// act initialisation
							if(arg.equals(Constants.ARG_ACT_FORWARD)
									&& !actStated) {
								acts.add(Constants.ACT_FORWARD);
								actStated = true;

							} else if(arg.equals(Constants.ARG_ACT_FORWARD_X_N)
									&& !actStated) {
								acts.add(Constants.ACT_FORWARD_X_N);
								actStated = true;

							} else if(arg.equals(Constants.ARG_ACT_FORWARD_X_S)
									&& !actStated) {
								acts.add(Constants.ACT_FORWARD_X_S);
								actStated = true;
							
							// act period initialisation
							} else if(isNumeric(arg) && actStated) {
								actPeriods.add(Float.parseFloat(arg));
								actStated = false;

							// etc.
							} else {
								Gdx.app.error(TAG, "Invalid argument in the file: " + spell.path());
								Gdx.app.exit();
							}
						}	// if init magic's end
					}	// arg iterator's end
				}	// line iterator's end
			}	// file iterator's end

		} else {		// already been initialised
			Gdx.app.error(TAG, "MagicFactory is initialised more than once");	
			Gdx.app.exit();
		}
	
	}	// init()'s end

	public static boolean isNumeric(String str)	{
		// return whether the string is numeric

		return str.matches("-?\\d+(\\.\\d+)?"); 
			// match a number with optional '-' and decimal.
	}


// magic casting --------------------------------------------------------------------------------------------
	public static Magic[] cast(String spellName) {
		// return the magic with properties of the specified spell
	
		if(!initialized) {
			Gdx.app.error(TAG, "Spell book haven't been initialized yet");
			Gdx.app.exit(0);
		}
	
		// initialise the magics based on the properties
		for(MagicProperties prop : spellBook.get(name).)
			return new Magic(prop);
		}

	}	// cast(String)'s end

}
