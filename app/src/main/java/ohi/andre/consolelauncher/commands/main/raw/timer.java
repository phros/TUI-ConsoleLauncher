package ohi.andre.consolelauncher.commands.main.raw;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.provider.AlarmClock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.tuils.Tuils;

public class timer implements CommandAbstraction {


    @Override
    public String exec(ExecutePack pack) throws Exception {
        String text = pack.getString();
        Context context = ((MainPack) pack).context;

        int hours = getHours(text);
        int minutes = getMinutes(text);
        int seconds = getSeconds(text);

        int time = hours * 3600 + minutes * 60 + seconds;

        Intent i = new Intent(AlarmClock.ACTION_SET_TIMER);
        i.putExtra(AlarmClock.EXTRA_LENGTH, time);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

        context.startActivity(i);

        return String.format("Timer set: %dh %dm %ds", hours, minutes, seconds);
    }

    @Override
    public int[] argType() {
        return new int[] {CommandAbstraction.PLAIN_TEXT};
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public int helpRes() {
        return R.string.help_timer;
    }

    @Override
    public String onArgNotFound(ExecutePack pack, int index) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        return ((MainPack) pack).context.getString(helpRes());
    }

    private static int getHours(String input){
        Pattern pattern = Pattern.compile("(\\d+)h|H");
        Matcher h = pattern.matcher(input);
        if(h.find()) {
            return Integer.parseInt(h.group(1));
        }

        return 0;
    }

    private static int getMinutes(String input){
        Pattern pattern = Pattern.compile("(\\d+)m|M");
        Matcher h = pattern.matcher(input);
        if(h.find()) {
            return Integer.parseInt(h.group(1));
        }

        return 0;
    }

    private static int getSeconds(String input){
        Pattern pattern = Pattern.compile("(\\d+)s|S");
        Matcher h = pattern.matcher(input);
        if(h.find()) {
            return Integer.parseInt(h.group(1));
        }

        return 0;
    }
}
