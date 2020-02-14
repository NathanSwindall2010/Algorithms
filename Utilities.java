import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;



public class Utilities {
	
	
	public static Boolean isBefore()
	{
		return false;
	}
	
	public static ArrayList<ArrayList<Integer>> makeLocationPositionsList(Integer locations)
	{
		ArrayList<ArrayList<Integer>> location_positions = new ArrayList<>();
		for(int location_num = 0; location_num < locations; location_num ++)
		{
			location_positions.add(new ArrayList<>());
		}
		
		return location_positions;
	}
	
	public static ArrayList<Integer> makeEmployeeApplicationsCount(Matching marriage)
	{
		
		ArrayList<Integer> employee_applications = new ArrayList<>();
		for(int employee_number = 0; employee_number < marriage.getEmployeeCount(); employee_number++)
		{
			employee_applications.add(0);
		}
		
		return employee_applications;
	}
	
	public static ArrayList<Integer> makeLocation_offers(Matching marriage)
	{
		ArrayList<Integer> locations_offers = new ArrayList<>();
		for(int employee_num = 0; employee_num < marriage.getLocationCount(); employee_num++)
		{
			locations_offers.add(0);
		}
		
		return locations_offers;
		
	}
	
	
	  public static LinkedList<Integer> makeQueue(Integer employee_count)
	    {
	    	LinkedList<Integer> queue = new LinkedList<>();
	    	for(int employee_num = 0; employee_num < employee_count; employee_num ++)
	    		queue.add(employee_num);
	    	return queue;
	    }
	  
	  
	    public static ArrayList<Integer> getPeopleAtStore(Integer currentHighLocation, ArrayList<Integer> employee_matching)
	    {
	    	int employee_matching_length = employee_matching.size();
	    	ArrayList<Integer> workers_At_Location = new ArrayList<Integer>();
	    	for(int employee_number = 0; employee_number < employee_matching_length; employee_number ++)
	    	{
	    		if(employee_matching.get(employee_number).equals(currentHighLocation))
	    		{
	    			workers_At_Location.add(employee_number);
	    		}
	    	}
	    	
	    	return workers_At_Location;
	    }
	    
	    
	    public static ArrayList<Integer> makeInitialEmployee_matching(Integer employee_count)
	    {
	    	ArrayList<Integer> employee_matching = new ArrayList<>();
	    	for(int employee_num = 0; employee_num < employee_count; employee_num++)
	    	{
	    		employee_matching.add(-1);
	    	}
	    	
	    	return employee_matching;
	    
	    }
	    
	    
	    public static boolean slotsTheSame(Matching marriage)
	    {
	    	ArrayList<Integer> Location_slots = marriage.getLocationSlots();
	    	
	        for(int count = 0; count < marriage.getLocationCount(); count++)
	        {
	        	
	        	Integer workers = getPeopleAtStore(count,marriage.getEmployeeMatching()).size();
	        	if(!workers.equals(Location_slots.get(count)))
	        	{
	        		return false;
	        	}
	        	
	        }
	    	
	        return true;
	    	
	    }
	    
	 
	
	    
	    

}
