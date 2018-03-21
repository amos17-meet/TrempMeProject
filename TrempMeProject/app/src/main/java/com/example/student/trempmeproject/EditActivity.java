package com.example.student.trempmeproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    SQLiteHelper dbHelper;
    SQLiteDatabase db;
    private int USER_ID;

    EditText userName, password, confirmPassword, address;
    Button takePhoto, submit, cancle,delete;
    ImageView imgPhoto;
    Bitmap bitmap;
    Boolean hasLicence;
    Boolean licenceChanged=false;
    boolean imgChanged=false;
    RadioGroup licence;
    RadioButton rbYes, rbNo;

    final int RESULT_DELETE=-2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        openDatabase();
        setUSER_ID();
        onClickedRadioGroup();
        onTakePhotoClicked();
        onSubmitClicked();
        onCancleClicked();
        onDeleteClicked();
    }

    private void openDatabase(){
        dbHelper=new SQLiteHelper(this);
        db=dbHelper.getWritableDatabase();
    }

    public void setUSER_ID(){
        Intent intent = getIntent();
        USER_ID=Integer.parseInt(intent.getStringExtra("id"));
        Log.e("DEBAG","intent id"+Integer.parseInt(intent.getStringExtra("id"))+"");
        Log.e("DEBAG","user id"+USER_ID+"");
        setRadioButton();
        setDetails();
    }
    public void setDetails(){
        Cursor c=dbHelper.getQueryById(db,USER_ID);
        userName=(EditText)findViewById(R.id.et_user_name);
        address=(EditText)findViewById(R.id.et_address);
        imgPhoto=(ImageView)findViewById(R.id.img_photo);

        bitmap=BitmapHelper.getImage(dbHelper.getRowImage(c));
        imgPhoto.setImageBitmap(bitmap);
        userName.setHint(dbHelper.getRowUserName(c));
        address.setHint(dbHelper.getRowAddress(c));

    }

    public void onTakePhotoClicked(){
        takePhoto=(Button)findViewById(R.id.btn_take_photo);
        takePhoto.setOnClickListener(this);
    }

    public void onSubmitClicked(){
        submit=(Button)findViewById(R.id.btn_submit);
        submit.setOnClickListener(this);
    }

    public void onCancleClicked(){
        cancle=(Button)findViewById(R.id.btn_cancle);
        cancle.setOnClickListener(this);
    }

    public void onDeleteClicked(){
        delete=(Button)findViewById(R.id.btn_delete);
        delete.setOnClickListener(this);
    }

    public void setRadioButton(){
        rbNo=(RadioButton)findViewById(R.id.rb_no);
        rbYes=(RadioButton)findViewById(R.id.rb_yes);
        if(USER_ID!=-1) {
            Cursor c = dbHelper.getQueryById(db, USER_ID);
            if (dbHelper.hasDrivingLicence(c) == 1) {
                rbYes.setChecked(true);
                hasLicence = true;
            } else {
                rbNo.setChecked(true);
                hasLicence = false;
            }
        }else
            setUSER_ID();
    }

    public void onClickedRadioGroup(){
        licence=(RadioGroup)findViewById(R.id.rg_licence);
        licence.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                licenceChanged=true;
                switch (i){
                    case R.id.rb_yes:
                        hasLicence=true;
                        break;
                    case R.id.rb_no:
                        hasLicence=false;
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==takePhoto){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,0);
        }
        if(view==submit){

            userName=(EditText)findViewById(R.id.et_user_name);
            password=(EditText)findViewById(R.id.et_password);
            confirmPassword=(EditText)findViewById(R.id.et_confirm_password);
            address=(EditText)findViewById(R.id.et_address);
            imgPhoto=(ImageView)findViewById(R.id.img_photo);

            String newUsername=userName.getText().toString();
            String newPassword=password.getText().toString();
            String newConfirmPassword=confirmPassword.getText().toString();
            String newAddress=address.getText().toString();

            if(!newUsername.equals("")){
                dbHelper.setUserNameColumn(db,newUsername,USER_ID);
            }
            if(!newPassword.equals("")){
                if(newPassword.equals(newConfirmPassword)){
                    dbHelper.setPasswordColumn(db,newPassword,USER_ID);
                }else{
                    Toast.makeText(this, "Different Passwords Entered", Toast.LENGTH_SHORT).show();
                }
            }
            if(!newAddress.equals("")){
                dbHelper.setAddressColumn(db,newAddress,USER_ID);
            }

            if(imgChanged){
                dbHelper.setImgColumn(db,BitmapHelper.getBytes(bitmap),USER_ID);
            }
            if(licenceChanged){
                if(hasLicence)
                    dbHelper.setDrivingLicenceColumn(db,1,USER_ID);
                else
                    dbHelper.setDrivingLicenceColumn(db,0,USER_ID);
            }
            setResult(RESULT_OK);
            finish();


        }
        if(view==cancle){
            setResult(RESULT_CANCELED);
            finish();
        }
        if(view==delete){
            Log.e("DEBAG","delete");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setMessage("Are You Sure You Want To Delete You'r Account");

            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            dbHelper.deleteUser(db,USER_ID);
                            setResult(RESULT_DELETE);
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0);
        {
            if(resultCode==RESULT_OK)
            {
                bitmap = (Bitmap)data.getExtras().get("data");
                if(bitmap!=null)
                    imgPhoto=(ImageView)findViewById(R.id.img_photo);
                    imgChanged=true;
                imgPhoto.setImageBitmap(bitmap);

            }
        }
    }



}
