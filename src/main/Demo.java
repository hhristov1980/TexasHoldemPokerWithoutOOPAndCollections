package main;

import java.util.Random;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers = 4;
        //Въвеждане на начален вход
        double entryFee;
        do {
            System.out.println("Please enter the minimum bet to enter each game less or equal to 20:");
            entryFee = scanner.nextDouble();
        }while (entryFee<=0|| entryFee>20);
        //Имаме 4 играчи, инициализираме всеки от тях. Засега без въвеждане на пари.
        Player player1 = new Player("Peter",40);
        Player player2 = new Player("Georgi",50);
        Player player3 = new Player("Dimitar",60);
        Player player4 = new Player("Stanimir",40);
        // Правим масив от играчи и ги въвеждам в него
        Player [] players = new Player[4];
        players[0] = player1;
        players[1] = player2;
        players[2] = player3;
        players[3] = player4;
        // клас Рандом - за генериране на карти
        Random rnd = new Random();
        while (true){
            // Проверка за наличност на пари. Ако е останал 1 играч го обявяваме за краен победител.
            int countHasMoney = 0;
            double betPool = 0.0;
            for(int i = 0; i<players.length; i++){
                if(players[i]==null){
                    continue;
                }
                players[i].checkEntryMoney(entryFee);
                if(!players[i].isQuitGame()){
                    countHasMoney+=1;
                    //събиране на залозите за вход
                    betPool+=entryFee;
                    players[i].addOrRemoveMoney(entryFee*-1); //отрицателно, за да се извади
                }
                else{
                    players[i]=null; //изтриване от масива на играча, който напуска играта
                }
            }
            //Ако само 1 има пари, край на играта!
            if(countHasMoney==1){
                for (int i =0; i<players.length;i++){
                    if(players[i]!=null){
                        players[i].addOrRemoveMoney(betPool);
                        System.out.println("The winner is "+players[i].getName()+" who has "+players[i].getMoney()+" dollars!");
                        return;
                    }
                }
            }
            //Раздаваме картите на всеки играч, който има пари за входа
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < numberOfPlayers; j++) {
                    if(players[j]==null){
                        continue;
                    }
                    while (true) {
                        int card = rnd.nextInt(52);
                        boolean isValid = checkCard(players, card);
                        if (isValid) {
                            players[j].addCard(card);
                            break;
                        }
                    }
                }
            }
            //Създаваме масата
            int[] board = new int[5];
            //раздаваме картите на масата
            for (int i = 0; i < board.length; i++) {
                while (true) {
                    int card = rnd.nextInt(52);
                    boolean isValid = checkCard(players, card);
                    if (isValid) {
                        for (int j = 0; j < i; j++) {
                            if (card == board[j]) {
                                isValid = false;
                                break;
                            }
                        }
                    }
                    if (isValid) {
                        board[i] = card;
                        break;
                    }
                }
            }
            //Печат на картите на масата
            printBoardCards(board);
            // Генериране на резултатите на всеки играч
            for (int i = 0; i < players.length; i++) {
                if (players[i]==null){
                    continue;
                }
                players[i].createBestCombination(players[i].getInitialCards(), board);
            }

            //Определяне на победител
            Player winner = new Player("Winner",0);
            int indexFound = 0;
            for(int i =0; i<players.length; i++){
                if(players[i]!=null){
                    indexFound = i;
                    winner=players[i];
                    break;
                }
            }

            boolean draw = false;
            Player[] draws = new Player[countHasMoney]; //създаваме масив, който да държи играчите при равенство.
            int drawCount = 0;
            for (int i = indexFound+1; i < players.length; i++) {
                if(players[i]==null){
                    continue;
                }
                boolean newWinner = false;
                if(players[i].getName().equals(winner.getName())){
                    continue;
                }
                if (players[i].getBestScore() > winner.getBestScore()) {
                    newWinner = true;
                } else if (players[i].getBestScore() == winner.getBestScore()) {
                    switch (winner.getBestScore()) {
                        case 10: //Royal Straight Flush
                            draw = true;
                            break;
                        case 9: //Straight Flush
                            for (int j = winner.getBestCombination().length - 1; j >= 0; j--) {
                                if (players[i].getBestCombination()[j] > winner.getBestCombination()[j]) {
                                    newWinner = true;
                                    break;
                                }
                                else if(players[i].getBestCombination()[j] < winner.getBestCombination()[j]){
                                    break;
                                }
                                if (j == 0) {
                                    if (players[i].getBestCombination()[j] == winner.getBestCombination()[j]) {
                                        draw = true;
                                    }
                                }
                            }
                            break;
                        case 8: //Four of a kind
                            if (winner.getBestFours() < players[i].getBestFours()) {
                                newWinner = true;
                            }
                            break;
                        case 7: //Full House
                            if (winner.getBestThrees() < players[i].getBestThrees()) {
                                newWinner = true;
                            } else if (winner.getBestThrees() == players[i].getBestThrees()) {
                                if (winner.getBestTwos() < players[i].getBestTwos()) {
                                    newWinner = true;
                                } else if (winner.getBestTwos() == players[i].getBestTwos()) {
                                    draw = true;
                                }
                            }
                            break;
                        case 6: //Flush
                            for (int j = winner.getBestCombination().length - 1; j >= 0; j--) {
                                if (players[i].getBestCombination()[j] > winner.getBestCombination()[j]) {
                                    newWinner = true;
                                    break;
                                }
                                else if(players[i].getBestCombination()[j] < winner.getBestCombination()[j]){
                                    break;
                                }
                                if (j == 0) {
                                    if (players[i].getBestCombination()[j] == winner.getBestCombination()[j]) {
                                        draw = true;
                                    }
                                }
                            }
                            break;
                        case 5: //Straight
                            for (int j = winner.getBestCombination().length - 1; j >= 0; j--) {
                                if (players[i].getBestCombination()[j] > winner.getBestCombination()[j]) {
                                    newWinner = true;
                                    break;
                                }
                                else if(players[i].getBestCombination()[j] < winner.getBestCombination()[j]){
                                    break;
                                }
                                if (j == 0) {
                                    if (players[i].getBestCombination()[j] == winner.getBestCombination()[j]) {
                                        draw = true;
                                    }
                                }
                            }
                            break;
                        case 4: //Three of a kind
                            if (players[i].getBestThrees() > winner.getBestThrees()) {
                                newWinner = true;
                            } else if (players[i].getBestThrees() == winner.getBestThrees()) {
                                int cardW1 = -1;
                                int cardW2 = -2;
                                int card1 = -1;
                                int card2 = -2;
                                for (int j = 0; j < winner.getBestCombination().length; j++) {
                                    if (winner.getBestCombination()[j] != winner.getBestThrees()) {
                                        if (cardW1 >= 0) {
                                            cardW2 = winner.getBestCombination()[j];
                                        } else {
                                            cardW1 = winner.getBestCombination()[j];
                                        }
                                    }
                                }
                                for (int k = 0; k < players[i].getBestCombination().length; k++) {
                                    if (players[i].getBestCombination()[k] != players[i].getBestThrees()) {
                                        if (card1 >= 0) {
                                            card2 = players[i].getBestCombination()[k];
                                        } else {
                                            card1 = players[i].getBestCombination()[k];
                                        }
                                    }
                                }
                                int cardWMax = Math.max(cardW1, cardW2);
                                int cardWMin = Math.min(cardW1, cardW2);
                                int cardMax = Math.max(card1, card2);
                                int cardMin = Math.min(card1, card2);
                                if (cardMax > cardWMax) {
                                    newWinner = true;
                                } else if (cardMax == cardWMax) {
                                    if (cardMin > cardWMin) {
                                        newWinner = true;
                                    } else if (cardMin == cardWMin) {
                                        draw = true;
                                    }
                                }
                            }
                            break;
                        case 3: //Two pairs
                            if (players[i].getBestTwosPair1() > winner.getBestTwosPair1()) {
                                newWinner = true;
                            } else if (players[i].getBestTwosPair1() == winner.getBestTwosPair1()) {
                                if (players[i].getBestTwosPair2() > winner.getBestTwosPair2()) {
                                    newWinner = true;
                                } else if (players[i].getBestTwosPair2() == winner.getBestTwosPair2()) {
                                    int cardW5 = -1;
                                    int card5 = -1;
                                    for (int j = 0; j < winner.getBestCombination().length; j++) {
                                        if (winner.getBestCombination()[j] / 4 != winner.getBestTwosPair1() || winner.getBestCombination()[j] / 4 != winner.getBestTwosPair2()) {
                                            cardW5 = winner.getBestCombination()[j] / 4;
                                            break;
                                        }
                                    }
                                    for (int k = 0; k < players[i].getBestCombination().length; k++) {
                                        if (players[i].getBestCombination()[k] / 4 != players[i].getBestTwosPair1() || players[i].getBestCombination()[k] / 4 != players[i].getBestTwosPair2()) {
                                            card5 = players[i].getBestCombination()[k] / 4;
                                            break;
                                        }
                                    }
                                    if (card5 > cardW5) {
                                        newWinner = true;
                                    } else if (card5 == cardW5) {
                                        draw = true;
                                    }
                                }
                            }
                            break;
                        case 2: //One pair
                            if (players[i].getBestTwosPair() > winner.getBestTwosPair()) {
                                newWinner = true;
                            } else if (players[i].getBestTwosPair() == winner.getBestTwosPair()) {
                                int cardW1 = -1;
                                int cardW2 = -1;
                                int card1 = -1;
                                int card2 = -1;
                                int cardW3 = -1;
                                int card3 = -1;
                                int counter = 0;
                                for (int j = 0; i < winner.getBestCombination().length; j++) {
                                    if (winner.getBestCombination()[j] / 4 != winner.getBestTwosPair()) {
                                        if (counter == 0) {
                                            cardW1 = winner.getBestCombination()[j] / 4;
                                            counter++;
                                        } else if (counter == 1) {
                                            cardW2 = winner.getBestCombination()[j] / 4;
                                            counter++;
                                        } else if(counter==2){
                                            cardW3 = winner.getBestCombination()[j] / 4;
                                            counter = 0;
                                            break;
                                        }
                                    }
                                }
                                for (int k = 0; k < players[i].getBestCombination().length; k++) {
                                    if (players[i].getBestCombination()[k] / 4 != players[i].getBestTwosPair()) {
                                        if (counter == 0) {
                                            card1 = players[i].getBestCombination()[k] / 4;
                                            counter++;
                                        } else if (counter == 1) {
                                            card2 = players[i].getBestCombination()[k] / 4;
                                            counter++;
                                        } else if(counter==2) {
                                            card3 = players[i].getBestCombination()[k] / 4;
                                            counter = 0;
                                            break;
                                        }
                                    }
                                }
                                int[] arrW = new int[3];
                                arrW[0] = cardW1;
                                arrW[1] = cardW2;
                                arrW[2] = cardW3;
                                sortArray(arrW);
                                int[] arr = new int[3];
                                arr[0] = card1;
                                arr[1] = card2;
                                arr[2] = card3;
                                sortArray(arr);
                                for (int l = arrW.length - 1; l >= 0; l--) {
                                    if (arr[l] > arrW[l]) {
                                        newWinner = true;
                                        break;
                                    }
                                    else if(arr[l]<arrW[l]){
                                        break;
                                    }
                                    if (l == 0) {
                                        if (arr[l] == arrW[l]) {
                                            draw = true;
                                        }
                                    }
                                }
                            }
                            break;
                        case 1: //High Card
                            //Отново сортираме масива, за да се избегнат грешки
                            sortArray(winner.getBestCombination());
                            sortArray(players[i].getBestCombination());
                            for (int j = winner.getBestCombination().length - 1; j >= 0; j--) {
                                if (players[i].getBestCombination()[j] > winner.getBestCombination()[j]) {
                                    newWinner = true;
                                    break;
                                }
                                else if(players[i].getBestCombination()[j] < winner.getBestCombination()[j]){
                                    break;
                                }
                                if (j == 0) {
                                    if (players[i].getBestCombination()[j] == winner.getBestCombination()[j]) {
                                        draw = true;
                                    }
                                }
                            }
                            break;
                    }
                }
                if (newWinner) { // Ако има победител
                    winner = players[i];
                    draw = false;
                    for (int j = 0; j < draws.length; j++) {
                        draws[j] = null;
                    }
                    drawCount = 0;
                }
                if (draw) { //При равенство
                    if (drawCount == 0) {
                        draws[0] = winner;
                        draws[1] = players[i];
                        drawCount = 2;
                    } else {
                        draws[drawCount] = players[i];
                        drawCount++;
                    }
                }
            }
            //Печатаме картите на всеки играч
            for (int i = 0; i < players.length; i++) {
                if(players[i]!=null) {
                    players[i].printRealCards();
                }
            }

            //Печат в случай на равенство
            if (draw) {
                System.out.println("The following players has equal result:");
                for (int i = 0; i < draws.length; i++) {
                    if (draws[i] == null) {
                        break;
                    }
                    System.out.println(draws[i].getName());
                    //При равенство се разделят залозите между играчите, които имат равенство
                    for (int j = 0; j<players.length;j++){
                        if(players[i]!=null&&players[j]!=null) {
                            if (draws[i].getName().equals(players[j].getName())) {
                                players[j].addOrRemoveMoney(betPool / drawCount);
                            }
                        }
                    }
                }
            }
            //Печат на победител
            else {
                System.out.println("The winner of the draw is: " + winner.getName()+" and won "+betPool+" dollars!");
                for (int i = 0; i<players.length; i++){
                    if(players[i]!=null) {
                        if (players[i].getName().equals(winner.getName())) {
                            players[i].addOrRemoveMoney(betPool);
                        }
                        System.out.println(players[i].getName() + " has " + players[i].getMoney() + " dollars!");
                    }
                }
            }
            scanner.nextLine();
            int choice;
            do {
                System.out.println("Press 1 to draw cards and 2 to quit the game");
                choice = scanner.nextInt();
            }while (choice<1||choice>2);
            if(choice ==2){
                for (int i = 0; i<players.length;i++){
                    if(players[i]!=null){
                        System.out.println("At the end of the game, "+players[i].getName()+" has "+players[i].getMoney()+" dollars!");
                    }
                }
                System.out.println("GAME OVER!");
                return;
            }
            for (int i = 0; i<players.length;i++){
                if(players[i]!=null) {
                    players[i].nullParameters();
                }
            }

        }


    }
    //Проверка за уникалност на карта
    static boolean checkCard(Player [] players, int card){
        boolean isValid = true;
        for (int i = 0; i< players.length; i++){
            if(players[i]!=null){
                Player player = players[i];
                for(int j = 0; j<player.getInitialCards().length; j++){
                    if(card==player.getInitialCards()[j]){
                        isValid=false;
                        break;
                    }
                }
                if(!isValid){
                    break;
                }
            }
        }
        return isValid;
    }
    //Сортиране на масив
    static void sortArray(int [] combination) {
        for (int l = 0; l < combination.length / 2; l++) {
            int maxIndex = combination.length - l - 1;
            int minIndex = l;
            for (int m = l; m < combination.length - l; m++) {
                if (combination[m] < combination[minIndex]) {
                    minIndex = m;
                }
                if (combination[m] > combination[maxIndex]) {
                    maxIndex = m;
                }

            }
            int temp = combination[minIndex];
            combination[minIndex] = combination[l];
            combination[l] = temp;

            if (maxIndex == l) {
                maxIndex = minIndex;
            }
            temp = combination[maxIndex];
            combination[maxIndex] = combination[combination.length - l - 1];
            combination[combination.length - l - 1] = temp;
        }
    }
    static void printBoardCards(int [] board){
        System.out.println("The cards on the board are:");
        for(int i = 0; i<board.length; i++){
            int num = board[i]/4;
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
        for(int i = 0; i<board.length; i++){
            int num = board[i]%4;
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
}
