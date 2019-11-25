# SNACKKING
 
 We used three kinds of code: Java, PHP, and XML files.
 
 Java files are in SNACKKING/app/src/main/java/com/example/snackking. These files are used to operate the android application. For the detailed explanation, please refer to README.md file in that folder.
 
 PHP files are in SNACKKING/PHP files. These files are used to connect the android application with database and server and manage the data in database. For the detailed explanation, please refer to README.md file in that folder.

 XML files are in SNACKKING/app/src/main/res/layout. These files are used to design user interface for the android application. For the detailed explanation, please refer to below explanations.


<h2>XML Files Explanations</h2>

- <b>Login</b>
  - <b>activity_login.xml</b> : Design of login page
  
- <b>Search system</b>
  - <b>activity_search_combined.xml</b> : Design for search page (both using name and keywords). First page of search system
  - <b>activity_search_keyword_result.xml</b> : Design for page for search result using keywords
  - <b>activity_snack_info.xml</b> : Design of snack information page
  - <b>activity_snack_review.xml</b> : Design of snack review page
  - <b>search_snack_row_view.xml</b> : The template for each element in the search result (both using name and keywords)
  
- <b>Recommendation system</b>
  - <b>activity_recommendation.xml</b> : Design for recommendation page. If the user have already requested recommendation, this design shows the responses. If not, it shows the page where users can choose two options: requesting recommendation and respond to others' requests
  - <b>activity_recommendation_respond.xml</b> : Show each requests with candidates
  - <b>activity_recommendation_response_result.xml</b> : Show the responses from other users
  - <b>recommendation_snack_view.xml</b> : The template for each element in the response
  
- <b>Achievement system</b>
  - <b>activity_achievement.xml</b> : Design for achievement page. It shows the list of completed/incompleted achievement tasks
  - <b>listview_achievement.xml</b> : The template for each element in the achievement (containg progress bar, image, etc)
  
- <b>Setting</b>
  - <b>activity_setting.xml</b> : Design for setting page.
