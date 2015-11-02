// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class MagicFactory {
	// initialise, store and return a Magic 
	
	private static String TAG = MagicFactory.class.getName();
	
	private static boolean initialised = false;

	private static HashMap<String, Spell> spellBook;
		// store the spells

// constructor -----------------------------------------------------------------------------------------------
	private MagicFactory() {
		// make it a static class
	}

	public static void init() {
		// initialise the spell book
		
		spellBook = new HashMap<String, Spell>();
		
		// constants for reading file
		final String ARG_ATT_MANA = "mana";
		final String ARG_ATT_ACCEL = "accel";
		final String ARG_ATT_FORCE = "force";
		final String ARG_INIT_MAGIC = "spell";
		final String ARG_INIT_MAGIC_END = "spellend";
		final String ARG_INIT_HIT = "hit";
		final String ARG_INIT_HIT_END = "hitend";
		final String ARG_INIT_CAST = "cast";
		final String ARG_INIT_CAST_END = "castend";
		
		// attribute IDs
		final int ATT_MANA = 205;
		final int ATT_ACCEL = 206;
		final int ATT_FORCE = 207;
	
		if(!initialised) {
			initialised = true;

			// read and use the data from the magic files **********
			/* every spell has an extension ".spell" 
			 * and is stored in "internal/magic/spells/" */
			FileHandle[] files = openSpellFiles();
			BufferedReader reader;
				// a (reader to a) file
			for(FileHandle file : files) {
				// read every ".spell" files
				GameChar.Attributes hit = null;
				GameChar.Attributes cast = null;
				boolean initMagic = false;
				boolean initHit = false;
				boolean initCast = false;
				boolean attStated = false;
				int attStatedID = 0;
				Spell nSpell = null;

				reader = new BufferedReader(new InputStreamReader(file.read()));
				String line;
				while((line = readNextLine(reader)) != null) {
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
							if(initHit ^ initCast) {
								if(!attStated) {
									if(arg.equals(ARG_ATT_MANA)) {
										attStatedID = ATT_MANA;
										attStated = true;

									} else if(arg.equals(ARG_ATT_ACCEL)) {
										attStatedID = ATT_ACCEL;
										attStated = true;

									} else if(arg.equals(ARG_ATT_FORCE)) {
										attStatedID = ATT_FORCE;
										attStated = true;
										
									// finish init hit/cast
									} else if(arg.equals(ARG_INIT_HIT_END)
											&& !initCast) {
										initHit = false;
										
									} else if(arg.equals(ARG_INIT_CAST_END)
											&& !initHit) {
										initCast = false;
									} 
								// if actStated == false's end

								} else if(isNumeric(arg) && attStated) {
									attStated = false;
									GameChar.Attributes cCond = initHit ? hit : cast;
									switch(attStatedID) {
									case ATT_MANA:
										cCond.applyMana(Float.parseFloat(arg));
										Gdx.app.log(TAG, arg + " mana applied");
										break;

									case ATT_ACCEL:
										cCond.applyAccel(Float.parseFloat(arg));
										Gdx.app.log(TAG, arg + " accel applied");
										break;

									case ATT_FORCE:
										cCond.applyForce(Float.parseFloat(arg));
										Gdx.app.log(TAG, arg + " force applied");
										break;
									}
								// if(arg is numeric)'s
								
								} else {
									Gdx.app.error(TAG, "Invalid argument in the file " 
										+ file.path() + ": " + arg);
									Gdx.app.exit();
								}
							// if init hit/cast's
							
							} else if(arg.equals(ARG_INIT_HIT)
									&& !initHit && !initCast) {
								initHit = true;
								
							} else if(arg.equals(ARG_INIT_CAST)
									&& !initHit && !initCast) {
								initCast = true;
							
							} else if(arg.equals(ARG_INIT_MAGIC_END)) {
								// end initialisation
								initMagic = false;
								nSpell.initAtts(hit, cast);
									// initialise a spell based on the file reading result
								spellBook.put(file.nameWithoutExtension(), nSpell);
									// and save it
								Gdx.app.log(TAG, "Spell added: " + file.nameWithoutExtension());
							}
						// if init magic's

						} else if(arg.equals(ARG_INIT_MAGIC)) {
							// start initialisation
							Gdx.app.log(TAG, "Initialising spell: " + file.nameWithoutExtension());
							initMagic = true;
							nSpell = new Spell();
							hit = new GameChar.Attributes();
							cast = new GameChar.Attributes();
								
						} else {
							Gdx.app.error(TAG, "Invalid argument in the file " 
									+ file.path() + ": " + arg);
							Gdx.app.exit();
						}
					}	// argument iterator's end
				}	// line iterator's end

				if(initMagic) {
					// if magic initalisation hasn't been ended
					Gdx.app.error(TAG, "Unfinished chain of arguments in the file: "
							+ file.path());
					Gdx.app.exit();
				}

			}	// file iterator's end
		// if initialised == false 's end

		} else {		// already been initialised
			Gdx.app.error(TAG, "MagicFactory is initialised more than once");	
			Gdx.app.exit();
		}
	
	}	// init()'s end

	private static FileHandle[] openSpellFiles() {
		// return the files for the spells
		// all the spell files are stored in "internal/magic/data/"
		// and all have the extension .spell
		
		ArrayList<FileHandle> files = new ArrayList<FileHandle>();
		String[] spellList = {"Attack"};
		for(String spellName : spellList) {
			files.add(Gdx.files.internal(
					"magic/data/" + spellName + ".spell"));
		}

		FileHandle[] spellFiles = new FileHandle[files.size()];
		spellFiles = files.toArray(spellFiles);
		return spellFiles;
	}

	private static String readNextLine(BufferedReader reader) {
		// return the next line
		
		try {
			String line = reader.readLine();
			return line;
		} catch(IOException e) {
			Gdx.app.error(TAG, "Failed to read the file", e);
			Gdx.app.exit();
			return null;
		}
	}
	
	private static boolean isNumeric(String str)	{
		// return whether the string is numeric

		return str.matches("-?\\d+(\\.\\d+)?"); 
			// match a number with optional '-' and decimal.
	}


// magic casting --------------------------------------------------------------------------------------------
	public static Magic cast(String spellName, float x, float y, int dir) {
		// return the magic with properties of the specified spell
	
		if(!initialised) {
			Gdx.app.error(TAG, "Spell book haven't been initialized yet");
			Gdx.app.exit();
		}
	
		// initialise a magic based on the properties
		return new Magic(spellBook.get(spellName), x, y, dir);

	}	// cast(String)'s end

}
