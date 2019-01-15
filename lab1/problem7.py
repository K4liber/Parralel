import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from mpl_toolkits.mplot3d import Axes3D

# Run: python3 problem5.py

df = pd.read_csv('problem7aSmall.csv',
    names = [
        "arraySize", "time", "size"
    ]
)

df2 = pd.read_csv('problem7-3.csv',
    names = [
        "size", "time"
    ]
)

df3 = pd.read_csv('problem7-4.csv',
    names = [
        "size", "time"
    ]
)

meanDf = df.groupby("size", as_index=False).mean()
meanDf2 = df2.groupby("size", as_index=False).mean()
meanDf3 = df3.groupby("size", as_index=False).mean()

stdDf = df[['time','size']].groupby("size", as_index=False).std()
stdDf2 = df2.groupby("size", as_index=False).std()
stdDf3 = df3.groupby("size", as_index=False).std(ddof=0)

times3 = [meanDf3["time"].values[0]] * meanDf["size"].size
times2 = [meanDf2["time"].values[0]] * meanDf["size"].size

plt.errorbar(x=meanDf["size"].values, y=times2, label="One thread")
plt.errorbar(x=meanDf["size"].values, y=times3, label="Two threads")
plt.errorbar(x=meanDf["size"].values, y=meanDf["time"].values, yerr=stdDf["time"].values, fmt='-o', label="Race on batches (two threads)")

plt.xlabel("batch size")
plt.ylabel("Time [ms]")
#plt.yscale("log")
#plt.xscale("log")
plt.legend()
plt.savefig("problem7.png")
plt.show()
