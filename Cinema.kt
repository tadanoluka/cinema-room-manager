package cinema

class CinemaGrid {
    val row = 7
    val column = 8
    val grid = mutableListOf(mutableListOf<String>())

    init {
        val firstRow = mutableListOf<String>(" ")
        for (i in 1..column) {
            firstRow.add(i.toString())
        }
        grid.add(firstRow)
        for (i in 1..row) {
            val rowList = mutableListOf<String>(i.toString())
            repeat (column) {
                rowList.add("S")
            }
            grid.add(rowList)
        }
        grid.removeAt(0)
    }
}

class CinemaManager {
    val cinemaGrid = CinemaGrid()

    fun printGrid() {
        println("Cinema:")
        for (i in 0..cinemaGrid.row) {
            println(cinemaGrid.grid[i].joinToString(" "))
        }

    }
}

fun main() {
    val cinemaManager = CinemaManager()
    cinemaManager.printGrid()
}