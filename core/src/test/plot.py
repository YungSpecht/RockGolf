from turtle import ycor
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

x = [0.5, 0.3, 0.25, 0.125, 0.1, 0.05, 0.01, 0.005, 0.001]


errRK4 = [1.1386048570136418E-4, 1.5768541405717163E-5, 7.746043346409199E-6,
          5.085060185529215E-7, 2.1044338465056902E-7, 1.3433016574679878E-8, 2.184572890974662E-11, 1.3466910521755426E-12, 1.7466982953960008E-14]

errRK2 = [0.020989957153160488, 0.007973918543120595, 0.0056199896664159464,
          0.0014610033678357185, 9.426920904485503E-4, 2.3961672161554773E-4, 9.715372923150828E-6, 2.4329972864415307E-6, 9.745324065631161E-8]

errEuler = [0.10686578313694699, 0.07437828849201554, 0.06433104491235085,
            0.03533393790369355, 0.02880989723551773, 0.014967811281544209, 0.003087754114917243, 0.0015499006300107396, 3.109483958813643E-4]

plt.loglog(x, errRK2, label="Runge-Kutta 2")
plt.loglog(x, errRK4, label="Runge-Kutta 4")
plt.loglog(x, errEuler, label="Euler")
plt.xlabel("Step size")
plt.ylabel("Relative error")
plt.legend()
plt.show()
