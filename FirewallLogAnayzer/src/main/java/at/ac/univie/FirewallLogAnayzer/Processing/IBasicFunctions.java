package at.ac.univie.FirewallLogAnayzer.Processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

import at.ac.univie.FirewallLogAnayzer.Data.IpLocation;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.PortScanner.PortScan;
import at.ac.univie.FirewallLogAnayzer.Exceptions.StringNotFoundException;

public interface IBasicFunctions {
	
	public  String[] readeFile(String filePath) throws FileNotFoundException;
		
	public  String searchTheNStringWithPreAndPostfix(String line, int i, String preFix, String postFix) throws StringNotFoundException;
			
	public  String searchTheNIpInRow(String line,int n);
			
	public  String getNullString();
	
	public  SimpleDateFormat getSimpleDateFormat();
	
	public  IpLocation findeLocation(String ip);
		
	public  ArrayList doPortScan(String host, int portrange);
		
	public  void cleanFile(String filePath);
		
	public  Date getLogBeginDate(ArrayList<LogRow> LogRows) ;
	
	public  Date getLogEndDate(ArrayList<LogRow> LogRows);
	

}
