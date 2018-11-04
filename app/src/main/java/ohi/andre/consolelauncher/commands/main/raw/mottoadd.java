package ohi.andre.consolelauncher.commands.main.raw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;

public class mottoadd implements CommandAbstraction {
    @Override
    public String exec(ExecutePack pack) throws Exception {
        String motto = pack.getString();
        String result = addMotto(motto);
        return String.format("Trying to add: %s\n--> %s", motto, result);
    }

    @Override
    public int[] argType() {
        return new int[] {CommandAbstraction.PLAIN_TEXT};
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public int helpRes() {
        return R.string.help_mottoadd;
    }

    @Override
    public String onArgNotFound(ExecutePack pack, int indexNotFound) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        return null;
    }

    private static String addMotto(String mottoText) throws Exception{
        String data = getDataToSend(mottoText);
        byte[] postDataBytes = data.getBytes("UTF-8");
        URL url = new URL("https://docs.google.com/forms/d/e/1FAIpQLSe470bjTjHoSAcEiZFY-MOft5jux3G6wkQ0oukWm0S1FXH1dw/formResponse");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataBytes.length ));

        conn.getOutputStream().write( postDataBytes );
        conn.getOutputStream().flush();
        conn.getOutputStream().close();

        // Get the server response
        return String.format("%d %s", conn.getResponseCode(), conn.getResponseMessage());
    }

    private static String getDataToSend(String mottoText)throws Exception{
        //        'entry.1440409975_day': 1, 'entry.1440409975_month': 1, 'entry.1440409975_year': 2018, 'entry.804156229': 'test'
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String data = URLEncoder.encode("entry.1440409975_day", "UTF-8")
                + "=" + URLEncoder.encode(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), "UTF-8");

        data += "&" + URLEncoder.encode("entry.1440409975_month", "UTF-8") + "="
                + URLEncoder.encode(String.valueOf(cal.get(Calendar.MONTH)), "UTF-8");

        data += "&" + URLEncoder.encode("entry.1440409975_year", "UTF-8")
                + "=" + URLEncoder.encode(String.valueOf(cal.get(Calendar.YEAR)), "UTF-8");

        data += "&" + URLEncoder.encode("entry.804156229", "UTF-8")
                + "=" + URLEncoder.encode(new String(mottoText), "UTF-8");

        return data;
    }
}
