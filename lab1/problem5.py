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

meanDf = df.groupby("size", as_index=False).mean()
print(meanDf)

plt.plot(meanDf["size"].values, meanDf["time"].values, '-o', label="Thread pool (two threads)")
plt.xlabel("Array size")
plt.ylabel("Time [ms]")
plt.legend()
plt.savefig("problem5.png")
plt.show()
