import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from mpl_toolkits.mplot3d import Axes3D

# Run: python3 problem5.py

df = pd.read_csv('problem5.csv',
    names = [
        "size", "time"
    ]
)

df2 = pd.read_csv('problem4.csv',
    names = [
        "size", "time"
    ]
)

df3 = pd.read_csv('problem3.csv',
    names = [
        "size", "time"
    ]
)

meanDf = df.groupby("size", as_index=False).mean()
meanDf2 = df2.groupby("size", as_index=False).mean()
meanDf3 = df3.groupby("size", as_index=False).mean()

stdDf = df.groupby("size", as_index=False).std()
stdDf2 = df2.groupby("size", as_index=False).std()
stdDf3 = df3.groupby("size", as_index=False).std(ddof=0)

plt.errorbar(x=meanDf3["size"].values, y=meanDf3["time"].values, yerr=stdDf3["time"].values, fmt='-o', label="One thread")
plt.errorbar(x=meanDf2["size"].values, y=meanDf2["time"].values, yerr=stdDf2["time"].values, fmt='-o', label="Two threads")
plt.errorbar(x=meanDf["size"].values, y=meanDf["time"].values, yerr=stdDf["time"].values, fmt='-o', label="Thread pool (two threads)")
plt.xlabel("Array size")
plt.ylabel("Time [ms]")
#plt.yscale("log")
#plt.xscale("log")
plt.legend()
plt.savefig("problem5.png")
plt.show()
