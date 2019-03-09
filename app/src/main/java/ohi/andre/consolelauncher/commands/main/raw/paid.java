package ohi.andre.consolelauncher.commands.main.raw;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.commands.main.specific.ParamCommand;
import ohi.andre.consolelauncher.tuils.Tuils;

public class paid extends ParamCommand {

    /**
     * Sends the given data to google forms
     * @param postDataBytes
     */
    private static String addData(byte[] postDataBytes){
        try {

            URL url = new URL("https://docs.google.com/forms/d/e/1FAIpQLSfi6WZx7Rh-QEKE3Dq-b4eRmnMaqOiXffyMEkN5F3OoqRi9qw/formResponse");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataBytes.length));

            conn.getOutputStream().write(postDataBytes);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            return String.format("%d %s", conn.getResponseCode(), conn.getResponseMessage());
        } catch (Exception e){
            return e.toString();
        }
    }

    private static String processInput(String tag, ExecutePack pack){
        String valueStr = pack.getString();
        try{
            double value = Double.parseDouble(valueStr);
        }catch (NumberFormatException e){
            return String.format("Illegal number: %s", valueStr);
        }
        try{
            byte[] bytes = getDataToSend(tag, valueStr);
            return paid.addData(bytes);
        } catch (Exception e){
            return String.format("Cannot parse input: %s %s", tag, valueStr);
        }
    }

    private enum Param implements ohi.andre.consolelauncher.commands.main.Param {


        essen {
            @Override
            public int[] args() {
                return new int[]{CommandAbstraction.PLAIN_TEXT};
            }

            public String formsTag() {
                return "Essen";
            }

            @Override
            public String exec(ExecutePack pack) {
                return paid.processInput(formsTag(), pack);
            }
        },
        alk {
            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.PLAIN_TEXT};
            }

            public String formsTag(){
                return "Alkohol";
            }

            @Override
            public String exec(ExecutePack pack) {
                return paid.processInput(formsTag(), pack);
            }
        },
        sport {
            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.PLAIN_TEXT};
            }

            public String formsTag(){
                return "Sport";
            }

            @Override
            public String exec(ExecutePack pack) {
                return paid.processInput(formsTag(), pack);
            }
        },
        tanken {
            @Override
            public int[] args() {
                return new int[] {CommandAbstraction.PLAIN_TEXT};
            }

            public String formsTag(){
                return "Tanken";
            }

            @Override
            public String exec(ExecutePack pack) {
                return paid.processInput(formsTag(), pack);
            }
        };;

        @Override
        public int[] args() {
            return new int[0];
        }

        static Param get(String p) {
            p = p.toLowerCase();
            Param[] ps = values();
            for (Param p1 : ps)
                if (p.endsWith(p1.label()))
                    return p1;
            return null;
        }

        static String[] labels() {
            Param[] ps = values();
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
            return pack.context.getString(R.string.help_notes);
        }

        @Override
        public String onNotArgEnough(ExecutePack pack, int n) {
            return pack.context.getString(R.string.help_notes);
        }
    }

    private static byte[] getDataToSend(String tag, String amount) throws Exception{
        // entry.411231019=Tanken&entry.281167778=1&fvv=1&draftResponse=%5Bnull%2Cnull%2C%22-2384832379776292796%22%5D%0D%0A&pageHistory=0&fbzx=-2384832379776292796
        String data = URLEncoder.encode("entry.411231019", "UTF-8")
                + "=" + URLEncoder.encode(tag, "UTF-8");

        data += "&" + URLEncoder.encode("entry.281167778", "UTF-8") + "="
                + URLEncoder.encode(amount, "UTF-8");

        byte[] bytes = data.getBytes("UTF-8");
        return bytes;
    }


    @Override
    protected ohi.andre.consolelauncher.commands.main.Param paramForString(MainPack pack, String param) {
        return Param.get(param);
    }

    @Override
    protected String doThings(ExecutePack pack) {
        return null;
    }

    @Override
    public String[] params() {
        return Param.labels();
    }

    @Override
    public int priority() {
        return 4;
    }

    @Override
    public int helpRes() {
        return R.string.help_notes;
    }

}
