package model

import tools.CardsReader

class Game {
    private var players: List<Player>
    private var currentPlayer: Player
    private var gameLoop: Boolean = true
    private var round: Int = 0

    init {
        println("============= MAGIC: THE KOTLIRING =============")
        players = readPlayers()
        currentPlayer = players.first()
    }

    private fun readPlayers(): List<Player> {
        val playersList = mutableListOf<Player>()

        for (playerNumber in 1..2) {
            var playerName: String

            do {
                println("Player $playerNumber, please enter your name:")
                playerName = readlnOrNull().toString()
            } while (playerName.isBlank())

            playersList.add(Player(playerName))
        }

        return playersList
    }

    fun start() {
        while (gameLoop) {
            finish()
            giveCard()
            printBoard()
            selectCardToPlay()
            changeTurn()
            round++
        }
    }

    private fun giveCard() {
        if (round > 2) {
            currentPlayer.receiveCard()
        }
    }

    private fun printBoard() {
        println("\nBoard:")
        players.forEachIndexed { index, player ->
            println("${index + 1} - ${player.name} - ${player.getTotalAttack()} attack - ${player.getTotalDefense()} defense")
            player.printBoardCards()
        }
        println()
    }

    private fun changeTurn() {
        currentPlayer = if (currentPlayer == players.first()) players.last() else players.first()
        println("\n==============================================================================\n")
        println("It's ${currentPlayer.name}'s turn")
    }

    private fun selectCardToPlay() {
        val card = currentPlayer.selectCard()

        when (card.type) {
            "monstro" -> handleMonsterCard(card)
            "equipamento" -> handleEquipmentCard(card)
        }

        println("You selected ${card.name}")
        println("Now you have ${currentPlayer.getTotalAttack()} attack and ${currentPlayer.getTotalDefense()} defense.")
        println("Press enter to continue")
        readln()
    }

    private fun handleMonsterCard(card: Card) {
        if (currentPlayer.boardCards.size == 5) {
            println("You have 5 cards on the board, you can't put more cards")
            return
        }

        println("Select a position to put the card:")
        println("1 - Attack")
        println("2 - Defense")
        var position: Int

        do {
            position = (readln().toIntOrNull() ?: 0)
            if (position < 1 || position > 2) {
                println("Invalid position")
            }
        } while (position < 1 || position > 2)

        card.position = position
        currentPlayer.putCardOnBoard(card)
    }

    private fun handleEquipmentCard(card: Card) {
        if (currentPlayer.boardCards.isEmpty()) {
            println("You don't have any cards on the board to equip")
            return selectCardToPlay()
        }

        println("Select a card to equip:")
        currentPlayer.printBoardCards()
        var cardToEquipIndex: Int

        do {
            cardToEquipIndex = (readln().toIntOrNull() ?: 0) - 1
            if (cardToEquipIndex < 0 || cardToEquipIndex >= currentPlayer.boardCards.size) {
                println("Invalid card index")
            }
        } while (cardToEquipIndex < 0 || cardToEquipIndex >= currentPlayer.boardCards.size)

        currentPlayer.boardCards[cardToEquipIndex].equip(card)
    }

    private fun finish() {
        if (CardsReader.getCards().isEmpty()) {
            println("No more cards to play. Game over.")
            val winner = players.maxByOrNull { it.points }
            val losers = players.filter { it != winner }

            val winners: List<Player> = if (losers.all { it.points == winner?.points }) {
                players
            } else {
                listOfNotNull(winner)
            }

            if (winners.size == 1) {
                if (winner != null) {
                    println("${winner.name} wins!")
                }
            } else {
                println("It's a tie between ${winners.joinToString { it.name }}!")
            }

            gameLoop = false
        }
    }
}
