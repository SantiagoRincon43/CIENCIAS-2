import sys
from collections import deque

def bfs_distancias(grid, x, y, origen):
    """
    Calcula la distancia mas corta (en pasos) desde 'origen'
    hacia todas las demas celdas abiertas del laberinto.
    Retorna una matriz 'dist' del mismo tamano que el grid,
    con -1 en las celdas no alcanzadas (paredes o inalcanzables).
    """
    dist = [[-1] * x for _ in range(y)]
    oc, of = origen  # (columna, fila)
    dist[of][oc] = 0
    cola = deque()
    cola.append((oc, of))

    # Movimientos: norte, sur, este, oeste
    movimientos = [(0, -1), (0, 1), (1, 0), (-1, 0)]

    while cola:
        cx, cy = cola.popleft()
        for dx, dy in movimientos:
            nx, ny = cx + dx, cy + dy
            # Verificar que este dentro del grid
            if 0 <= nx < x and 0 <= ny < y:
                # Solo avanzar si es celda abierta (' ', 'A' o 'S') y no visitada
                if grid[ny][nx] != '#' and dist[ny][nx] == -1:
                    dist[ny][nx] = dist[cy][cx] + 1
                    cola.append((nx, ny))
    return dist


def resolver_caso(x, y, grid):
    # 1. Encontrar todos los puntos de interes: la 'S' y todas las 'A'
    puntos = []
    for fy in range(y):
        for fx in range(x):
            if grid[fy][fx] == 'S' or grid[fy][fx] == 'A':
                puntos.append((fx, fy))

    n = len(puntos)
    if n <= 1:
        # Si solo esta 'S' y no hay aliens, el costo es 0
        return 0

    # 2. Calcular matriz de distancias entre todos los puntos de interes
    # dist_matriz[i][j] = distancia minima (en pasos) entre puntos[i] y puntos[j]
    dist_matriz = [[0] * n for _ in range(n)]
    for i in range(n):
        dist_grid = bfs_distancias(grid, x, y, puntos[i])
        for j in range(n):
            px, py = puntos[j]
            dist_matriz[i][j] = dist_grid[py][px]

    # 3. Algoritmo de Prim para hallar el MST
    # visitado[i] indica si el punto i ya esta en el arbol
    visitado = [False] * n
    # costo_minimo[i] = costo minimo para conectar el punto i al arbol actual
    costo_minimo = [float('inf')] * n
    costo_minimo[0] = 0  # empezamos por el punto 0 (que es 'S')

    costo_total = 0
    for _ in range(n):
        # Elegir el punto no visitado con menor costo_minimo
        u = -1
        for i in range(n):
            if not visitado[i] and (u == -1 or costo_minimo[i] < costo_minimo[u]):
                u = i

        visitado[u] = True
        costo_total += costo_minimo[u]

        # Actualizar costos de los vecinos de u
        for v in range(n):
            if not visitado[v] and dist_matriz[u][v] < costo_minimo[v]:
                costo_minimo[v] = dist_matriz[u][v]

    return costo_total


def main():
    datos = sys.stdin.read().split('\n')
    idx = 0
    n_casos = int(datos[idx].strip()); idx += 1

    resultados = []
    for _ in range(n_casos):
        x, y = map(int, datos[idx].split()); idx += 1
        grid = []
        for _ in range(y):
            linea = datos[idx]; idx += 1
            # Quitar posible retorno de carro, pero NO quitar espacios internos
            linea = linea.rstrip('\r')
            # IMPORTANTE: rellenar con espacios si la linea quedo mas corta que x
            # (esto pasa seguido cuando el mapa viene de un PDF y se pierden
            # los espacios en blanco al final de cada fila)
            if len(linea) < x:
                linea = linea + ' ' * (x - len(linea))
            else:
                linea = linea[:x]
            grid.append(linea)

        resultado = resolver_caso(x, y, grid)
        resultados.append(str(resultado))

    print('\n'.join(resultados))


if __name__ == '__main__':
    main()