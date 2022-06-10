import matplotlib.pyplot as plt

h = [1, 0.5, 0.1, 0.01]
errRK4 = [4.884981308350689E-15, 4.884981308350689E-15,
          4.773959005888173E-15, 4.440892098500626E-15]
errRK2 = [4.884981308350689E-15, 4.773959005888173E-15,
          4.6629367034256575E-15, 5.10702591327572E-15]
plt.loglog(h, errRK4, errRK2)
plt.show()