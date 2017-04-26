import business.LeCompteEstBon
import business.Solver

/**
 * Let's playyyyyyyyyyy.
 * Run it and let the magic happen.
 */
fun main(args: Array<String>) {
    val game = LeCompteEstBon.create()
    game.printStatement()

    val solver = Solver(game.toGuess, game.numbers.toIntArray())
    solver.naiveAlgorithm()
}
