package de.hawkiel.eki.cant_stop.ai
import kotlinx.serialization.*

@Serializable
data class BoardAPI (
    val blue: List<List<Int>>,
    val green: List<List<Int>>,
    val purple: List<List<Int>>,
    val yellow: List<List<Int>>,
    val mountaineer: List<List<Int>>,
    )

@Serializable
data class GameStateAPI(
    val board: Map<String, List<List<Int>>>,
    val yourTurn: Boolean,
    val yourColor: String,
    val gameOver: Boolean,
    val winner: String? = "",
)