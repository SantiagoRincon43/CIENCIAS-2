from rotor import Rotor
from reflector import Reflector
from clavijero import Clavijero


class Enigma:
    def __init__(self, rotores, reflector, clavijero):
        # rotores: lista de objetos Rotor, de izquierda a derecha
        # la señal entra por la derecha, así que se invierte internamente
        self.rotores = rotores
        self.reflector = reflector
        self.clavijero = clavijero

    def _avanzar_rotores(self):
        # Mecanismo de doble paso (double stepping anomaly incluida)
        r_izq, r_med, r_der = self.rotores[0], self.rotores[1], self.rotores[2]

        if r_med.en_notch():
            r_med.avanzar()
            r_izq.avanzar()
        elif r_der.en_notch():
            r_med.avanzar()

        r_der.avanzar()

    def cifrar_letra(self, letra):
        letra = letra.upper()
        if letra not in "ABCDEFGHIJKLMNOPQRSTUVWXYZ":
            return letra  # espacios, números, etc. pasan sin cambio

        self._avanzar_rotores()

        # 1. Clavijero de entrada
        c = self.clavijero.pasar(letra)

        # 2. Rotores de derecha a izquierda
        for rotor in reversed(self.rotores):
            c = rotor.pasar_adelante(c)

        # 3. Reflector
        c = self.reflector.reflejar(c)

        # 4. Rotores de izquierda a derecha (camino de vuelta)
        for rotor in self.rotores:
            c = rotor.pasar_atras(c)

        # 5. Clavijero de salida
        c = self.clavijero.pasar(c)

        return c

    def cifrar_mensaje(self, mensaje):
        return "".join(self.cifrar_letra(c) for c in mensaje)
