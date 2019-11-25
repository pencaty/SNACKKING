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
