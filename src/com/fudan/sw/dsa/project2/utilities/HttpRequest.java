package com.fudan.sw.dsa.project2.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class HttpRequest {
    /**
     * Send a GET request to the specified URL
     *
     * @param param The parameters of the sending request, with different parameters
     *              separated by symbol "&amp;"
     * @return URL The response given by the remote server
     */
    static String sendGet(String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = "http://api.map.baidu.com/place/v2/search?" + param;
            URL realUrl = new URL(urlNameString);
            // Opens and closed the URL connection
            URLConnection connection = realUrl.openConnection();
            // Set a universal request header
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // Establish the real connection
            connection.connect();
            // A BufferedReader InputStreamReader is used to read the request
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
                result = result.concat(line);
        } catch (Exception e) {
            System.out.println("An error occurred while sending GET request." + e);
            e.printStackTrace();
        }
        // finally block is used to close the stream
        finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}