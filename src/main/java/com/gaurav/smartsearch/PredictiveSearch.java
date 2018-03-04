package com.gaurav.smartsearch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.gaurav.smartsearch.com.gaurav.smartsearch.algo.Stemmer;
import com.gaurav.smartsearch.com.gaurav.smartsearch.util.SmartSearchSupport;

import static com.gaurav.smartsearch.com.gaurav.smartsearch.util.SmartSearchSupport.getDbStringForColumn;


/**
 * Created by gaurav on 24/1/17.
 */

public class PredictiveSearch extends SQLiteOpenHelper {

    private Context context;
    private static final String ftsTableName = "FTS4_Table";
    private static final String TAG = "PredictiveSearch";


    public PredictiveSearch(Context context, String database_name, int database_version) {
        super(context, database_name, null, database_version);
        this.context = context;


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    /**
     * Get the list of matches sans prediction for a word
     *
     * @param partWord
     * @return List of matched db entries
     */
    public ArrayList<String> getSearchList(String partWord) {

        try {
            if (partWord.isEmpty()) {
                return null;
            }

            return getMatchesWOStemming(partWord);
        } catch (Exception ex) {
            Log.e(TAG, "getSearchList: Exception in generating search results ", ex);
        }

        return null;
    }


    /***
     * creates FTS Table
     *
     * @param dbToBeSearched
     * @param tableOfData
     * @param columnNames
     * @return success status
     */
    public boolean createFTS4Table(String dbToBeSearched, String tableOfData, ArrayList<String> columnNames) {
        boolean success = true;
        String fromTable = "`" + dbToBeSearched + "`" + "." + "`" + tableOfData + "`";
        String attachDb = context.getDatabasePath(dbToBeSearched).getAbsolutePath();

        try {
            getWritableDatabase().execSQL("ATTACH DATABASE `" + attachDb + "` AS `" + dbToBeSearched + "`");
            getWritableDatabase().execSQL("CREATE VIRTUAL TABLE if not exists " + ftsTableName + " USING fts4 ( " + getDbStringForColumn(columnNames) + " )");
            getWritableDatabase().execSQL("INSERT INTO " + ftsTableName + " SELECT " + getDbStringForColumn(columnNames) + " FROM " + fromTable);
        } catch (Exception ex) {
            Log.e(TAG, "createFTS4Table: Error in generating FTS table ", ex);
            success = false;
        }

        return success;


    }

    /**
     * Function that actually performs the matching sans prediction
     */
    public ArrayList<String> getMatchesWOStemming(String partWord) {
        ArrayList<String> wordList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + ftsTableName + " WHERE word match ?", new String[]{partWord + "*"});

        try {

            if (cursor.moveToFirst()) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String word = cursor.getString(cursor.getColumnIndex("word"));
                    wordList.add(word);
                }
            }

            wordList = getPriorityList(partWord, wordList);
        } catch (Exception e) {
            Log.e(TAG, "getMatchesWOStemming: Exception in getting a search results ", e);
            e.printStackTrace();
        } finally {
            cursor.close();
        }


        return wordList;
    }

    /**
     * Function that sorts a list of words based on the probability of it matching a given word
     */
    ArrayList<String> getPriorityList(String word, ArrayList<String> list) {

        HashMap<Integer, String> sortMap = new HashMap<>();
        for (String s : list) {
            sortMap.put(SmartSearchSupport.getLevenshteinDistance(word, s), s);
        }
        ArrayList<Integer> sortKeys = new ArrayList<>(sortMap.keySet());
        Collections.sort(sortKeys);
        ArrayList<String> op = new ArrayList<>();
        for (Integer val : sortKeys) {
            op.add(sortMap.get(val));
        }

        return op;

    }


    /**
     * Main method for a prediction based search using stemming and getPriorityList
     *
     * @param word
     * @return
     */

    public ArrayList<String> getMatchesWStemming(String word) {

        Stemmer stemmer = new Stemmer();
        stemmer.add(word.toCharArray(), word.length());
        stemmer.stem();
        String stemmedWord = stemmer.toString();


        ArrayList<String> wordList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + ftsTableName + " WHERE word match ?", new String[]{stemmedWord + "*"});

        try {

            if (cursor.moveToFirst()) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String word_1 = cursor.getString(cursor.getColumnIndex("word"));
                    wordList.add(word_1);
                }
            }

            wordList = getPriorityList(stemmedWord, wordList);
        } catch (Exception e) {
            Log.e(TAG, "getMatchesWStemming: ", e);
        } finally {
            cursor.close();
        }


        return wordList;
    }


    /**
     * Rebuild FTS table
     */
    public void ftsRebuilder() {
        try {
            getWritableDatabase().execSQL("INSERT INTO " + ftsTableName + " VALUES(\"rebuild\")");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}



