package at.ac.univie.FirewallLogAnayzer.Input;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class IPBackgroundParser implements IIPBackgroundParser{

	private static final String URL="http://www.mein-whois.de/?";
	
	@Override
	public String getBackgroundInfos(String ipAddress) {
		try {
			String pageContent= loadPage(URL+ipAddress+"");
			String cleanedPageContent= pageContent.replaceAll("\\<.*?>","");
			String onlyImportentText=cleanedPageContent.substring(	cleanedPageContent.indexOf("Abfrage nach IP-Adresse zu Name:"), 
																	cleanedPageContent.indexOf("Bitte w&auml;hlen Sie nun den zu befragenden Whois-Server"));
			return onlyImportentText;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String deleteTagBoxes(String sourceCode){
		
		String cleanedCode ="";
		int count = 0;
		while(sourceCode.indexOf('>', count)!=sourceCode.lastIndexOf('>')){
			int o = sourceCode.indexOf('>', count);
			if(sourceCode.charAt(o+1)!='<'){
				cleanedCode=cleanedCode+" "+(sourceCode.substring(o+1, sourceCode.indexOf('<', o+1)));
			}
			count = o+1;
		}
		return sourceCode;
	}
	
	private String loadPage(String expandedURL) throws MalformedURLException, IOException {
		Scanner scanner = new Scanner(new URL(expandedURL).openStream());
		StringBuilder sb = new StringBuilder();
        while(scanner.hasNextLine()){
        	sb.append(scanner.nextLine());
        	sb.append(System.lineSeparator());
        }
        scanner.close();
		return sb.toString();
	}
	
}
