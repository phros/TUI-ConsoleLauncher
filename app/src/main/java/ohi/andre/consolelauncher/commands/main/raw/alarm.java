package ohi.andre.consolelauncher.commands.main.raw;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Pair;

import java.text.ParseException;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.commands.specific.ParamCommand;
import ohi.andre.consolelauncher.tuils.Tuils;


public class alarm extends ParamCommand {

    private static String alarmExtraMessage = "T-UI ALARM";

    private static void setAlarm(Context context, int hour, int minute){
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, alarm.alarmExtraMessage);
        i.putExtra(AlarmClock.EXTRA_HOUR, hour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(i);
    }

    private static void stopAlarm(Context context){
//        TODO: Use the alarm manager to stop the alarm without showing the gui.
        Intent i = new Intent(AlarmClock.ACTION_DISMISS_ALARM);
        i.putExtra(AlarmClock.ALARM_SEARCH_MODE_ALL, true);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, alarm.alarmExtraMessage);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(i);
    }

    private static Pair<Integer, Integer> getTime(String input) throws Exception {
        int hour;
        int minute;
        String[] parts;
        if (input.contains(":")){
            parts = input.split(":");
        } else {
            if (input.length() < 3){
                throw new IllegalArgumentException("[ERROR] Time needs at least 3 characters");
            }
            parts = new String[] {
                    input.substring(0, input.length()-2),
                    input.substring(input.length()-2, input.length())
            };
        }
        try {
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
        } catch (Exception e){
            throw new IllegalArgumentException("[ERROR] Cannot parse argument: " + input);
        }

        if ( hour < 0 || hour > 24){
            throw new IllegalArgumentException("[ERROR] Illegal hour: " + hour);
        }

        if ( minute < 0 || minute > 60){
            throw new IllegalArgumentException("[ERROR] Illegal minutes: " + minute);
        }

        return new Pair<>(hour, minute);
    }

    private enum Param implements ohi.andre.consolelauncher.commands.main.Param {

        in {
            @Override
            public int[] args() {
                return new int[]{CommandAbstraction.PLAIN_TEXT};
            }

            @Override
            public String exec(ExecutePack pack) {
                return "not supported yet";
            }
            @Override
            public String onNotArgEnough(ExecutePack pack, int n) {
                return "not supported yet";
//                return "-in XXXm | -in XXXh | -n XXXh XXXm";
            }
        },
        at {
            @Override
            public int[] args() {
                return new int[]{CommandAbstraction.PLAIN_TEXT};
            }

            @Override
            public String exec(ExecutePack pack) {
                Pair<Integer, Integer> time;
                try {
                    time = alarm.getTime(pack.getString());
                    setAlarm(pack.context, time.first, time.second);
                }catch (Exception e) {
                    return e.getMessage();
                }
                return String.format("Alarm set: %02d:%02d", time.first, time.second);
            }

            @Override
            public String onNotArgEnough(ExecutePack pack, int n) {
                return "-at hh:mm | -at hhmm";
            }
        },
        del {
            @Override
            public int[] args() {
                return new int[0];
            }

            @Override
            public String exec(ExecutePack pack) {
                stopAlarm(pack.context);
                return "delete everything";
            }

            @Override
            public String onNotArgEnough(ExecutePack pack, int n) {
                return "shouldn't happen";
            }
        };

        @Override
        public int[] args() {
            return new int[0];
        }

        static alarm.Param get(String p) {
            p = p.toLowerCase();
            alarm.Param[] ps = values();
            for (alarm.Param p1 : ps)
                if (p.endsWith(p1.label()))
                    return p1;
            return null;
        }

        static String[] labels() {
            alarm.Param[] ps = values();
            String[] ss = new String[ps.length];

            for(int count = 0; count < ps.length; count++) {
                ss[count] = ps[count].label();
            }

            return ss;
        }

        @Override
        public String label() {
            return Tuils.MINUS + name();
        }

        @Override
        public String onArgNotFound(ExecutePack pack, int index) {
            return pack.context.getString(R.string.help_alarm);
        }

    }

    @Override
    protected ohi.andre.consolelauncher.commands.main.Param paramForString(MainPack pack, String param) {
        return alarm.Param.get(param);
    }

    @Override
    protected String doThings(ExecutePack pack) {
        return null;
    }

    @Override
    public String[] params() {
        return alarm.Param.labels();
    }

    @Override
    public int priority() {
        return 4;
    }

    @Override
    public int helpRes() {
        return R.string.help_alarm;
    }
}
