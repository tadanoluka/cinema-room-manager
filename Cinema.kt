package cinema

class CinemaRoom(_rows: Int, _columns: Int) {
    val rows = if (_rows <= 9) _rows else 0
    val columns = if (_columns <= 9) _columns else 0
    val grid = mutableListOf(mutableListOf<String>())

    val defaultPrice = 10
    val defaultBackPrice = 8

    val numberOfSeats: Int
    val isALargeRoom: Boolean

    init {
        makeGrid()
        numberOfSeats = rows * columns
        isALargeRoom = numberOfSeats > 60
    }

    private fun makeGrid() {
        val firstRow = mutableListOf<String>(" ")
        for (i in 1..columns ) {
            firstRow.add(i.toString())
        }
        grid.add(firstRow)
        for (i in 1..rows) {
            val rowList = mutableListOf<String>(i.toString())
            repeat (columns) {
                rowList.add("S")
            }
            grid.add(rowList)
        }
        grid.removeAt(0)
    }

    fun printGrid() {
        println("Cinema:")
        for (i in 0..rows) {
            println(grid[i].joinToString(" "))
        }
    }

    fun bookSeat(row: Int, seat: Int) {
        if (row <= rows && seat <= columns) {
            if (grid[row][seat] == "S") {
                grid[row][seat] = "B"

                val ticketPrice = if (isALargeRoom) {
                    if (row <= rows / 2) {
                        defaultPrice
                    } else defaultBackPrice
                } else defaultPrice
                println("Ticket price: \$$ticketPrice")
            }
        }
    }
}

class CinemaManager {
    val cinemaRooms: MutableList<CinemaRoom> = mutableListOf()
    var currentRoom: CinemaRoom

    init {
        currentRoom = if (cinemaRooms.isNotEmpty()) {
            cinemaRooms[0]
        } else CinemaRoom(0,0)
    }

    fun addCinemaRoom(rows: Int, columns: Int) {
        cinemaRooms.add(CinemaRoom(rows, columns))
        currentRoom = cinemaRooms[0]
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

    println("Enter the number of rows:")
    val rows = readln().toInt()
    println("Enter the number of seats in each row:")
    val columns = readln().toInt()

    cinemaManager.addCinemaRoom(rows, columns)
    println()
    cinemaManager.currentRoom.printGrid()
    println()

    println("Enter a row number:")
    val x = readln().toInt()
    println("Enter a seat number in that row:")
    val y = readln().toInt()

    println()
    cinemaManager.currentRoom.bookSeat(x, y)
    println()

    cinemaManager.currentRoom.printGrid()

}