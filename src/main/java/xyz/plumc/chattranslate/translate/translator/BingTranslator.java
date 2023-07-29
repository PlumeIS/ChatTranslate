package xyz.plumc.chattranslate.translate.translator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import xyz.plumc.chattranslate.ChatTranslate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BingTranslator extends Translator {
    public final String INFO_URL = "https://cn.bing.com/search?q=\"translate\"";
    public final String TRANSLATE_URL = "https://cn.bing.com/ttranslatev3?&IG=%s&IID=%s";

    public Pattern IGPattern = Pattern.compile("_IG=\"(.*?)\"");
    public Pattern IIDPattern = Pattern.compile("_iid=\"(.*?)\"");
    public Pattern tokenDataPattern = Pattern.compile("params_AbusePreventionHelper = \\[(.*?)\\];");

    public Map<String, String> headers = new HashMap<>();
    public Map<String, String> cookies = new HashMap<>();

    private String IG;
    private String IID;
    private String key;
    private String token;
    private int maxAge;

    private long updateTime;


    public BingTranslator() {
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36 Edg/115.0.1901.183");
        updateToken();
    }

    @Override
    public String translate(String text, String langFrom, String langTo) {
        if (isOverAge()) updateToken();
        Map<String, String> translateData = new HashMap<>();
        translateData.put("fromLang", langFrom);
        translateData.put("text", text);
        translateData.put("to", langTo);
        translateData.put("token", token);
        translateData.put("key", key);
        translateData.put("tryFetchingGenderDebiasedTranslations", "true");
        String response = sendPostRequest(TRANSLATE_URL.formatted(IG, IID), headers, cookies, translateData);
        return readTranslated(response);
    }

    public void updateToken() {
        ChatTranslate.LOGGER.info("");
        cookies.put("MUID", generateRandomToken());
        String infoPage = sendGetRequest(INFO_URL, headers, cookies);

        Matcher IGMatcher = IGPattern.matcher(infoPage);
        IGMatcher.find();
        IG = IGMatcher.group(0);
        Matcher IIDMatcher = IIDPattern.matcher(infoPage);
        IIDMatcher.find();
        IID = IIDMatcher.group(0);
        Matcher tokenDataMatcher = tokenDataPattern.matcher(infoPage);
        tokenDataMatcher.find();
        String tokenDataString = tokenDataMatcher.group(0);

        tokenDataString = tokenDataString.substring(32, tokenDataString.length() - 2);
        String[] tokenData = tokenDataString.split(",");
        key = tokenData[0];
        token = tokenData[1].substring(1, tokenData[1].length() - 1);
        maxAge = Integer.parseInt(tokenData[2]);
        updateTime = System.currentTimeMillis();
    }

    private static String generateRandomToken() {
        Random random = new Random();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private boolean isOverAge() {
        return System.currentTimeMillis() - updateTime > maxAge;
    }

    private static String readTranslated(String response) {
        Gson gson = new Gson();
        JsonArray jsonTranslated = gson.fromJson(response, JsonArray.class);
        return jsonTranslated.get(0).getAsJsonObject().getAsJsonArray("translations").get(0).getAsJsonObject().get("text").getAsString();
    }

    private static String sendGetRequest(String url, Map<String, String> headers, Map<String, String> cookies) {
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                connection.addRequestProperty("Cookie", entry.getKey() + "=" + entry.getValue());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String sendPostRequest(String url, Map<String, String> headers, Map<String, String> cookies, Map<String, String> data) {
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                connection.addRequestProperty("Cookie", entry.getKey() + "=" + entry.getValue());
            }

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(entry.getKey());
                postData.append('=');
                postData.append(entry.getValue());
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
            connection.getOutputStream().write(postDataBytes);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}