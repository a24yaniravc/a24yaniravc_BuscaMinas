data class Celda(
    var tieneMina: Boolean = false, // Tiene una mina
    var destapada: Boolean = false, // Está destapada
    var tieneBandera: Boolean = false, // Tiene una bandera
    var minasAdyacentes: Int = 0 // Cantidad de minas adyacentes a la celda
)
