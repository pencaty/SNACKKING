# Low-Fi Prototype by COOK-King

### Problem Statement

​	People take into account various factors like taste and cost when choosing snacks. Some people do not have enough information about new snacks to choose satisfactory products. Other people already have much information about familiar snacks, but they still feel stressed when they have to choose one. These two situations cause indecision and induce stress. Besides, gathering enough information about each snack is a waste of time and money.

### Tasks

#### 1. Give people organized information

​	People are willing to search for information about snacks they do not know well. The opinions of various people are subjective and they are not integrated, so it takes a lot of time and cost to obtain enough information.

#### 2. Reduce the range of choice

​	There are many kinds of snacks in convenience stores, so customers have to choose from them. Nowadays, however, many people are suffering from decision-making disorder, which means people are having difficulty in choosing things and a variety of snacks are stressful for people.

#### 3. Motivate people to participate

​	As snack information, especially tastes and cost-efficiency, is subjective, listening to as many opinions as possible can help customers make decisions based on popular opinion.

### Prototype

#### Link to our prototype

[Prototype](https://marvelapp.com/5ci14a7)

#### Prototyping tool: Marvel

​	We designed the prototype through the prototype design tool provided by [Marvel](Marvelapp.com) because of the following reasons. First, since the method of usage is much simpler, creating a prototype through Marvel is faster than using other tools. Furthermore, we only need screen-to-screen operations, not other kinds of functions such as animations, and Marvel provides links between screens and visualizes them. It is simple to link multiple pages because we only need to designate the parts of space that are visible to the screen and link to other pages.

​	Our application needs multiple screen-transition operations and as we used Marvel for design tool, the prototype works well. Many buttons on one screen are linked to different pages and we could design them in a short time using only a few functions.

#### What didn't work well?

​	One of the main problems of Marvel is that we cannot make the hotspot region (a region that can be used to interact with users) round. Marvel only supports drag for setting hotspot region, and it is always rectangle shape. Also, we cannot adjust the (rectangle) region by numeric input, so it is hard to make the perfect region we expected. Therefore, while testing our prototype, we can observe some issues about touch.

#### Design Choices

​	In actual application to be developed in the high-fi period, we are planning to allow users to select keywords when searching, recommending, and reviewing. However, it is too wasteful to create pages for each keyword combinations at the low-fi level and the structure of the next page is consistent regardless of the combination of keywords. (The only difference is the result of searching, but the overall structure is still constant.) Therefore, we create only one page for each searching, recommending, and reviewing with a predetermined combination of keywords. (We selected **Chip** and **Salty** for snack search.) Along with, as the search system using keywords is an application's unique point, we omit it to make a search system through the snack name.

​	Also, we just want to investigate how users feel when using our application. Making people use a full-operating application is out of our scope. Thus, ratings on the screen are fake data. The achievement rate of each performance mission is also fake data that we have created.

​	We did not have enough data to construct a word cloud image. Thus, in our prototype, we inserted word cloud image from the internet (not related to evaluation keywords of snacks) as an alternative for our intention.

​	Furthermore, we thought that the detailed functions in the setting menu such as **Edit Profile** and **Customer Service** were unnecessary when testing user evaluations. Thus, we did not make designs for them. We also thought that choosing badge in achievement tag is also unessential so we skip to make it. (It is just an additional function.)

​	When a user clicks the **Send Request** button, five random users (with similar preferences) receive the request. They have five options: four different snack images and one “I will search for another snack” button. When they finish selecting a snack, it returns to the user who requested a recommendation and shows him/her as a chatroom format. However, we want to ask test participants only the UI for users  who request recommendations. So we skip to make a recommendation selection page.

​	Finally, in actual application, when responses to a recommendation request arrive, there will be a notification such as a pop-up message or icon. The notification continues to appear until the user clicks the **Accept** button. However, this requires that all pages be replicated to add a notification icon and allow the user to click the **Send Request** button before displaying the notification. We thought that it is time-consuming and might not affect prototype tests. Therefore, we decided not to develop such a function. We design the prototype to show responses to a request just after the user clicks the **Send Request** button.

#### Representative Screenshots

​	The following images are some representative screenshots of our prototype.

  <img src = "./images/Search_Snack_01.png" width= "32%" height = "32%"></img><img src = "./images/Recommend_Received.png" width= "32%" height = "32%"></img><img src = "./images/Achievement_Main.png" width= "32%" height = "32%"></img>

**[Figure 1]** A user can see brief information about snacks, like scores with distribution and word cloud using keywords. With word cloud, the user can figure out the characteristics of the snacks at a look. Moreover, as word cloud only gives information about features, two kinds of ratings can complement information not shown on the word cloud.

**[Figure 2]** When other users answer the request, the responses are displayed to the user as this image. The user can see responses at a glance and determine whether each response is helpful or not. Since there is a maximum of five choices, the range of choices is reduced.

**[Figure 3]** This page shows a list of achievement missions that can motivate people to participate well in crowdsourcing.

#### Instruction

​	When a user connects to the application, there are four buttons on the top menu. From the left, each icon represents **Search**, **Recommendation**, **Achievement**, and **Setting**, and the screen switches to the corresponding page when the user clicks each button.

##### 1. Search page

​	Several keywords are listed under categories such as types and tastes. Each keyword is added to the **Selected Tags** box when it is selected. Users can search for snacks by typing a name or by adding keywords. Clicking the magnifying glass icon, users can see the results and they can move into the snack information page by clicking snack image. On that page, users can view rate, distribution and word cloud. To review snacks, they have to click the **Make Review** button to score and select keywords. (In our prototype, only **Chip** and **Salty** can be selected when searching for snacks, and only **Snack_01** is available from the search results.)

##### 2. Recommendation page

​	Similar to the searching system, a user needs to select keywords. When the user selects keywords and clicks the **Send Request** button, the request is forwarded to five users. When one or more answers arrive, a notification appears on the screen of the person who requested it. The user can see the responses (in a chatroom format) by clicking the notification and it disappears after the user accepts the recommendation. (in this prototype, users can see responses just after they click **Send Request** button.)

##### 3. Achievement page

​	The achievement page is divided into two parts: finished achievements and ongoing achievement. The user can choose his/her achievement badge from the completed works. The user can also see how much each task is left on the unfinished achievement side.

##### 4. Setting page

​	A user can change his/her profile (including profile photos) and contact to the service center. (However, we thought that these functions were not essential to the design of the low-fi prototype so there were no additional pages.)

### Observations

#### Design / User Interface

##### 1. When the user moves to another menu and returns to the original menu, the screen does not retain its previous state. (P1) [High]

​	In our prototype, due to Marvel's complexity, if users move to some menu, the initial state of that menu is given. However, retaining the previous state of the application is a lot beneficial for users' misclicking.

​	We will add this functionality to our final application.

##### 2. The current interface makes it difficult for users to understand the fact that they can search for snacks using tags. (P1) [High]

​	In our main search page, there is no description that users can search snacks only with selected tags. We have to indicate that users can search snacks only with selected tags, too.

​	We will add a short description of tag-only searching on the searching page.

##### 3. There are too many menus. (P6) [Medium]

​	We can deal with this problem by:

1. Delete the Setting tab and locate it on the left / right side of the screen so that users can reach the setting tab by sliding screen.
2. Delete themenu bar and change the menus by sliding up / down.
3. Give some tutorials for the menu bar after users first use this application.

##### 4. There is no Dark Mode. (P8) [Medium]

​	We think that users mainly use this application at night, so for users, we are going to add a similar mode in the actual developing stage.

#### Search / Review

##### 1. No keyword/category proposal function. Users may want to suggest new keywords. (P1, P2, P9) [High]

​	Due to the variety of vocabulary, we are going to make a space where users can suggest new snacks/categories/keywords and adding them.

##### 2. Snack Company employees might write biased reviews or recommend only their products. (P3) [High]

​	Almost every other application with a similar aim to us have the same problem. Since our recommendation request is sent to the users in active, company employees can maintain in active all day and response for recommendation requests beneficial to their employers. So we will design a filtering system for spam reviews or recommends with some conditions. (Ex. Inappropriate connection time)

##### 3. The number of categories is too small. (P5, P9) [High]

​	We think that the number of categories should be not too large since simplicity and convenience is inversely proportional to the number of categories. However, we will consider some more categories that can be fit into our application.

##### 4. Users may want to read comments from other users (short, funny, and creative). (Only keywords can be seen in the current prototype.) (P5, P8) [Medium]

​	Our first design didn't include user-input keywords since it is hard to merge similar keywords to one keyword in the word cloud and we can filter inappropriate keywords. For this problem, we have to add new functionality to solve, but since we aim to provide users simpler information, we will not going to add a comment system. Instead, users can write a short review while recommending snacks.

##### 5. The application did not show information on best-sellers and new snacks. (P5) [Low]

​	Our main target users are someone who has a hard time choosing snacks or gathering information. For the second type of user, we can provide some information on best-sellers or new snacks as some information.

​	After implementing the main functionalities we will consider whether we will implement this functionality or not, but our main target is users who have a hard time to choose. Also, we want to keep our application simple. Therefore this functionality has a lower priority.

##### 6. The search category did not include keywords for context and mood. (P6) [High]

​	We can solve this problem by making new categories of context and mood.

##### 7. Users can misinterpret the term 'Cost' as actual snack prices. (The original intention was cost-efficiency.) (P6, P8) [High]

​	Since the space for cost-efficiency is not enough and we want to make this application simple, we have to find appropriate word or give a concrete description of the word 'cost'.

##### 8. There are no specific criteria for 'Cost'. (P6) [Low]

​	Though every people have different criteria for cost-efficiency, we thought that rating cost is still helpful for users.

##### 9. Searching results did not display price information. (P8) [Low]

​	Cost of snacks can be various for different conditions like the size of snacks or the place where customers buy snacks. Therefore it would be hard to organize price information. We can input the minimum price for each snack, but we thought that this information is too much and not very useful.

##### 10. The rating scores on the snack information page did not represent the maximum score. (P9) [Low]

​	We will add maximum scores next to user ratings. This will solve the problem.

#### Recommendation

##### 1. Users cannot go directly to the snack information page from the recommendation response page. (P1) [High]

​	We are going to link the recommendation response to the snack information page.

##### 2. The recommendation response page provides only a 'Like' button, but the user may want to press 'Dislike' button for an inappropriate response. (P2) [Medium]

​	'Dislike' button can be a good solution for dealing with spam recommendations. We can add a 'Dislike' button in the actual developing period.

##### 3. Users may want to know additional information, such as other foods go well with snacks, but they do not have space to write such wishes. (P4) [Low]

​	Similar to the 'More Options' tab in the recommend request page, we can add the 'Additional Information' tab for sending a request. Then users who recommend can share some information like their recipes to others.

##### 4. The recommendation system requires a large number of users to function properly. (P4) [High]

​	Naturally, there should be lots of people to make the recommendation function properly. Besides, most of the social computing works well when there is some proper number of people. So we will spend more time on achievements and recommend tab so that users can be motivated more.

##### 5. It may take a long time to receive responses from recommendation requests. (P5) [High]

​	We will send requests to the users with the following priority:

1. Users in active and share similar snack preferences
2. Users in active
3. Users share similar snack preferences

​	By doing so we expect that response time is not too long.

##### 6. The icon in the recommendation menu is not representative. (P6) [High]

​	We will change the icon more appropriate to the recommendation menu.

#### Achievement

##### 1. Users who consume only a small variety of snacks do not have a chance to get a badge. (P4) [High]

​	In the prototype, there was only one type of achievement but we will add a lot of different types of achievements. While selecting and adding the achievements we are going to consider different consume types of users.

​	Also, we expect users can try different types of snacks than usual to gain achievements. Then someone might find snacks more suit to them.

##### 2. Apply ranking system can motivate people (P6, P7, P8) [High]

​	We are planning to construct ranking system for each achievement and add it to the achievement menu.

##### 3. The system should track which users have reviewed which products. (P4) [Medium]

​	We are going to make a database to store all the reviews from each user. This database is to find similar users for the recommendation system, but we can use this database and show users which snacks they reviewed. We don't think that showing lists that others review is needed.

​	Similar to this problem we are considering the 'Collection' tab in the achievement tab, which shows the list of snacks user reviewed.

#### Others

##### 1. There is no function to suggest new snacks. (P3) [Medium]

​	Making a space where users can suggest new snacks/categories/keywords and adding them may solve the problem.

##### 2. The search result did not show the nutritional ingredient labeling of snacks. (P7) [Medium]

​	We agree that nutritional ingredient labeling of snacks can help some types of customers. Since it is hard to make every labeling by masters, we have to use crowdsourcing to gather information. For testing, we will use only snacks in the KAIST convenience store so it is okay, but if we expand the scope, a good system for collecting labelings is necessary. We will consider this functionality if time allows after implementing the major features.