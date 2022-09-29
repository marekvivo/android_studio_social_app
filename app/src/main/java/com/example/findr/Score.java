package com.example.findr;

import java.util.ArrayList;

public class Score {

    private int genreId;
    private int level; //heirachy



    private ArrayList<Score> subScore = new ArrayList<>(); //same as subGenreArrayList

    public Score(int genreId, int level) {

        this.genreId = genreId;
        this.level = level;
    }

    public Score (){
        this.genreId = -1;
        this.level = -1;
    }


    public void addScores(ArrayList<Integer> genreIdList){

        int levelCounter = 1;
        Score scorePointer = this;
        for(Integer i : genreIdList){
            if(i <= 9){
                if(containsId(i,scorePointer.getSubScore())){
                    scorePointer = returnScoreFromId(i,scorePointer.getSubScore());
                }
                else{
                    ArrayList<Score> subScoreList = scorePointer.getSubScore();
                    subScoreList.add(new Score(i,levelCounter));
                    scorePointer = subScoreList.get(subScoreList.size()-1);
                }

            }
            else{
                String[] multiGenreId = i.toString().split("");
                ArrayList<Score> subScoreList = scorePointer.getSubScore();

                for(int i1=0;i1<multiGenreId.length;i1++){
                    int genreId = Integer.parseInt(multiGenreId[i1]);
                    //System.out.println(genreId);
                    if(!containsId(genreId,scorePointer.getSubScore())){
                        subScoreList.add(new Score(genreId,levelCounter));
                    }

                }
            }
            levelCounter++;
        }
    }

    public boolean containsId (int genreId,ArrayList<Score> scoreList){
        if(scoreList.isEmpty()){
            return false;
        }

        for(Score s :scoreList){
            if(genreId == s.getGenreId())return true;
        }
        return false;
    }

    public Score returnScoreFromId(int genreId,ArrayList<Score> scoreList){
        for(Score s: scoreList){
            if (genreId == s.getGenreId())return s;
        }
        return null;
    }

    public int calculateScore (Score userScore, Score targetScore){
        ArrayList<Score> userScoreList = userScore.getSubScore();
        ArrayList<Score> targetScoreList = targetScore.getSubScore();
        int score = 0;
        int totalUserScore = 0;
        for(Score s: userScoreList){
            if(containsId(s.getGenreId(),targetScoreList)){
                score += s.getLevel();
                if(isLowerLevel(s)){
                    score += calculateScore(s,returnScoreFromId(s.getGenreId(),targetScoreList));
                }
            }
        }
        return score;
    }

    public int calculateTotalScore(Score userScore){
        int totalScore = 0;
        for(Score s : userScore.getSubScore()){
            totalScore += s.getLevel();
            if(isLowerLevel(s)){
                totalScore += calculateTotalScore(s);
            }
        }
        return totalScore;
    }

    public double percentageMatched(Score targetScore){
        int score = calculateScore(this,targetScore);
        int totalScore = calculateTotalScore(this);

        return (double)score/totalScore *100;
    }

    public boolean isLowerLevel(Score score){
        return !score.getSubScore().isEmpty();
    }

    public void printIdMap(){
        for(Score s:subScore){
            System.out.print(s.getGenreId());
        }
        System.out.println(" ENDOFPRINT");
    }


    public int getLevel() {
        return level;
    }

    public ArrayList<Score> getSubScore() {
        return subScore;
    }

    public int getGenreId() {
        return genreId;
    }
}
