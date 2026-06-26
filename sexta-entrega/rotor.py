class Rotor:
    # Configuraciones históricas de los rotores de la Enigma Wehrmacht/Luftwaffe
    CONFIGURACIONES = {
        "I":   {"cableado": "EKMFLGDQVZNTOWYHXUSPAIBRCJ", "notch": "Q"},
        "II":  {"cableado": "AJDKSIRUXBLHWTMCQGZNPYFVOE", "notch": "E"},
        "III": {"cableado": "BDFHJLCPRTXVZNYEIWGAKMUSQO", "notch": "V"},
        "IV":  {"cableado": "ESOVPZJAYQUIRHXLNFTGKDCMWB", "notch": "J"},
        "V":   {"cableado": "VZBRGITYUPSDNHLXAWMJQOFECK", "notch": "Z"},
    }

    def __init__(self, nombre, posicion_inicial="A", anillo=0):
        config = self.CONFIGURACIONES[nombre]
        self.nombre = nombre
        self.cableado = config["cableado"]
        self.notch = config["notch"]
        self.posicion = ord(posicion_inicial) - ord("A")
        self.anillo = anillo  # ring setting (0-25)

    def avanzar(self):
        self.posicion = (self.posicion + 1) % 26

    def en_notch(self):
        return chr(self.posicion + ord("A")) == self.notch

    def pasar_adelante(self, letra):
        # Entrada -> salida (de derecha a izquierda)
        idx = (ord(letra) - ord("A") + self.posicion - self.anillo) % 26
        salida = self.cableado[idx]
        resultado = chr((ord(salida) - ord("A") - self.posicion + self.anillo) % 26 + ord("A"))
        return resultado

    def pasar_atras(self, letra):
        # Salida -> entrada (de izquierda a derecha, camino de vuelta)
        idx = (ord(letra) - ord("A") + self.posicion - self.anillo) % 26
        letra_cable = chr(idx + ord("A"))
        pos_en_cableado = self.cableado.index(letra_cable)
        resultado = chr((pos_en_cableado - self.posicion + self.anillo) % 26 + ord("A"))
        return resultado
