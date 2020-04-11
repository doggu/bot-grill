package events.commands;

import events.MessageListener;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerListener extends MessageListener {
    @Override
    public void onCommand() {
        String[] time = new String[args.length-4];
        System.arraycopy(args, 4, time, 0, args.length-4);

        long t = 0;
        int i = 0;
        try {          //second arg
            for (i=0; i+1<time.length; i+=2) {
                double n = Double.parseDouble(time[i]);
                //convert passed number to ms
                switch (time[i+1]) {
                    case "millenium":
                    case "millenia":
                        n *= 10;
                    case "century":
                    case "centuries":
                        n *= 10;
                    case "decade":
                    case "decades":
                        n *= 10;
                    case "y":
                    case "yr":
                    case "yrs":
                    case "year":
                    case "years":
                        n *= 365.0/30;
                    case "m":
                    case "mo":
                    case "mos":
                    case "month":
                    case "months":
                        n *= 30.0/7;
                    case "w":
                    case "wk":
                    case "wks":
                    case "week":
                    case "weeks":
                        n *= 7;
                    case "d":
                    case "day":
                    case "days":
                        n *= 24;
                    case "h":
                    case "hr":
                    case "hrs":
                    case "hour":
                    case "hours":
                        n *= 60;
                    case "minute":
                    case "minutes":
                        n *= 60;
                    case "s":
                    case "second":
                    case "seconds":
                        n *= 1000;
                    case "ms":
                    case "millisecond":
                    case "milliseconds":
                        t += n;
                        break;
                    default:
                        System.out.println("waut");
                }
            }
        } catch (IndexOutOfBoundsException ioobe) {
            sendMessage(ioobe.getMessage());
            return;
        } catch (NumberFormatException nfe) {
            sendMessage("couldn't read number: "+time[i]);
            return;
        }

        if (t==0) {
            sendMessage("ding ding motherfucker");
            return;
        }

        Timer f = new Timer(e.getAuthor().toString());
        f.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage("ding ding ding");
            }
        }, new Date(System.currentTimeMillis()+t));

        double s = t/1000.0%60;
        t /= 1000;
        t /= 60;
        int m = (int)(t%60);
        t /= 60;
        int h = (int)(t%24);
        t /= 24;
        int d = (int)(t%365);
        t /= 365;
        // t = years

        String date_str =
                (t>0 ? " " + t + " year" + (t>1 ? "s" : "") : "")+
                (d>0 ? " " + d + " day" + (d>1 ? "s" : "") : "")+
                (h>0 ? " " + h + " hour" + (h>1 ? "s" : "") : "")+
                (m>0 ? " " + m + " minute" + (m>1 ? "s" : "") : "")+
                (s>0 ? " " + s + " second" + (s!=1 ? "s" : "") : "");

        sendMessage("timer set for" + date_str + " from now.");
    }

    @Override
    public boolean isCommand() {
        return e.getMessage().getContentRaw().startsWith("set a timer for ");
    }

    @Override
    protected char getPrefix() {
        return 's';
    }
}
