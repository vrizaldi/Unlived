package com.kmvrt.Unlived;

import com.badlogic.gdx.utils.Timer;
import java.util.Iterator;
import java.util.ArrayList;

public class WorldTimer {
	// timer that you can slow/fasten (e.g. for slow mo)

	private ArrayList<Task> tasks;
	private static WorldTimer ins = new WorldTimer();
	
// constructor ----------------------------------------------------------------------------------------------
	public WorldTimer() {
	
		tasks = new ArrayList<Task>();
	}



// schedule task --------------------------------------------------------------------------------------------
	public static void update(float delta) {
	
		// decrease all the cDelay
		// if it reaches 0, run the task
		for(Iterator<Task> iter = ins.tasks.iterator(); iter.hasNext();) {
			Task t = iter.next();

			t.cTimer -= delta;
			if(t.cTimer <= 0) {
				t.task.run();
				if(t.repeating) {
					t.cTimer += t.getInterval();
				} else if(t.ct > 0) {
					t.cTimer += t.getInterval();
					t.ct--;
				} else {
					iter.remove();
				}
			}
		}	// task iter's
	}	// update()'s

	public static void schedule(Timer.Task task, float interval, 
			boolean repeating) {

		ins.tasks.add(new Task(task, interval, repeating, 0));
	}
	
	public static void schedule(Timer.Task task, float interval, 
			int ct) {
		ins.tasks.add(new Task(task, interval, false, ct));
	}
	
	public static void schedule(Timer.Task task, float interval) {
		ins.tasks.add(new Task(task, interval, false, 0));
	}
	
	public static void clear() {
	
		ins.tasks.clear();
	}



// inner class -------------------------------------------------------------------------------------------------
	static class Task {
			
		public Timer.Task task;
		public float cTimer;
		private float interval;
		private int ct;
		private boolean repeating;

		public Task(Timer.Task task, float interval, boolean repeating, int ct) {

			this.task = task;
			this.interval = interval;
			this.cTimer = interval;
			this.ct = ct;
			this.repeating = repeating;
		}

		public float getInterval() {
			return interval;
		}

		public boolean isRepeating() {
			return repeating;
		}
	}	// inner class'

}	// public class'
