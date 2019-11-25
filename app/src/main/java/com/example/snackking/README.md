JAVA Files explanation

- login
  - Login.java : The first page of the application. The users have to enter id in order to use the application.

- structure for whole program
  - Snack_DataStructure.java : In order to bring all information of each snack.

- search system
  - Search_Adapter.java : The result of search should be shown on Listview. This file is a template for each element(snack).
  - Search_Combined.java : This file contains two search methods: using name and using keywords. The result of search using name is shown right after the user writes down some letters in textbox.
  - Search_Keyword_Result.java : Show the result of search using keywords. 
  - Snack_Info.java : The page for detailed information (score and distribution for taste/cost) of each snack.
  - Snack_Review.java :Tthe page where the users can leave review (rate taste/cost and choose keywords (min 1, max 3).

- structure for recommendation system
  - Response_DataStructure.java : In order to bring data (user_id and selected snack) at once. It is used when bringing other users' recommendation.
  - Chatroom_DataStructure.java : In order to bring data (user_id, keyword1, keyword2, keyword3, and comment) at once. It is used when the user requests a recommendation to other users.

- recommendation system
  - Recommendation.java : If the user previously requested the recommendation, the current responded result is shown. If not, the user can select among two options: request a recommendation or respond to others' request.
  - Recommendation_Adapter.java : The list of responses for the request should be shown on Listview. This file is a template for each element (each response).
  - Recommendation_respond.java : The user can answer or skip on other users' request.
  - Recommendation_response_result.java : Show the result of current responses that the user gets.

- structure for achievement system
  - AchievementData.java : In order to bring data at once. It is used when making progress bar for each achievement task.

- achievement system
  - Achievement.java : Show the completed/incompleted achievement tasks.
  - ListAdapter_Achievement.java : Each progress bar (with an image and achievement name) should be shown on Listview. This file is a template for each progressbar(achievement task).

- setting
  - Setting.java : Create setting page. Every functions in this page are not related to our tasks so it just show message 'Implementation in progress'.
