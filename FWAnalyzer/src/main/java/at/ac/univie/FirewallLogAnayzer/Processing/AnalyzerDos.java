package at.ac.univie.FirewallLogAnayzer.Processing;

import at.ac.univie.FirewallLogAnayzer.Data.DoSData;
import at.ac.univie.FirewallLogAnayzer.Data.DoSDataList;
import at.ac.univie.FirewallLogAnayzer.Data.IpLocation;
import at.ac.univie.FirewallLogAnayzer.Data.LogRow;

import java.util.*;

public class AnalyzerDos implements IProcessingAnalyseGenerel {

    public AnalyzerDos(){}

    @Override
    public DoSDataList analyseDos(String dosProtocolType, int timeSlot) {

        ArrayList<LogRow> fpl = StaticDos.filterProtocol(dosProtocolType);

        HashMap<String, ArrayList<LogRow>> map = StaticDos.countIpDenies(fpl);

        DoSDataList ddlist = StaticDos.manageAll(map);
        ddlist.setName(dosProtocolType+"-Data");

        StaticDos.assignMpt(ddlist, timeSlot);

        return ddlist;
    }

    @Override
    public void analyseDDoS() {

    }


    public ArrayList<DoSData> analyzeMpt(DoSDataList processedData, Double criticalValue){
        ArrayList<DoSData> critical = new ArrayList<>();
        boolean tmp = false;
        for (DoSData dd: processedData.getDataEdited()){
            for (int i = 0; i < dd.getMptList().size(); i++) {
                if (dd.getMptList().get(i) > criticalValue){
                    tmp = true;
                }
            }
            if (tmp){
                critical.add(dd);
                tmp = false;
            }
        }
        return critical;
    }

    public ArrayList<DoSData> sortMessagePerMinute(DoSDataList processedData, String ascdesc){
        ArrayList<DoSData> dataraw = processedData.getDataEdited();
        System.out.println("Sort MPM: " + dataraw.size());

        if (ascdesc.equals("asc")){
            Collections.sort(dataraw, new Comparator<DoSData>() {
                @Override
                public int compare(DoSData o1, DoSData o2) {
                    return Double.compare(o2.getStd().getMessagePerMinute(), o1.getStd().getMessagePerMinute());
                }
            });
        }

        if (ascdesc.equals("desc")){
            Collections.sort(dataraw, new Comparator<DoSData>() {
                @Override
                public int compare(DoSData o1, DoSData o2) {
                    return Double.compare(o1.getStd().getMessagePerMinute(), o2.getStd().getMessagePerMinute());
                }
            });
        }

        for (int i = 0; i<dataraw.size();i++){
            System.out.println(i + " : " + dataraw.get(i).getStd().getMessagePerMinute()
                    + " ip: " +  dataraw.get(i).getMessages().get(0).getSrcIP() );
            /*
            int check = dataraw.get(i).getMessages().size();
            if (check > 1) {
                IpLocation iltemp = dataraw.get(i).getMessages().get(0).getLocation();
                if (iltemp == null) {
                    System.out.println("    No IpLocation: ");
                } else {
                    String check2 = iltemp.getCityName();
                    if (check2 == null){
                        System.out.println("    = null: ");
                    } else {
                        System.out.println("    country: " + check2);
                    }
                }
            }
            */
        }
        return dataraw;
    }


    public HashMap<String, ArrayList<DoSData>> messagesOfCountry(DoSDataList processedData){
        // Alle rohen DoSData Objekte
        ArrayList<DoSData> dataraw = processedData.getDataEdited();
        // Hashmap -> Key = Country, Value = Alle IPs mit gleichem Country
        HashMap<String, ArrayList<DoSData>> countrymap = new HashMap<>();

        int check = 0;
        IpLocation iltemp;
        ArrayList<DoSData> unassigned = new ArrayList<>();

        // Haupt For Each Schleife
        for (DoSData dd: dataraw) {
            iltemp = dd.getMessages().get(0).getLocation();
            // Wenn das Objekt IpLocation null ist dann dd in die unassigned Liste speichern
            if (iltemp == null) {
                unassigned.add(dd);
            } else {
                String check2 = iltemp.getCountryName();
                // Wenn keine Landname verfügbar ist dann in die Liste unassigned speichern
                if (check2 == null) {
                    unassigned.add(dd);
                } else {
                    // Wenn der Key noch nicht vorhanden -> neue Liste und in die Hashmap speichern
                    if (!countrymap.containsKey(dd.getMessages().get(0).getLocation().getCountryName())) {
                        ArrayList<DoSData> ips = new ArrayList<DoSData>();
                        ips.add(dd);
                        countrymap.put(dd.getMessages().get(0).getLocation().getCountryName(), ips);
                    } else {
                        // Wenn der Key bereits vorhanden -> Liste updaten und in die Hashmap speichern
                        ArrayList<DoSData> r = countrymap.get(dd.getMessages().get(0).getLocation().getCountryName());
                        r.add(dd);
                        countrymap.put(dd.getMessages().get(0).getLocation().getCountryName(), r);
                    }
                }
            }
        }
        // Speichere die Liste der nicht zuweisbaren auch in die Hashmap
        countrymap.put("unassignedMessages", unassigned);
        return countrymap;
    }


    public HashMap<String, Integer> sumMessagesPerCountry(HashMap<String, ArrayList<DoSData>> countrymap){
        HashMap<String,Integer> countryCount = new HashMap<String,Integer>();
        int tmpCount = 0;
        // Hauptschleife für das iterieren der übergebenen Hashmap
        for (Map.Entry<String, ArrayList<DoSData>> entry : countrymap.entrySet()){
            ArrayList<DoSData> alr = entry.getValue();
            // zurücksetzen des tmpCount für das nächste Land
            tmpCount = 0;
            // Iteriere durch jede Arraylist von DosData Objekten für jede Land
            for (DoSData dd: alr){
                // Addiere die Größe aller Messages pro DoSData
                tmpCount = tmpCount + dd.getMessages().size();
            }
            // Schlüssel: Land, Wert: Summe aller Nachrichten aller DoSData
            countryCount.put(entry.getKey(), tmpCount);
        }
        return countryCount;
    }


    public void sortStabw(){

    }

    // get Single IP from DoSList -> Check for null
    public DoSData getSingleIP(DoSDataList processedData, String ip){
        DoSData getthis = StaticDos.getSingleIp(processedData, ip);
        return getthis;
    }

}
