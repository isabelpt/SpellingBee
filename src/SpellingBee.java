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
 * @author Zach Blick, Isabel Prado-Tucker
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
    public void generate() {
        generateWords("", letters);
    }

    /**
     * The generateWords method recursively creates every possible set of chars) from the letters given
     * @param str1 originally empty, stores created word
     * @param str2 original set of chars, chars removed as words made
     * @return String to be able to recursively create words
     */
    public String generateWords(String str1, String str2) {
        // Base case: when second string is empty, so the largest word in that recursive segment has been created
        if (str2.length() == 0) {
            return str1;
        }
        // Adds word from first string every time, to make sure every length and possible has been created
        if (!str1.isEmpty()) {
            words.add(str1);
        }
        // For-loop to add each possible next character
        for (int i = 0; i < str2.length(); i++) {
            // Adds a char to the end
            String sub1 = str1 + str2.charAt(i);
            // Removes that char from the other string
            String sub2 = str2.substring(0, i) + str2.substring(i+1);
            // Recursively calls method again
            String result = generateWords(sub1, sub2);
            // As long as the word is not empty, add word to the list
            if (!result.isEmpty()) {
                words.add(result);
            }
        }
        return "";
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        words = mergeSort(words, 0, words.size());
    }

    /**
     * Combines two arraylists, while sorting them
     * @param arr1 first array
     * @param arr2 second array
     * @return merged arraylist
     */
    public ArrayList<String> merge (ArrayList<String> arr1, ArrayList<String> arr2) {
        // Create new arraylist to merge into
        ArrayList<String> merged = new ArrayList<String>();
        // Track indices of each arraylist
        int i = 0, j = 0;
        // While indices valid for both arraylists
        while (i < arr1.size() && j < arr2.size()) {
            // Add smaller element to the end of the merged list
            if (arr1.get(i).compareTo(arr2.get(j)) < 0) {
                merged.add(i+j, arr1.get(i));
                i++;
            }
            else {
                merged.add(i+j, arr2.get(j));
                j++;
            }
        }
        // Add the rest of the elements from the arraylist that still has elements
        // Only one while loop will run
        while (i < arr1.size()) {
            merged.add(i+j, arr1.get(i));
            i++;
        }
        while (j < arr2.size()) {
            merged.add(i+j, arr2.get(j));
            j++;
        }
        // Return merged list
        return merged;
    }

    /**
     * Recursive algorithm to apply mergesort to the arraylist of generated words
     * @param words arraylist of words recursively created
     * @param low low index for portion being looked at
     * @param high high index for portion being looked at
     * @return sorted arraylist
     */
    public ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
        // Base case: return arraylist of one item
        if (high - low <= 1) {
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(words.get(low));
            return newArr;
        }

        // Find midpoint of segment being looked at
        int med = (high + low) / 2;
        // Recursively run mergesort on divided segments
        ArrayList<String> arr1 = mergeSort(words, low, med);
        ArrayList<String> arr2 = mergeSort(words, med + 1, high);

        // Return merged arrays
        return merge(arr1, arr2);
    }

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
    public void checkWords() {
        int i = 0;
        // For each word, run binary search to see if it is in the dictionary
        while (i < words.size()) {
            // If not in dictionary
            if (!binarySearch(words.get(i), 0, DICTIONARY_SIZE)) {
                // Remove word from arraylist
                words.remove(i);
            }
            else {
                i++;
            }
        }
    }

    /**
     * Recursively run binary search to see if word is in the dictionary
     * @param word word from list of words created
     * @param beg low index of segment being looked at
     * @param end high index of segment being looked at
     * @return true if word found in dictionary, false otherwise
     */
    public boolean binarySearch(String word, int beg, int end) {
        // Find midpoint of segment
        int med = (beg + end) / 2;
        // Base case: if the word is the middle value or if only looking at one item
        if (word.equals(DICTIONARY[med])) {
            return true;
        }
        // Need +1 to cover if down to one element and it isn't equal to the word
        if (beg + 1 >= end) {
            return false;
        }

        // If the word is before the med point, look to the left
        if (word.compareTo(DICTIONARY[med]) < 0) {
            return binarySearch(word, beg, med);
        }
        // Else, look to the right
        return binarySearch(word, med, end);

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
    }
}
