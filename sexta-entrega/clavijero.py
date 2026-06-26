class Clavijero:
    def __init__(self, pares=None):
        # pares es una lista de strings tipo ["AB", "CD", "EF"]
        self.mapeo = {}
        if pares:
            for par in pares:
                a, b = par[0].upper(), par[1].upper()
                self.mapeo[a] = b
                self.mapeo[b] = a

    def pasar(self, letra):
        return self.mapeo.get(letra, letra)
