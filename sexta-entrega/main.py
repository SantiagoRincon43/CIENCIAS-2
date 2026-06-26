from rotor import Rotor
from reflector import Reflector
from clavijero import Clavijero
from enigma import Enigma


def crear_enigma(rotor_izq, rotor_med, rotor_der,
                 pos_izq="A", pos_med="A", pos_der="A",
                 reflector="B", pares_clavijero=None):
    """
    Crea una máquina Enigma lista para usar.

    rotor_izq/med/der : nombre del rotor ("I" al "V")
    pos_izq/med/der   : posición inicial ("A"-"Z")
    reflector         : "B" o "C"
    pares_clavijero   : lista de pares, ej. ["AB", "CD"]
    """
    rotores = [
        Rotor(rotor_izq, pos_izq),
        Rotor(rotor_med, pos_med),
        Rotor(rotor_der, pos_der),
    ]
    ref = Reflector(reflector)
    clav = Clavijero(pares_clavijero or [])
    return Enigma(rotores, ref, clav)


if __name__ == "__main__":
    print("=" * 50)
    print("         MÁQUINA ENIGMA - SIMULADOR")
    print("=" * 50)

    # --- Configuración de ejemplo ---
    # Rotores I, II, III con posiciones A, B, C
    # Reflector B
    # Clavijero con los pares AZ, BX, CY
    maquina = crear_enigma(
        rotor_izq="I",   pos_izq="A",
        rotor_med="II",  pos_med="B",
        rotor_der="III", pos_der="C",
        reflector="B",
        pares_clavijero=["AZ", "BX", "CY"]
    )

    mensaje = "HOLA MUNDO"
    print(f"\nConfiguración:")
    print(f"  Rotores    : I(A)  II(B)  III(C)")
    print(f"  Reflector  : B")
    print(f"  Clavijero  : AZ BX CY")
    print(f"\nMensaje original : {mensaje}")

    cifrado = maquina.cifrar_mensaje(mensaje)
    print(f"Mensaje cifrado  : {cifrado}")

    # Para descifrar, se recrea la máquina con la misma config y se pasa el cifrado
    maquina2 = crear_enigma(
        rotor_izq="I",   pos_izq="A",
        rotor_med="II",  pos_med="B",
        rotor_der="III", pos_der="C",
        reflector="B",
        pares_clavijero=["AZ", "BX", "CY"]
    )
    descifrado = maquina2.cifrar_mensaje(cifrado)
    print(f"Mensaje descifrado: {descifrado}")

    print("\n" + "=" * 50)
    print("Modo interactivo (Enter vacío para salir)")
    print("=" * 50)

    while True:
        texto = input("\nEscribe un mensaje: ").strip()
        if not texto:
            break

        pos_izq = input("Posición rotor izquierdo (A-Z, default A): ").strip().upper() or "A"
        pos_med = input("Posición rotor medio    (A-Z, default A): ").strip().upper() or "A"
        pos_der = input("Posición rotor derecho  (A-Z, default A): ").strip().upper() or "A"

        maquina_inter = crear_enigma(
            rotor_izq="I",   pos_izq=pos_izq,
            rotor_med="II",  pos_med=pos_med,
            rotor_der="III", pos_der=pos_der,
            reflector="B",
        )
        resultado = maquina_inter.cifrar_mensaje(texto.upper())
        print(f"Resultado: {resultado}")
