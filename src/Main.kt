fun main() {
    println("Bienvenido a Buscaminas!")
    print("Introduce el número de filas: ")
    val filas = readln().toIntOrNull() ?: return
    print("Introduce el número de columnas: ")
    val columnas = readln().toIntOrNull() ?: return
    print("Introduce el número de minas: ")
    val minas = readln().toIntOrNull() ?: return

    try {
        val buscaminas = Buscaminas(filas, columnas, minas)

        while (!buscaminas.juegoTerminado) {
            // Mostrar el tablero
            println("Tablero actual:")
            for (fila in buscaminas.obtenerTablero()) {
                for (celda in fila) {
                    if (celda.destapada) {
                        print("${celda.minasAdyacentes} ")
                    } else if (celda.tieneBandera) {
                        print("F ")
                    } else {
                        print("X ")
                    }
                }
                println()
            }

            println("Selecciona una acción:")
            println("1. Destapar una celda")
            println("2. Colocar bandera")
            println("3. Quitar bandera")
            println("4. Salir")
            print("Elige una opción (1/2/3/4): ")
            val accion = readln().toIntOrNull()

            if (buscaminas.juegoTerminado) {
                // Si el juego ya ha terminado, no permitir más acciones.
                println("¡El juego ha terminado!")
                break
            }

            when (accion) {
                1 -> {
                    print("Introduce fila y columna para destapar (separados por espacio): ")
                    val (fila, columna) = readln().split(" ").mapNotNull { it.toIntOrNull() }

                    if (fila !in 0 until filas || columna !in 0 until columnas) {
                        println("¡Coordenadas fuera del tablero!")
                        continue
                    }

                    val celda = buscaminas.obtenerTablero()[fila][columna]

                    if (celda.destapada) {
                        println("¡La celda ya está destapada!")
                        continue
                    }

                    val destapar = buscaminas.destapar(fila, columna)

                    // Si explota una mina
                    if (!destapar && !buscaminas.verificarVictoria()) {
                        println("¡Explotó!") // Bomba
                    } else if (!destapar && buscaminas.verificarVictoria()) {
                        println("¡Has ganado!") // Gana
                        buscaminas.terminarJuego()
                    } else if (!destapar) {
                        println("No se pudo destapar esa celda o ya está destapada.")
                    }
                }
                2 -> {
                    print("Introduce fila y columna para colocar bandera (separados por espacio): ")
                    val (fila, columna) = readln().split(" ").mapNotNull { it.toIntOrNull() }

                    if (fila !in 0 until filas || columna !in 0 until columnas) {
                        println("¡Coordenadas fuera del tablero!")
                        continue
                    }

                    if (!buscaminas.colocarBandera(fila, columna)) {
                        println("No se pudo colocar la bandera en esa celda.")
                    }
                }
                3 -> {
                    print("Introduce fila y columna para quitar bandera (separados por espacio): ")
                    val (fila, columna) = readln().split(" ").mapNotNull { it.toIntOrNull() }

                    if (fila !in 0 until filas || columna !in 0 until columnas) {
                        println("¡Coordenadas fuera del tablero!")
                        continue
                    }

                    if (!buscaminas.quitarBandera(fila, columna)) {
                        println("No se pudo quitar la bandera de esa celda.")
                    }
                }
                4 -> {
                    println("Has salido con éxito.")
                    break
                }
                else -> println("Opción no válida.")
            }
        }

        println("Tablero completo:")
        buscaminas.imprimirTableroCompleto()

        println("¡Juego terminado!")
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }
}
