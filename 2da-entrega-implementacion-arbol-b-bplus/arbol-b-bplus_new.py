import math

class Node:
    def __init__(self, leaf=False):
        self.keys = []
        self.children = []
        self.leaf = leaf

class BPlusNode:
    def __init__(self, order):
        self.order = order
        self.values = []
        self.keys = []
        self.nextKey = None
        self.parent = None
        self.check_leaf = False

    # Insert at the leaf
    # CORRECCIÓN: se eliminó el parámetro 'leaf' que nunca se usaba
    def insert_at_leaf(self, value, key):
        if (self.values):
            temp1 = self.values
            for i in range(len(temp1)):
                if (value == temp1[i]):
                    self.keys[i].append(key)
                    break
                elif (value < temp1[i]):
                    self.values = self.values[:i] + [value] + self.values[i:]
                    self.keys = self.keys[:i] + [[key]] + self.keys[i:]
                    break
                elif (i + 1 == len(temp1)):
                    self.values.append(value)
                    self.keys.append([key])
                    break
        else:
            self.values = [value]
            self.keys = [[key]]


class BTree:
    def __init__(self, t):
        self.root = Node(True)
        self.t = t

    def search(self, key, node=None):
        node = self.root if node is None else node

        i = 0
        while i < len(node.keys) and key > node.keys[i]:
            i += 1
        if i < len(node.keys) and key == node.keys[i]:
            return (node, i)
        elif node.leaf:
            return None
        else:
            return self.search(key, node.children[i])

    def split_child(self, x, i):
        t = self.t
        y = x.children[i]

        # Crear nuevo nodo y añadirlo a los hijos de x
        z = Node(y.leaf)
        x.children.insert(i + 1, z)

        # Insertar la mediana del hijo lleno y en x
        x.keys.insert(i, y.keys[t - 1])

        # Dividir las claves de y entre y y z
        z.keys = y.keys[t: (2 * t) - 1]
        y.keys = y.keys[0: t - 1]

        # Si y no es hoja, reasignar sus hijos entre y y z
        if not y.leaf:
            z.children = y.children[t: 2 * t]
            y.children = y.children[0: t]

    def insert(self, k):
        t = self.t
        root = self.root

        # Si la raíz está llena, crear un nuevo nodo (la altura crece 1)
        if len(root.keys) == (2 * t) - 1:
            new_root = Node()
            self.root = new_root
            new_root.children.insert(0, root)
            self.split_child(new_root, 0)
            self.insert_non_full(new_root, k)
        else:
            self.insert_non_full(root, k)

    def insert_non_full(self, x, k):
        t = self.t
        i = len(x.keys) - 1

        # Encontrar la posición correcta en la hoja para insertar
        if x.leaf:
            x.keys.append(None)
            while i >= 0 and k < x.keys[i]:
                x.keys[i + 1] = x.keys[i]
                i -= 1
            x.keys[i + 1] = k
        # Si no es hoja, encontrar el subárbol correcto
        else:
            while i >= 0 and k < x.keys[i]:
                i -= 1
            i += 1
            # Si el nodo hijo está lleno, dividirlo
            if len(x.children[i].keys) == (2 * t) - 1:
                self.split_child(x, i)
                if k > x.keys[i]:
                    i += 1
            self.insert_non_full(x.children[i], k)

    def delete(self, x, k):
        t = self.t
        i = 0

        while i < len(x.keys) and k > x.keys[i]:
            i += 1
        if x.leaf:
            if i < len(x.keys) and x.keys[i] == k:
                x.keys.pop(i)
            return

        if i < len(x.keys) and x.keys[i] == k:
            return self.delete_internal_node(x, k, i)
        elif len(x.children[i].keys) >= t:
            self.delete(x.children[i], k)
        else:
            # CORRECCIÓN: la condición era 'i + 2 < len(x.children)',
            # lo que dejaba sin manejar el penúltimo hijo.
            # Debe ser 'i + 1 < len(x.children)' para cubrir todos los casos intermedios.
            if i != 0 and i + 1 < len(x.children):
                if len(x.children[i - 1].keys) >= t:
                    self.delete_sibling(x, i, i - 1)
                elif len(x.children[i + 1].keys) >= t:
                    self.delete_sibling(x, i, i + 1)
                else:
                    self.delete_merge(x, i, i + 1)
            elif i == 0:
                if len(x.children[i + 1].keys) >= t:
                    self.delete_sibling(x, i, i + 1)
                else:
                    self.delete_merge(x, i, i + 1)
            elif i + 1 == len(x.children):
                if len(x.children[i - 1].keys) >= t:
                    self.delete_sibling(x, i, i - 1)
                else:
                    self.delete_merge(x, i, i - 1)
            self.delete(x.children[i], k)

    def delete_internal_node(self, x, k, i):
        t = self.t
        if x.leaf:
            if x.keys[i] == k:
                x.keys.pop(i)
            return

        if len(x.children[i].keys) >= t:
            x.keys[i] = self.delete_predecessor(x.children[i])
            return
        elif len(x.children[i + 1].keys) >= t:
            x.keys[i] = self.delete_successor(x.children[i + 1])
            return
        else:
            self.delete_merge(x, i, i + 1)
            self.delete_internal_node(x.children[i], k, self.t - 1)

    def delete_predecessor(self, x):
        if x.leaf:
            return x.keys.pop()
        n = len(x.keys) - 1
        if len(x.children[n].keys) >= self.t:
            self.delete_sibling(x, n + 1, n)
        else:
            self.delete_merge(x, n, n + 1)
        # CORRECCIÓN: faltaba 'return'; sin él la función retornaba None
        return self.delete_predecessor(x.children[n])

    def delete_successor(self, x):
        if x.leaf:
            return x.keys.pop(0)
        if len(x.children[1].keys) >= self.t:
            self.delete_sibling(x, 0, 1)
        else:
            self.delete_merge(x, 0, 1)
        # CORRECCIÓN: faltaba 'return'; sin él la función retornaba None
        return self.delete_successor(x.children[0])

    def delete_merge(self, x, i, j):
        cnode = x.children[i]

        if j > i:
            rsnode = x.children[j]
            cnode.keys.append(x.keys[i])
            for k in range(len(rsnode.keys)):
                cnode.keys.append(rsnode.keys[k])
                if len(rsnode.children) > 0:
                    cnode.children.append(rsnode.children[k])
            if len(rsnode.children) > 0:
                cnode.children.append(rsnode.children.pop())
            new = cnode
            x.keys.pop(i)
            x.children.pop(j)
        else:
            lsnode = x.children[j]
            lsnode.keys.append(x.keys[j])
            # CORRECCIÓN: el bucle usaba 'i' como variable de iteración,
            # pisando el parámetro 'i' y haciendo que x.children.pop(i)
            # eliminara el hijo incorrecto. Se renombra a 'k'.
            for k in range(len(cnode.keys)):
                lsnode.keys.append(cnode.keys[k])
                if len(lsnode.children) > 0:
                    lsnode.children.append(cnode.children[k])
            if len(lsnode.children) > 0:
                lsnode.children.append(cnode.children.pop())
            new = lsnode
            x.keys.pop(j)
            x.children.pop(i)  # 'i' ahora sí es el parámetro original

        if x == self.root and len(x.keys) == 0:
            self.root = new

    def delete_sibling(self, x, i, j):
        cnode = x.children[i]
        if i < j:
            rsnode = x.children[j]
            cnode.keys.append(x.keys[i])
            x.keys[i] = rsnode.keys[0]
            if len(rsnode.children) > 0:
                cnode.children.append(rsnode.children[0])
                rsnode.children.pop(0)
            rsnode.keys.pop(0)
        else:
            lsnode = x.children[j]
            cnode.keys.insert(0, x.keys[i - 1])
            x.keys[i - 1] = lsnode.keys.pop()
            if len(lsnode.children) > 0:
                cnode.children.insert(0, lsnode.children.pop())


class BplusTree:
    def __init__(self, order):
        self.root = BPlusNode(order)
        self.root.check_leaf = True

    # Operación de inserción
    def insert(self, value, key):
        value = str(value)
        old_node = self.search(value)
        # CORRECCIÓN: se pasaba 'old_node' como primer argumento explícito,
        # pero ya es 'self' implícitamente; eso desplazaba value y key.
        old_node.insert_at_leaf(value, key)

        if (len(old_node.values) == old_node.order):
            node1 = BPlusNode(old_node.order)
            node1.check_leaf = True
            node1.parent = old_node.parent
            mid = int(math.ceil(old_node.order / 2)) - 1
            node1.values = old_node.values[mid + 1:]
            node1.keys = old_node.keys[mid + 1:]
            node1.nextKey = old_node.nextKey
            old_node.values = old_node.values[:mid + 1]
            old_node.keys = old_node.keys[:mid + 1]
            old_node.nextKey = node1
            self.insert_in_parent(old_node, node1.values[0], node1)

    # Búsqueda para uso interno
    def search(self, value):
        current_node = self.root
        while current_node.check_leaf == False:
            temp2 = current_node.values
            for i in range(len(temp2)):
                if (value == temp2[i]):
                    current_node = current_node.keys[i + 1]
                    break
                elif (value < temp2[i]):
                    current_node = current_node.keys[i]
                    break
                elif (i + 1 == len(current_node.values)):
                    current_node = current_node.keys[i + 1]
                    break
        return current_node

    # Búsqueda de un registro concreto
    def find(self, value, key):
        l = self.search(value)
        for i, item in enumerate(l.values):
            if item == value:
                if key in l.keys[i]:
                    return True
                else:
                    return False
        return False

    # Inserción en el nodo padre
    def insert_in_parent(self, n, value, ndash):
        if (self.root == n):
            rootBPlusNode = BPlusNode(n.order)
            rootBPlusNode.values = [value]
            rootBPlusNode.keys = [n, ndash]
            self.root = rootBPlusNode
            n.parent = rootBPlusNode
            ndash.parent = rootBPlusNode
            return

        parentBPlusNode = n.parent
        temp3 = parentBPlusNode.keys
        for i in range(len(temp3)):
            if (temp3[i] == n):
                parentBPlusNode.values = parentBPlusNode.values[:i] + \
                    [value] + parentBPlusNode.values[i:]
                parentBPlusNode.keys = parentBPlusNode.keys[:i + 1] + \
                    [ndash] + parentBPlusNode.keys[i + 1:]
                if (len(parentBPlusNode.keys) > parentBPlusNode.order):
                    parentdash = BPlusNode(parentBPlusNode.order)
                    parentdash.parent = parentBPlusNode.parent
                    mid = int(math.ceil(parentBPlusNode.order / 2)) - 1
                    parentdash.values = parentBPlusNode.values[mid + 1:]
                    parentdash.keys = parentBPlusNode.keys[mid + 1:]
                    value_ = parentBPlusNode.values[mid]
                    if (mid == 0):
                        parentBPlusNode.values = parentBPlusNode.values[:mid + 1]
                    else:
                        parentBPlusNode.values = parentBPlusNode.values[:mid]
                    parentBPlusNode.keys = parentBPlusNode.keys[:mid + 1]
                    for j in parentBPlusNode.keys:
                        j.parent = parentBPlusNode
                    for j in parentdash.keys:
                        j.parent = parentdash
                    self.insert_in_parent(parentBPlusNode, value_, parentdash)


def mostrar_btree(node, nivel=0):
    print("Nivel", nivel, ":", node.keys)
    if not node.leaf:
        for hijo in node.children:
            mostrar_btree(hijo, nivel + 1)


def mostrar_bplustree(tree):
    nodo = tree.root
    while not nodo.check_leaf:
        nodo = nodo.keys[0]

    print("\nHojas del B+ Tree:")
    while nodo:
        print(nodo.values, end=" -> ")
        nodo = nodo.nextKey
    print("None\n")


def menu():
    orden_btree = int(input("Ingrese el grado mínimo (t) del B-Tree: "))
    btree = BTree(orden_btree)

    orden_bplus = int(input("Ingrese el orden del B+ Tree: "))
    bplus = BplusTree(orden_bplus)

    while True:
        print("\n===== MENÚ =====")
        print("1. Insertar en B-Tree")
        print("2. Buscar en B-Tree")
        print("3. Eliminar en B-Tree")
        print("4. Mostrar B-Tree")
        print("5. Insertar en B+ Tree")
        print("6. Buscar en B+ Tree")
        print("7. Mostrar hojas B+ Tree")
        print("8. Salir")

        opcion = input("Seleccione una opción: ")

        # B-TREE
        if opcion == "1":
            clave = int(input("Clave a insertar: "))
            btree.insert(clave)
            print("Clave insertada.")

        elif opcion == "2":
            clave = int(input("Clave a buscar: "))
            resultado = btree.search(clave)
            if resultado:
                nodo, pos = resultado
                print(f"Encontrada en posición {pos}: {nodo.keys}")
            else:
                print("No encontrada.")

        elif opcion == "3":
            clave = int(input("Clave a eliminar: "))
            btree.delete(btree.root, clave)
            print("Operación completada.")

        elif opcion == "4":
            print("\nEstructura del B-Tree:")
            mostrar_btree(btree.root)

        # B+ TREE
        elif opcion == "5":
            valor = input("Valor: ")
            clave = input("Clave asociada: ")
            bplus.insert(valor, clave)
            print("Registro insertado.")

        elif opcion == "6":
            valor = input("Valor a buscar: ")
            clave = input("Clave asociada: ")
            if bplus.find(valor, clave):
                print("Registro encontrado.")
            else:
                print("Registro no encontrado.")

        elif opcion == "7":
            mostrar_bplustree(bplus)

        elif opcion == "8":
            print("Saliendo...")
            break

        else:
            print("Opción inválida.")


if __name__ == "__main__":
    menu()
