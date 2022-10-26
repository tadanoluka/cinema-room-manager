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

    fun calculateExpectedIncomeForRoom (rows: Int, columns: Int) {
        val numberOfSeats = rows * columns
        val expectedIncome: Int
        val defaultPrice = 10
        val defaultBackPrice = 8
        expectedIncome = if (numberOfSeats <= 60) {
            numberOfSeats * defaultPrice
        } else {
            val firstHalfOfRoomIncome = rows / 2 * columns * defaultPrice
            val secondHalfOfRoomIncome = (rows / 2 + rows % 2) * columns * defaultBackPrice
            firstHalfOfRoomIncome + secondHalfOfRoomIncome
        }
        println("Total income:")
        println("\$$expectedIncome")
    }
}

fun main() {
    val cinemaManager = CinemaManager()
    // cinemaManager.printGrid()

    println("Enter the number of rows:")
    val rows = readln().toInt()
    println("Enter the number of seats in each row:")
    val columns = readln().toInt()
    cinemaManager.calculateExpectedIncomeForRoom(rows, columns)
}