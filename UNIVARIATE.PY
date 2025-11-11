import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import norm

# Generate x values
x = np.linspace(-3, 5, 150)

# Define mean, variance, and labels
params = [(0, 1, "N(0,1)"), (2, 3, "N(2,3)"), (0, 0.2, "N(0,0.2)")]

plt.figure(figsize=(6, 4))

# Plot each distribution
for mean, var, label in params:
    pdf = norm.pdf(x, loc=mean, scale=np.sqrt(var))
    plt.plot(x, pdf, label=label)

# Labels and title
plt.title("Univariate Normal Distributions")
plt.xlabel("x")
plt.ylabel("Density: P(x)")
plt.xlim(-3, 5)
plt.ylim(0, 1)
plt.legend()
plt.show()
