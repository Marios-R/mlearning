# MathLearn

Android application for educational purposes (mlearning). The application is called MathLearn because I used it for education in Mathematics but it could be used in other fields as well. 
The interesting thing with this application is that it uses a local database to recognize the viewed videos and that it connects to a server where all resources are located (videos, announcements, assignements). 
The application synchronizes with the server automatically but the user can also check on his own if there is any updates on the server by pressing a "refresh button".  

* Most of the content is in Greek.

The local database is updating videos, assignments and announcements by comunicating with the server and getting in response new resources or resources that should be deleted localy. This is done by using AsyncTask, JSONObject and the strategy pattern from the side of the application:

![alt text] (sync.png)
