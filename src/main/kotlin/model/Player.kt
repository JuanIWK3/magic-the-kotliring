package model

import tools.CardsReader

class Player(var name: String) {
    private var handCards: MutableList<Card> = mutableListOf()
    var boardCards: MutableList<Card> = mutableListOf()
    var points: Int = 10000

    init {
        handCards.addAll(CardsReader.get5RandomCards())
    }

    private fun printCards(cards: List<Card>, title: String) {
        println("$title:")
        cards.forEachIndexed { index, card ->
            println("${index + 1} - $card")
        }
    }

    private fun isValidCardIndex(cardIndex: Int): Boolean {
        return cardIndex >= 0 && cardIndex < handCards.size
    }

    fun receiveCard() {
        if (handCards.size >= 10) {
            println("You have 10 cards in your hand, you can't receive more cards")
            return
        }

        handCards.add(CardsReader.getRandomCard())
    }

    fun selectCard(): Card {
        printCards(handCards, "Hand cards")
        print("Select a card: ")

        var cardIndex = -1

        while (!isValidCardIndex(cardIndex)) {
            cardIndex = (readln().toIntOrNull() ?: -1) - 1

            if (!isValidCardIndex(cardIndex)) {
                println("Invalid card index")
            }
        }

        return handCards.removeAt(cardIndex)
    }

    fun getTotalAttack(): Int {
        return boardCards.filter { it.position == 1 }.sumBy { it.attack }
    }

    fun getTotalDefense(): Int {
        return boardCards.filter { it.position == 2 }.sumBy { it.defense }
    }

    fun putCardOnBoard(card: Card) {
        handCards.remove(card)
        boardCards.add(card)
    }

    fun printHandCards() {
        printCards(handCards, "Hand cards")
    }

    fun printBoardCards() {
        printCards(boardCards, "Board cards")
    }
}
