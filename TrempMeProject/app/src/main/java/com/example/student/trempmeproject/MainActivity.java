package com.example.student.trempmeproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    SQLiteHelper dbHelper;
    private final int RESULT_LOG_IN=0;
    private final int RESULT_SIGN_UP=1;
    private int USER_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDatabase();
        goToLogIn();
    }

    private void goToLogIn(){
        Intent intent=new Intent(this,LogIn.class);
        startActivityForResult(intent,0);
    }

    private void openDatabase(){
        dbHelper=new SQLiteHelper(this);
        db=dbHelper.getWritableDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            if (resultCode==RESULT_LOG_IN){

                int id=dbHelper.matchUsernameToPassword(db,data.getStringExtra("Username"),data.getStringExtra("Password"));
                if(id!=-1){
                    Toast.makeText(this,"Hellow "+ dbHelper.getRowUserName(dbHelper.getQueryById(db,id)),Toast.LENGTH_LONG).show();
                }
                else
                    goToLogIn();
            }
            if (resultCode==RESULT_SIGN_UP){
                Log.e("DEBAG","HERE");
                USER_ID=dbHelper.insert(db,data.getStringExtra("newUsername"),
                        Integer.parseInt(data.getStringExtra("newStringHasLicence")),
                        data.getStringExtra("newAddress"),
                        data.getStringExtra("newPassword"),
                        data.getByteArrayExtra("newImg"));
                showUser();

            }
        }
    }

    public void showUser(){
        Cursor user= dbHelper.getQueryById(db,USER_ID);
        Toast.makeText(this,"Username: "+dbHelper.getRowUserName(user)+
                "\n+Address: "+dbHelper.getRowAddress(user)+
                "\nHas Driving Licence: "+dbHelper.hasDrivingLicence(user),Toast.LENGTH_LONG).show();
    }
}
