# SNACKKING
 
 <h2>Brief Overview</h2>
<p>SNACKKING is a project for KAIST CS473(Interoduction to Social Computing). This application can provide snack information with keywords&reviews and support user-user recommendation service. We used Marvel to make design prototype, and Android Studio to develop actual application. Detailed explanations are in our reports.</p>
<br>
 
 <h2>Collaborators</h2>
  <p><a href="https://github.com/pencaty">Changgeon Ko</a><br>
  <a href="https://github.com/CubeDo">Chanyoung Chung</a><br> </p>
 <br>
 
 <h2>Files Overview</h2>
 
 We used three kinds of code: Java, PHP, and XML files.
 
 Java files are in SNACKKING/app/src/main/java/com/example/snackking. These files are used to operate the android application. For the detailed explanation, please refer to below explanations.
 
 XML files are in SNACKKING/app/src/main/res/layout. These files are used to design user interface for the android application. For the detailed explanation, please refer to below explanations.
 
 PHP files are in SNACKKING/PHP files. These files are used to connect the android application with database and server and manage the data in database. For the detailed explanation, please refer to README.md file in that folder.
 
<br><br>
<h2>JAVA Files Explanations</h2>

- <b>Login</b>
  - <b>Login.java</b> : The first page of the application. The users have to enter id in order to use the application.

- <b>Structure for whole program</b>
  - <b>Snack_DataStructure.java</b> : In order to bring all information of each snack.

- <b>Search System</b>
  - <b>Search_Adapter.java</b> : The result of search should be shown on Listview. This file is a template for each element(snack).
  - <b>Search_Combined.java</b> : This file contains two search methods: using name and using keywords. The result of search using name is shown right after the user writes down some letters in textbox.
  - <b>Search_Keyword_Result.java</b> : Show the result of search using keywords. The result can be sorted by two standard: taste and cost.
  - <b>Snack_Info.java</b> : The page for detailed information (score and distribution for taste/cost) of each snack.
  - <b>Snack_Review.java</b> :Tthe page where the users can leave review (rate taste/cost and choose keywords (min 1, max 3)).

- <b>Structure for recommendation system</b>
  - <b>Response_DataStructure.java</b> : In order to bring data (user_id and selected snack) at once. It is used when bringing other users' recommendation.
  - <b>Chatroom_DataStructure.java</b> : In order to bring data (user_id, keyword1, keyword2, keyword3, and comment) at once. It is used when the user requests a recommendation to other users.

- <b>Recommendation system</b>
  - <b>Recommendation.java</b> : If the user previously requested the recommendation, the current responded result is shown. If not, the user can select among two options: request a recommendation or respond to others' request.
  - <b>Recommendation_Adapter.java</b> : The list of responses for the request should be shown on Listview. This file is a template for each element (each response).
  - <b>Recommendation_respond.java</b> : The user can answer or skip on other users' request.
  - <b>Recommendation_response_result.java</b> : Show the result of current responses that the user gets.

- <b>Structure for achievement system</b>
  - <b>AchievementData.java</b> : In order to bring data at once. It is used when making progress bar for each achievement task.

- <b>Achievement system</b>
  - <b>Achievement.java</b> : Show the completed/incompleted achievement tasks.
  - <b>ListAdapter_Achievement.java</b> : Each progress bar (with an image and achievement name) should be shown on Listview. This file is a template for each progressbar(achievement task).

- <b>Setting</b>
  - <b>Setting.java</b> : Create setting page. Every functions in this page are not related to our tasks so it just show message 'Implementation in progress'.

<br><br>
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
