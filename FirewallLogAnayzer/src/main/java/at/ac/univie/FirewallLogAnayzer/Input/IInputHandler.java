package at.ac.univie.FirewallLogAnayzer.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import at.ac.univie.FirewallLogAnayzer.Data.LogType;
import at.ac.univie.FirewallLogAnayzer.Exceptions.LogIdNotFoundException;
import at.ac.univie.FirewallLogAnayzer.GUI.FileChooseController;

public interface IInputHandler {
	public void loadeFirewallLog(String logpath,LogType logtype) throws FileNotFoundException, LogIdNotFoundException;

	public void loadeFirewallLog(List<File> fileList, LogType logtype, FileChooseController fcc) throws FileNotFoundException, LogIdNotFoundException;
}
