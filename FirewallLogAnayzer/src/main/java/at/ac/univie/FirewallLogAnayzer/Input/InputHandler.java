package at.ac.univie.FirewallLogAnayzer.Input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import at.ac.univie.FirewallLogAnayzer.Data.LogRow;
import at.ac.univie.FirewallLogAnayzer.Data.LogRows;
import at.ac.univie.FirewallLogAnayzer.Data.LogType;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.GUI.FileChooseController;
import at.ac.univie.FirewallLogAnayzer.Processing.BasicFunctions;
import at.ac.univie.FirewallLogAnayzer.Processing.IBasicFunctions;


public class InputHandler implements IInputHandler{

	private IBasicFunctions basicFunctions;
	
	
	



	public InputHandler() {
		basicFunctions = new BasicFunctions();
	}


	public void loadeFirewallLog(String logpath, LogType logType) throws FileNotFoundException, LogIdNotFoundException{
		
		basicFunctions.cleanFile("Files\\errorLog.txt");
		
		String[] logFileContentAndNumberOfRows = basicFunctions.readeFile(logpath);
		String logFileContent = logFileContentAndNumberOfRows[0];
		int numberOfRows = Integer.parseInt(logFileContentAndNumberOfRows[1]);
		
		switch (logType.getId()) {
		case 0:
			ParserCisco parser = new ParserCisco(numberOfRows);
			ReportNumberOfLinesParsed reporter = new ReportNumberOfLinesParsed(parser, null);
			reporter.start();
			parser.parse(logFileContent);
			reporter.stop();
			break;

		default:
			throw new LogIdNotFoundException();
		}
		
	}


	@Override
	public void loadeFirewallLog(List<File> fileList, LogType logType, FileChooseController fcc) throws FileNotFoundException, LogIdNotFoundException {
		basicFunctions.cleanFile("Files\\errorLog.txt");
		String logFileContent ="";
		StringBuilder sb = new StringBuilder();
		int numberOfRows =0;
		for(File f:fileList){
			String[] logFileContentAndNumberOfRows = basicFunctions.readeFile(f.getAbsolutePath());
			sb.append(logFileContentAndNumberOfRows[0]);
			numberOfRows += Integer.parseInt(logFileContentAndNumberOfRows[1]);
		}
		logFileContent = sb.toString();
		
		switch (logType.getId()) {
		case 0:
			ParserCisco parser = new ParserCisco(numberOfRows);
			ReportNumberOfLinesParsed reporter = new ReportNumberOfLinesParsed(parser, fcc);
			reporter.start();
			parser.parse(logFileContent);
			reporter.stop();
			break;

		default:
			throw new LogIdNotFoundException();
		}
		
	}


	@Override
	public void readParsedFiles(List<File> fileList) {
		ArrayList<LogRow> additionalLogRows = new ArrayList<>();
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try {
			for(File f:fileList){
				fin = new FileInputStream(f.getAbsolutePath());
				ois = new ObjectInputStream(fin);
				ArrayList<LogRow> logRowsFromFile = new ArrayList<>();
				logRowsFromFile = (ArrayList<LogRow>) ois.readObject();
				for(LogRow lr: logRowsFromFile){
					additionalLogRows.add(lr);
				}
			}
			for(LogRow lr: additionalLogRows){
				LogRows.getInstance().addLogRow(lr);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	

}
