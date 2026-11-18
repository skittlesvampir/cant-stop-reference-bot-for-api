package de.hawkiel.eki.cant_stop.ai

fun getPermutations(dices: List<Int>): Set<List<Int>> {
    var permutations = mutableSetOf<List<Int>>()
    for (i in 0..3) {
        val first = dices[i]
        for (j in (0..3).filter { it != i}) {
            val second = dices[j]
            for (k in (0..3).filter { it != i && it != j}) {
                val third = dices[k]
                for (l in (0..3).filter { it != i && it != j && it != k}) {
                    val fourth = dices[l]
                    permutations.add(listOf(first, second, third, fourth))
                }
            }
        }
    }

    return permutations
}

// only rolls the dices if there's still a spare mountaineer in case of an unlucky dice roll
class Scary (token: String): SpiceGirl(token) {
    fun getColumnScore(column: Int): Float {
        val color = gameState.yourColor
        for (field in gameState.board[color] ?: listOf()) {
            if (field[0] == column) {
                return (field[1].toFloat() + 1) / field.size
            }
        }

        return 0f
    }

    override fun gameLogic() {
        val columns = mutableSetOf<Int>()
        while (columns.size < 3) {
            val dices = rollTheDices()

            var mostReusedMountaineers = -100
            var bestScore = -100f
            var bestChoice = dices
            for (perm in getPermutations(dices)) {
                val first = perm[0] + perm[1]
                val second = perm[2] + perm[3]

                var reusedMountaineers = 0
                var score = 0f
                if (first in columns) {
                    reusedMountaineers++
                    score += getColumnScore(first)
                }

                if (second in columns) {
                    reusedMountaineers++
                    score += getColumnScore(second)
                }

                if (reusedMountaineers > mostReusedMountaineers) {
                    mostReusedMountaineers = reusedMountaineers
                    bestScore = score
                    bestChoice = perm
                }

                if (reusedMountaineers == mostReusedMountaineers) {
                    if (score > bestScore) {
                        bestScore = score
                        bestChoice = perm
                    }
                }
            }

            columns.add(bestChoice[0] + bestChoice[1])
            columns.add(bestChoice[2] + bestChoice[3])

            pairDices(listOf(listOf(bestChoice[0], bestChoice[1]), listOf(bestChoice[2], bestChoice[3])))
        }
    }
}