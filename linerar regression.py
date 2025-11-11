# Linear Regression using Scikit-Learn

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split

# Load dataset
data = pd.read_csv("../MLdatasets/headbrain.csv")

# Features and labels
X = data[['Head Size(cm^3)']]
y = data['Brain Weight(grams)']

# Split dataset
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33, random_state=42)

# Train model
model = LinearRegression()
model.fit(X_train, y_train)

# Predict
predictions = model.predict(X_test)

# Plot
plt.scatter(X_test, y_test, color='r', label='Scatter Plot')
plt.plot(X_test, predictions, color='b', label='Regression Line')
plt.xlabel('Head Size (cm³)')
plt.ylabel('Brain Weight (grams)')
plt.legend()
plt.show()








r2_score = model.score(X_test, y_test)
mse = mean_squared_error(y_test, predictions)
rmse = np.sqrt(mse)

# Print results
print(f"RMSE VALUE : {rmse:.4f}")
print(f"R2 SCORE   : {r2_score:.4f}")
