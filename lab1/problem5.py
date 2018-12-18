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

plt.plot(meanDf["size"].values, meanDf["time"].values, '-o', label="Thread pool (two threads)")
plt.plot(meanDf2["size"].values, meanDf2["time"].values, '-o', label="Two threads")
plt.plot(meanDf3["size"].values, meanDf3["time"].values, '-o', label="One thread")
plt.xlabel("Array size")
plt.ylabel("Time [ms]")
plt.yscale("log")
plt.xscale("log")
plt.legend()
plt.savefig("problem5.png")
plt.show()
