package com.example.findr;

import java.util.ArrayList;

public class Genre {
    private int genreId; //basically index of genre+1
    private String genre; //use as double use string

    private ArrayList<Genre> subGenreArrayList = new ArrayList<>();

    //private ArrayList<SubInterest> subInterestsArrayList = new ArrayList<>();

    public Genre(){
        this.genreId = 1;
        this.genre = "MAIN";
    }

    //Might be redundant because of addNewSubGenre method
    public Genre(String genre,int genreId){//Create genre only
        this.genre = genre.toLowerCase();
        this.genreId = genreId;
    }

    public Genre(String defaultSetup){
        //might be able to load defaults for students, working adults, etc
        this.genreId = 1;
        this.genre = "MAIN";
        if(defaultSetup.equalsIgnoreCase("default")){

            addNewSubGenre("Entertainment");
            addNewSubGenre("Education");
            addNewSubGenre("Profession");

            Genre entertainment = getSubGenreArrayList().get(0);
            Genre education = getSubGenreArrayList().get(1);
            Genre profession = getSubGenreArrayList().get(2);

            entertainment.addNewSubGenre("Video Games");
            entertainment.addNewSubGenre("Movies");
            entertainment.addNewSubGenre("Sports");
            entertainment.addNewSubGenre("Music");

            Genre entVideoGames = entertainment.getSubGenre("Video Games");
            entVideoGames.addNewSubGenre("Action");
            entVideoGames.addNewSubGenre("Fantasy");
            entVideoGames.addNewSubGenre("Adventure");
            entVideoGames.addNewSubGenre("Shooter");
            entVideoGames.addNewSubGenre("Tactical");
            entVideoGames.addNewSubGenre("Horror");
            entVideoGames.addNewSubGenre("Platformer");
            entVideoGames.addNewSubGenre("RPG");
            entVideoGames.addNewSubGenre("Survival");
            //Maybe let users choose a wide variety then specify individual interests
            //etc, "Action,Fantasy,RPG -> Tales of Arise

            Genre entMovies = entertainment.getSubGenre("Movies");



            Genre entSports = entertainment.getSubGenre("Sports");
            Genre racketSport = entSports.addNewSubGenre("Racket");
            //Users may be able to add their own in the app
            /*racketSport.addNewSubInterest("Tennis");
            racketSport.addNewSubInterest("Squash");
            racketSport.addNewSubInterest("Badminton");
            racketSport.addNewSubInterest("Table Tennis");
            racketSport.addNewSubInterest("Baseball");*/

            Genre ballSport = entSports.addNewSubGenre("Ball");
           /* racketSport.addNewSubInterest("Football");
            racketSport.addNewSubInterest("Rugby");
            racketSport.addNewSubInterest("Volleyball");*/


            entSports.addNewSubGenre("Electronic");


            entSports.addNewSubGenre("Physical");



            Genre entMusic = entertainment.getSubGenre("entMusic");

            education.addNewSubGenre("Computer Science");
            education.addNewSubGenre("Mathematics");
            education.addNewSubGenre("History");
            education.addNewSubGenre("Sociology");






        }

    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public ArrayList<Genre> getSubGenreArrayList() {
        return subGenreArrayList;
    }

    public void setSubGenreArrayList(ArrayList<Genre> subGenreArrayList) {
        this.subGenreArrayList = subGenreArrayList;
    }

    /*public void setSubInterestsArrayList(ArrayList<SubInterest> subInterestsArrayList) {
        this.subInterestsArrayList = subInterestsArrayList;
    }

    public ArrayList<SubInterest> getSubInterestsArrayList() {
        return subInterestsArrayList;
    }*/


    //returns null if not found otherwise i where i = Interest
    public Genre getSubGenre (String genre){

        for(Genre i : subGenreArrayList){
            if(i.getGenre().equalsIgnoreCase(genre)){
                return i;
            }
        }
        return null;
    }
    public Genre getSubGenre(int genreId){
        if(genreId<1)return null;
        return subGenreArrayList.get(genreId-1);
    }

    public Genre addNewSubGenre(String subGenre){
       //Might need to check if subgenre already exists

        subGenreArrayList.add(new Genre(subGenre,subGenreArrayList.size()+1));
        return subGenreArrayList.get(subGenreArrayList.size()-1);
    }

    // Should be adding new interest if not found
    /*public SubInterest addNewSubInterest( String subInterest){

        SubInterest newSubInterest = null;
        if(subInterestsArrayList.isEmpty()){
            newSubInterest= new SubInterest(0,subInterest.toLowerCase());
            subInterestsArrayList.add(newSubInterest);
        }
        else if (!containsSubInterestString(subInterest)){
            int newId = 1 + subInterestsArrayList.get(subInterestsArrayList.size()-1).getId();
            newSubInterest = new SubInterest(newId, subInterest.toLowerCase());
            subInterestsArrayList.add(newSubInterest);
        }
        return newSubInterest;

    }*/

    /*public boolean containsSubInterestString (String subInterest){
        for(SubInterest sI : subInterestsArrayList){
           if(sI.getSubInterest().equals(subInterest)){
               return true;
           }
        }
        return false;
    }*/

    //TO TEST
    //Gives the callback sequence in relation to index of each elements
    //etc, ["entertainment","video games","adventure"] -> 000
    public ArrayList<Integer> indexOfSubGenre(ArrayList<String> genreCallBack1){// ArrList = {Entertainment,Movies,Action}
        ArrayList<Genre> currentTargetList = getSubGenreArrayList();
        ArrayList<Integer> callBackSequence = new ArrayList<>();
        Genre nextInterest;
        ArrayList<String> genreCallBack = (ArrayList<String>) genreCallBack1.clone();//This is a reference its not a copy... need .clone() to create true copy
        while(!currentTargetList.isEmpty()&&!genreCallBack.isEmpty()){

            //potential problem handling null pointers
            if(getNextSubGenre(currentTargetList,genreCallBack.get(0)).equals(null)){
                return callBackSequence;
            }
            else{
                nextInterest = getNextSubGenre(currentTargetList,genreCallBack.get(0));
                callBackSequence.add(nextInterest.genreId);
                currentTargetList = nextInterest.subGenreArrayList;
                genreCallBack.remove(0);
            }
        }
        System.out.println(genreCallBack1);
        return callBackSequence;

    }

    //combines multi genres to proper classify all users genres of liking
    //etc, [1, 1, 3] & [1, 1, 1] -> [1, 1, 13]
    //return null if parent genres are not matched
    //sorts the number at the end to numerical order
    //this method fails if id have 0 in them
    //only works to a max of 9 subgenres for now
    //can rinse and repeat indexOfSubGenre to create multiple genre, might not have a need for this
    //although this way is a lot cleaner to store in database
    public ArrayList<Integer> combineSubGenres (ArrayList<Integer> callBackSeq1, ArrayList<Integer> callBackSeq2){
        if(callBackSeq1.size()!=callBackSeq2.size())return null;

        for(int i = 0;i < callBackSeq1.size()-1 ;i++){
            if(!callBackSeq1.get(i).equals(callBackSeq2.get(i)))return null;
        }

        //ArrayList<Integer> combinedSubGenres = new ArrayList<>();

        String str1 = callBackSeq1.get(callBackSeq1.size()-1).toString();
        String str2 = callBackSeq2.get(callBackSeq2.size()-1).toString();
        String sortStr = str1.concat(str2);

        //Sorting method
        String[] sortStrList = sortStr.split("");
        int[] sortIntList = new int[sortStrList.length];

        for(int i=0;i<sortStrList.length;i++){
            sortIntList[i] = Integer.parseInt(sortStrList[i]);

        }

        for(int i=0;i<sortIntList.length;i++){
            int temp = sortIntList[i];
            int tempIndex = -1;
            for(int i2=i;i2<sortIntList.length;i2++){
                if(temp>sortIntList[i2]){
                    temp=sortIntList[i2];
                    tempIndex = i2;
                }
            }
            if(tempIndex!=-1){
                sortIntList[tempIndex] = sortIntList[i];
                sortIntList[i] = temp;
            }

        }
        String returnStr = "";
        for(int i : sortIntList){
            returnStr += String.valueOf(i);

        }


        callBackSeq1.set(callBackSeq1.size()-1,Integer.parseInt(returnStr));

        System.out.println(callBackSeq1.toString());

        return callBackSeq1;
    }


    public Genre getNextSubGenre(ArrayList<Genre> target, String genreTarget){
        for(Genre i : target){
            //Might be better to use equalsIgnoreCase()
            if(genreTarget.toLowerCase().equals(i.getGenre().toLowerCase())){
                return i;
            }
        }
        return null;
    }

    public Genre getTargetSubgenre (ArrayList<Integer> callbackSequence){
        Genre returnInterest = this;
        for(Integer i: callbackSequence){
            returnInterest = returnInterest.getSubGenreArrayList().get(i);
        }
        return returnInterest;
    }

    public String printParseGenre (ArrayList<Integer> callBackSequence){
        if(callBackSequence.isEmpty())return "EMPTY CALL SEQUENCE";

        String returnStr = "[";
        Genre currentGenre = this;


        for(Integer i : callBackSequence){
            if(i>9)break;
            currentGenre = currentGenre.getSubGenre(i);
            //System.out.println(currentGenre.getGenre());
            returnStr += (currentGenre.getGenre() + ", ");
        }

        if(callBackSequence.get(callBackSequence.size()-1)>9){
            //account for multigenre

            String tempStr = callBackSequence.get(callBackSequence.size()-1).toString();
            String[] tempStrArr = tempStr.split("");

            returnStr += "[";
            for(String s : tempStrArr){
                returnStr += (currentGenre.getSubGenre(Integer.parseInt(s)).getGenre() + ", ");
            }
            returnStr = returnStr.substring(0,returnStr.length()-2);
            returnStr += "]";

        }


        returnStr += "]";
        return returnStr;
    }

    public boolean hasNoSubGenres(){
        return subGenreArrayList.isEmpty();
    }


}
