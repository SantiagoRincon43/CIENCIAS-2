import sys
import heapq

def resolver():
    # Leemos todo el input de una vez y lo separamos en tokens (mucho más rápido que input() línea por línea)
    data = sys.stdin.read().split()
    idx = 0

    def leer_int():
        nonlocal idx
        val = int(data[idx])
        idx += 1
        return val

    N = leer_int()  # cantidad de casos de prueba
    resultados = []

    for caso in range(1, N + 1):
        n = leer_int()  # cantidad de servidores
        m = leer_int()  # cantidad de cables
        S = leer_int()  # servidor origen
        T = leer_int()  # servidor destino

        # Lista de adyacencia: grafo[u] = lista de (vecino, peso)
        grafo = [[] for _ in range(n)]

        for _ in range(m):
            u = leer_int()
            v = leer_int()
            w = leer_int()
            # El cable es bidireccional: se puede ir en ambos sentidos
            grafo[u].append((v, w))
            grafo[v].append((u, w))

        # --- Dijkstra desde S ---
        INF = float('inf')
        dist = [INF] * n
        dist[S] = 0

        # El heap guarda tuplas (distancia_actual, nodo)
        # heapq en Python es un min-heap, así que siempre saca el menor primero
        heap = [(0, S)]
        visitado = [False] * n

        while heap:
            d, u = heapq.heappop(heap)

            if visitado[u]:
                # Ya lo procesamos con una distancia mejor o igual, lo ignoramos
                continue
            visitado[u] = True

            if u == T:
                # Ya encontramos la distancia mínima a T, podemos cortar antes (optimización)
                break

            # Revisamos todos los vecinos de u
            for v, w in grafo[u]:
                if not visitado[v] and d + w < dist[v]:
                    dist[v] = d + w
                    heapq.heappush(heap, (dist[v], v))

        # --- Guardar resultado ---
        if dist[T] == INF:
            resultados.append(f"Case #{caso}: unreachable")
        else:
            resultados.append(f"Case #{caso}: {dist[T]}")

    # Imprimimos todo junto al final (más rápido que hacer print() muchas veces)
    print("\n".join(resultados))

resolver()