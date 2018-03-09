package com.example.student.trempmeproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class UserAdapter extends ArrayAdapter<UserObject> {
    Context context;
    List<UserObject> users;

    public UserAdapter(Context context, int resource, int textViewResourceId, List<UserObject> users) {
        super(context, resource, textViewResourceId, users);
        this.context=context;
        this.users =users;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_user_object, parent, false);
        TextView tvId = (TextView) view.findViewById(R.id.tv_id);
        TextView tvUsername = (TextView) view.findViewById(R.id.tv_username);
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
        TextView tvDrivingLicence = (TextView) view.findViewById(R.id.tv_driving_licence);
        ImageView ivImg = (ImageView) view.findViewById(R.id.iv_img);
        UserObject user = users.get(position);
        tvId.setText(user.getId()+"");
        ivImg.setImageBitmap(user.getUserImg());
        tvUsername.setText(user.getUsername());
        tvAddress.setText(user.getAddress());
        String drivingLicence;
        if(user.getDrivingLicence()==1)
            drivingLicence="Has Driving Licence";
        else
            drivingLicence="Dosen't have Driving Licence";
        tvDrivingLicence.setText(drivingLicence);



        return view;
    }

    }

