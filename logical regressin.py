import pandas as pd 
import numpy as np 
import matplotlib.pyplot as plt 
import seaborn as sns 
from sklearn.linear_model import LogisticRegression 
from sklearn.model_selection import train_test_split 
from  sklearn.metrics  import  classification_report,confusion_matrix 
diabetes_data = pd.read_csv(r"D:\ML LAB\dataset ml\diabetes.csv")  
X = diabetes_data.drop("Outcome",axis=1) 
y = diabetes_data["Outcome"] 
X_train, X_test, y_train, y_test = train_test_split(X,y, test_size = 0.3) 
model = LogisticRegression() 
model.fit(X_train,y_train) 
predictions = model.predict(X_test) 
print("*Classification Report*\n") 
print(classification_report(y_test, predictions)) 
 
print("Confusion Matrix") 
print(confusion_matrix(y_test, predictions))
