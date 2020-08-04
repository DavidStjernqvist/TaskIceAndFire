import org.json.*;

import java.io.*;
import java.util.*;
import java.net.*;

public class ApiCall {
    public StringBuffer getContent(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if(con.getResponseCode() == HttpURLConnection.HTTP_OK){

                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String inputLine;
                StringBuffer content = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    content.append(inputLine);
                }
                br.close();

                return content;

            } else {
                System.out.println("Error");
                System.out.println("Server responded with: " + con.getResponseCode());
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

        }
        return null;
    }
    //Returns a jsonobject
    public JSONObject getObject(String urlString){
        StringBuffer content = getContent(urlString);
        JSONObject json = new JSONObject(content.toString());
        return json;
    }
    //Returns a jsonarray
    public JSONArray getArray(String urlString){
        StringBuffer content = getContent(urlString);
        JSONArray json = new JSONArray(content.toString());
        return json;
    }
}
