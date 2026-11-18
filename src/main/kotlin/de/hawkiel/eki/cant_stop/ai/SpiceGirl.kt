package de.hawkiel.eki.cant_stop.ai

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.json.*

abstract class SpiceGirl(val token: String) {
    var gameState = GameStateAPI(mapOf("blue" to listOf(), "green" to listOf(), "purple" to listOf(), "yellow" to listOf(), "mountaineer" to listOf()), false, "", false, "")
    val client = HttpClient.newBuilder().build()
    val json = Json { ignoreUnknownKeys = true }

    init {
        mainLoop()
    }

    abstract fun gameLogic()

    fun getGameState() {
        val request = HttpRequest.newBuilder().uri(URI.create("https://sisyphos.ist-toll.xyz/getState"))
            .POST(HttpRequest.BodyPublishers.ofString("{\"token\": \"${token}\"}"))
            .header("Content-Type", "application/json")
            .build()
        val responseText = client.send(request, HttpResponse.BodyHandlers.ofString()).body()
        println(responseText)
        gameState = json.decodeFromString<GameStateAPI>(responseText)
    }

    fun joinGame() {
        val request = HttpRequest.newBuilder().uri(URI.create("https://sisyphos.ist-toll.xyz/join"))
            .POST(HttpRequest.BodyPublishers.ofString("{\"token\": \"${token}\"}"))
            .header("Content-Type", "application/json")
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString()).body()
    }

    fun rollTheDices(): List<Int> {
        val request = HttpRequest.newBuilder().uri(URI.create("https://sisyphos.ist-toll.xyz/rollTheDices"))
            .POST(HttpRequest.BodyPublishers.ofString("{\"token\": \"${token}\"}"))
            .header("Content-Type", "application/json")
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString()).body()
        return json.decodeFromString<List<Int>>(response)
    }

    fun pairDices(dices: List<List<Int>>) {
        val request = HttpRequest.newBuilder().uri(URI.create("https://sisyphos.ist-toll.xyz/pairDices"))
            .POST(HttpRequest.BodyPublishers.ofString("{\"pairings\": ${json.encodeToString(dices)}, \"token\": \"${token}\"}"))
            .header("Content-Type", "application/json")
            .build()
        println("{\"pairings\": ${json.encodeToString(dices)}, \"token\": \"${token}\"}")
        client.send(request, HttpResponse.BodyHandlers.ofString()).body()
    }

    fun endTurn() {
        val request = HttpRequest.newBuilder().uri(URI.create("https://sisyphos.ist-toll.xyz/endTurn"))
            .POST(HttpRequest.BodyPublishers.ofString("{\"token\": \"${token}\"}"))
            .header("Content-Type", "application/json")
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString()).body()
    }

    fun quitGame() {
        val request = HttpRequest.newBuilder().uri(URI.create("https://sisyphos.ist-toll.xyz/quitGame"))
            .POST(HttpRequest.BodyPublishers.ofString("{\"token\": \"${token}\"}"))
            .header("Content-Type", "application/json")
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString()).body()
    }

    fun mainLoop() {
        joinGame()
        getGameState()

        while (!gameState.gameOver) {
            if (!gameState.yourTurn) {
                getGameState()
                continue;
            }

            gameLogic()

            endTurn()
            getGameState()
        }

        println("player ${gameState.winner} won!");
        quitGame()
    }
}