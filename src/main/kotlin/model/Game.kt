package model

import tools.CardsReader
import tools.Colors

class Game {
    private var players: List<Player>
    private var currentPlayer: Player
    private var gameLoop: Boolean = true
    private var round: Int = 1

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
            // Check if the game is over
            finish()
            // Give a card to the current player
            giveCard()
            // Print the board
            printBoard()
            // Choose use card, discard card, attack or change card position
            chooseAction()
            // Change current player and increment round
            changeTurn()
        }
    }

    private fun chooseAction() {
        val actionsNumber = if (round > 2) 4 else 2
        currentPlayer.printHandCards()

        println("\nChoose an action:")
        println("1 - Put card on board or equip card")
        println("2 - Discard card")

        if (round > 2) {
            println("3 - Attack")
            println("4 - Change card position")
        }

        var action: Int

        do {
            action = (readln().toIntOrNull() ?: 0)
            if (action < 1 || action > actionsNumber) {
                println("Invalid action")
            }
        } while (action < 1 || action > actionsNumber)

        println()

        when (action) {
            1 -> selectCardToPlay()
            2 -> currentPlayer.discardCard()
            3 -> attack()
            4 -> currentPlayer.changeCardPosition()
        }
    }

    private fun attack() {
        val opponent = if (currentPlayer == players.first()) players.last() else players.first()
        val cardsInAttack = currentPlayer.boardCards.filter { it.position == 1 }

        if (cardsInAttack.isEmpty()) {
            println(Colors.RED + "You don't have any cards in attack position to attack" + Colors.RESET)

            return chooseAction()
        }

        // iterate over the cards in attack
        for (attackerCard in cardsInAttack) {
            println("Attacking with ${Colors.RED}${attackerCard.name} ${Colors.RESET} (${attackerCard.attack} attack)")

            continueGame()

            println("Select a ${Colors.RED}enemy's card${Colors.RESET} to attack:")
            opponent.printBoardCards()
            var cardToAttackIndex: Int

            do {
                cardToAttackIndex = (readln().toIntOrNull() ?: 0) - 1
                if (cardToAttackIndex < 0 || cardToAttackIndex >= opponent.boardCards.size) {
                    println(Colors.RED + "Invalid card index" + Colors.RESET)
                }
            } while (cardToAttackIndex < 0 || cardToAttackIndex >= opponent.boardCards.size)

            val cardToAttack = opponent.boardCards[cardToAttackIndex]

            if (cardToAttack.position == 1) {
                // if the opponent's card is in attack, the opponent loses points equal to the difference between the cards' attack
                val attackDifference = attackerCard.attack - cardToAttack.attack
                opponent.points -= attackDifference

                if (attackDifference > 0) {
                    println(Colors.ORANGE + "${opponent.name} loses $attackDifference points" + Colors.RESET)
                } else {
                    println(Colors.ORANGE + "${currentPlayer.name} loses ${-attackDifference} points" + Colors.RESET)
                }

                continueGame()
            } else {
                // if the opponent's card is in defense:
                // if the defense is greater, the attacker loses points equal to the difference between the cards' defense,
                // else the opponent's card is destroyed and the player don't lose points
                val defenseDifference = attackerCard.attack - cardToAttack.defense

                if (cardToAttack.defense < attackerCard.attack) {
                    opponent.boardCards.removeAt(cardToAttackIndex)
                    println(Colors.ORANGE + "${cardToAttack.name} was destroyed" + Colors.RESET)
                }
            }
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
            println("${index + 1} - ${player.name} - ${player.points} points")
            player.printBoardCards()
            println()
        }
        println()
    }

    private fun changeTurn() {
        round++
        currentPlayer = if (currentPlayer == players.first()) players.last() else players.first()
        println("\n==============================================================================\n")
        println("Round $round")
        println(Colors.GREEN + "It's ${currentPlayer.name}'s turn" + Colors.RESET)
    }

    private fun selectCardToPlay() {
        val card = currentPlayer.selectCard()

        when (card.type) {
            "monstro" -> handleMonsterCard(card)
            "equipamento" -> handleEquipmentCard(card)
        }
    }

    private fun handleMonsterCard(card: Card) {
        if (currentPlayer.boardCards.size == 5) {
            println("You have 5 cards on the board, you can't put more cards")
            return
        }

        println("\nYou selected ${card.name}")

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

        println("Now you have ${currentPlayer.getTotalAttack()} attack and ${currentPlayer.getTotalDefense()} defense.")
        continueGame()
    }

    private fun handleEquipmentCard(card: Card) {
        println("\nYou selected ${card.name}")

        if (currentPlayer.boardCards.isEmpty()) {
            println(Colors.RED + "You don't have any cards on the board to equip" + Colors.RESET + "\n")

            // Return the card to the player's hand
            currentPlayer.handCards.add(card)
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

        println("Now you have ${currentPlayer.getTotalAttack()} attack and ${currentPlayer.getTotalDefense()} defense.")
        continueGame()
    }

    private fun continueGame() {
        println("\n${Colors.CYAN}Press enter to continue ${Colors.RESET}")
        readln()
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
