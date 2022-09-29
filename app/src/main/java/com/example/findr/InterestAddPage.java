package com.example.findr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class InterestAddPage extends AppCompatActivity {

    private static final String TAG = "InterestAddPage";

    private TextView interestsHeader;
    private EditText interestsDescription;
    private Button btnSubmit;

    private String fbUserId;
    private FirebaseUserHelper fb = new FirebaseUserHelper();
    private ArrayList<Interest> interestArrayList = new ArrayList<>();


    private Spinner spin1;
    private Spinner spin2;
    private Spinner spin3;
    private Spinner spin4;
    private Spinner spin5;
    private Spinner spin6;

    private ArrayList<String> spin1Genre = new ArrayList<>();
    private ArrayList<String> spin2Genre = new ArrayList<>();
    private ArrayList<String> spin3Genre = new ArrayList<>();
    private ArrayList<String> spin4Genre = new ArrayList<>();
    private ArrayList<String> spin5Genre = new ArrayList<>();
    private ArrayList<String> spin6Genre = new ArrayList<>();

    private ArrayList<Integer> genreIdList = new ArrayList<>();
    private ArrayList<Integer> multiGenre = new ArrayList<>();

    private Genre gmapRoot;
    private Genre gmapPointer;
    //private Genre gmapPointerTemp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_add);

        interestsHeader = findViewById(R.id.interests_header);
        interestsHeader.setText("Select Genres");
        interestsHeader.setTypeface(Typeface.DEFAULT_BOLD);

        interestsDescription = findViewById(R.id.interests_description);

        gmapRoot = new Genre("default");
        gmapPointer = gmapRoot;

        fbUserId = getIntent().getStringExtra("fbUserId");
        btnSubmit = findViewById(R.id.interests_btn_submit);
        spin1 = findViewById(R.id.spinner1);
        spin2 = findViewById(R.id.spinner2);
        spin3 = findViewById(R.id.spinner3);
        spin4 = findViewById(R.id.spinner4);
        spin5 = findViewById(R.id.spinner5);
        spin6 = findViewById(R.id.spinner6);

        spin2.setVisibility(View.INVISIBLE);
        spin3.setVisibility(View.INVISIBLE);
        spin4.setVisibility(View.INVISIBLE);
        spin5.setVisibility(View.INVISIBLE);
        spin6.setVisibility(View.INVISIBLE);

        fromGenreToStringArray(spin1Genre);


        /*Nested ArrayAdapters for nested spinners which depend on each other,
        values for the spinner change when a user selects different values*/
        ArrayAdapter<String> spin1Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spin1Genre);
        spin1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(spin1Adapter);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                makeBelowSpinnerInvis(1);
                gmapPointer = gmapRoot;
                String selectedGenre1 = adapterView.getItemAtPosition(i).toString();
                updateGenreIdList(gmapPointer.getSubGenre(selectedGenre1).getGenreId(),0);
                updateGenrePointer(selectedGenre1);
                fromGenreToStringArray(spin2Genre);
                spin2.setVisibility(View.VISIBLE);

                ArrayAdapter<String> spin2Adapter = new ArrayAdapter<String>(InterestAddPage.this, android.R.layout.simple_spinner_item,spin2Genre);
                spin2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin2.setAdapter(spin2Adapter);
                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        makeBelowSpinnerInvis(2);
                        gmapPointer = gmapRoot.getSubGenre(selectedGenre1);
                        String selectedGenre2 = adapterView.getItemAtPosition(i).toString();
                        updateGenreIdList(gmapPointer.getSubGenre(selectedGenre2).getGenreId(),1);

                        updateGenrePointer(selectedGenre2);
                        fromGenreToStringArray(spin3Genre);
                        spin3.setVisibility(View.VISIBLE);

                        ArrayAdapter<String> spin3Adapter = new ArrayAdapter<String>(InterestAddPage.this, android.R.layout.simple_spinner_item,spin3Genre);
                        spin3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin3.setAdapter(spin3Adapter);
                        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                makeBelowSpinnerInvis(3);
                                gmapPointer = gmapRoot.getSubGenre(selectedGenre1).getSubGenre(selectedGenre2);
                                String selectedGenre3 = adapterView.getItemAtPosition(i).toString();
                                updateGenreIdList(gmapPointer.getSubGenre(selectedGenre3).getGenreId(),2);

                                //Enable multi genre selection if no more subgenres
                                //Multi genre selection section
                                if(gmapPointer.getSubGenre(selectedGenre3).hasNoSubGenres()){
                                    fromGenreToMultiGenreStringArray(spin4Genre,gmapPointer.getSubGenre(selectedGenre3));
                                    spin4.setVisibility(View.VISIBLE);

                                    ArrayAdapter<String> spin4Adapter = new ArrayAdapter<String>(InterestAddPage.this, android.R.layout.simple_spinner_item,spin4Genre);
                                    spin4Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spin4.setAdapter(spin4Adapter);
                                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            makeBelowSpinnerInvis(4);
                                            String selectedGenre4 = adapterView.getItemAtPosition(i).toString();
                                            if(!selectedGenre4.equals("Add Multi-Genre")){
                                                updateMultiGenreIdList(gmapPointer.getSubGenre(selectedGenre4).getGenreId(),0);
                                                copyAndUpdateMultiGenreStringArray(spin4Genre,spin5Genre,selectedGenre4);
                                                spin5.setVisibility(View.VISIBLE);

                                                ArrayAdapter<String> spin5Adapter = new ArrayAdapter<String>(InterestAddPage.this, android.R.layout.simple_spinner_item,spin5Genre);
                                                spin5Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spin5.setAdapter(spin5Adapter);
                                                spin5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        makeBelowSpinnerInvis(5);
                                                        String selectedGenre5 = adapterView.getItemAtPosition(i).toString();
                                                        if(!selectedGenre5.equals("Add Multi-Genre")){
                                                            updateMultiGenreIdList(gmapPointer.getSubGenre(selectedGenre5).getGenreId(),1);
                                                            copyAndUpdateMultiGenreStringArray(spin5Genre,spin6Genre,selectedGenre5);
                                                            spin6.setVisibility(View.VISIBLE);

                                                            ArrayAdapter<String> spin6Adapter = new ArrayAdapter<String>(InterestAddPage.this, android.R.layout.simple_spinner_item,spin6Genre);
                                                            spin6Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            spin6.setAdapter(spin6Adapter);
                                                            spin6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    String selectedGenre6 = adapterView.getItemAtPosition(i).toString();
                                                                    if(!selectedGenre6.equals("Add Multi-Genre")){
                                                                        updateMultiGenreIdList(gmapPointer.getSubGenre(selectedGenre6).getGenreId(),2);
                                                                    }

                                                                }

                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                                }
                                                            });
                                                        }


                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                                    }
                                                });
                                            }





                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                }

                                //Still has more sub genres
                                else{

                                }

                                /*ArrayAdapter<String> spin4Adapter = new ArrayAdapter<String>(InterestAddPage.this, android.R.layout.simple_spinner_item,spin4Genre);
                                spin4Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spin4.setAdapter(spin4Adapter);
                                spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        gmapPointer = gmapRoot.getSubGenre(selectedGenre1).getSubGenre(selectedGenre2).getSubGenre(selectedGenre3);
                                        String selectedGenre4 = adapterView.getItemAtPosition(i).toString();
                                        //if hasNoSubGenres allow multi genre selection
                                        if(gmapPointer.getSubGenre(selectedGenre4).hasNoSubGenres()){
                                            fromGenreToMultiGenreStringArray(spin5Genre,gmapPointer.getSubGenre(selectedGenre4));
                                            spin5.setVisibility(View.VISIBLE);

                                            ArrayAdapter<String> spin5Adapter = new ArrayAdapter<String>(InterestAddPage.this, android.R.layout.simple_spinner_item,spin5Genre);
                                            spin5Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spin5.setAdapter(spin5Adapter);
                                            spin5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                    String selectedGenre5 = adapterView.getItemAtPosition(i).toString();



                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> adapterView) {

                                                }
                                            });


                                        }
                                        //Still has more sub genres...
                                        else{

                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });*/



                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(interestsDescription.getText().toString().equals("")){
                    Toast.makeText(InterestAddPage.this, "Description is Empty",Toast.LENGTH_LONG).show();

                }
                else{
                    addMultiGenreToGenreId();
                    Log.d("GenreIdList",genreIdList.toString());
                    Interest i = new Interest(genreIdList,interestsDescription.getText().toString());
                    //Todo add interest i to firebase according to the user that updated this

                    loadInterestData(new FirebaseInterestsCallback() {
                        @Override
                        public void onCallBack(ArrayList<Interest> interestArrayList) {
                            if(containsInterest(i)){
                                Log.d(TAG, "onCallBack: Contains Interest already");
                                fb.getInterestDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot d : snapshot.getChildren()){
                                            Log.d(TAG, "onDataChange: called. Contains interest");
                                            if(i.equals(d.getValue(Interest.class))){
                                                fb.updateUserInterest(d.getKey(),fbUserId);
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            //Does not contain interest, add new entry and link to user
                            else{
                                Log.d(TAG, "onCallBack: Does not contain interest");
                                String interestKey = fb.add(i);
                                fb.updateUserInterest(interestKey,fbUserId);
                            }
                        }
                    });


                }
            }
        });
    }

    public boolean containsInterest(Interest input){
        for(Interest i : interestArrayList ){
            if(i.equals(input)){
                return true;
            }
        }
        return false;
    }

    private void loadInterestData(FirebaseInterestsCallback firebaseInterestsCallback){
        fb.getInterestDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                interestArrayList.removeAll(interestArrayList);
                Log.d(TAG, "onDataChange: loadInterestData called");
                for(DataSnapshot d : snapshot.getChildren()){
                    Interest i = d.getValue(Interest.class);
                    interestArrayList.add(i);
                }
                firebaseInterestsCallback.onCallBack(interestArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseInterestsCallback{
        void onCallBack(ArrayList<Interest> interestArrayList);
    }


    public void addMultiGenreToGenreId(){
        if(genreIdList.isEmpty())return;
        if(multiGenre.isEmpty())return;


        String multiGenreStr = "";
        int i = genreIdList.get(genreIdList.size()-1);
        multiGenre.add(i);
        genreIdList.remove(genreIdList.size()-1);
        Collections.sort(multiGenre);

        for(Integer integer: multiGenre){
            multiGenreStr += String.valueOf(integer);
        }
        Log.d("multiGenreStr",multiGenreStr);
        if(!multiGenreStr.isEmpty()){
            genreIdList.add(Integer.parseInt(multiGenreStr));
        }



    }

    public void updateMultiGenreIdList (int genreId,int position){

        int size = multiGenre.size();
        if(size > position){
            if(size > position+1) {
                //Log.d("multigenre", String.valueOf(multiGenre.size()));
                for (int i = position + 1; i < size; i++) {
                    //Log.d("multigenre", String.valueOf(i));
                    multiGenre.remove(multiGenre.size()-1);
                }
            }
            multiGenre.set(position,genreId);
        }
        else{
            multiGenre.add(genreId);
        }

        Log.d("UPDATE_M_GENRE_ID_LIST",multiGenre.toString());

    }

    //adds genreId if not added yet, updates if already added
    public void updateGenreIdList (int genreId,int position){

        int size = genreIdList.size();

        if(genreIdList.size()>position){
            genreIdList.set(position,genreId);
            if(genreIdList.size() > position+1) {
                for (int i = position + 1; i < size; i++) {
                    genreIdList.remove(genreIdList.size()-1);
                }
            }
        }
        else{
            genreIdList.add(genreId);
        }

        Log.d("UPDATE_GENRE_ID_LIST",genreIdList.toString());


    }

    public void makeBelowSpinnerInvis(int spinnerNum){
        switch(spinnerNum){
            case 1:
                spin2.setVisibility(View.INVISIBLE);
                spin3.setVisibility(View.INVISIBLE);
                spin4.setVisibility(View.INVISIBLE);
                spin5.setVisibility(View.INVISIBLE);
                spin6.setVisibility(View.INVISIBLE);
                break;

            case 2:
                spin3.setVisibility(View.INVISIBLE);
                spin4.setVisibility(View.INVISIBLE);
                spin5.setVisibility(View.INVISIBLE);
                spin6.setVisibility(View.INVISIBLE);
                break;

            case 3:
                spin4.setVisibility(View.INVISIBLE);
                spin5.setVisibility(View.INVISIBLE);
                spin6.setVisibility(View.INVISIBLE);
                break;

            case 4:
                spin5.setVisibility(View.INVISIBLE);
                spin6.setVisibility(View.INVISIBLE);
                break;

            case 5:
                spin6.setVisibility(View.INVISIBLE);
                break;

        }
    }

    public void copyAndUpdateMultiGenreStringArray(ArrayList<String> spinIn,ArrayList<String> spinOut,String toRemove){
        spinOut.removeAll(spinOut);
        for(String s: spinIn){
            if(!s.equalsIgnoreCase(toRemove)){
                spinOut.add(s);
            }
        }
    }


    public void fromGenreToMultiGenreStringArray (ArrayList<String> spinIn,Genre toRemove){
        spinIn.removeAll(spinIn);
        spinIn.add("Add Multi-Genre");
        for(Genre g : gmapPointer.getSubGenreArrayList()){
            if(!g.getGenre().equalsIgnoreCase(toRemove.getGenre())){
                spinIn.add(g.getGenre());
            }
        }
    }

    public void fromGenreToStringArray(ArrayList<String> spinIn){
        spinIn.removeAll(spinIn);
        for(Genre g : gmapPointer.getSubGenreArrayList()){
            spinIn.add(g.getGenre());
        }
    }

    public void updateGenrePointer(String genre){
        for(Genre g : gmapPointer.getSubGenreArrayList()){
            if(g.getGenre().equalsIgnoreCase(genre)){
                gmapPointer = g;
            }
        }
    }

}