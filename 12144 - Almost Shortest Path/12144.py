import sys
import heapq

def dijkstra(n, adj, origen):
    """
    Calcula la distancia más corta desde 'origen' hacia todos los nodos,
    usando una cola de prioridad (heap).
    adj[u] es una lista de tuplas (v, peso) que representan aristas u->v.
    Retorna una lista 'dist' donde dist[x] = distancia mínima origen->x,
    o infinito si x no es alcanzable.
    """
    INF = float('inf')
    dist = [INF] * n
    dist[origen] = 0
    # heap guarda tuplas (distancia_actual, nodo)
    heap = [(0, origen)]
    
    while heap:
        d, u = heapq.heappop(heap)
        if d > dist[u]:
            # Ya encontramos un camino mejor antes, esta entrada es obsoleta
            continue
        for v, peso in adj[u]:
            nueva_dist = d + peso
            if nueva_dist < dist[v]:
                dist[v] = nueva_dist
                heapq.heappush(heap, (nueva_dist, v))
    return dist


def resolver(n, m, s, d, aristas):
    INF = float('inf')
    
    # Construimos el grafo original (adj) y el grafo invertido (adj_rev)
    adj = [[] for _ in range(n)]
    adj_rev = [[] for _ in range(n)]
    for u, v, p in aristas:
        adj[u].append((v, p))
        adj_rev[v].append((u, p))  # arista invertida: v recibe desde u en el original,
                                     # entonces en el invertido va de v a u
    
    # 1) Distancia desde S a todos los nodos (grafo original)
    distS = dijkstra(n, adj, s)
    
    # 2) Distancia desde D a todos los nodos, pero usando el grafo invertido
    #    (esto equivale a "distancia de cada nodo hacia D" en el grafo original)
    distD = dijkstra(n, adj_rev, d)
    
    # Si D no es alcanzable desde S, ni siquiera existe el camino más corto normal
    if distS[d] == INF:
        return -1
    
    dist_minima = distS[d]
    
    # 3) Construimos un nuevo grafo SIN las aristas que pertenecen a algún camino más corto
    adj_filtrado = [[] for _ in range(n)]
    for u, v, p in aristas:
        # Condición: si esta arista es parte de algún camino óptimo, la saltamos
        if distS[u] + p + distD[v] == dist_minima:
            continue  # arista vetada, no la agregamos
        adj_filtrado[u].append((v, p))
    
    # 4) Dijkstra nuevamente desde S, pero en el grafo filtrado
    distS2 = dijkstra(n, adj_filtrado, s)
    
    if distS2[d] == INF:
        return -1
    return distS2[d]


def main():
    data = sys.stdin.read().split()
    idx = 0
    resultados = []
    
    while True:
        n = int(data[idx]); m = int(data[idx+1]); idx += 2
        if n == 0 and m == 0:
            break  # fin del input
        
        s = int(data[idx]); d = int(data[idx+1]); idx += 2
        
        aristas = []
        for _ in range(m):
            u = int(data[idx]); v = int(data[idx+1]); p = int(data[idx+2])
            idx += 3
            aristas.append((u, v, p))
        
        respuesta = resolver(n, m, s, d, aristas)
        resultados.append(str(respuesta))
    
    print('\n'.join(resultados))


if __name__ == '__main__':
    main()