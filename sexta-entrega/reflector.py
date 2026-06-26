class Reflector:
    # Reflectores históricos
    CONFIGURACIONES = {
        "B": "YRUHQSLDPXNGOKMIEBFZCWVJAT",
        "C": "FVPJIAOYEDRZXWGCTKUQSBNMHL",
    }

    def __init__(self, nombre="B"):
        self.nombre = nombre
        self.cableado = self.CONFIGURACIONES[nombre]

    def reflejar(self, letra):
        idx = ord(letra) - ord("A")
        return self.cableado[idx]
