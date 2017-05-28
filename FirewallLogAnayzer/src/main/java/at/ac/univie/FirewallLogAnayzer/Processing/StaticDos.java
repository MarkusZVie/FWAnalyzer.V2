package at.ac.univie.FirewallLogAnayzer.Processing;

import at.ac.univie.FirewallLogAnayzer.Data.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// Static Funktionen für DoS Analyse
public class StaticDos {

    // Filtere nachrichten nach Protokoll
    // 		icmp
    // 		TCP
    public static ArrayList<LogRow> filterProtocol(String protocol){
        ArrayList<LogRow> fpl = new ArrayList<LogRow>();
        for(LogRow lr: LogRows.getInstance().getLogRows()){
            if (lr.getProtocol()!= null && lr.getProtocol().equals(protocol)){
                fpl.add(lr);
            }
        }
        return fpl;
    }

    public static void printFilterProtocol(ArrayList<LogRow> fpl){
        for (LogRow lr: fpl){
            System.out.println(lr.getSrcIP());
        }
    }

    // Retouniere Hashmap bestehend aus:
    //		IP = Key
    //		Arraylist = Value (beinhaltet alle LogRows der IP)
    static HashMap<String, ArrayList<LogRow>> countIpDenies(ArrayList<LogRow> denies){
        HashMap<String, ArrayList<LogRow>> map = new HashMap<String, ArrayList<LogRow>>();
        for (LogRow lr: denies){
            // Wenn die Map den Schlüssel noch nicht beinhaltet erstelle ein neues Array
            if (!map.containsKey(lr.getSrcIP())){
                ArrayList<LogRow> rn = new ArrayList<LogRow>();
                rn.add(lr);
                map.put(lr.getSrcIP(), rn);
            } else {
            // Wenn die Map den Schlüssel beinhaltet dann füge die LogRow zum Array hinzu
                ArrayList<LogRow> r = map.get(lr.getSrcIP());
                r.add(lr);
                map.put(lr.getSrcIP(), r);
            }
        }
        return map;
    }

    public static void printHashmap(HashMap<String, ArrayList<LogRow>> map){
        for (Map.Entry<String, ArrayList<LogRow>> entry : map.entrySet()){
            System.out.println("#ip: " + entry.getKey().toString() + " | Array: " + entry.getValue().size());
            ArrayList<LogRow> alr = entry.getValue();

            for (LogRow lrr: alr){
                if (lrr.getLocation() != null) {
                    if (lrr.getLocation().getCountryName() != null) {
                        System.out.println(lrr.getLocation().getCountryName());
                    }
                }
            }

        }
    }

    // Retouniere DoSDataList bestehend aus DoSData Objekten
    static DoSDataList manageAll(HashMap<String, ArrayList<LogRow>> map){
        DoSDataList ddl = new DoSDataList();
        for (Map.Entry<String, ArrayList<LogRow>> entry : map.entrySet()){
            ArrayList<LogRow> alr = entry.getValue();
            // Relevant sind IP's mit LogRow's > 1 sonst kann keine Zeit verglichen werden
            if (alr.size() > 1){
                // Weise jedem DoSData Objekt weitere Attribute hinzu
                ArrayList<Integer> oneIpDifferences = calcTimeInterval(alr);
                StandardDeviation sData = new StandardDeviation(oneIpDifferences);
                DoSData dd = new DoSData(alr, sData);
                // speichern in der DoSDataList
                ddl.addDoSData(dd);
            }
        }
        return ddl;
    }

    // get Single IP from Hashmap DoSDataList
    static DoSData getSingleIp(DoSDataList ddl, String ip){
        DoSData singleIp = null;
        for (int i = 0; i < ddl.getDataEdited().size() ; i++) {
            if (ddl.getDataEdited().get(i).getMessages().get(0).getSrcIP().equals(ip)){
                singleIp = ddl.getDataEdited().get(i);
            }
        }
        return singleIp;
    }

    // Retouniere eine Arraylist von Integer Werten für die spätere DoS Analyse
    private static ArrayList<Integer> calcTimeInterval(ArrayList<LogRow> denies){
        ArrayList<Integer> differences = new ArrayList<>();
        for (int i = 0; i<denies.size(); i++){
            // übeprüfe ob es ein weiteres Objekt gibt um die zeitliche Differenz zu berechnen
            if (i+1 < denies.size()) {
                // Vergleiche jeweils 2 Zeiten von LogRows
                Date dateA = denies.get(i).getDateTime();
                Date dateB = denies.get(i + 1).getDateTime();
                long diff = dateB.getTime() - dateA.getTime();
                long sec = TimeUnit.MILLISECONDS.toSeconds(diff);
                Integer parseLong = (int) (long) sec;
                // speichere den Wert in die ArrayList differences
                differences.add(parseLong);
            }
        }
        return differences;
    }

    // für alle dd aus ddl wird mpt berechnet mit den minuten
    // dd -> setMpt -> gibt ein array zurück
    // für das objekt dd -> mptList
    // dort sind alle mpt Berechungen in den min x Abständen

    // Für jedes DoSData Objekt aus der übergebenen DoSDataList
    // berechne Message Per Time mit den vorgegebenen Minuten-Zeitfenstern
    static void assignMpt(DoSDataList ddl, int minutes){
        for (DoSData dd: ddl.getDataEdited()){
            dd.setMpt(minutes);
        }
    }

}
