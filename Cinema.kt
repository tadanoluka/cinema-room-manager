package cinema

class CinemaRoom(_rows: Int, _columns: Int) {
    var rows = if (_rows <= 9) _rows else 0
        set(value) {
            field = if (value <= 9) value else 0
            makeGrid()
            numberOfSeats = rows * columns
            isALargeRoom = numberOfSeats > 60
        }
    var columns = if (_columns <= 9) _columns else 0
        set(value) {
            field = if (value <= 9) value else 0
            makeGrid()
            numberOfSeats = rows * columns
            isALargeRoom = numberOfSeats > 60
        }
    private var grid = mutableListOf(mutableListOf<String>())

    private val defaultPrice = 10
    private val defaultBackPrice = 8

    private var numberOfSeats: Int
    private var isALargeRoom: Boolean

    init {
        makeGrid()
        numberOfSeats = rows * columns
        isALargeRoom = numberOfSeats > 60
    }

    private fun makeGrid() {
        grid = mutableListOf()
        val firstRow = mutableListOf(" ")
        for (i in 1..columns ) {
            firstRow.add(i.toString())
        }
        grid.add(firstRow)
        for (i in 1..rows) {
            val rowList = mutableListOf(i.toString())
            repeat (columns) {
                rowList.add("S")
            }
            grid.add(rowList)
        }
    }

    fun printGrid() {
        println()
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
    private val cinemaRooms: MutableList<CinemaRoom> = mutableListOf()
    private var currentRoom: CinemaRoom

    private var currentState: CinemaManagerState

    private var chosenRow = 0
    private var chosenSeat = 0

    init {
        currentState = CinemaManagerState.EDITING_ROOM_ROWS

        cinemaRooms.add(CinemaRoom(0, 0))
        currentRoom = if (cinemaRooms.isNotEmpty()) {
            cinemaRooms[0]
        } else CinemaRoom(0,0)

        println("Enter the number of rows:")
    }

    enum class CinemaManagerState {
        EDITING_ROOM_ROWS, EDITING_ROOM_SEATS, CHOOSING_ACTION, BUYING_TICKET_CHOOSING_ROW, BUYING_TICKET_CHOOSING_SEAT
    }

    fun inputInterface(inputString: String){
        when (currentState) {
            CinemaManagerState.CHOOSING_ACTION -> {
                when(inputString.lowercase()) {
                    "1" -> {
                        currentRoom.printGrid()
                        printMenu()
                    }
                    "2" -> {
                        println("\nEnter a row number:")
                        currentState = CinemaManagerState.BUYING_TICKET_CHOOSING_ROW
                    }
                    "0" -> TODO()
                    else -> println("Invalid input")
                }
            }
            CinemaManagerState.BUYING_TICKET_CHOOSING_ROW -> {
                chosenRow = inputString.toInt()

                println("Enter a seat number in that row:")
                currentState = CinemaManagerState.BUYING_TICKET_CHOOSING_SEAT
            }
            CinemaManagerState.BUYING_TICKET_CHOOSING_SEAT -> {
                chosenSeat = inputString.toInt()

                currentRoom.bookSeat(chosenRow, chosenSeat)
                chosenRow = 0
                chosenSeat = 0

                printMenu()
                currentState = CinemaManagerState.CHOOSING_ACTION
            }
            CinemaManagerState.EDITING_ROOM_ROWS -> {
                changeCurrentRoomRows(inputString.toInt())

                currentState = CinemaManagerState.EDITING_ROOM_SEATS
                println("Enter the number of seats in each row:")
            }
            CinemaManagerState.EDITING_ROOM_SEATS -> {
                changeCurrentRoomSeatsInRow(inputString.toInt())

                currentState = CinemaManagerState.CHOOSING_ACTION
                printMenu()
            }
        }
    }

    private fun changeCurrentRoomRows(newRowsValue: Int) {
        currentRoom.rows = newRowsValue
    }

    private fun changeCurrentRoomSeatsInRow(newSeatsValue: Int) {
        currentRoom.columns = newSeatsValue
    }

    private fun printMenu() {
        println()
        println("""
            1. Show the seats
            2. Buy a ticket
            0. Exit
        """.trimIndent())
    }
}

fun main() {
    val cinemaManager = CinemaManager()

    while (true) {
        val userInput = readln()
        if (userInput == "0") break
        else cinemaManager.inputInterface(userInput)
    }

}