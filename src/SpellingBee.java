import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate()
    {
        String newWords = "";
        // YOUR CODE HERE — Call your recursive method!
        generateHelper(newWords, letters);

    }

    public void generateHelper(String w, String newLetters)
    {
        String newWord;
        if (newLetters.length() == 0)
        {
            words.add(w);
            return;
        }


        for (int i = 0; i < newLetters.length(); i++)
        {
            words.add(w);
            generateHelper(w + newLetters.charAt(i), newLetters.substring(0,i) + newLetters.substring( i + 1));
        }
    }

    public void sort()
    {
        words = mergeHelper(0, words.size() - 1);
    }

    public ArrayList<String> mergeHelper(int left, int right)
    {
        if (left == right)
        {
            ArrayList<String> one = new ArrayList<>();
            one.add(words.get(left));
            return one;
        }
        int mid = (left + right) / 2;
        ArrayList<String> words1 = mergeHelper(left, mid);
        ArrayList<String> words2 = mergeHelper(mid + 1, right);
        return merge(words1, words2);
    }

    public ArrayList<String> merge(ArrayList<String> words1, ArrayList<String> words2)
    {
        ArrayList<String> big = new ArrayList<>();
        while (!words1.isEmpty() && !words2.isEmpty())
        {
            if (words1.get(0).compareTo(words2.get(0)) < 0)
            {
                big.add(words1.remove(0));
            }
            else
            {
                big.add(words2.remove(0));
            }
        }
        while (!words1.isEmpty())
        {
            big.add(words1.remove(0));
        }
        while (!words2.isEmpty())
        { // Fixed: Check words2
            big.add(words2.remove(0));
        }
        return big;
    }




//    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
      //  that will find the substrings recursively.





    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords()
    {
        // YOUR CODE HERE
        for (int i = 0 ; i < words.size(); i++)
        {
            boolean exists = true;
            exists = checkerHelper(0, DICTIONARY_SIZE, words.get(i));
            if (exists == false)
            {
                words.remove(i);
                i--;
            }
        }

    }

    public boolean checkerHelper(int start, int end, String word)
    {
        int mid = (start + end) / 2;
        if (start > end)
        {
            return false;
        }
        else if (DICTIONARY[mid].equals(word))
        {
            return true;
        }
        else if (word.compareTo((DICTIONARY[mid])) < 0)
        {
            return checkerHelper(start, mid - 1, word);
        }
        else
        {
            return checkerHelper(mid + 1, end, word);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
        System.out.println("a".compareTo("b"));
    }
}
