package at.ac.univie.FirewallLogAnayzer.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import at.ac.univie.FirewallLogAnayzer.Data.LogTypeSingelton;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.GUI.FileChooseController;
import at.ac.univie.FirewallLogAnayzer.GUI.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class AsynchronFileParse extends Thread{

	private List<File> fileList;
	private FileChooseController fileChooseController;
	
	public AsynchronFileParse(List<File> fileList, FileChooseController fileChooseController ) {
		this.fileList = fileList;
		this.fileChooseController = fileChooseController;
	}

	@Override
	public void run() {
		IInputHandler inputHandler = new InputHandler();
        // /Users/josefweber/Desktop/SyslogCatchAll-2017-03-14.txt
        // C:\Users\Lezard\Desktop\SyslogCatchAll-2017-03-14.txt
        try {        	
        	inputHandler.loadeFirewallLog(fileList, LogTypeSingelton.getInstance().getSupportedLogTypeList().get(0), fileChooseController);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (LogIdNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}
