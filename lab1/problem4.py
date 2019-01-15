import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from mpl_toolkits.mplot3d import Axes3D

# Run: python3 problem4.py

df = pd.read_csv('problem4.csv',
    names = [
        "size", "time"
    ]
)

df2 = pd.read_csv('problem3.csv',
    names = [
        "size", "time"
    ]
)

meanDf = df.groupby("size", as_index=False).mean()
meanDf2 = df2.groupby("size", as_index=False).mean()
stdDf = df.groupby("size", as_index=False).std()
stdDf2 = df2.groupby("size", as_index=False).std()

plt.errorbar(x=meanDf["size"].values, y=meanDf["time"].values, yerr=stdDf["time"].values, fmt='-o', label="Two threads")
plt.errorbar(x=meanDf2["size"].values, y=meanDf2["time"].values, yerr=stdDf2["time"].values, fmt='-o', label="One thread")

plt.xlabel("Array size")
plt.ylabel("Time [ms]")
plt.legend()

plt.savefig("problem4.png")
plt.show()
