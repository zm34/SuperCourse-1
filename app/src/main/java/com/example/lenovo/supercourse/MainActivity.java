package com.example.lenovo.supercourse;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button searchbt;
    private ImageButton findJSbt;
    private ListView SelectLV;
    private ListView Courselv;
    private EditText JSet;
    private List<JS> allJS=new ArrayList<JS>();
    private List<Course> courses=new ArrayList<Course>();
    private String[] course=null;
    private JSAdapter JSadapter;
    private CourseAdapter courseAdapter;
    private ImageView img;
    private Handler handler;
    private String inputText;
    private EditText YZMet;
    private Button YZMbt;
    private String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/supercourse/";
    private String YZM;
    private String selectedValue;
    private boolean m=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        }
//        JS js=new JS();
//        js.setJSname("zhaoman");
//        js.setValue("16");
//        allJS.add(js);
        searchbt=findViewById(R.id.searchbt);
        findJSbt=findViewById(R.id.findJSbt);
        SelectLV=findViewById(R.id.SeleteLV);
        Courselv=findViewById(R.id.Courselv);
        JSet=findViewById(R.id.JSet);
        img=findViewById(R.id.img);
        YZMbt=findViewById(R.id.YZMbt);
        YZMet=findViewById(R.id.YZMet);

        SelectLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedValue=allJS.get(position).getValue();
                    Log.i("zmtt",selectedValue);
                    JSet.setText(allJS.get(position).getJSname());
                    allJS.clear();
                    JSadapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        JSadapter=new JSAdapter(this,R.layout.js_layout,allJS);
        SelectLV.setAdapter(JSadapter);
        courseAdapter=new CourseAdapter(this,R.layout.course_layout,courses);
        Courselv.setAdapter(courseAdapter);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    JSadapter.notifyDataSetChanged();
                }
                else if(msg.what==2){
                    Bitmap bmp= BitmapFactory.decodeFile(path+"m.png");
                    img.setImageBitmap(bmp);
                }
                else if(msg.what==3){
                    courseAdapter.notifyDataSetChanged();
                }
            }
        };

    }

    public void findALLJS(View view){
        final String Filename="JS.txt";
        inputText=JSet.getText().toString();
        Log.i("zm4",inputText);
        new Thread() {
            @Override
            public void run() {
                try {
                    allJS.clear();
                    if(m=true) {
                        Fetcher fetcher = new Fetcher();
                        fetcher.getJS();
                        m=false;
                    }

                    File data=new File(path+Filename);
                    org.jsoup.nodes.Document doc= Jsoup.parse(data,"GB2312");
                    Element ele=doc.select("script").get(0);
                    System.out.println(ele.data());
                    String JS=ele.data();
                    org.jsoup.nodes.Document doct=Jsoup.parse(JS);
                    Elements eles=doct.select("option");
                    int len=eles.size();
                    System.out.println(len);
                    for (Element e: eles) {
                        if(e.text().contains(inputText)) {
                            JS js = new JS();
                            js.setJSname(e.text());
                            js.setValue(e.attr("value"));
                            allJS.add(js);
                        }
                    }

                    Log.i("zm2", allJS.size() + "");
                    handler.sendEmptyMessage(1);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getYZM(){
        new Thread(){
            @Override
            public void run() {
                try{
                    Fetcher fetcher=new Fetcher();
                    fetcher.getValidateCode();
                    handler.sendEmptyMessage(2);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void findCourse(View view) {
        getYZM();
    }

    public void imgChange(View view) {
        getYZM();
    }

    public void submitYZM(View view) {
        YZM=YZMet.getText().toString();
        Log.i("zmt",YZM);
        new Thread(){
            @Override
            public void run() {
                try{
                    courses.clear();
                    Fetcher fetcher=new Fetcher();
                    fetcher.getCourse(YZM,selectedValue);
                    Log.i("zmt",YZM);

                    course=fetcher.parseCourse();
                    System.out.println(course.length);
                    Course cs = null;
                    for(int k=0;k<course.length;k++){
                        switch (k%7){
                            case 0:{
                                cs=new Course();
                                cs.setMonday(course[k]);
                                break;}
                            case 1:cs.setTuesday(course[k]);
                                break;
                            case 2:cs.setWednesday(course[k]);
                                break;
                            case 3:cs.setThursday(course[k]);
                                break;
                            case 4:cs.setFriday(course[k]);
                                break;
                            case 5:cs.setSaturday(course[k]);
                                break;
                            case 6:{cs.setSunday(course[k]);
                                courses.add(cs);
                                break;}
                            default:
                                break;
                        }
                    }

                    handler.sendEmptyMessage(3);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
