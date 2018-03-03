package com.example.student.trempmeproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
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

import static java.security.AccessController.getContext;


public class SignUp extends AppCompatActivity implements View.OnClickListener {
    EditText userName, password, confirnPassword, address;
    Button takePhoto, submit, cancle;
    ImageView imgPhoto;
    //Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(), R.mipmap.user_base_img);
    Bitmap bitmap;
    Boolean hasLicence;
    RadioGroup licence;
    Boolean hasLisence=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        onClickedRadioGroup();
        onTakePhotoClicked();
        onSubmitClicked();
        onCancleClicked();




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

    public void onClickedRadioGroup(){
        licence=(RadioGroup)findViewById(R.id.rg_licence);
        licence.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();

                if(isChecked){
                    if(checkedRadioButton.getText().toString().compareTo("YES")==0)
                        hasLicence=true;

                    else
                        hasLicence=false;
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
            confirnPassword=(EditText)findViewById(R.id.et_confirn_password);
            address=(EditText)findViewById(R.id.et_address);
            imgPhoto=(ImageView)findViewById(R.id.img_photo);

            String newUsername=userName.getText().toString();
            String newPassword=password.getText().toString();
            String newConfirnPassword=confirnPassword.getText().toString();
            String newAddress=address.getText().toString();
            if(newUsername.compareTo("")!=0&&newAddress.compareTo("")!=0&&newPassword.compareTo("")!=0&&newConfirnPassword.compareTo("")!=0) {
                String newStringHasLicence;
                if (hasLisence)
                    newStringHasLicence = "1";
                else
                    newStringHasLicence = "0";
                byte[] newImg = BitmapHelper.getBytes(bitmap);

                if (newPassword.compareTo(newConfirnPassword) == 0) {
                    Intent intent = new Intent();

                    intent.putExtra("newUsername", newUsername);
                    intent.putExtra("newPassword", newPassword);
                    intent.putExtra("newAddress", newAddress);
                    intent.putExtra("newStringHasLicence", newStringHasLicence);
                    intent.putExtra("newImg", newImg);
                    setResult(RESULT_OK, intent);
                    finish();
                } else
                    Toast.makeText(this, "Different Passwords Entered", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this,"Pleas Enter All Parameters",Toast.LENGTH_LONG).show();
        }
        if(view==cancle){
            setResult(RESULT_CANCELED);
            finish();
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
                    imgPhoto.setImageBitmap(bitmap);
            }
        }
    }
}
