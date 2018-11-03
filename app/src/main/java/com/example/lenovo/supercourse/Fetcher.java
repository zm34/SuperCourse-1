package com.example.lenovo.supercourse;

import android.os.Environment;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Fetcher {
    private List<JS> allJS=new ArrayList<JS>();
    private static String Filename="JS.txt";
    private static String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/supercourse/";
    private static Properties conf=new Properties();

    public void getJS() throws Exception {
        String referer="http://121.248.70.120/jwweb/ZNPK/TeacherKBFB.aspx";
        String address="http://121.248.70.120/jwweb/ZNPK/Private/List_JS.aspx";
        URL url=new URL(address);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        //con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Referer", referer);
        con.setRequestMethod("GET");
        InputStream input=con.getInputStream();
        int len=con.getContentLength();
        System.out.println(len);
        byte[] buf=new byte[512];
        OutputStream out=new FileOutputStream(path+Filename);
        Log.i("zm","getJS");
//        StringBuffer buffer=new StringBuffer();
//        String JSname=new String();
        while((len=input.read(buf))!=-1) {
            out.write(buf, 0, len);
//            JSname=JSname+new String(buf,"GB2312");
            System.out.println(new String(buf,"GB2312"));
            System.out.println("..........");
        }
        input.close();
//        out.close();

    }

    public List<JS> parseJS() throws Exception{
//        String data=getData();
        File data=new File(path+Filename);
        org.jsoup.nodes.Document doc= Jsoup.parse(data,"GB2312");
        Element ele=doc.select("script").get(0);
        System.out.println(ele.data());
        String JS=ele.data();
        org.jsoup.nodes.Document doct=Jsoup.parse(JS);
        Elements eles=doct.select("option");
        int len=eles.size();
        System.out.println(len);
//        String allJS[]=new String[len];
//        String allJSValue[]=new String[len];
//        int k=0;
        for (Element e: eles) {
            JS js=new JS();
            String jsname=new String(e.text().getBytes("gbk"),"UTF-8");
            String value=new String(e.attr("value").getBytes("gbk"),"UTF-8");
            js.setJSname(jsname);
            js.setValue(value);
            allJS.add(js);
//            allJS[k]=e.text();
//            allJSValue[k]=e.attr("value");
//            k++;
//			    System.out.println(e.attr("value"));
//			    System.out.println(e.text());
        }
//        for(int m=0;m<len;m++) {
//            System.out.println(allJS[m]);
//            System.out.println(allJSValue[m]);
//        }

//        Log.i("zm",allJS);
        System.out.println(allJS);
        return allJS;
    }

    public void getValidateCode() throws Exception {
        String address="http://121.248.70.120/jwweb/sys/ValidateCode.aspx";
        URL url=new URL(address);
        URLConnection con=url.openConnection();
        con.setRequestProperty("Referer", "http://121.248.70.120/jwweb/ZNPK/TeacherKBFB.aspx");
        con.setDoInput(true);
        con.connect();
        String cookie=con.getHeaderField("Set-Cookie");
        System.out.println(cookie);
        conf.setProperty("cookie", cookie.split(";")[0]);
        conf.store(new FileOutputStream(path+"conf.properties"), "comment");
        byte[] buf=new byte[512];
        InputStream input=con.getInputStream();
        OutputStream out=new FileOutputStream(path+"m.png");
        Log.i("zmm","getImg");
        int len=0;
        while((len=input.read(buf))!=-1) {
            out.write(buf, 0, len);
        }
        input.close();
        out.close();
    }

    public void getCourse(String YZM,String selectedValue) throws Exception {

        conf.load(new FileInputStream(path+"conf.properties"));
        String cookie=conf.getProperty("cookie");
        String referer="http://121.248.70.120/jwweb/ZNPK/TeacherKBFB.aspx";
        String address="http://121.248.70.120/jwweb/ZNPK/TeacherKBFB_rpt.aspx";
        URL url=new URL(address);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Cookie", cookie);
        con.setRequestProperty("Referer", referer);
        con.setRequestMethod("POST");
        StringBuilder builder=new StringBuilder();
        builder.append("Sel_XNXQ=20180&Sel_JS="+selectedValue+"&type=1&txt_yzm="+YZM);
        OutputStream output=con.getOutputStream();
        output.write(builder.toString().getBytes());
        int len=con.getContentLength();
        System.out.println(len);
        byte[] buf=new byte[512];
        InputStream input=con.getInputStream();
        OutputStream out=new FileOutputStream(path+"Course.txt");
        while((len=input.read(buf))!=-1) {
            out.write(buf, 0, len);
            System.out.println(new String(buf,"GB2312"));
            System.out.println("..........");
        }
        input.close();
        out.close();
    }

    public String[] parseCourse() throws Exception {
        File data=new File(path+"Course.txt");
        org.jsoup.nodes.Document doc=Jsoup.parse(data,"GB2312");
        Elements eles=doc.select("table");
        Elements trs=eles.get(3).select("tr");
        String course[]=new String[42];
        int c=0;
        for(int i=0;i<trs.size();i++) {
            Elements tds=trs.get(i).select("td");
            if(i==1||i==3) {
                for(int j=2;j<tds.size();j++) {
                    if(tds.get(j).text()==null) {
                        course[c]=null;
                        c++;
                    }
                    course[c]=tds.get(j).text();
                    c++;
                }
            }
            else{
                for(int j=1;j<tds.size();j++) {
                    if(tds.get(j).text()==null) {
                        course[c]=null;
                        c++;
                    }
                    course[c]=tds.get(j).text();
                    c++;
                }
            }
        }
        return course;
    }
}
