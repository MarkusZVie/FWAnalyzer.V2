package at.ac.univie.FirewallLogAnayzer.Input;

import at.ac.univie.FirewallLogAnayzer.GUI.FileChooseController;

public class ReportNumberOfLinesParsed extends Thread{

	private ParserCisco parser;
	private FileChooseController fcc;
	private long numberToRead;
	private long readedRows;
	private boolean run;
	
	public ReportNumberOfLinesParsed(ParserCisco parser, FileChooseController fcc) {
		this.parser = parser;
		this.fcc = fcc;
		numberToRead =0;
		readedRows = 0;
		run = true;
	}


	@Override
	public void run() {
		try {
		Thread.sleep(5);
		while(run){
			readedRows = parser.getNumberOfRowsReaded();
			numberToRead  = parser.getNumberToRead();
			double percentage = ((Double.parseDouble(readedRows+"")/(Double.parseDouble(numberToRead+"")))*100);
			double percentageRound = Math.round(percentage*100);
			percentageRound = percentageRound/100;
			System.out.println("Logrows read: " + readedRows + " of " + numberToRead + " => " + percentageRound + " %");
			if(fcc!=null){
				fcc.updateProgressValues(numberToRead, readedRows);
			}
			Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void endThread(){
		run = false;
	}

}
