package com.kmvrt.Unlived;

public class MagicProperties {

	private ArrayList<int> acts;
		// what the magic do
		// the action is done in the order it's stored
		// the action could be taken is predefined in Constants
	private ArrayList<float> actPeriods;
		// how long the act would be performed
		// the intervals are predefined in Constants
	
// constructor ------------------------------------------------------------------------------------------------
	public MagicProperties(ArrayList<int> acts, ArrayList<float> actPeriods) {
		
		this.acts = acts;
		this.actPeriods = actPeriods;
	}


// getters and setters ----------------------------------------------------------------------------------------

	public int getCurrentAct(float timeSpent) {
		// return the action for time specified
		
		for(int i = 0; i < acts.size(); i++)  {
			// find which act the time period belongs to
			/* keep substracting time spent by the time periods 
			 * * of the acts in the collection */
			/* if the time spent reach 0, it means the act 
			 * substracting it is the current act */
			timeSpent -= actPeriods.get(i);
			if(timeSpent <= 0) {
				return acts.get(i - 1);
			}
		}
	} // getAct(float)'s end

}
