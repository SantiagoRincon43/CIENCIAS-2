import os
import time

def xor_compresion(byte_val: int) -> int:
    bits = format(byte_val, "08b")
    cuatro = ""
    for i in range(0, 8, 2):
        cuatro += "0" if bits[i] == bits[i + 1] else "1"
    return int(cuatro, 2)

def rotar_izq(byte_val: int, n: int) -> int:
    n %= 8
    return ((byte_val << n) | (byte_val >> (8 - n))) & 0xFF

_INIT_CONSTANTS = [
    0x6A, 0x09, 0xE6, 0x67, 0xBB, 0x67, 0xAE, 0x85,
    0x3C, 0x6E, 0xF3, 0x72, 0xA5, 0x4F, 0xF5, 0x3A,
    0x51, 0x0E, 0x52, 0x7F, 0x9B, 0x05, 0x68, 0x8C,
    0x1F, 0x83, 0xD9, 0xAB, 0x5B, 0xE0, 0xCD, 0x19,
]

def hash_unidir(mensaje: str, bits_salida: int = 128) -> str:
    if bits_salida not in (64, 128, 256):
        raise ValueError("bits_salida debe ser 64, 128 o 256")

    n_bytes = bits_salida // 8
    estado = bytearray(_INIT_CONSTANTS[:n_bytes])

    datos = mensaje.encode("utf-8")
    longitud_original = len(datos)

    datos_pad = bytearray(datos)
    datos_pad.append(0x80)
    while len(datos_pad) % n_bytes != 0:
        datos_pad.append(0x00)

    datos_pad += longitud_original.to_bytes(8, "big")
    while len(datos_pad) % n_bytes != 0:
        datos_pad.append(0x00)

    for bloque_inicio in range(0, len(datos_pad), n_bytes):
        bloque = datos_pad[bloque_inicio : bloque_inicio + n_bytes]
        for j, byte_val in enumerate(bloque):
            combinado    = (estado[j] ^ byte_val) & 0xFF
            nibble_datos = xor_compresion(byte_val)
            nibble_mix   = xor_compresion(combinado)
            estado[j] = rotar_izq(estado[j], nibble_datos + 1) ^ nibble_mix
            estado[j] ^= estado[(j + 1) % n_bytes]
            estado[j] = (estado[j] + estado[(j - 1) % n_bytes]) & 0xFF

    CONSTANTES_FIN = [0x5A, 0xA5, 0xC3, 0x3C]
    for ronda in range(4):
        for j in range(n_bytes):
            estado[j] = rotar_izq(estado[j], ronda + 1)
            estado[j] ^= estado[(j + ronda + 1) % n_bytes]
            estado[j] = (estado[j] + CONSTANTES_FIN[ronda]) & 0xFF

    return estado.hex()


_registro: dict[str, list[str]] = {}

def registrar_y_detectar(mensaje: str, bits_salida: int = 128) -> dict:
    h = hash_unidir(mensaje, bits_salida)
    clave = f"{bits_salida}:{h}"

    colisores = []
    if clave in _registro:
        previos = _registro[clave]
        colisores = [m for m in previos if m != mensaje]
        if mensaje not in previos:
            _registro[clave].append(mensaje)
    else:
        _registro[clave] = [mensaje]

    return {
        "hash"     : h,
        "bits"     : bits_salida,
        "colision" : len(colisores) > 0,
        "colisores": colisores,
    }

def limpiar_registro():
    _registro.clear()


def separador(titulo: str = ""):
    ancho = 62
    if titulo:
        print(f"\n{'─'*3} {titulo} {'─'*(ancho - len(titulo) - 5)}")
    else:
        print("─" * ancho)

def mostrar_resultado(res: dict, mensaje: str):
    h       = res["hash"]
    bits    = res["bits"]
    hex_len = len(h)
    print(f"  Entrada  : {repr(mensaje)}")
    print(f"  Hash     : {h}")
    print(f"  Dimensión: {hex_len} hex = {bits} bits  ← FIJO siempre")
    if res["colision"]:
        print(f"  ⚠  COLISIÓN DETECTADA con: {res['colisores']}")
    else:
        print(f"  ✓  Sin colisión registrada")

def menu():
    print("   HASH DE UNA SOLA VÍA — Universidad Distrital")
    print("   Ciencias de la Computación II  |  Entrega #4 (Plus)")
    print("\nSelecciona el tamaño del hash:")
    print("  [1] 64 bits  (16 hex chars)")
    print("  [2] 128 bits (32 hex chars)  ← predeterminado")
    print("  [3] 256 bits (64 hex chars)")
    print("  [4] Demo automático")
    print("  [0] Salir")
    print()

    opcion = input("Opción: ").strip()
    mapa = {"1": 64, "2": 128, "3": 256}

    if opcion == "0":
        print("Hasta luego.")
        return
    if opcion == "4":
        demo_automatico()
        return

    bits = mapa.get(opcion, 128)

    while True:
        print()
        texto = input("Escribe un mensaje ('q' para volver): ")
        if texto.lower() == "q":
            break
        inicio = time.perf_counter()
        res    = registrar_y_detectar(texto, bits)
        dur    = (time.perf_counter() - inicio) * 1000
        mostrar_resultado(res, texto)
        print(f"  Tiempo   : {dur:.3f} ms")
        separador()

def demo_automatico():
    separador("DEMOSTRACIÓN AUTOMÁTICA")

    casos = [
        ("Texto corto",               "hola",                          128),
        ("Texto largo",               "Criptografia personalizada sin patrones comerciales, ¡funciona!", 128),
        ("Un carácter",               "A",                             128),
        ("Mismo msg, distintos bits", "hola",                          256),
        ("Mínima diferencia (a/b)",   "a",                              64),
        ("Mínima diferencia (a/b)",   "b",                              64),
        ("Cadena vacía",              "",                              128),
        ("Emojis",                    "🔒 Cripto_N0_St4nd4rd 🚀",      128),
        ("Dup. a propósito",          "hola",                          128),
    ]

    separador("Prueba 1 — Dimensión fija sin importar longitud")
    for desc, msg, bits in casos[:4]:
        h = hash_unidir(msg, bits)
        print(f"  [{bits:3d}b] {desc:35s} | len={len(msg):3d} | hash({len(h)*4}b)={h}")

    separador("Prueba 2 — Efecto Avalancha (1 bit de diferencia → hash completamente distinto)")
    msgs_avalancha = ["abc", "abd", "Abc", "ABC", "abc "]
    print(f"  {'Mensaje':10s}  {'Hash (128 bits)'}")
    for m in msgs_avalancha:
        print(f"  {repr(m):10s}  {hash_unidir(m, 128)}")

    separador("Prueba 3 — Detección de Colisiones")
    limpiar_registro()
    for desc, msg, bits in casos:
        res = registrar_y_detectar(msg, bits)
        icono = "⚠ COLISIÓN" if res["colision"] else "✓ OK       "
        print(f"  {icono} | [{bits}b] {repr(msg):45s} → {res['hash'][:16]}…")
        if res["colision"]:
            print(f"          Colisiona con: {res['colisores']}")

    separador("Prueba 4 — Determinismo (mismo mensaje = mismo hash siempre)")
    msg_test = "Universidad Distrital"
    hashes = [hash_unidir(msg_test, 128) for _ in range(5)]
    iguales = all(h == hashes[0] for h in hashes)
    print(f"  Mensaje : {repr(msg_test)}")
    print(f"  5 ejecuciones: {hashes[0]}")
    print(f"  Todas idénticas: {'✓ SÍ' if iguales else '✗ NO'}")
    separador()


if __name__ == "__main__":
    if len(os.sys.argv) > 1:
        msg  = " ".join(os.sys.argv[1:])
        bits = 128
        res  = registrar_y_detectar(msg, bits)
        mostrar_resultado(res, msg)
    else:
        menu()
