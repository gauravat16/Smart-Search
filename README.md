# Smart-Search
A predictive search API that gives you the freedom to search on millions of entries in a database with ease.
A combination of Full Text Search, Ranking and Stemming to give you the best output for your android application/Application using SQLite3.

<img src="https://cloud.githubusercontent.com/assets/12914180/22394905/b0904936-e553-11e6-9b0e-9ca50af258c4.png" width="300" height="500">

[App Link](https://play.google.com/store/apps/details?id=gaurav.lookup)


**How to use**

Add the predictiveSearch.java file to your project. [Download](https://github.com/gauravat16/Smart-Search/archive/v1.0.zip)

**Example**

          PredictiveSearch search = new PredictiveSearch(getApplicationContext());
          ArrayList<String> columns = new ArrayList<>();
          columns.add("word");
      try {
              search.createFTS4Table("learn.db", "entries", columns);
              search.ftsRebuilder(); //Use it to rebulid after any change
              ArrayList<String> resp1 = search.getSearchList("2"); //Get result w/o stemming
              ArrayList<String> resp2 = search.getGuessWord("2"); //Get result with stemming - predictive

          } catch (Exception ex) {
              ex.printStackTrace();
          } finally {
              search.close();
          }



###Important functions

**1. createFTS4Table(String dbToBeSearched, String tableOfData, ArrayList<String> columnNames)**

    Creates/Builds the Full Text Search Virtual Table.

 * String dbToBeSearched
         The name of the database that has the table you want to smart-search.

 * String tableOfData
         The name of the table containing the data.

 * ArrayList<String> columnNames
         The list of all the columns in above table.
       
**2. ftsRebuilder()**

    Rebulids the FTS Virtual Table. Run this each time you make changes to your main database.
   
**3. getSearchList(String partWord)**

    returns the list of the words that match with your query. This is without stemming search.
   
**4. String getGuessWord(String word)**
   
    Performs everything that getSearchList() does but with stemming. Use it for prediction.
   

   
   
