package cinema

class CinemaRoom(_rows: Int, _columns: Int) {
    var rows = if (_rows <= 9) _rows else 0
        set(value) {
            field = if (value <= 9) value else 0
            updateFields()
        }
    var columns = if (_columns <= 9) _columns else 0
        set(value) {
            field = if (value <= 9) value else 0
            updateFields()
        }

    private var grid = mutableListOf(mutableListOf<String>())

    private val defaultPrice = 10
    private val defaultBackPrice = 8

    var numberOfSeats = 0
    var numberOfSoldTickets = 0

    var currentIncome = 0
    var totalIncome = 0

    private var isALargeRoom = false

    init {
        updateFields()
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

    fun bookSeat(row: Int, seat: Int): Boolean {
        if (row <= rows && seat <= columns) {
            if (grid[row][seat] == "S") {
                grid[row][seat] = "B"
                numberOfSoldTickets += 1

                val ticketPrice = if (isALargeRoom) {
                    if (row <= rows / 2) {
                        defaultPrice
                    } else defaultBackPrice
                } else defaultPrice

                currentIncome += ticketPrice

                println("Ticket price: \$$ticketPrice")
                return true
            } else {
                println()
                println("That ticket has already been purchased!")
            }
        } else {
            println()
            println("Wrong input!")
        }
        return false
    }

    private fun updateFields() {
        makeGrid()
        numberOfSeats = rows * columns
        isALargeRoom = numberOfSeats > 60
        calculateExpectedIncomeForRoom()
    }

    private fun calculateExpectedIncomeForRoom() {
        totalIncome = if (numberOfSeats <= 60) {
            numberOfSeats * defaultPrice
        } else {
            val firstHalfOfRoomIncome = rows / 2 * columns * defaultPrice
            val secondHalfOfRoomIncome = (rows / 2 + rows % 2) * columns * defaultBackPrice
            firstHalfOfRoomIncome + secondHalfOfRoomIncome
        }
    }
}

class CinemaManager {
    private val cinemaRooms: MutableList<CinemaRoom> = mutableListOf()

    private var currentState: CinemaManagerState

    private var chosenRoom: CinemaRoom
    private var chosenRow = 0
    private var chosenSeat = 0

    init {
        currentState = CinemaManagerState.EDITING_ROOM_ROWS

        cinemaRooms.add(CinemaRoom(0, 0))
        chosenRoom = if (cinemaRooms.isNotEmpty()) {
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
                        chosenRoom.printGrid()
                        printMenu()
                    }
                    "2" -> {
                        println("\nEnter a row number:")
                        currentState = CinemaManagerState.BUYING_TICKET_CHOOSING_ROW
                    }
                    "3" -> {
                        displayStatistic()
                        printMenu()
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

                currentState = if (chosenRoom.bookSeat(chosenRow, chosenSeat)) {
                    printMenu()
                    CinemaManagerState.CHOOSING_ACTION
                } else {
                    println("\nEnter a row number:")
                    CinemaManagerState.BUYING_TICKET_CHOOSING_ROW
                }
                chosenRow = 0
                chosenSeat = 0
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
        chosenRoom.rows = newRowsValue
    }

    private fun changeCurrentRoomSeatsInRow(newSeatsValue: Int) {
        chosenRoom.columns = newSeatsValue
    }

    private fun printMenu() {
        println()
        println("""
            1. Show the seats
            2. Buy a ticket
            3. Statistics
            0. Exit
        """.trimIndent())
    }

    private fun displayStatistic() {
        val percentage: Double = chosenRoom.numberOfSoldTickets.toDouble() / chosenRoom.numberOfSeats.toDouble() * 100
        val formatPercentage = "%.2f".format(percentage).replace(',','.')
        println()
        println("""
            Number of purchased tickets: ${chosenRoom.numberOfSoldTickets}
            Percentage: $formatPercentage%
            Current income: ${'$'}${chosenRoom.currentIncome}
            Total income: ${'$'}${chosenRoom.totalIncome}
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