
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
from sklearn.datasets import load_breast_cancer
data = load_breast_cancer()
X = data['data']
scaled_X= StandardScaler().fit_transform(X)
reduced_X = PCA(n_components=2).fit_transform(scaled_X)
sns.scatterplot(
    x=reduced_X[:,1],
    y=reduced_X[:,0],
    hue=data['target']
    )
plt.title("Scatter Plot after reducing the dimension of feature vectors using PCA")
plt.xlabel('Feature 1')
plt.ylabel('Feature 2')
plt.legend()
plt.grid()
