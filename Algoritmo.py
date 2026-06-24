
semilla = 7
a = 1664525
c = 1013904223
m = 4294967296  

# genero los 20 numeros
numeros = []
x = semilla

for i in range(20):
    x = (a * x + c) % m
    numeros.append(x)

# los imprimo
print("Numeros generados:")
print()
for i in range(len(numeros)):
    # lo normalizo entre 0 y 1 dividiendo entre m
    normalizado = numeros[i] / m
    print("n =", i+1, "  valor:", numeros[i], "  entre 0 y 1:", round(normalizado, 4))

# calculo el promedio de los normalizados para ver si esta bien distribuido
# si da cerca de 0.5 esta bien
suma = 0
for n in numeros:
    suma = suma + (n / m)

promedio = suma / 20
print()
print("promedio:", round(promedio, 4), "  (si da cerca de 0.5 la distribucion es buena)")