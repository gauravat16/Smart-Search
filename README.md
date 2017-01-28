# Smart-Search

A combination of Full Text Search, Ranking and Stemming to give you the best output for your android application/Application using SQLite3.


 <b>How to use</b>

Add the predictiveSearch.java file to your project. [Download](https://github.com/gauravat16/Smart-Search/archive/v1.0.zip)

##Example

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

####1. createFTS4Table(String dbToBeSearched, String tableOfData, ArrayList<String> columnNames)

   Creates/Builds the Full Text Search Virtual Table.

a. <strong>String dbToBeSearched</strong>
       The name of the database that has the table you want to smart-search.
    
   b.<strong>String tableOfData</strong> 
       The name of the table containg the data.
    
   c. <strong>ArrayList<String> columnNames</strong>
       The list of all the columms in above table.
       
####2. ftsRebuilder()

   Rebulids the FTS Virtual Table. <strong>Run this each time you make changes to your main database</strong>
   
####3. getSearchList(String partWord)

   returns the list of the words that match with your query. <strong>This is without stemming search.</strong>
   
####4. String getGuessWord(String word)
   
   Performs everything that getSearchList() does but with stemming.<strong> Use it for prediction.<strong>
   

   
   
