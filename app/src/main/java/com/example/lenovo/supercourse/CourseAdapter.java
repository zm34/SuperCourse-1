package com.example.lenovo.supercourse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {
    private Context context;
    private int resource;
    private List<Course> courses;

    public CourseAdapter(@NonNull Context context, int resource, List<Course> courses) {
        super(context, resource, courses);
        this.context = context;
        this.resource = resource;
        this.courses = courses;
        Log.i("zm3",courses.size()+"");
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Nullable
    @Override
    public Course getItem(int position) {
        return courses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView day1Tv = convertView.findViewById(R.id.day1Tv);
        TextView day2Tv = convertView.findViewById(R.id.day2Tv);
        TextView day3Tv = convertView.findViewById(R.id.day3Tv);
        TextView day4Tv = convertView.findViewById(R.id.day4Tv);
        TextView day5Tv = convertView.findViewById(R.id.day5Tv);
        TextView day6Tv = convertView.findViewById(R.id.day6Tv);
        TextView day7Tv = convertView.findViewById(R.id.day7Tv);
        day1Tv.setText(getItem(position).getMonday());
        day2Tv.setText(getItem(position).getTuesday());
        day3Tv.setText(getItem(position).getWednesday());
        day4Tv.setText(getItem(position).getThursday());
        day5Tv.setText(getItem(position).getFriday());
        day6Tv.setText(getItem(position).getSaturday());
        day7Tv.setText(getItem(position).getSunday());

        return convertView;
    }
}
