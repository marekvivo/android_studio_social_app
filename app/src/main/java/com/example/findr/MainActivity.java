package com.example.findr;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText userName;
    EditText password;
    EditText passwordVerify;

    Button btnRegister;
    Button btnSignIn;

    ArrayList<UserInfo> userInfoList;
    UserDataBaseHelper userDbHelper;

    FirebaseUserHelper fb;
    String fbUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        email = findViewById(R.id.txtEmail);
        userName = findViewById(R.id.txtUserName);
        password = findViewById(R.id.txtPassword);
        passwordVerify = findViewById(R.id.txtPasswordVerify);

        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);


        userDbHelper = new UserDataBaseHelper(MainActivity.this, "user.db", null, 1);

        //Db reset tool
        //dbHelper.deleteDb();


        //userInfoList = userDbHelper.getAll();

        //Firebase operations
        fb = new FirebaseUserHelper();
        //fb.add(userInfoList.get(0));
        userInfoList = new ArrayList<>();
        loadUserData(new FirebaseUserCallback() {
            @Override
            public void onCallBack(ArrayList<UserInfo> userInfoArrayList) {
                //Use this to update UI calls as long as data is updated on firebase
                //Toast.makeText(MainActivity.this,String.valueOf(userInfoList.size()),Toast.LENGTH_LONG).show();



                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(verifyRegistration()){
                            SHAHelper shaHelper = onCreateSHAHelper(password.getText().toString());
                            if(shaHelper.equals(null)){
                                Toast.makeText(MainActivity.this,"Failed to create SHA Helper!", Toast.LENGTH_LONG).show();
                                return;
                            }
                            //creates a new user with Hashed passwords & salts
                            UserInfo newUserInfo = new UserInfo(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),userName.getText().toString(),shaHelper.getSalt(),shaHelper.getHashedPassword());
                            Log.d("User Password", newUserInfo.getPassword());
                            fbUserId = fb.add(newUserInfo);
                            if(!fbUserId.isEmpty()){
                                Log.d(TAG, "onClick: Successfully added user to Firebase");

                                // TODO: 12/11/2021 navigate user to profile page /home screen

                                Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                                intent.putExtra("fbUserId", fbUserId);
                                startActivity(intent);
                            }
                            else{
                                Log.d(TAG, "onClick: Failed to add user to database");
                                Toast t = Toast.makeText(MainActivity.this, "Failed to add user to database!",Toast.LENGTH_LONG);
                                t.show();
                            }
                        }



                    }
                });

                btnSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //TESTING FIREBASE
                /*fb.logUserKeys();
                fb.updateUserInterest("DOG","-MxY0t40FJHuO7Guh0j1");
                Interest i = new Interest("");
                fb.add(i);*/

                /*Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);*/

                        Intent intent = new Intent(MainActivity.this, LoginPage.class);
                        startActivity(intent);
                    }
                });

            }
        });


        /*UserInfo newUser = new UserInfo("mack","seah","mack@gmail.com","wtf","wtf","wtf");
        ArrayList<String> interestId = new ArrayList<>();
        interestId.add("asdvd");
        newUser.setInterestIdArr(interestId);
        String key = fb.add(newUser);
        interestId.add("sdasdasf");
        fb.updateUser(newUser,key);*/

        //fb.updateUserInterest("DOG","MxY0t40FJHuO7Guh0j1");

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
        fb.getUserDatabaseReference().addListenerForSingleValueEvent(userListener);


    }

    private interface FirebaseUserCallback{
        void onCallBack(ArrayList<UserInfo> userInfoArrayList);
    }

    public SHAHelper onCreateSHAHelper(String passwordToHash) {
        SHAHelper shaHelper;
        try {
            shaHelper = new SHAHelper(passwordToHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return shaHelper;

    }

    public boolean isTxtEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public boolean verifyPassword (EditText password, EditText passwordVerify){
        return password.getText().toString().equals(passwordVerify.getText().toString());
    }


    public boolean verifyRegistration(){
        if(isTxtEmpty(firstName)||isTxtEmpty(lastName)||isTxtEmpty(email)||isTxtEmpty(userName)||isTxtEmpty(password)||isTxtEmpty(passwordVerify)){
            Toast t = Toast.makeText(this, "Please enter all fields to register!", Toast.LENGTH_LONG);
            t.show();
            return false;
        }

        if(!verifyPassword(password,passwordVerify)){
            Toast t = Toast.makeText(this,"Your passwords do not match!", Toast.LENGTH_LONG);
            t.show();
            return false;
        }
        // TODO: 12/11/2021 check for user already exists
        if(doesUserExist(userName)){
            Toast t = Toast.makeText(this,"User already exists!", Toast.LENGTH_LONG);
            t.show();
            return false;
        }

        return true;
    }

    public boolean doesUserExist(EditText userName){
        String userNameStr = userName.getText().toString();
        for(UserInfo u:userInfoList){
            if(u.getUserName().equalsIgnoreCase(userNameStr)){
                return true;
            }
        }
        return false;
    }

}