// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

//import com.kmvrt.Unlived.gameplay.Arena;
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
	private static ArrayList<String> keys;

// constructor -----------------------------------------------------------------------------------------------
	private MagicFactory() {
		// make it a static class
	}

	public static void init() {
		// initialise the spell book
		
		spellBook = new HashMap<String, Spell>();
		keys = new ArrayList<String>();
		
		// constants for reading file ------------------
		// props
		final String ARG_ATT_BURST = "burst";
		final String ARG_ATT_BURST_INTERVAL = "burstinterval";
		final String ARG_ATT_INTERVAL = "interval";
		final String ARG_ATT_WIDTH = "width";
		final String ARG_ATT_HEIGHT = "height";
		final String ARG_ATT_SPEED = "speed";
		final String ARG_ATT_TRAVEL_DIST = "traveldist";
		
		// atts
		final String ARG_ATT_MANA = "mana";
		final String ARG_ATT_ACCEL = "accel";
		final String ARG_ATT_FORCE = "force";

		// attribute IDs
		final int ATT_TRAVEL_DIST = 198;
		final int ATT_SPEED = 199;
		final int ATT_HEIGHT = 200;
		final int ATT_WIDTH = 201;
		final int ATT_BURST = 202;
		final int ATT_BURST_INTERVAL = 203;
		final int ATT_INTERVAL = 204;
		final int ATT_MANA = 205;
		final int ATT_ACCEL = 206;
		final int ATT_FORCE = 207;
	
		// opening and closing a statement
		final String ARG_INIT_MAGIC = "spell";
		final String ARG_INIT_MAGIC_END = "spellend";
		final String ARG_INIT_HIT = "hit";
		final String ARG_INIT_HIT_END = "hitend";
		final String ARG_INIT_CAST = "cast";
		final String ARG_INIT_CAST_END = "castend";

//		if(!initialised) {
			initialised = true;

			// read and use the data from the magic files **********
			/* every spell has an extension ".spell" 
			 * and is stored in "internal/magic/spells/" */
			FileHandle[] files = openSpellFiles();
			BufferedReader reader;
				// a (reader to a) file
			for(FileHandle file : files) {
				// read every ".spell" files
				
				// flags
				boolean initMagic = false;
				boolean initHit = false;
				boolean initCast = false;
				boolean attStated = false;
				int attStatedID = 0;
				
				// the spell initialised and its attributes
				Spell nSpell = null;
				GameChar.Attributes hit = null;
				GameChar.Attributes cast = null;
				int burst = 1;
				float burstInterval = 0;
				float interval = 0.5f;
				float width = 1; float height = 1;
				float speed = 50;
				float travelDist = 60;
				
				reader = new BufferedReader(
						new InputStreamReader(file.read()));
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
								// looking for attribute/closing statement
								if(!attStated) {
									// declare an attribute
									if(arg.equals(ARG_ATT_MANA)) {
										attStatedID = ATT_MANA;
										attStated = true;

									} else if(arg.equals(ARG_ATT_ACCEL)) {
										attStatedID = ATT_ACCEL;
										attStated = true;

									} else if(arg.equals(ARG_ATT_FORCE)) {
										attStatedID = ATT_FORCE;
										attStated = true;
										
									// close hit/cast init
									} else if(arg.equals(ARG_INIT_HIT_END)
											&& !initCast) {
										initHit = false;
										
									} else if(arg.equals(ARG_INIT_CAST_END)
											&& !initHit) {
										initCast = false;
									} 
								// if attStated == false's end

								// initialise the attribute
								} else if(isNumeric(arg)) {
									attStated = false;
									GameChar.Attributes cCond = initHit ? hit : cast;
									switch(attStatedID) {
									case ATT_MANA:
										cCond.applyMana(Float.parseFloat(arg));
										Gdx.app.debug(TAG, "\t" + arg + " mana applied");
										break;

									case ATT_ACCEL:
										cCond.applyAccel(Float.parseFloat(arg) 
												* Constants.ins.UNIT_CONV);
										Gdx.app.debug(TAG, "\t" + arg + " accel applied");
										
										break;

									case ATT_FORCE:
										cCond.applyForce(Float.parseFloat(arg)
												* Constants.ins.UNIT_CONV);
										Gdx.app.debug(TAG, "\t" + arg + " force applied");
										break;

									default:
										Gdx.app.error(TAG, 
												"Invalid attribute ID declared in the file "
												+ file.path() + ": " + attStatedID);
										Gdx.app.exit();
									}
								// if(arg is numeric)'s
								
								} else {
									Gdx.app.error(TAG, "Invalid argument in the file " 
										+ file.path() + ": " + arg);
									Gdx.app.exit();
								}
							// if init hit/cast's
							
							// open hit/init initialisation
							} else if(arg.equals(ARG_INIT_HIT)
									&& !initHit && !initCast) {
								initHit = true;
								
							} else if(arg.equals(ARG_INIT_CAST)
									&& !initHit && !initCast) {
								initCast = true;

							} else if(!attStated) {
								// initialise props
								if(arg.equals(ARG_ATT_BURST)) {
									attStatedID = ATT_BURST;
									attStated = true;

								} else if(arg.equals(ARG_ATT_BURST_INTERVAL)) {
									attStatedID = ATT_BURST_INTERVAL;
									attStated = true;
									
								} else if(arg.equals(ARG_ATT_INTERVAL)) {
									attStatedID = ATT_INTERVAL;
									attStated = true;
									
								} else if(arg.equals(ARG_ATT_WIDTH)) {
									attStatedID = ATT_WIDTH;
									attStated = true;
									
								} else if(arg.equals(ARG_ATT_HEIGHT)) {
									attStatedID = ATT_HEIGHT;
									attStated = true;		
								
								} else if(arg.equals(ARG_ATT_SPEED)) {
									attStatedID = ATT_SPEED;
									attStated = true;
								
								} else if(arg.equals(ARG_ATT_TRAVEL_DIST)) {
									attStatedID = ATT_TRAVEL_DIST;
									attStated = true;
									
								} else if(arg.equals(ARG_INIT_MAGIC_END)) {
									// end initialisation
									initMagic = false;
									nSpell.initAtts(hit, cast);
									nSpell.initProps(speed, travelDist, 
											burst, burstInterval, 
											interval, width, height);
										// initialise a spell based on the file reading result
									spellBook.put(file.nameWithoutExtension(), nSpell);
										// and save it
									keys.add(file.nameWithoutExtension());
									Gdx.app.debug(TAG, "Spell added: " + file.nameWithoutExtension());
								}
							// if !attStated's

							} else if(isNumeric(arg)) {
								// init properties
								attStated = false;

								switch(attStatedID) {
								case ATT_BURST:
									burst = Integer.parseInt(arg);
									Gdx.app.debug(TAG, "\t" 
											+ Integer.parseInt(arg) + " shots/cast");
									break;

								case ATT_BURST_INTERVAL:
									burstInterval = Float.parseFloat(arg);
									Gdx.app.debug(TAG, 
											"\t" + Float.parseFloat(arg) + "s between each shots");
									break;
									
								case ATT_INTERVAL:
									interval = Float.parseFloat(arg);
									Gdx.app.debug(TAG, 
											"\t" + Float.parseFloat(arg) + " s between each cast");
									break;
									
								case ATT_WIDTH:
									width = Float.parseFloat(arg) * Constants.ins.UNIT_CONV;
									Gdx.app.debug(TAG, 
											"\twidth = " + Float.parseFloat(arg));
									break;
									
								case ATT_HEIGHT:
									height = Float.parseFloat(arg) * Constants.ins.UNIT_CONV;
									Gdx.app.debug(TAG, 
											"\theight = " + Float.parseFloat(arg));
									break;
									
								case ATT_SPEED:
									speed = Float.parseFloat(arg) * Constants.ins.UNIT_CONV;
									Gdx.app.debug(TAG, 
											"\tspeed = " + Float.parseFloat(arg));
									break;
									
								case ATT_TRAVEL_DIST:
									travelDist = Float.parseFloat(arg) * Constants.ins.UNIT_CONV;
									Gdx.app.debug(TAG, 
											"\ttravel distance = " + Float.parseFloat(arg));
									break;
									
								default:
									Gdx.app.error(TAG, 
											"Invalid attribute ID declared in file "
											+ file.path() + ": " + attStatedID);
									Gdx.app.exit();
								}		
							} 
						// if init magic's

						} else if(arg.equals(ARG_INIT_MAGIC)) {
							// start initialisation
							Gdx.app.debug(TAG, "Initialising spell: " + file.nameWithoutExtension());
							initMagic = true;
							nSpell = new Spell(file.nameWithoutExtension());
							hit = new GameChar.Attributes();
							cast = new GameChar.Attributes();
								
						} else {
							Gdx.app.error(TAG, "Invalid argument in the file " 
									+ file.path() + ": " + arg);
							Gdx.app.exit();
						}
					}	// argument iterator's end
				}	// line iterator's end

				try {
					reader.close();
				} catch(IOException e) {
					Gdx.app.error(TAG, "Failed to close file");
					Gdx.app.exit();
				}
				if(initMagic) {
					// if magic initalisation hasn't been ended
					Gdx.app.error(TAG, "Unfinished chain of arguments in the file: "
							+ file.path());
					Gdx.app.exit();
				}

			}	// file iterator's end
		// if initialised == false 's end

/*		} else {		// already been initialised
			Gdx.app.error(TAG, "MagicFactory is initialised more than once");	
			Gdx.app.exit();
		}*/
	
	}	// init()'s end

	private static FileHandle[] openSpellFiles() {
		// return the files for the spells
		// all the spell files are stored in "internal/magic/data/"
		// and all have the extension .spell
		
		ArrayList<String> spellList = new ArrayList<String>();
		BufferedReader r = new BufferedReader(
				Gdx.files.internal("res/magic/data/spells").reader());
		String line;
		while((line = readNextLine(r)) != null) {
			spellList.add(line);
		}

		ArrayList<FileHandle> files = new ArrayList<FileHandle>();
		for(String spellName : spellList) {
			files.add(Gdx.files.internal(
					"res/magic/data/" + spellName + ".spell"));
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
			try {
				reader.close();
			} catch(IOException ex) {
				Gdx.app.error(TAG, "Failed to close file");
				Gdx.app.exit();
			}
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
	public static Magic cast(String spellName, float x, float y, int dir, GameChar src) {
		// return the magic with properties of the specified spell
	
		if(!initialised) {
			Gdx.app.error(TAG, "Tried to access unitialised magic factory");
			Gdx.app.exit();
		}
	
		// initialise a magic based on the properties
		return new Magic(spellBook.get(spellName), x, y, dir, src);

	}	// cast(String)'s end
	
	public static Spell getSpell(String name) {

		return spellBook.get(name);
	}

	public static String getSpellName(int index) {
		
		return getSpellNames().get(index);
	}

	public static ArrayList<String> getSpellNames() {
		
		return keys;
	}
	
	public static int totalSpells() {
		
		return spellBook.size();
	}

}
