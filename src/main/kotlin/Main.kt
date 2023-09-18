import model.Player
import tools.CardsReader

fun main(args: Array<String>) {
    val cards = CardsReader.get5RandomCards()
    val player1 = Player("Player 1")
    val player2 = Player("Player 2")
    println(player1.getCards())
    println(player2.getCards())
}