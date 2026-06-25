# Programa de cifrado de una sola via
# Estoy practicando python :)
# La idea: el mensaje NO se puede recuperar porque se pierde informacion
 
# primero una funcion para pasar una letra a binario de 8 bits
def letra_a_binario(letra):
    numero = ord(letra)        # ord me da el numero de la letra
    binario = bin(numero)      # bin lo pasa a binario pero pone "0b" adelante
    binario = binario[2:]      # le quito el "0b"
    # ahora relleno con ceros adelante hasta que tenga 8 bits
    while len(binario) < 8:
        binario = "0" + binario
    return binario
 
 
def cifrar(mensaje):
    resultado = ""
 
    # recorro letra por letra
    for letra in mensaje:
        bits = letra_a_binario(letra)
        # print("la letra", letra, "en binario es", bits)  # esto era para probar
 
        # ahora hago XOR pero de dos en dos bits
        # si los dos bits son iguales da 0, si son diferentes da 1
        cuatro_bits = ""
        posicion = 0
        while posicion < 8:
            bit1 = bits[posicion]
            bit2 = bits[posicion + 1]
            if bit1 == bit2:
                cuatro_bits = cuatro_bits + "0"
            else:
                cuatro_bits = cuatro_bits + "1"
            posicion = posicion + 2
 
        # aqui ya solo me quedan 4 bits (perdi la mitad)
        # ahora relleno intercalando el patron: dato 1 dato 0 dato 1 dato 0
        patron = "1010"
        ocho_bits = ""
        for i in range(4):
            ocho_bits = ocho_bits + cuatro_bits[i]   # un dato
            ocho_bits = ocho_bits + patron[i]        # un bit del patron
 
        # convierto esos 8 bits otra vez en una letra
        numero = int(ocho_bits, 2)
        letra_cifrada = chr(numero)
        resultado = resultado + letra_cifrada
 
    return resultado
 
 
def descifrar(mensaje_cifrado):
    resultado = ""
 
    for letra in mensaje_cifrado:
        bits = letra_a_binario(letra)
 
        # el patron estaba en las posiciones 1, 3, 5, 7
        # entonces mis datos estan en las posiciones 0, 2, 4, 6
        datos = ""
        datos = datos + bits[0]
        datos = datos + bits[2]
        datos = datos + bits[4]
        datos = datos + bits[6]
 
        # trato de devolver los 8 bits, pero como no se que habia en cada par
        # pongo un 0 al lado y ya. (por eso no va a salir igual)
        ocho_bits = ""
        for i in range(4):
            ocho_bits = ocho_bits + datos[i]
            ocho_bits = ocho_bits + "0"
 
        numero = int(ocho_bits, 2)
        resultado = resultado + chr(numero)
 
    return resultado
 
 
# parte principal del programa
mensaje = input("Escribe una frase: ")
 
encriptado = cifrar(mensaje)
desencriptado = descifrar(encriptado)
 
print("Mensaje original: ", mensaje, "(", len(mensaje), "letras )")
print("Encriptado:       ", encriptado, "(", len(encriptado), "letras )")
print("Desencriptado:    ", desencriptado, "(", len(desencriptado), "letras )")
 
# compruebo si se recupero el mensaje
if desencriptado == mensaje:
    print("Se pudo recuperar el mensaje")
else:
    print("NO se pudo recuperar el mensaje (asi tiene que ser, es de una sola via)")
 
print("="*30)
