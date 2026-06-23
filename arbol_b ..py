objetivo = 7_352_000

mejor_m = None
mejor_h = None
mejor_valor = None
menor_exceso = float("inf")

print(f"{'m':>3} {'h':>5} {'m^h':>15} {'exceso':>15}")
print("-" * 45)

for m in range(3, 51):
    h = 1

    while m ** h < objetivo:
        h += 1

    valor = m ** h
    exceso = valor - objetivo

    print(f"{m:>3} {h:>5} {valor:>15} {exceso:>15}")

    if exceso < menor_exceso:
        menor_exceso = exceso
        mejor_m = m
        mejor_h = h
        mejor_valor = valor

print("\n" + "=" * 45)
print("MEJOR RESULTADO GLOBAL")
print(f"m = {mejor_m}")
print(f"h = {mejor_h}")
print(f"m^h = {mejor_valor}")
print(f"Exceso = {menor_exceso}")
print(f"Objetivo = {objetivo}")
