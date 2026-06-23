import math

objetivo = 7_352_000

def ceil_log2(m):
    """Comparaciones por nodo usando busqueda binaria: ceil(log2(m))"""
    return math.ceil(math.log2(m))

mejor_m = None
mejor_h = None
mejor_valor = None
menor_exceso = float("inf")

print("m\th\tm^h-1\t\texceso\t\tlog2(m)\tpasos")
print("-" * 55)

for m in range(3, 51):
    h = 1
    while (m ** h - 1) < objetivo:
        h += 1

    valor  = m ** h - 1
    exceso = valor - objetivo
    cl2    = ceil_log2(m)
    pasos  = h * cl2

    print(f"{m}\t{h}\t{valor:,}\t{exceso:,}\t{cl2}\t{pasos}")

    if exceso < menor_exceso:
        menor_exceso = exceso
        mejor_m      = m
        mejor_h      = h
        mejor_valor  = valor

print("\n" + "=" * 55)
print("MEJOR RESULTADO (menor exceso)")
print(f"  m      = {mejor_m}")
print(f"  h      = {mejor_h}")
print(f"  m^h-1  = {mejor_valor:,}")
print(f"  exceso = {menor_exceso:,}")
print(f"  pasos  = {mejor_h * ceil_log2(mejor_m)}  (h × ceil(log2(m)))")
print(f"  obj    = {objetivo:,}")
