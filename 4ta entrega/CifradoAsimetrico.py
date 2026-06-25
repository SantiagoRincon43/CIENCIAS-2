import hashlib

class CriptoAsimetricoDeterminista:
    def __init__(self):
        self.p = 2**31 - 1  
        self.__clave_privada = 987654321
        self.generador_publico = 11
        self.clave_publica = pow(self.generador_publico, self.__clave_privada, self.p)

    def _string_a_bloques(self, texto: str, tamano_bloque: int = 3) -> list:
        bytes_texto = texto.encode('utf-8')
        bloques = []
        for i in range(0, len(bytes_texto), tamano_bloque):
            fragmento = bytes_texto[i:i+tamano_bloque]
            num = int.from_bytes(fragmento, byteorder='big')
            bloques.append(num)
        return bloques

    def _bloques_a_string(self, bloques: list) -> str:
        bytes_totales = bytearray()
        for num in bloques:
            num_bytes = (num.bit_length() + 7) // 8 if num > 0 else 1
            bytes_totales.extend(num.to_bytes(num_bytes, byteorder='big'))
        return bytes_totales.decode('utf-8', errors='ignore')

    def cifrar(self, mensaje: str) -> str:
        bloques = self._string_a_bloques(mensaje)
        bloques_cifrados = []
        
        for bloque in bloques:
            hash_bloque = int(hashlib.sha256(str(bloque).encode()).hexdigest(), 16)
            k_determinista = (hash_bloque % (self.p - 2)) + 2
            
            c1 = pow(self.generador_publico, k_determinista, self.p)
            secreto_compartido = pow(self.clave_publica, k_determinista, self.p)
            c2 = (bloque ^ secreto_compartido) % self.p
            
            bloques_cifrados.append(f"{c1:x}-{c2:x}")
            
        return ":".join(bloques_cifrados)

    def descifrar(self, criptograma: str) -> str:
        bloques_cifrados = criptograma.split(":")
        bloques_descifrados = []
        
        for bloque in bloques_cifrados:
            c1_hex, c2_hex = bloque.split("-")
            c1 = int(c1_hex, 16)
            c2 = int(c2_hex, 16)
            
            secreto_compartido = pow(c1, self.__clave_privada, self.p)
            bloque_original = c2 ^ secreto_compartido
            bloques_descifrados.append(bloque_original)
            
        return self._bloques_a_string(bloques_descifrados)


cripto = CriptoAsimetricoDeterminista()

casos_prueba = {
    "Texto Corto": "hola",
    "Frase Larga": "Criptografia personalizada sin patrones comerciales, ¡funciona!",
    "Caracteres Especiales": "🔒 Cripto_N0_St4nd4rd_2026? 🚀",
    "Datos Numericos": "192.168.1.254:8080",
}

print("=== INICIANDO PRUEBAS ===")

for tipo, dato in casos_prueba.items():
    print(f"\n[{tipo}]")
    print(f"Original: {dato}")
    
    cifrado_A = cripto.cifrar(dato)
    cifrado_B = cripto.cifrar(dato)
    cifrado_C = cripto.cifrar(dato)
    
    print(f"Cifrado: {cifrado_A[:30]}...")  
    
    assert cifrado_A == cifrado_B == cifrado_C, "Error en cifrado"
    
    descifrado_final = cripto.descifrar(cifrado_A)
    assert descifrado_final == dato, "Error en descifrado"
    
    print("Estado: OK")

print("\n" + "="*30)
print("TODAS LAS PRUEBAS OK")
print("="*30)
