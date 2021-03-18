package main;

import java.util.Arrays;

public class Player {
    private static int uniqueId=1;
    private String name;
    private double money;
    private boolean quitGame;
    private int [] initialCards = new int[2];
    int index = 0;
    private int score;
    private int[] combination = new int[5];
    private int [] bestCombination = new int[5];
    private boolean straightFlush = false;
    private boolean flush = false;
    private boolean straight = false;
    private int fours;
    private int threes;
    private int highStraight;
    private int highFlush;
    private int highStraightFlush;
    private int twos;
    private int twosPair1;
    private int twosPair2;
    private int twosPair;
    private int high;
    private int bestScore;
    private int bestFours;
    private int bestThrees;
    private int bestHighStraight;
    private int bestHighFlush;
    private int bestHighStraightFlush;
    private int bestTwos;
    private int bestTwosPair1;
    private int bestTwosPair2;
    private int bestTwosPair;
    private int bestHigh;



    Player(String name, double money){
        if(name.length()>0){
            this.name = name;
        }
        else{
            this.name = "Player"+uniqueId;
            System.out.println("Invalid input! Default name "+this.name+" was set!");
            uniqueId++;
        }
        if(money>=0){
            this.money = money;
        }
        else {
            this.money = 0;
            System.out.println("Invalid input! Default amount of "+this.money+ " was set!");
        }
    }


    int[] getInitialCards() {
        return initialCards;
    }

    //Добавяне на първоначалните карти
    void addCard (int card){
        initialCards[index] = card;
        index++;
    }
    //Печат на картите с първоначалните им стойности от 0 до 51
    void printCards(){
        System.out.println(Arrays.toString(bestCombination));
    }

    //Печат на картите с реалните им стойности и боя.
    void printRealCards(){
        System.out.print(name+" has: ");
        scoreToText();
        for(int i = 0; i<bestCombination.length; i++){
            int num = bestCombination[i]/4;
            switch (num){
                case 0:
                    System.out.print(2+" ");
                    break;
                case 1:
                    System.out.print(3+" ");
                    break;
                case 2:
                    System.out.print(4+" ");
                    break;
                case 3:
                    System.out.print(5+" ");
                    break;
                case 4:
                    System.out.print(6+" ");
                    break;
                case 5:
                    System.out.print(7+" ");
                    break;
                case 6:
                    System.out.print(8+" ");
                    break;
                case 7:
                    System.out.print(9+" ");
                    break;
                case 8:
                    System.out.print(10+" ");
                    break;
                case 9:
                    System.out.print("J ");
                    break;
                case 10:
                    System.out.print("Q ");
                    break;
                case 11:
                    System.out.print("K ");
                    break;
                case 12:
                    System.out.print("A ");
                    break;

            }
        }
        System.out.println();
        for(int i = 0; i<bestCombination.length; i++){
            int num = bestCombination[i]%4;
            switch (num){
                case 0:
                    System.out.print("S ");
                    break;
                case 1:
                    System.out.print("D ");
                    break;
                case 2:
                    System.out.print("H ");
                    break;
                case 3:
                    System.out.print("C ");
                    break;
            }
        }
        System.out.println();
    }



    //Проверка на всяка една комбинация от картите на масата, заедно с определяне на резултата от всяка една и избор на по-високата
    void createBestCombination(int [] initialCards, int []board){

        for (int i = 0; i < board.length-2; i++) {
            for (int j = i+1; j<board.length-1; j++){
                for (int k = j+1; k< board.length; k++){
                    combination[0] = initialCards[0];
                    combination[1] = initialCards[1];
                    combination[2] = board[i];
                    combination[3] = board[j];
                    combination[4] = board[k];
                    makeScore(combination);
                    findBestCombination(combination);
                }
            }
        }
    }
    //Сравнение на 2 комбинации за избор на по-високата
    void findBestCombination(int [] combination){
        if(score>bestScore){
            makeSwap();
        }
        else if(score==bestScore){
            boolean foundBetter = false;
            switch (score){
                case 9://Straight Flush
                    if(highStraightFlush>bestHighStraightFlush){
                        foundBetter=true;
                    }
                    break;
                case 7: //Full House
                    if(threes>bestThrees){
                        foundBetter = true;
                    }
                    break;
                case 6: //Flush
                    for (int i = bestCombination.length-1; i>=0; i--){
                        if(combination[i]>bestCombination[i]){
                            foundBetter=true;
                            break;
                        }
                        else if(combination[i]<bestCombination[i]){
                            break;
                        }
                    }
                    break;
                case 5: //Straight
                    if(highStraight>bestHighStraight){
                        foundBetter=true;
                    }
                    break;
                case 4://Three of a kind
                    int cardB1 = -1;
                    int cardB2 = -2;
                    int card1 = -1;
                    int card2 = -2;
                    for (int i = 0; i < bestCombination.length; i++) {
                        if(bestCombination[i]!=bestThrees){
                            if(cardB1>=0){
                                cardB2 = bestCombination[i];
                            }
                            else {
                                cardB1 = bestCombination[i];
                            }
                        }
                    }
                    for (int i = 0; i < combination.length; i++) {
                        if(combination[i]!=bestThrees){
                            if(card1>=0){
                                card2 = combination[i];
                            }
                            else {
                                card1 = combination[i];
                            }
                        }
                    }
                    int cardBMax = Math.max(cardB1,cardB2);
                    int cardBMin = Math.min(cardB1,cardB2);
                    int cardMax=Math.max(card1,card2);
                    int cardMin=Math.min(card1,card2);
                    if(cardMax>cardBMax){
                        foundBetter = true;
                    }
                    else if(cardMax==cardBMax){
                        if(cardMin>cardBMin){
                            foundBetter = true;
                        }
                    }

                    break;
                case 3://Two pairs
                    if(twosPair1>bestTwosPair1){
                        foundBetter = true;
                    }
                    else if(twosPair1==bestTwosPair1){
                        if (twosPair2>bestTwosPair2){
                            foundBetter = true;
                        }
                        else if(twosPair2==bestTwosPair2){
                            int cardB5 = -1;
                            int card5 = -1;
                            for(int i = 0; i<bestCombination.length; i++){
                                if(bestCombination[i]/4!=bestTwosPair1||bestCombination[i]/4!=bestTwosPair2){
                                    cardB5 = bestCombination[i]/4;
                                    break;
                                }
                            }
                            for(int i = 0; i<combination.length; i++){
                                if(combination[i]/4!=twosPair1||combination[i]/4!=twosPair2){
                                    card5 = combination[i]/4;
                                    break;
                                }
                            }
                            if(card5>cardB5){
                                foundBetter = true;
                            }
                        }
                    }
                    break;
                case 2: //One pair
                    if(twosPair>bestTwosPair){
                        foundBetter = true;
                    }
                    else if(twosPair == bestTwosPair){
                        cardB1 = -1;
                        cardB2 = -1;
                        card1 = -1;
                        card2 = -1;
                        int cardB3 = -1;
                        int card3 = -1;
                        int counter = 0;
                        for(int i = 0; i<bestCombination.length; i++){
                            if(bestCombination[i]/4!=bestTwosPair){
                                if(counter==0){
                                    cardB1 = bestCombination[i]/4;
                                    counter++;
                                }
                                else if(counter==1){
                                    cardB2 = bestCombination[i]/4;
                                    counter++;
                                }
                                else {
                                    cardB3 = bestCombination[i]/4;
                                    counter=0;
                                    break;
                                }
                            }
                        }
                        for(int i = 0; i<combination.length; i++){
                            if(combination[i]/4!=twosPair){
                                if(counter==0){
                                    card1 = combination[i]/4;
                                    counter++;
                                }
                                else if(counter==1){
                                    card2 = combination[i]/4;
                                    counter++;
                                }
                                else {
                                    card3 = combination[i]/4;
                                    counter=0;
                                    break;
                                }
                            }
                        }
                        int [] arrB = new int[3];
                        arrB[0] = cardB1;
                        arrB[1] = cardB2;
                        arrB[2] = cardB3;
                        sortArray(arrB);
                        int [] arr = new int[3];
                        arr[0] = card1;
                        arr[1] = card2;
                        arr[2] = card3;
                        sortArray(arr);
                        for (int i = arrB.length-1;i>=0; i--){
                            if(arr[i]>arrB[i]){
                                foundBetter = true;
                                break;
                            }
                            else if(arr[i]<arrB[i]){
                                break;
                            }
                        }
                    }
                    break;
                case 1: //High card
                    sortArray(combination);
                    sortArray(bestCombination);
                    for (int i = bestCombination.length-1; i>=0; i--){
                        if(combination[i]/4>bestCombination[i]/4){
                            foundBetter = true;
                            break;
                        }
                        else if(combination[i]/4<bestCombination[i]/4){
                            break;
                        }
                    }
                    break;
                }
                if(foundBetter){
                    makeSwap();
                }
        }
    }

    //Проверка за флъш
    boolean checkFlush(int [] combination){
        boolean isFlush = true;
        for(int i = 0; i<combination.length-1; i++){
            if(combination[i]%4!=combination[i+1]%4){
                isFlush=false;
                break;
            }
        }
        return isFlush;
    }
    //Проверка за стрейт
    boolean checkStraight(int [] combination){
        boolean isStraight = true;
        int count = 1;
        for(int i = 0; i<combination.length-1; i++){
            if(combination[i+1]/4-combination[i]/4!=1){
                isStraight=false;
                break;
            }
            count++;
        }
        if(count==4){ // Проверка за случай Асото да е единица и да направи стрейт с най-висока карта 5.
            if(combination[combination.length-1]/4==12&&combination[0]/4==0){
                isStraight=true;
            }
        }
        return isStraight;
    }
    //Сортиране на комбинацията за по-добро сравение на стойностите
    void sortArray(int [] combination){
        for(int l = 0; l<combination.length/2; l++){
            int maxIndex = combination.length-l-1;
            int minIndex = l;
            for(int m = l; m<combination.length-l;m++){
                if(combination[m]<combination[minIndex]){
                    minIndex = m;
                }
                if(combination[m]>combination[maxIndex]){
                    maxIndex = m;
                }

            }
            int temp = combination[minIndex];
            combination[minIndex] = combination[l];
            combination[l] = temp;

            if(maxIndex==l){
                maxIndex=minIndex;
            }
            temp = combination[maxIndex];
            combination[maxIndex] = combination[combination.length-l-1];
            combination[combination.length-l-1] = temp;
        }
    }
    // Определяне на резултата на всяка комбинация
    void makeScore(int []combination) {
        sortArray(combination);
        flush = checkFlush(combination);
        straight = checkStraight(combination);
        straightFlush = flush && straight;
        if (straightFlush) {
            if (combination[4] / 4 == 12) {
                score = 10;
            } else {
                score = 9;
                if (combination[4] / 4 == 12 && combination[3] / 4 == 3) {
                    reSortStraight(combination);
                }
                highStraightFlush = combination[4] / 4;

            }
        } else if (flush) {
            score = 6;
            highFlush = combination[4] / 4;
        } else if (straight) {
            score = 5;
            if (combination[4] / 4 == 12 && combination[3] / 12 == 3) {
                reSortStraight(combination);
            }
                highStraight = combination[4] / 4;
        } else {
            int[] counted = new int[13];
            for (int i = 0; i < combination.length; i++) {
                counted[combination[i] / 4]++;
            }
            int countMax = 0;
            int maxIndex = counted.length-1;
            for (int i = 0; i < counted.length; i++) {
                if (counted[i] > countMax) {
                    countMax = counted[i];
                    maxIndex = i;
                }
            }
            if (countMax == 4) {
                score = 8;
                fours = maxIndex;

            } else if (countMax == 3) {
                boolean findTwos = false;
                threes = maxIndex;
                for (int i = 0; i < counted.length; i++) {
                    if(counted[i]==2){
                        findTwos = true;
                        twos = i;
                        break;
                    }
                }
                if (findTwos){
                    score = 7;
                }
                else {
                    score = 4;
                }
            }else if(countMax==2){
                boolean pairs = false;
                int indexSecond = -1;
                int counter = 0;
                for(int i = 0; i<counted.length; i++){
                    if(counted[i]==2){
                        counter++;
                        if(counter==2){
                            pairs=true;
                            indexSecond = i;
                            break;
                        }
                    }
                }
                if(pairs){
                    score = 3;
                    twosPair1 = indexSecond;
                    twosPair2 = maxIndex;
                }
                else {
                    score = 2;
                    twosPair = maxIndex;
                }
            }
            else {
                score = 1;
                high = combination[4]/4;
            }

        }
    }

    public int[] getBestCombination() {
        return bestCombination;
    }
    //записване на новите най-добри стойности
    void makeSwap(){
        bestScore=score;
        bestFours = fours;
        bestThrees = threes;
        bestHighStraightFlush = highStraightFlush;
        bestHighStraight = highStraight;
        bestHighFlush = highFlush;
        bestTwos = twos;
        bestTwosPair1 = twosPair1;
        bestTwosPair2 = twosPair2;
        bestTwosPair = twosPair;
        bestHigh = high;
        for (int i= 0; i<combination.length; i++){
            bestCombination[i] = combination[i];
        }
    }

    public int getBestScore() {
        return bestScore;
    }
    public int getBestFours(){
        return bestFours;
    }
    public int getBestThrees(){
        return bestThrees;
    }
    public int getBestTwos(){
        return bestTwos;
    }
    public int getBestTwosPair1(){
        return bestTwosPair1;
    }
    public int getBestTwosPair2(){
        return bestTwosPair2;
    }
    public int getBestTwosPair(){
        return bestTwosPair;
    }
    public String getName(){
        return name;
    }
    public double getMoney(){
        return money;
    }

    public boolean isQuitGame() {
        return quitGame;
    }

    public int getBestHighStraightFlush() {
        return bestHighStraightFlush;
    }
    // Ако Асото е единица - пренареждане.
    void reSortStraight(int [] combination){
        int temp = combination[4];
        combination [4] = combination[3];
        combination [3] = combination[2];
        combination [2] = combination[1];
        combination [1] = combination[0];
        combination[0] = temp;

    }
    //Трансформиране на резултата от число в името на комбинацията от карти
    void scoreToText(){
        switch (bestScore){
            case 10:
                System.out.println("Royal straight flush of "+scoreToColor(bestCombination[4]%4));
                break;
            case 9:
                System.out.println("Straight flush of "+scoreToColor(bestCombination[4]%4));
                break;
            case 8:
                System.out.println("Four of a kind of "+numbersToText(bestFours));
                break;
            case 7:
                System.out.println("Full house of "+numbersToText(bestThrees)+" and "+numbersToText(bestTwos));
                break;
            case 6:
                System.out.println("Flush of "+scoreToColor(bestCombination[4]%4));
                break;
            case 5:
                System.out.println("Straight");
                break;
            case 4:
                System.out.println("Three of a kind of "+numbersToText(bestThrees));
                break;
            case 3:
                System.out.println("Two pairs of "+numbersToText(bestTwosPair1)+" and "+numbersToText(bestTwosPair2));
                break;
            case 2:
                System.out.println("Pair of "+numbersToText(bestTwosPair));
                break;
            case 1:
                System.out.println("High card "+numbersToText(bestHigh));
                break;

        }
    }
    //изписване на цвета на картата
    String scoreToColor(int number){
        String color = "";
        switch (number){
            case 0:
                color = "spades";
                break;
            case 1:
                color = "diamonds";
                break;
            case 2:
                color = "hearts";
                break;
            case 3:
                color = "club";
                break;
        }
        return color;
    }
    //Изписване на числата от печелившата комбинация в текст
    String numbersToText(int number) {
        String text = "";
        switch (number) {
            case 0:
                text="twoes";
                break;
            case 1:
                text="threes";
                break;
            case 2:
                text="fours";
                break;
            case 3:
                text="fives";
                break;
            case 4:
                text="sixes";
                break;
            case 5:
                text="sevens";
                break;
            case 6:
                text="eights";
                break;
            case 7:
                text="nines";
                break;
            case 8:
                text="tens";
                break;
            case 9:
                text="Jacks";
                break;
            case 10:
                text="Queens";
                break;
            case 11:
                text="Kings";
                break;
            case 12:
                text="Aces";
                break;

        }
        return text;
    }
    //Зануляване преди раздаване на картите при повторна игра
    void nullParameters(){
        initialCards = new int[2];
        index = 0;
        score = 0;
        combination = new int[5];
        bestCombination = new int[5];
        straightFlush = false;
        flush = false;
        straight = false;
        fours=0;
        threes=0;
        highStraight=0;
        highFlush=0;
        highStraightFlush=0;
        twos=0;
        twosPair1=0;
        twosPair2=0;
        twosPair=0;
        high=0;
        bestScore=0;
        bestFours=0;
        bestThrees=0;
        bestHighStraight=0;
        bestHighFlush=0;
        bestHighStraightFlush=0;
        bestTwos=0;
        bestTwosPair1=0;
        bestTwosPair2=0;
        bestTwosPair=0;
        bestHigh=0;
    }

    void addOrRemoveMoney(double amount){
        money+=amount;

    }
    void checkEntryMoney(double amount){
        if(money-amount<0){
            quitGame = true;
            System.out.println(name+" is out of money and quits the game!");
        }
    }
    boolean hasEnoughMoney(double amount){
        boolean hasMoney = true;
        if(money-amount<0){
            hasMoney = false;
            System.out.println(name+" hasn't enough money!");
        }
        return hasMoney;
    }
}
