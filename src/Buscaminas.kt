class Buscaminas(private val filas: Int, private val columnas: Int, private val minas: Int) {
    private var _juegoTerminado: Boolean = false // Propiedad privada para manejar el estado interno

    var juegoTerminado: Boolean
        get() = _juegoTerminado // Getter público
        private set(value) { _juegoTerminado = value } // Setter privado

    private var banderasColocadas = 0 // Num banderas colocadas

    private val tablero: Array<Array<Celda>> = Array(filas) { Array(columnas) { Celda() } } // Tablero

    init {
        // Excepciones
        if (filas <= 1 || columnas <= 1) throw Exception("Tanto el número de filas como el de columnas deben ser mayor que 1.")
        if (minas >= filas * columnas) throw Exception("El número de minas no puede ser mayor o igual que el número de celdas.")

        // Colocamos las minas en el tablero de forma aleatoria
        colocarMinas()
        // Calculamos el número de minas adyacentes para cada celda
        calcularMinasAdyacentes()
    }

    // Colocar las minas de manera aleatoria
    private fun colocarMinas() {
        var minasColocadas = 0

        while (minasColocadas < minas) {
            val fila = (0 until filas).random()
            val columna = (0 until columnas).random()
            if (!tablero[fila][columna].tieneMina) {
                tablero[fila][columna].tieneMina = true
                minasColocadas++
            }
        }
    }

    // Calcular las minas adyacentes
    private fun calcularMinasAdyacentes() {
        for (fila in 0 until filas) {
            for (columna in 0 until columnas) {
                if (tablero[fila][columna].tieneMina) continue

                var minasAdyacentes = 0

                for (i in -1..1) {
                    for (j in -1..1) {
                        // Ignoramo la celda misma
                        if (i == 0 && j == 0) continue

                        val nuevaFila = fila + i
                        val nuevaColumna = columna + j

                        // Dentro límites
                        if (nuevaFila in 0 until filas && nuevaColumna in 0 until columnas) {
                            if (tablero[nuevaFila][nuevaColumna].tieneMina) {
                                minasAdyacentes++
                            }
                        }
                    }
                }
                tablero[fila][columna].minasAdyacentes = minasAdyacentes
            }
        }
    }

    // Destapar una celda
    fun destapar(fila: Int, columna: Int): Boolean {
        val celda = tablero[fila][columna]
        celda.destapada = true

        if (celda.tieneMina) {
            terminarJuego() // Termina el juego si se destapa una mina
            return false
        }

        // Si no hay minas adyacentes, destapamos celdas adyacentes automáticamente
        if (celda.minasAdyacentes == 0) {
            for (i in -1..1) {
                for (j in -1..1) {
                    val nuevaFila = fila + i
                    val nuevaColumna = columna + j
                    if (nuevaFila in 0 until filas && nuevaColumna in 0 until columnas) {
                        if (!tablero[nuevaFila][nuevaColumna].destapada) {
                            destapar(nuevaFila, nuevaColumna)
                        }
                    }
                }
            }
        }

        return true
    }


    // BANDERAS //

    // Colocar una bandera
    fun colocarBandera(fila: Int, columna: Int): Boolean {
        if (juegoTerminado || fila !in 0 until filas || columna !in 0 until columnas || tablero[fila][columna].destapada) {
            return false
        }
        if (banderasColocadas >= minas) {
            println("No puedes colocar más banderas.")
            return false
        }
        tablero[fila][columna].tieneBandera = true
        banderasColocadas++
        return true
    }


    // Quitar una bandera
    fun quitarBandera(fila: Int, columna: Int): Boolean {
        if (juegoTerminado || fila !in 0 until filas || columna !in 0 until columnas || tablero[fila][columna].destapada) {
            return false
        }
        if (!tablero[fila][columna].tieneBandera) {
            println("No hay bandera en esta celda.")
            return false
        }
        tablero[fila][columna].tieneBandera = false
        banderasColocadas--
        return true
    }

    // ------------------------------------------------- //

    // Imprimir tablero
    fun obtenerTablero(): Array<Array<Celda>> {
        return tablero
    }

    fun imprimirTableroCompleto() {
        for (fila in tablero) {
            for (celda in fila) {
                when {
                    celda.destapada -> {
                        if (celda.tieneMina) print("M ")  // Mina
                        else print("${celda.minasAdyacentes} ")  // Cantidad de minas adyacentes
                    }
                    else -> print("X ")  // Celda no destapada
                }
            }
            println()
        }
    }
    // Terminar juego
    fun terminarJuego() {
        _juegoTerminado = true
    }

    // Verificar si el jugador ha ganado
    fun verificarVictoria(): Boolean {
        for (fila in 0 until filas) {
            for (columna in 0 until columnas) {
                if (!tablero[fila][columna].tieneMina && !tablero[fila][columna].destapada) {
                    return false
                }
            }
        }
        terminarJuego() // Termina el juego si el jugador ha ganado
        return true
    }
}
