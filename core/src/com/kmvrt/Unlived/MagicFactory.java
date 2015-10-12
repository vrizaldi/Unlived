package com.kmvrt.Unlived;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;

public class MagicFactory {
	// initialise, store and return a Magic 
	
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
			FileHandle[] files = Gdx.files.local("magic/spells/").list(".spell");
			BufferedReader reader;
				// a (reader to a) file
			for(FileHandle file : files) {
				// read every ".spell" files

				ArrayList<int> atts;
					// the acts
				ArrayList<float> attSetters;
					// the actPeriods
				boolean initMagic = false;
				boolean attStated = false;

				reader = new BufferedReader(new InputStreamReader(reader.read()));
				while((String line = file.readLine().trim()) != null) {
					// keep looping until it reach the end of the file

					if(line.isEmpty()) {
						// if the line is empty, 
						// move to the next line
						continue;
					}

					String[] args = line.split("\\s");
					for(String arg : args) {
						 if(initMagic) {
							// initialise a magic
							if(!actStated) {
								if(arg.equals(Constants.ARG_ATT_MANA)) {
									atts.add(Constants.ATT_MANA);
									attStated = true;

								} else if(arg.equals(Constants.ARG_ATT_ACCEL)) {
									atts.add(Constants.ATT_ACCEL);
									attStated = true;

								} else if(arg.equals(Constants.ARG_ATT_FORCE)) {
									atts.add(Constants.ATT_FORCE);
									attStated = true;
								}
							// if actStated == false's end

							} else if(isNumeric(arg) && actStated) {
								attSetters.add(Float.parseFloat(arg));
								attStated = false;
							}
						// if init magic's end

						}	else if(arg.equals(Constants.ARG_MAGIC_INIT)
								&& !initMagic) {
							// start initialisation
							initMagic = true;
							atts = new ArrayList<int>();
							attSetters = new ArrayList<float>();

						} else if(arg.equals(Constants.ARG_MAGIC_INIT_END)
								&& initMagic) {
							// end initialisation
							initMagic = false;
							Spell nSpell = new Spell(atts, attSetters);
								// initalise a spell based on the file reading result
							spellBook.put(file.nameWithoutExtension(), nSpell);
								// and save it
								
						} else {
							Gdx.app.error(TAG, "Invalid argument in the file: " 
									+ file.path());
							Gdx.app.exit();
						}
					}	// argument iterator's end
				}	// line iterator's end

				if(initMagic) {
					// if magic initalisation hasn't been ended
					Gdx.app.error(TAG, "Unfinished chain of arguments in the file: "
							+ file.path());
				}
			}	// file iterator's end
		// if initalised == false 's end

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
	public static Magic cast(String spellName, float x, float y, int dir) {
		// return the magic with properties of the specified spell
	
		if(!initialized) {
			Gdx.app.error(TAG, "Spell book haven't been initialized yet");
			Gdx.app.exit(0);
		}
	
		// initialise a magic based on the properties
		Magic m = new Magic(spellBook.get(spellName), x, y, dir);

	}	// cast(String)'s end

}
