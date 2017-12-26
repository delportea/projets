# -*- coding: utf-8 -*-
"""
Created on Tue Dec 26 00:57:48 2017

@author: adelport
"""

#kmeans clustering
 https://drive.google.com/open?id=0B8iiZ7pSaSFZb3ItQ1l4LWRMTjg 
 
from sklearn.preprocessing import StandardScaler\n",
from sklearn.cluster import KMeans\n",
import utils\n",
import pandas as pd\n",
import numpy as np\n",
from itertools import cycle, islice\n",
import matplotlib.pyplot as plt\n",
from pandas.tools.plotting 
import parallel_coordinates
%matplotlib inline
    
data = pd.read_csv('./weather/minute_weather.csv')
    
data.shape

data.head()

sampled_df = data[(data['rowID'] % 10) == 0]\n",
sampled_df.shape
    
sampled_df.describe().transpose()

sampled_df[sampled_df['rain_accumulation'] == 0].shape
sampled_df[sampled_df['rain_duration'] == 0].shape

del sampled_df['rain_accumulation']
del sampled_df['rain_duration']

rows_before = sampled_df.shape[0]
sampled_df = sampled_df.dropna()
rows_after = sampled_df.shape[0]

sampled_df.columns
#features of interest for clustering
features = ['air_pressure', 'air_temp', 'avg_wind_direction', 'avg_wind_speed', 'max_wind_direction',
           'max_wind_speed','relative_humidity']
select_df = sampled_df[features]
select_df.columns
select_df

X = StandardScaler().fit_transform(select_df)
X
#perform kmeans clustering
kmeans = KMeans(n_clusters=12)
model = kmeans.fit(X)
print(\"model\\n\", model)

centers = model.cluster_centers_
centers

def pandas_centers(featuresUsed, centers):
    colNames = list(featuresUsed)
    colNames.append('prediction')
    Z = [np.append(A, index) for index, A in enumerate(centers)]
    P = pd.DataFrame(Z, columns=colNames)
    P['prediction'] = P['prediction'].astype(int)
    return P
  
def parallel_plot(data):
    my_colors = list(islice(cycle(['b', 'r', 'g', 'y', 'k']), None, len(data)))
    plt.figure(figsize=(15,8)).gca().axes.set_ylim([-3,+3])
    parallel_coordinates(data, 'prediction', color = my_colors, marker='o')
    
P = pandas_centers(features, centers)  
P

parallel_plot(P[P['relative_humidity'] < -0.5])
parallel_plot(P[P['air_temp'] > 0.5])
parallel_plot(P[(P['relative_humidity'] > 0.5) & (P['air_temp'] < 0.5)])















