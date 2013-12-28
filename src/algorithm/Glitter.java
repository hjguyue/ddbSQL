package algorithm;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class Glitter{

	public static BufferedReader reader;

	public static HashMap<String, Vector<String>> tweetsMap = new HashMap<String, Vector<String>>();

	// {name,(short_path1,short_path2)]
	public static HashMap<String, TreeSet<String>> statePath = new HashMap<String, TreeSet<String>>();
	public static HashMap<String, TreeSet<String>> cityPath = new HashMap<String, TreeSet<String>>();
	public static HashMap<String, TreeSet<String>> localityPath = new HashMap<String, TreeSet<String>>();
	public static HashMap<String, TreeSet<String>> addressPath = new HashMap<String, TreeSet<String>>();
	public static HashMap<String, TreeSet<String>> poiPath = new HashMap<String, TreeSet<String>>();

	// {short_path,name}
	public static HashMap<String, String> pathState = new HashMap<String, String>();
	public static HashMap<String, String> pathCity = new HashMap<String, String>();
	public static HashMap<String, String> pathLocality = new HashMap<String, String>();

	public static Vector<TreeSet<String>> matchSets = new Vector<TreeSet<String>>();

	public static void load() {
		try {
			String line = "";
			int process = 0;

			reader = new BufferedReader(new FileReader("/Users/Harry/Documents/workspace/projects/glitter/data/poi.csv"));
//			reader = new BufferedReader(new FileReader("/home/hujun/glitter/data/poi.csv"));
			// | id | name | shortAddr | state | city | locality | path_1 | path_2 | path_3 | path_4 | path_5 |
			System.out.println("loading poi...");
			process = 0;
			while ((line = reader.readLine()) != null) {
				String strs[] = line.split("!@#@!");
				if (!statePath.containsKey(strs[4])) {
					statePath.put(strs[4], new TreeSet<String>());
				}
				statePath.get(strs[4]).add(strs[7]);
				pathState.put(strs[7], strs[4]);

				if (!cityPath.containsKey(strs[5])) {
					cityPath.put(strs[5], new TreeSet<String>());
				}
				cityPath.get(strs[5]).add(strs[8]);
				pathCity.put(strs[8], strs[5]);

				if (!localityPath.containsKey(strs[6])) {
					localityPath.put(strs[6], new TreeSet<String>());
				}
				localityPath.get(strs[6]).add(strs[9]);
				pathLocality.put(strs[9], strs[6]);

				if (!addressPath.containsKey(strs[3])) {
					addressPath.put(strs[3], new TreeSet<String>());
				}
				addressPath.get(strs[3]).add(strs[10]);
				
				if (!poiPath.containsKey(strs[1])) {
					poiPath.put(strs[1], new TreeSet<String>());
				}
				poiPath.get(strs[1]).add(strs[10]);

//				 if(++process > 1000000)
//				 break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String Clear(String str){
		str = str.replaceAll("[^a-zA-Z0-9]", " ");
		while(str.indexOf("  ") >= 0){
			str = str.replaceAll("  ", " ");
		}
		str = str.toLowerCase();
		return str;
	}
	
	public static String match(String query) {
		
		String inputTweets = query;

		Vector<String> tweets = new Vector<String>();
		String inputStrs[] = inputTweets.split("##");
		for(String str:inputStrs){
			tweets.add(Clear(str));
		}

		HashMap<Integer, HashSet<String>> hitMap = new HashMap<Integer, HashSet<String>>();

		int process = 0;
		for (String tweet : tweets) {
			process++;
			hitMap.put(process, new HashSet<String>());
			Vector<String> vector = getSubstrs(tweet);
			for (String subString : vector) {
				if (statePath.containsKey(subString)) {
					hitMap.get(process).addAll(statePath.get(subString));
				}
				if (cityPath.containsKey(subString)) {
					hitMap.get(process).addAll(cityPath.get(subString));
				}
				if (localityPath.containsKey(subString)) {
					hitMap.get(process).addAll(localityPath.get(subString));
				}
				if (addressPath.containsKey(subString)) {
					hitMap.get(process).addAll(addressPath.get(subString));
				}
				if (poiPath.containsKey(subString)) {
					hitMap.get(process).addAll(poiPath.get(subString));
				}
			}
		}
		
		String answer = "";
		HashMap<String, Double> scoreMap = new HashMap<String, Double>();
		// get the state:
		for (Integer ID:hitMap.keySet()) {
			int size = hitMap.get(ID).size();
			for(String str:hitMap.get(ID)){
				String state = getPrefix(str, 0);
				if(!scoreMap.containsKey(state))
					scoreMap.put(state, 0.0);
				scoreMap.put(state, scoreMap.get(state)+1.0/size);
			}
		}
		String bestState = "";
		if (scoreMap.keySet().size() >= 1) {
			bestState = getTop(scoreMap);
			answer = pathState.get(bestState);
		}
		
		// get the city:
		scoreMap.clear();
		// 1. remove:
		for (Integer ID:hitMap.keySet()) {
			Vector<String> misStrings = new Vector<String>();
			for(String str:hitMap.get(ID)){
				if (!getPrefix(str, 0).equals(bestState) || str.equals(getPrefix(str, 0))) {
					misStrings.add(str);
				}
			}
			hitMap.get(ID).removeAll(misStrings);
		}
		// 2. score:
		for (Integer ID:hitMap.keySet()) {
			int size = hitMap.get(ID).size();
			for(String str:hitMap.get(ID)){
				String city = getPrefix(str, 1);
				if(!scoreMap.containsKey(city))
					scoreMap.put(city, 0.0);
				scoreMap.put(city, scoreMap.get(city)+1.0/size);
			}
		}
		String bestCity="";
		if (!bestState.equals("") && scoreMap.keySet().size() >= 1) {
			bestCity = getTop(scoreMap);
			answer += "!@#@!" + pathCity.get(bestCity);
		}
		else {
			answer += "!@#@!";
		}
		
		
		// get the locality:
		scoreMap.clear();
		// 1. remove:
		for (Integer ID:hitMap.keySet()) {
			Vector<String> misStrings = new Vector<String>();
			for(String str:hitMap.get(ID)){
				if (!getPrefix(str, 1).equals(bestCity) || str.equals(getPrefix(str, 1))) {
					misStrings.add(str);
				}
			}
			hitMap.get(ID).removeAll(misStrings);
		}
		// 2. score:
		for (Integer ID:hitMap.keySet()) {
			int size = hitMap.get(ID).size();
			for(String str:hitMap.get(ID)){
				String locality = getPrefix(str, 2);
				if(!scoreMap.containsKey(locality))
					scoreMap.put(locality, 0.0);
				scoreMap.put(locality, scoreMap.get(locality)+1.0/size);
			}
		}
		String bestLocality = "";
		if (!bestCity.equals("") && scoreMap.keySet().size() >= 1) {
			bestLocality = getTop(scoreMap);
			answer += "!@#@!" + pathLocality.get(bestLocality);
		}
		else {
			answer += "!@#@!";
		}
		
		return answer;
	}

	public static Vector<String> getSubstrs(String string) {
		Vector<String> subStrings = new Vector<String>();
		String words[] = string.split(" ");
		int size = words.length;
		if (size < 1)
			return subStrings;
		String substr = "";
		for (int count = 4; count >= 1; count--) {
			for (int start = 0; start <= size - count; start++) {
				substr = "";
				for (int i = 0; i < count; i++) {
					substr += words[start + i] + " ";
				}
				subStrings.add(substr.trim());
			}
		}
		return subStrings;
	}

	public static String getTop(HashMap<String, Double> counter) {
		double max = 0;
		String MAX = "";
		for (String string : counter.keySet()) {
			if (max < counter.get(string)) {
				max = counter.get(string);
				MAX = string;
			}
		}
		return MAX;
	}
	
	public static String getBest(HashMap<String, Integer> counter) {
		int max = 0;
		String MAX = "";
		for (String string : counter.keySet()) {
			if (max < counter.get(string)) {
				max = counter.get(string);
				MAX = string;
			}
		}
		return MAX;
	}
	
	public static String getPrefix(String str, int len){
		String strs[] = str.split("_");
		String ans = strs[0];

		// len = 0 return state
		for(int i = 1; i <= len; i++){
			ans += "_" + strs[i];
		}
		return ans;
	}
	
	
	public static int a = 0;
    public void init() { 
    	if(a == 1)
    		return;
    	else {
    		a = 1;
			load();
			System.out.println("done!");
//			String query = "I feel much better @Health Clinic, Hollywood. ##"
//					+ "I was able to get a tour at Film School, Sunset blvd.##"
//					+ "Muffin bar is so nice. @Highland Gardens Hotel. ##"
//					+ "My favorite coffee in Los Angeles. @Groundwork Coffee ##"
//					+ "Perfect Gift Store - Victoria’s Gift Shop in San Diego. ##";
//			match(query);
		}
	}
    
    public void query(){

		try {
	    	HttpServletResponse response = ServletActionContext.getResponse();
	    	HttpServletRequest request =  ServletActionContext.getRequest();
	    	String text = "";
			if(request.getParameter("text") != null){
				 text = request.getParameter("text");
			}
			String ans = match(text);
			response.setContentType("text/html");
			PrintStream out;
			out = new PrintStream(response.getOutputStream());
			out.print(ans);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) {
		load();
		String query = "I feel much better @Health Clinic, Hollywood. ##"
				+ "I was able to get a tour at Film School, Sunset blvd.##"
				+ "Muffin bar is so nice. @Highland Gardens Hotel. ##"
				+ "My favorite coffee in Los Angeles. @Groundwork Coffee ##"
				+ "Perfect Gift Store - Victoria’s Gift Shop in San Diego. ##";
		match(query);
	}

}
