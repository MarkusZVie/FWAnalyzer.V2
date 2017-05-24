package at.ac.univie.FirewallLogAnayzer.Processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Data.PortScanner.PortScan;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

import at.ac.univie.FirewallLogAnayzer.Data.IpLocation;
import at.ac.univie.FirewallLogAnayzer.Exceptions.StringNotFoundException;

public class StaticFunctions {
	public static String[] readeFile(String filePath) throws FileNotFoundException {
		//http://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
		int numberOfRows=0;
		File choosenFile = new File(filePath);
		File[] targrtFiles = choosenFile.listFiles();
		if(targrtFiles==null){
			targrtFiles = new File[1];
			targrtFiles[0] = choosenFile;
		}
		StringBuilder sb = new StringBuilder();
		for(File f:targrtFiles){
			try(BufferedReader br = new BufferedReader(new FileReader(f))) {
			    
			    String line = br.readLine();
	
			    while (line != null) {
			        sb.append(line);
			        numberOfRows++;
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			        
			    }
			    //add every line to logFileContent
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] returnString = new String[2];
		returnString[0]=sb.toString();
		returnString[1]=numberOfRows+"";
		return returnString;
		
	}
	
	public static String searchTheNStringWithPreAndPostfix(String line, int i, String preFix, String postFix) throws StringNotFoundException{
		
		//The N Search is not Developed, only the first is searchable,
		//Every call of the first substring is called with int n of one-> what means the first appierence of that post&prefix
		if(line.indexOf(preFix)<0||line.indexOf(postFix)<0)
			throw new StringNotFoundException();
		
		int beginIndexPreFix =line.indexOf(preFix);
		int lengthOfPreFix = preFix.length();
		int endIndexPrefix = beginIndexPreFix+lengthOfPreFix;
		String lineFromEndPreFix = line.substring(endIndexPrefix);
		int numberOfCuttingChar = line.length()-lineFromEndPreFix.length();
		int beginIndexPostFix = lineFromEndPreFix.indexOf(postFix)+numberOfCuttingChar;
		String result = line.substring(endIndexPrefix, beginIndexPostFix);
		//System.out.print(beginIndexPreFix + " " + endIndexPrefix + " " + beginIndexPostFix + " " + result);
		//System.out.println();
		return result;
		
		
	}
	
	public static String searchTheNIpInRow(String line,int n){
		//returns the IP Address with index = n (n=1 -> first Address)
		String ipPattern = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
		Pattern p = Pattern.compile(ipPattern);
		Matcher m = p.matcher(line);
		int count = 1;
		while (m.find()) {
			if(count++==n){
				return m.group(0);
			}
		}
		return null;
	}
	
	public static String getNullString(){
		return "undef";
	}

	public static SimpleDateFormat getSimpleDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public static IpLocation findeLocation(String ip){
		String DATABASE_CITY_PATH = "./Files/GeoLite2-City.mmdb";
		IpLocation ipLocation;
		//http://o7planning.org/en/10455/retrieving-geographic-information-based-on-ip-address-using-geoip2-java-api
		try {
			// A File object pointing to your GeoLite2 database
			File dbFile = new File(DATABASE_CITY_PATH);
			// This creates the DatabaseReader object,
			// which should be reused across lookups.
			DatabaseReader reader = new DatabaseReader.Builder(dbFile).build();
			// A IP Address
			InetAddress ipAddress = InetAddress.getByName(ip);
			// Get City info
			CityResponse response = reader.city(ipAddress);
			// Country Info
			Country country = response.getCountry();
			String countryIsoCode = country.getIsoCode();
			String countryName =  country.getName(); 
			Subdivision subdivision = response.getMostSpecificSubdivision();
			String subdivisionName = subdivision.getName();
			String subdivisionIsoCode = subdivision.getIsoCode(); 
			// City Info.
			City city = response.getCity();
			String cityName = city.getName(); 
			// Postal info
			Postal postal = response.getPostal();
			String postCode = postal.getCode();
			// Geo Location info.
			Location location = response.getLocation();
			// Latitude
			double latitude = location.getLatitude();     
			// Longitude
			double longitude= location.getLongitude();
			ipLocation = new IpLocation(countryIsoCode, countryName, subdivisionName, subdivisionIsoCode, cityName, 
										postCode, latitude, longitude);
			return ipLocation;
		} catch (Exception e) {
			return null;
		}
	}

	public static ArrayList doPortScan(String host, int portrange){
		PortScan ps = new PortScan(host, portrange);
		ArrayList open= ps.getopenPorts();
		System.out.println(open.toString());
		return open;
	}

	public static void cleanFile(String filePath) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(filePath));
	        writer.write("");
		    writer.close();
		           
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static Date getLogBeginDate(ArrayList<LogRow> LogRows) {
		Date beginDate = LogRows.get(0).getDateTime();
		for(LogRow lr: LogRows){
			if(lr.getDateTime().getTime()<beginDate.getTime()){
				beginDate = lr.getDateTime();
			}
		}
		return beginDate;
	}

	
	
	public static Date getLogEndDate(ArrayList<LogRow> LogRows) {
		Date endDate = LogRows.get(0).getDateTime();
		for(LogRow lr: LogRows){
			if(lr.getDateTime().getTime()>endDate.getTime()){
				endDate = lr.getDateTime();
			}
		}
		return endDate;
	}


	
}
