import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler


data = pd.read_csv("C:\dataset\Iris.csv")
X = data[data.columns[1:-1]]
scaled_X = StandardScaler().fit_transform(X)
reduced_X = PCA(n_components=2).fit_transform(scaled_X)
sns.scatterplot(
    x=reduced_X[:, 0],
    y=reduced_X[:, 1],
    hue=data['Species']
)

plt.title("Scatter Plot after reducing the dimension of feature vectors using PCA")
plt.xlabel("Feature 1")
plt.ylabel("Feature 2")
plt.grid()
plt.show()
