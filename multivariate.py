import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import multivariate_normal

x1, x2 = np.meshgrid(np.linspace(-5,5,100), np.linspace(-5,5,100))
pos = np.dstack((x1,x2))
dists = [(np.eye(2), "Independent variables"), ([[1,-0.8],[-0.8,1]], "Correlated variables")]

fig, ax = plt.subplots(1,2,figsize=(13,5))
plt.suptitle("Bivariate Normal Distributions")
for i,(cov,title) in enumerate(dists):
    pdf = multivariate_normal([0,0], cov).pdf(pos)
    c = ax[i].contourf(x1,x2,pdf,100,cmap='rainbow')
    ax[i].set_title(title )
    fig.colorbar(c, ax=ax[i]).ax.set_ylabel("$P(x_1,x_2)$")
plt.show()
