package at.ac.univie.FirewallLogAnayzer.Data;

import java.util.ArrayList;
import java.util.Date;

public class MessagePerTime {

    private ArrayList<Integer> msgPerSlot;

    public MessagePerTime(){}

    public ArrayList<Integer> getMsgPerSlot() {
        return msgPerSlot;
    }

    // diese Methode liefert ein Array von Integer Werten
    // Jeder Wert des Arrays basiert auf einem Zeitfenster
    public ArrayList<Integer> calcMsgPerSlot(int minutes, ArrayList<LogRow> messages){
        msgPerSlot = new ArrayList<>();
        int tmpCount = 0;
        // Berechne das erste Zeitfenster
        Date slotDate = calcNextSlot(minutes, messages.get(0).getDateTime());

        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getDateTime().equals(slotDate) || messages.get(i).getDateTime().before(slotDate) ){
                tmpCount++;
            }
            if (messages.get(i).getDateTime().after(slotDate)){
                //System.out.println("Count: " + tmpCount);
                msgPerSlot.add(tmpCount);
                tmpCount = 0;
                slotDate = calcNextSlot(minutes, slotDate);
                while (!messages.get(i).getDateTime().before(slotDate)){
                    slotDate = calcNextSlot(minutes, slotDate);
                    msgPerSlot.add(tmpCount);
                }
                tmpCount++;
            }
        }
        // der letzte Count
        msgPerSlot.add(tmpCount);
        return msgPerSlot;
    }

    public Date calcNextSlot(int min, Date ogDate){
        final long ONE_MIN_IN_MS=60000;
        long curTimeMs = ogDate.getTime();
        // das alte Datum Objekt + den als parameter übergebenen Minuten * einer Minute in ms
        // ergibt das Neue Datum welches retouniert wird
        Date newDate = new Date(curTimeMs + (min * ONE_MIN_IN_MS));
        return newDate;
    }

}


