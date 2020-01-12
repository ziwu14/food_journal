#### Back-end (Django server):
1.	Unzip foodJournalDB.zip and cd into directory .../foodJornalDB/
2.	Enter commands below to start the back-end server where the 0.0.0.0 can be replaced by the server address. The server is built using Django Framework on python3, so Django and python3 should be properly installed. These commands can also be seen in the initserver.py file:
```
python3 manage.py makemigrations
python3 manage.py migrate
python manage.py runserver 0.0.0.0:8000
```

3.	If the server is successfully started, the network traffic as well as HTTP requests sent to the server can be monitored from the terminal. Further, an admin page (which can be accessed at 0.0.0.0:8000/admin) can be used to monitor database inputs from the browser in a more user-friendly manner.

#### Key API requests into the back-end:

#### _**Account**_
*Create or delete accounts from the database.*
http://0.0.0.0:8000/food/account/

**POST**
```
Create an account.

Body (form-data):
1) username
2) password
3) email
```
**DELETE**
```
Delete an account.

Parameters:
1) username
```
#### _**Health Information**_
*Get, create, or modify health information linked to one user.*
http://0.0.0.0:8000/food/health_info/

**GET**
```
Get an account’s health information.

Parameters:
1) username
```

**POST**
```
Create an account’s health information if it does not already exist. Modifies if it does (each entry in the body is optional if the request is modifying).

Parameters:
1) username

Body (form-data):
1) firstName
2) lastName
3) gender
4) age
5) weight
6) height
7) bmi
8) caloriesPerDay
9) healthTarget
10) foodTimeLapse
11) waterTimeLapse
```
#### _**Food**_
*Get, create, or delete foods from an account’s history.*
http://0.0.0.0:8000/food/food/

**GET**
```
Get all food entries from an account’s history.

Parameters:
1) username
```
**GET**
```
Get all food entries from an account’s history for a specific day.

Parameters:
1) username
2) date (comma seperated year,month,day i.e. 2019,3,4) 
```
**POST**
```
Create a new food entry to be entered to an account’s history. This will automatically mark it as a danger food if the same food is listed in the user's danger food list (searches by food name, capitalization doesn't matter)

Parameters:
1) username

Body (form-data):
1) name (this is the name of the food, i.e. apple)
2) calories_gained (optional)
3) foodID (optional)
```
**DELETE**
```
Delete a food from an account’s history.

Parameters:
1) username
2) name (this is the name of the food, i.e. apple)
```
#### _**Exercise**_
*Get, create, or delete exercises from an account’s history*
http://0.0.0.0:8000/food/exercise/

**GET**
```
Get all exercise entries from an account’s history.

Parameters:
1) username
```
**GET**
```
Get all exercise entries from an account’s history for a specific day.

Parameters:
1) username
2) date (comma seperated year,month,day i.e. 2019,3,4)
```
**POST**
```
Create a new exercise entry to be entered to an account’s history.

Parameters:
1) username

Body (form-data):
1) name (this is the name of the exercise, i.e. jog)
2) intensity (optional)
3) calories_burned (optional)
4) length (in minutes) (optional)
```

**DELETE**
```
Delete an exercise from an account’s history.

Parameters:
1) username
2) name (this is the name of the exercise, i.e. jog)
```
#### _**Water**_
*Get, create, or delete water from an account’s history*
http://0.0.0.0:8000/food/water/

**GET**
```
Get all water entries from an account’s history.

Parameters:
1) username
```
**GET**
```
Get all water entries from an account’s history for a specific day.

Parameters:
1) username
2) date (comma seperated year,month,day i.e. 2019,3,4)
```
**POST**
```
Create a new water entry to be entered to an account’s history.

Parameters:
1) username

Body (form-data):
1) name (this is the name of the exercise, i.e. jog)
2) calories_gained (optional)
3) foodID (optional)
```
**DELETE**
```
Delete a water from an account’s history.

Parameters:
1) username
2) time (this must be the exact time -> take this from the GET request of all water intake)
```

#### _**Danger Foods**_
*Get, create, or delete danger foods from an account’s history*
http://0.0.0.0:8000/food/danger_food/

**GET**
```
Get all danger food entries from an account.

Parameters:
1) username
```
**POST**
```
Create a new danger food entry to be entered to an account.

Parameters:
1) username

Body (form-data):
1) name (this is the name of the danger food)
2) foodID (optional)
```
**DELETE**
```
Delete a danger food from an account.

Parameters:
1) username
2) name (this is the name of the danger food)
```
#### _**Health Conditions**_
*Get, create, or delete health conditions from an account’s history*
http://0.0.0.0:8000/food/health_condition/

**GET**
```
Get all health condition entries from an account’s history.

Parameters:
1) username
```

**POST**
```
Create a new health condition entry to be entered to an account’s history.

Parameters:
1) username

Body (form-data):
1) name (this is the name of the health condition)
2) description (optional)
```

**DELETE**
```
Delete a health condition from an account’s history.

Parameters:
1) username
2) name (this is the name of the health condition)
```