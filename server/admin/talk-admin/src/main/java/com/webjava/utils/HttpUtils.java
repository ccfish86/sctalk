package com.webjava.utils;


import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Jerry on 2017/10/15.
 */
public class HttpUtils {

    public static void setJsonBody(HttpServletResponse response,ResponseInfo info)
    {
        Gson gson = new Gson();
        String bodyStr = gson.toJson(info);
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(bodyStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static String getJsonBody(HttpServletRequest request)
    {
        BufferedReader reader = null;
        try {
            reader = request.getReader();

            StringBuffer buffer = new StringBuffer();
            String string;
            while ((string = reader.readLine()) != null) {
                buffer.append(string);
            }
            reader.close();
            return  buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return  "";
        }
    }
}
