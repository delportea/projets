# -*- coding: utf-8 -*-
"""
Created on Tue Dec 26 00:23:38 2017

@author: adelport
"""

Regresssion with scikit-learn<br><br> using Soccer Dataset

https://www.kaggle.com/hugomathien/soccer
database.sqlite
import sqlite3\n",
import pandas as pd \n",
from sklearn.tree import DecisionTreeRegressor
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
from math import sqrt
#create connection
cnx = sqlite3.connect('database.sqlite')
df = pd.read_sql_query(\"SELECT * FROM Player_Attributes\", cnx)    

df.head()
df.shape
df.columns
#declaration of columns-features
features = [
           'potential', 'crossing', 'finishing', 'heading_accuracy',
           'short_passing', 'volleys', 'dribbling', 'curve', 'free_kick_accuracy',
           'long_passing', 'ball_control', 'acceleration', 'sprint_speed',
           'agility', 'reactions', 'balance', 'shot_power', 'jumping', 'stamina',
           'strength', 'long_shots', 'aggression', 'interceptions', 'positioning',
           'vision', 'penalties', 'marking', 'standing_tackle', 'sliding_tackle',
           'gk_diving', 'gk_handling', 'gk_kicking', 'gk_positioning',
           'gk_reflexes']

target = ['overall_rating']
#cleaning data
df = df.dropna()

X = df[features]

y = df[target]

X.iloc[2]
y
#definition of test & training datasets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33, random_state=324)
#fitting a model
regressor = LinearRegression()
regressor.fit(X_train, y_train)
#perform prediciton
y_prediction = regressor.predict(X_test)
y_prediction
#describe target value in test set
y_test.describe()
#accuracy
RMSE = sqrt(mean_squared_error(y_true = y_test, y_pred = y_prediction))

print(RMSE)

#Decision Tree Regressor: Fit a new regression model to the training set

regressor = DecisionTreeRegressor(max_depth=20)
regressor.fit(X_train, y_train)

#Perform Prediction using Decision Tree Regressor
y_prediction = regressor.predict(X_test)
y_prediction

y_test.describe()

#Evaluate Decision Tree Regression Accuracy using Root Mean Square Error
RMSE = sqrt(mean_squared_error(y_true = y_test, y_pred = y_prediction))
print(RMSE)

