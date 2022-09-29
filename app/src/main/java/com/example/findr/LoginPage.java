package com.example.findr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class LoginPage extends AppCompatActivity {
    private static final String TAG = "LoginPage";

    EditText loginUsername;
    EditText loginPassword;

    Button signInBtn;

    ArrayList<UserInfo> userInfoList = new ArrayList<>();
    //UserDataBaseHelper dbHelper;
    FirebaseUserHelper fb = new FirebaseUserHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginUsername = findViewById(R.id.loginUserName);
        loginPassword = findViewById(R.id.loginPassword);

        signInBtn = findViewById(R.id.loginBtn);

        //dbHelper = new UserDataBaseHelper(LoginPage.this, "user.db", null, 1);

        /*signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfo = findUser(loginUsername);
                if(userInfo== null){
                    Toast t = Toast.makeText(LoginPage.this, userInfoList.toString(),Toast.LENGTH_LONG);
                    t.show();
                }
                else{
                    if(verifyPassword(userInfo,loginPassword)){
                        // TODO: 12/11/2021 navigate successful login
                        Toast t = Toast.makeText(LoginPage.this, "Successfully logged in!", Toast.LENGTH_LONG);
                        t.show();
                        Intent intent = new Intent(LoginPage.this, ProfilePage.class);
                        String username = loginUsername.getText().toString();
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                    else{
                        Toast t = Toast.makeText(LoginPage.this,"Login failed!, password is incorrect!", Toast.LENGTH_LONG);
                        t.show();
                    }
                }
            }
        });*/

        fb.getUserDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                signInBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean userFound = false;
                        for(DataSnapshot d : snapshot.getChildren()){
                            UserInfo fbUser = d.getValue(UserInfo.class);
                            if(fbUser.getUserName().equalsIgnoreCase(loginUsername.getText().toString())){
                                //User found
                                userFound = true;
                                if(verifyPassword(fbUser,loginPassword)){
                                    //Verified
                                    Toast.makeText(LoginPage.this, "Successfully logged in!",Toast.LENGTH_SHORT).show();
                                    String userKey = d.getKey();

                                    Intent intent = new Intent(LoginPage.this,ProfilePage.class);
                                    intent.putExtra("fbUserId", userKey);
                                    startActivity(intent);
                                    break;
                                }
                                else{
                                    //Wrong password
                                    Toast.makeText(LoginPage.this,"Login failed!, Password is incorrect!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if(!userFound){
                            //User not found
                            Toast.makeText(LoginPage.this,"User not found in Database",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public UserInfo findUser(EditText loginUsername){
        String userNameStr = loginUsername.getText().toString();
        for(UserInfo u: userInfoList){
            if(u.getUserName().equals(userNameStr)){
                return u;
            }
        }
        return null;
    }
    public boolean verifyPassword(UserInfo userInfo,EditText password){
        SHAHelper shaHelper;
        try {
            shaHelper = new SHAHelper(password.getText().toString(),userInfo.getSalt());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        String passwordStr = shaHelper.getHashedPassword();
        return userInfo.getPassword().equals(passwordStr);
    }

    private void loadUserData(FirebaseUserCallback firebaseUserCallback) {

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfoList.removeAll(userInfoList);
                for(DataSnapshot d : snapshot.getChildren()){
                    Log.d(TAG, "onDataChange: loadUserData called");
                    //String s = d.getValue().toString();
                    UserInfo u = d.getValue(UserInfo.class);
                    userInfoList.add(u);
                    //Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
                    //Toast.makeText(MainActivity.this,u.toString(),Toast.LENGTH_LONG).show();
                }
                firebaseUserCallback.onCallBack(userInfoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: error loading FireBase");
            }
        };
        fb.getUserDatabaseReference().addValueEventListener(userListener);


    }

    private interface FirebaseUserCallback{
        void onCallBack(ArrayList<UserInfo> userInfoArrayList);
    }

}