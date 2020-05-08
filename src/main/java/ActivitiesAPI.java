import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivitiesAPI {

    static String clientid = "45141";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://www.strava.com/api/v3/activities/" + id;
    private static final String AUTH_URL =
            "https://www.strava.com/oauth/token?client_id=45141&client_secret=954f9536bd040cf15a20e187b40f7c563e6a1228&code=" + code + "&grant_type=authorization_code";
//    + "?include_all_efforts=";
//    private static final String GET_URL = "https://swapi.co/api/people/1/";

    public static void main(String[] args) throws IOException {

    //    accessToken();
    //         getAthlete();
        getActivities();
  //      sendGET();
        System.out.println("GET DONE");
    }

    public static void accessToken() throws IOException
    {
        URL accessTokenUrl = new URL(ACCESS_TOKEN_URL);
        HttpURLConnection accessCon = (HttpURLConnection) accessTokenUrl.openConnection();
        accessCon.setRequestProperty("Content-Type","application/json");
        accessCon.setRequestMethod("GET");
        accessCon.setRequestProperty("Authorization", "Bearer e14cc43f89e02319c875e5918ed1bdb65760047d");
        int responseCode = accessCon.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            StringBuffer response = deserialiseJSON(accessCon);
            System.out.println(response);
        }
    }

    private static String getAthlete() throws IOException {
        URL accessTokenUrl = new URL(ACCESS_TOKEN_URL);
        HttpURLConnection accessCon = (HttpURLConnection) accessTokenUrl.openConnection();
        accessCon.setRequestProperty("Content-Type", "application/json");
        accessCon.setRequestMethod("GET");
        accessCon.setRequestProperty("Authorization", "Bearer e14cc43f89e02319c875e5918ed1bdb65760047d");
        int responseCode = accessCon.getResponseCode();
        //     System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuffer response = deserialiseJSON(accessCon);
            System.out.println(response);
//        URL auth = new URL(AUTH_URL);
//
//        HttpURLConnection authCon = (HttpURLConnection) auth.openConnection();
//        authCon.setRequestProperty("Content-Type","application/json");
//        authCon.setRequestMethod("POST");
//        authCon.setRequestProperty("User-Agent", USER_AGENT);
//
//        int responseCode = authCon.getResponseCode();
//        System.out.println("GET Response Code :: " + responseCode);
//
//        if (responseCode == HttpURLConnection.HTTP_OK)
//        {
//            StringBuffer response = deserialiseJSON(authCon);
//            System.out.println(response);

            String[] jsonString = response.toString().split(","); //assign your JSON String here
            String refresh_token = jsonString[3].split(":")[1].replace("\"", "");
            String access_token = jsonString[4].split(":")[1].replace("\"", "");

            accessCon.disconnect();

            System.out.println(access_token);
            return access_token;
        } else {
            return null;
        }
    }

        public static void getActivities() throws IOException
        {
            String access_token = getAthlete();

            URL obj = new URL(GET_URL + "?access_token=" + "e14cc43f89e02319c875e5918ed1bdb65760047d");
            //https://www.strava.com/oauth/authorize?client_id=45141&response_type=code&redirect_uri=http://localhost/exchange_token&approval_prompt=force&scope=activity:read
// http://localhost/exchange_token?state=&code=dad8aa6a91eefc387deac7fefa166d1fa0016c44&scope=read,activity:read_all
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Authorization", "Bearer e14cc43f89e02319c875e5918ed1bdb65760047d");
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            int i = 0;

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String responseGet = deserialiseJSON(con).toString();
                JsonArray jsonObject = new JsonParser().parse(responseGet).getAsJsonObject().getAsJsonArray("laps");

                while (i < (jsonObject.size())) {
                    String elapsedTimeString = jsonObject.get(i).getAsJsonObject().get("elapsed_time").toString();
                    double elapsedTime = Double.parseDouble(elapsedTimeString);

                    String distanceString = jsonObject.get(i).getAsJsonObject().get("distance").toString();
                    double distance = Double.parseDouble(distanceString);

                    double lapSpeedMetresPerSecond = distance / elapsedTime;

                    System.out.println(elapsedTime + "," + distance + "," + lapSpeedMetresPerSecond);
                    i++;
                }

            } else {
                System.out.println("GET request not worked");
            }
        }

    public static StringBuffer deserialiseJSON(HttpURLConnection connection) throws IOException
    {
        { // success 
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response;
        }
    }
}
