import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from mpl_toolkits.mplot3d import Axes3D

# Run: python3 result.py
names = ["N", "J", "h", "B", "M"]
df = pd.read_csv('dataN50J1.0h0.1.csv', names = names)
df2 = pd.read_csv('dataN50J1.0h-0.1.csv', names = names)
df3 = pd.read_csv('dataN50J1.0h0.0.csv', names = names)
df4 = pd.read_csv('dataN50J1.0h0.05.csv', names = names)
df5 = pd.read_csv('dataN50J1.0h-0.2.csv', names = names)

meanDf = df.groupby("B", as_index=False).mean()
stdDf = df.groupby("B", as_index=False).std()
meanDf2 = df2.groupby("B", as_index=False).mean()
stdDf2 = df2.groupby("B", as_index=False).std()
meanDf3 = df3.groupby("B", as_index=False).mean()
stdDf3 = df3.groupby("B", as_index=False).std()
meanDf4 = df4.groupby("B", as_index=False).mean()
stdDf4 = df4.groupby("B", as_index=False).std()
meanDf5 = df5.groupby("B", as_index=False).mean()
stdDf5 = df5.groupby("B", as_index=False).std()

plt.errorbar(x=meanDf["B"].values, y=meanDf["M"].values, yerr=stdDf["M"].values, fmt='-o', label="h=0.1")
plt.errorbar(x=meanDf2["B"].values, y=meanDf2["M"].values, yerr=stdDf2["M"].values, fmt='-o', label="h=-0.1")
plt.errorbar(x=meanDf3["B"].values, y=meanDf3["M"].values, yerr=stdDf3["M"].values, fmt='-o', label="h=0")
plt.errorbar(x=meanDf4["B"].values, y=meanDf4["M"].values, yerr=stdDf4["M"].values, fmt='-o', label="h=0.05")
plt.errorbar(x=meanDf5["B"].values, y=meanDf5["M"].values, yerr=stdDf5["M"].values, fmt='-o', label="h=-0.2")
plt.xlabel("B [1/J]")
plt.ylabel("<M>")
plt.title("<M> in the function of B for N=50, J=1")
plt.legend()
plt.savefig("result.png")

