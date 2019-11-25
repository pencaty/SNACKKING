<h2>PHP Files Explanations</h2>


- <b>index.html</b> : Front page of our hosting server(snack.dothome.co.kr). Since it is not needed in application development, we do not use it.

- <b>Database Connection</b>
  - <b>dbcon.php</b> : Basic setting to connect php files with database
  
- <b>Login</b>  
  - <b>insert_user_data.php</b> : For login. If there is no same id in database, the system add it to the database (make user individual table and add user id to User_data table). If not, just bring the data (using user id).

- <b>Search system</b>
  - <b>get_snack_data.php</b> : Bring all snack information from database (Snack_Score table)
  - <b>get_snack_order_by_taste.php</b> : Bring all snack information from database and sort them by the score of taste
  - <b>get_snack_order_by_cost.php</b> : Bring all snack information from database and sort them by the score of cost
  - <b>get_user_individual_data.php</b> : Bring the user's previous review about particular snack
  - <b>get_snack_keyword_score.php</b> : Bring the score of taste/cost, the top 3 keywords, and the score of each top 3 keywords of particular snack
  - <b>update_score.php</b> : Update the value of taste/cost/number of evaluation with new data
  - <b>update_snack.php</b> : Update the score of each keyword and change top 3 keywords based on updated score
  - <b>review_user_data.php</b> : If the user has reviewed before, change the value. If not, store the value in the database

- <b>Recommendation system</b>
  - <b>get_chatroom_data.php</b> : Bring all requests (with keywords) from database (Chatroom table)
  - <b>make_individual_recommend.php</b> : If the user is not requesting recommendation, his/her request (with keywords and comments) is added to database (his/her request is added to Chatroom table and recommmend_(user id) table is created)
  - <b>get_individual_response.php</b> : Bring all responses the user received (from recommend_(user id) table)
  - <b>answer_to_request.php</b> : If the user did not answer to the particular request, his/her answer is stored in the database (recommend_(user id) table)
  - <b>delete_accepted_request.php</b> : After the user clicks 'Accept' button, recommend_(user id) table is deleted and the related row in Chatroom table is also deleted
  - <b>get_snack_data</b> : This file is also used in recommendation system, but it is already explained in 'Search system' part.
  

- <b>Achievement system</b>
  - <b>get_review_num_for_achievement.php</b> : Bring the number of reviews that the user has written
