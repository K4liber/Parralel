import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from mpl_toolkits.mplot3d import Axes3D

# Run: python3 problem3.py

df = pd.read_csv('problem3.csv',
    names = [
        "size", "time"
    ]
)

meanDf = df.groupby("size", as_index=False).mean()
stdDf = df.groupby("size", as_index=False).std()

plt.errorbar(x=meanDf["size"].values, y=meanDf["time"].values, yerr=stdDf["time"].values, fmt='-o', label="One thread")
plt.xlabel("Array size")
plt.ylabel("Time [ms]")
plt.legend()

plt.savefig("problem3.png")
plt.show()
