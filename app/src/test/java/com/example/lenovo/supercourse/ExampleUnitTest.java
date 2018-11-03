package com.example.lenovo.supercourse;

import android.util.Log;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
//    public void getJS() throws Exception {
//        String referer="http://121.248.70.120/jwweb/ZNPK/TeacherKBFB.aspx";
//        String address="http://121.248.70.120/jwweb/ZNPK/Private/List_JS.aspx";
//        URL url=new URL(address);
//        HttpURLConnection con=(HttpURLConnection)url.openConnection();
//        con.setDoOutput(true);
//        con.setDoInput(true);
//        con.setRequestProperty("Referer", referer);
//        con.setRequestMethod("GET");
//        int len=con.getContentLength();
//        System.out.println(len);
//        byte[] buf=new byte[512];
//        InputStream input=con.getInputStream();
//        OutputStream out=new FileOutputStream("E:\\user\\appdata\\AndroidStudioProjects\\SuperCourse\\JS.txt");
//       // Log.i("zm","getJS");
//        while((len=input.read(buf))!=-1) {
//            out.write(buf, 0, len);
//            System.out.println(new String(buf,"GB2312"));
//            System.out.println("..........");
//        }
//        input.close();
//        out.close();
//
//    }

    public void getJS() throws Exception {
        String referer="http://121.248.70.120/jwweb/ZNPK/TeacherKBFB.aspx";
        String address="http://121.248.70.120/jwweb/ZNPK/Private/List_JS.aspx";
        URL url=new URL(address);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Referer", referer);
        con.setRequestMethod("GET");
        int len=con.getContentLength();
        System.out.println(len);
        byte[] buf=new byte[512];
        InputStream input=con.getInputStream();
//        OutputStream out=new FileOutputStream("E:\\user\\appdata\\AndroidStudioProjects\\SuperCourse\\JS.txt");
//        Log.i("zm","getJS");
        StringBuffer buffer=new StringBuffer();
        while((len=input.read(buf))!=-1) {
//            out.write(buf, 0, len);
            buffer.append(buf);
            System.out.println(new String(buf,"GB2312"));
            System.out.println("..........");
        }
        input.close();
//        out.close();
        System.out.print(buffer);
        //return builder;

    }

    @Test
    public void txstString(){
        String str = "string转为utf-8";

        try {
            str = new String(str.getBytes("gbk"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(str);
    }

    @Test
    public void getValidateCode() throws Exception {
        Properties conf=new Properties();
        String address="http://121.248.70.120/jwweb/sys/ValidateCode.aspx";
        URL url=new URL(address);
        URLConnection con=url.openConnection();
        con.setRequestProperty("Referer", "http://121.248.70.120/jwweb/ZNPK/TeacherKBFB.aspx");
        con.setDoInput(true);
        con.connect();
        String cookie=con.getHeaderField("Set-Cookie");
        System.out.println(cookie);
        conf.setProperty("cookie", cookie.split(";")[0]);
        conf.store(new FileOutputStream("conf.properties"), "comment");
        byte[] buf=new byte[512];
        InputStream input=con.getInputStream();
        OutputStream out=new FileOutputStream("m.png");
        int len=0;
        while((len=input.read(buf))!=-1) {
            out.write(buf, 0, len);
        }
        input.close();
        out.close();
    }
}

