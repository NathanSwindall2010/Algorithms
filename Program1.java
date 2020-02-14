/*
 * Name: <Nathan Swindall>
 * EID: <nts279>
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
	
	/*
	 * I need to check that there are the correct number of slots too
	 */
	
    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    @Override
    public boolean isStableMatching(Matching marriage) {
    	
    	ArrayList<Integer> employee_matching = marriage.getEmployeeMatching();
    	Iterator<Integer> emIt = employee_matching.iterator();
    	Integer employee_number = 0;
    	
    	//boolean isStable = true;
    	boolean isStable = Utilities.slotsTheSame(marriage);
    	
    	
    	while(emIt.hasNext())
    	{
    		Integer CurrentPerson = employee_number;
    		Integer CurrentPerson_location = emIt.next(); //location
    		ArrayList<Integer> currentPerson_preference_List = marriage.getEmployeePreference().get(employee_number); // get preference list for person
    		employee_number++; // for the next person
    		
    		// Now let's get every location on that list
    		Iterator<Integer> cp_prefIt = currentPerson_preference_List.iterator();
    		while(cp_prefIt.hasNext())
    		{
    			Integer currentHighLocation  = cp_prefIt.next(); // get the first location
    			if(currentPerson_preference_List.indexOf(currentHighLocation) < currentPerson_preference_List.indexOf(CurrentPerson_location) ) // Need to figure out why I didn't get that location
    			{
    				ArrayList<Integer> location_preference_list = marriage.getLocationPreference().get(currentHighLocation); // get this location's preference list
    				ArrayList<Integer> workers_At_Location = Utilities.getPeopleAtStore(currentHighLocation,employee_matching); // just iterates over the employee_matching and gets everyone at a specific store
    				Iterator<Integer> wALIt = workers_At_Location.iterator(); // make an iterator for this list
    				
    				// Check if place has same number of slots

    				// We want to go through this list and make sure that everyone is higher up on the location's preference list
    				while(wALIt.hasNext())
    				{
    					Integer next = wALIt.next(); // get the first person at this location
    					if(location_preference_list.indexOf(next) > location_preference_list.indexOf(CurrentPerson)) // if this person is at a higher index thatn the other current person. Bad stable marriage
    					{
    						System.out.println("Current high location: " + currentHighLocation);
    						System.out.println("Current Person Location: " + CurrentPerson_location);
    						System.out.println("Current Perons: " + CurrentPerson);
    						System.out.println("Person working there: " + next);
    						
    						isStable = false;
    					}
    				}
    			}else
    			{
    				break;
    			}
    		}
    		
    	}
    	
    	
        return isStable;
    }
    

    
    


    /**
     * Determines a employee optimal solution to the Stable Marriage problem from the given input set.
     * Study the description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_employeeoptimal(Matching marriage) {
    	
    	
    	// need a counter for the alogrithm to how many applications they have left
    	ArrayList<Integer> employee_applications = Utilities.makeEmployeeApplicationsCount(marriage);
    	LinkedList<Integer> employee_queue = Utilities.makeQueue(marriage.getEmployeeCount());
    	ArrayList<ArrayList<Integer>> workers_at_location =  Utilities.makeLocationPositionsList(marriage.getLocationCount());
    	ArrayList<Integer> employee_matching = Utilities.makeInitialEmployee_matching(marriage.getEmployeeCount());
    	
    	while(!employee_queue.isEmpty())
    	{
    		Integer employee = employee_queue.pop();
    		ArrayList<Integer> employee_preference_list = marriage.getEmployeePreference().get(employee); // get current employee pref list
    		Integer prefered_place = employee_preference_list.get(employee_applications.get(employee));
    		ArrayList<Integer> prefered_place_pref_list = marriage.getLocationPreference().get(prefered_place);
    		// update the applications count because you are making a new one
    		employee_applications.set(employee, employee_preference_list.indexOf(prefered_place)+1);
    		
    		/*
    		 * Check if the current place applying has an opening and put the employee at that opening
    		 */
    		if(workers_at_location.get(prefered_place).size() < marriage.getLocationSlots().get(prefered_place) )
    		{
    			
    			employee_matching.set(employee, prefered_place); // update employee position in the employee_matching array
    			workers_at_location.get(prefered_place).add(prefered_place_pref_list.indexOf(employee)); // added employee to list of emmployees at prefered place
    		}else
    		{
    			Integer least_prefered_rank = Collections.max(workers_at_location.get(prefered_place));// gives you the lowest rank integer
    			Integer least_prefered = prefered_place_pref_list.get(least_prefered_rank);
    			/*
    			 * if the employee has a better ranking than the lowest ranked person 
    			 * put the employee at the location and remove the other employee
    			 */
    			if(prefered_place_pref_list.indexOf(employee) < prefered_place_pref_list.indexOf(least_prefered))
    			{
    				//change employee matching to 
    				employee_matching.set(least_prefered, -1);
    				employee_matching.set(employee, prefered_place);
    				
    				// change the workers at the location: free one and add one
    				workers_at_location.get(prefered_place).remove(least_prefered_rank);
    				workers_at_location.get(prefered_place).add(prefered_place_pref_list.indexOf(employee));
    				
    				// add back into queue for now free one
    				if(employee_applications.get(least_prefered) < marriage.getLocationCount())
    					employee_queue.add(least_prefered);
    				
    				
    				
    				
    			}else
    			{
    				/*
    				 * if employee has not applied to every location, put them back in the queue
    				 */
    				if(employee_applications.get(employee) < marriage.getLocationCount())
    					employee_queue.add(employee);
    			}
    			
    		}
    		
    	}
        marriage.setEmployeeMatching(employee_matching);
    	
        return marriage;
    }
    
    
 
    
 
    /**
     * Determines a location optimal solution to the Stable Marriage problem from the given input set.
     * Study the description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_locationoptimal(Matching marriage) {
    	
    	
    	LinkedList<Integer> locationQueue = Utilities.makeQueue(marriage.getLocationCount()); // make a queue with all the locations in it
    	ArrayList<Integer> location_num_slots_to_fill = (ArrayList<Integer>) marriage.getLocationSlots().clone(); // now we can edit the slots ****** Update ********8
    	ArrayList<Integer> location_current_offer = Utilities.makeLocation_offers(marriage); // ****Update*******
    	ArrayList<Integer> employee_matching = Utilities.makeInitialEmployee_matching(marriage.getEmployeeCount()); // ****Update***** Array to store all the employees with their final locations
    	
    	
    	ArrayList<ArrayList<Integer>> locations_preferences = (ArrayList<ArrayList<Integer>>) marriage.getLocationPreference().clone();    	
    	
    	while(!locationQueue.isEmpty())
    	{
    		/*
    		 * We are getting a location, their preference list, and how many offers they have made
    		 * Then we will get the next person on their preference list
    		 * 
    		 */
    		Integer currentLocation = locationQueue.pop(); // get location
    		ArrayList<Integer> location_preference_list = marriage.getLocationPreference().get(currentLocation); // get the preference list of location
    		Integer offers_made = location_current_offer.get(currentLocation); // get how many offers the locations have
    		Integer highest_ranked_employee = location_preference_list.get(offers_made); //get highest ranked employee in preference list that hasn't had an offer
    		location_current_offer.set(currentLocation, ++ offers_made); // just made and offer now update
    		Integer open_slots = location_num_slots_to_fill.get(currentLocation); // get the number of slots
  			/*
    			* if the current person is free we will have them work at our location
    		*/
    		if(employee_matching.get(highest_ranked_employee) == -1) // employee is free
    		{
    			employee_matching.set(highest_ranked_employee, currentLocation);
    				///current_location_employees.get(currentLocation).add(highest_ranked_employee);
    			location_num_slots_to_fill.set(currentLocation, open_slots -1);
    				
    				/*
    				 * if they have more positions to be filled we will put them back in the queue
    				 */
    			if((open_slots -1) > 0)
    			{
    					locationQueue.add(currentLocation);
    			}
    					
    			}
    		else
    		{
    			ArrayList<Integer> highest_ranked_employee_pref_list = marriage.getEmployeePreference().get(highest_ranked_employee);
    			Integer highest_ranked_employee_current_store = employee_matching.get(highest_ranked_employee);
    				/*
    				 * if highest ranked employee prefers store at compared to the store offering
    				 */
    			if(highest_ranked_employee_pref_list.indexOf(highest_ranked_employee_current_store) 
    				   < highest_ranked_employee_pref_list.indexOf(currentLocation) )
    			{

    				locationQueue.add(currentLocation);
    					
    			}else
    			{
    				// We need to fill the slot at the new location
    				location_num_slots_to_fill.set(currentLocation, open_slots -1);
    					
    				//Open the slot at the other location
    				Integer open_slots_other_store = location_num_slots_to_fill.get(highest_ranked_employee_current_store);
    				location_num_slots_to_fill.set(highest_ranked_employee_current_store, open_slots_other_store + 1);
    					
    				//update employee position
    				employee_matching.set(highest_ranked_employee, currentLocation);
    					
    				// add the highest store back to the queue
    				if(!locationQueue.contains(highest_ranked_employee_current_store)) // be
    					locationQueue.add(highest_ranked_employee_current_store);
    					
    				if((open_slots -1) > 0)
        			{
        				locationQueue.add(currentLocation);
        			}
    					
    					
    			}
    		}
    		
    		
    	}
    	marriage.setEmployeeMatching(employee_matching);
    	
        return marriage; 
    }
}


































