package model

import tools.CardsReader
import tools.Colors

class Player(var name: String) {
    var handCards: MutableList<Card> = mutableListOf()
    var boardCards: MutableList<Card> = mutableListOf()
    var points: Int = 10000

    init {
        handCards.addAll(CardsReader.get5RandomCards())
    }

    private fun printCards(cards: List<Card>, title: String) {
        println("$title:")
        if (cards.isEmpty()) {
            println(Colors.RED + "No cards" + Colors.RESET)
        }

        cards.forEachIndexed { index, card ->
            println("${index + 1} - $card")
        }
    }

    private fun isValidCardIndex(cardIndex: Int): Boolean {
        return cardIndex >= 0 && cardIndex < handCards.size
    }

    fun receiveCard() {
        if (handCards.size >= 10) {
            println(Colors.RED + "You have 10 cards in your hand, you can't receive more cards" + Colors.RESET)
            return
        }

        val newCard = CardsReader.getRandomCard()

        println(Colors.YELLOW + "You received a new card: $newCard" + Colors.RESET)

        handCards.add(newCard)
    }

    fun selectCard(): Card {
        printHandCards()
        print("Select a card: ")

        var cardIndex = -1

        while (!isValidCardIndex(cardIndex)) {
            cardIndex = (readln().toIntOrNull() ?: -1) - 1

            if (!isValidCardIndex(cardIndex)) {
                println(Colors.RED + "Invalid card index" + Colors.RESET)
            }
        }

        return handCards.removeAt(cardIndex)
    }

    fun getTotalAttack(): Int {
        return boardCards.filter { it.position == 1 }.sumOf { it.attack }
    }

    fun getTotalDefense(): Int {
        return boardCards.filter { it.position == 2 }.sumOf { it.defense }
    }

    fun putCardOnBoard(card: Card) {
        handCards.remove(card)
        boardCards.add(card)
    }

    fun printHandCards() {
        printCards(handCards, "Your hand cards")
    }

    fun printBoardCards() {
        printCards(boardCards, "Board cards")
    }

    fun discardCard() {
        if (handCards.isEmpty()) {
            println(Colors.RED + "You don't have any cards in your hand to discard" + Colors.RESET)
            return
        }
        
        printHandCards()
        print("Select a card to discard: ")

        var cardIndex = -1

        while (!isValidCardIndex(cardIndex)) {
            cardIndex = (readln().toIntOrNull() ?: -1) - 1

            if (!isValidCardIndex(cardIndex)) {
                println(Colors.RED + "Invalid card index" + Colors.RESET)
            }
        }

        val card = handCards.removeAt(cardIndex)

        println("\n" + Colors.YELLOW + "You discarded $card" + Colors.RESET + "\n")
    }

    fun changeCardPosition() {
        if (boardCards.isEmpty()) {
            println(Colors.RED + "You don't have any cards on the board to change position" + Colors.RESET)
            return
        }

        printBoardCards()
        print("Select a card to change position: ")

        var cardIndex = -1

        while (!isValidCardIndex(cardIndex)) {
            cardIndex = (readln().toIntOrNull() ?: -1) - 1

            if (!isValidCardIndex(cardIndex)) {
                println(Colors.RED + "Invalid card index" + Colors.RESET)
            }
        }

        val card = boardCards[cardIndex]

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

        println(Colors.YELLOW + "You changed the position of $card" + Colors.RESET)
    }
}
