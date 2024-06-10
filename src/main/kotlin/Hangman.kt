package com.game

import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import java.io.File
import java.io.InputStream
import kotlin.random.Random

class HangmanGame : Application() {

    private var playAgain: Boolean = true
    private var health: Int = 6
    private val wordsDatabase = mutableListOf<String>()
    private lateinit var word: String
    private lateinit var blanks: CharArray
    private val wordLabel = Label()
    private val healthLabel = Label()
    private val messageLabel = Label()
    private val inputField = TextField()
    private lateinit var primaryStage: Stage  // Class-level variable to hold the primary stage


    override fun start(primaryStage: Stage) {
        val inputStream: InputStream = File("./src/main/resources/word-database.txt").inputStream()
        inputStream.bufferedReader().useLines { lines -> lines.forEach { wordsDatabase.add(it) } }

        val root = VBox(10.0)
        root.alignment = Pos.CENTER
        root.children.addAll(healthLabel, wordLabel, messageLabel, inputField)

        inputField.setOnAction { handleInput() }

        startNewGame()

        val scene = Scene(root, 400.0, 300.0)
        primaryStage.title = "Hangman Game"
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun startNewGame() {
        health = 6
        healthLabel.text = "Health: $health"
        messageLabel.text = "Welcome. Let's play Hangman!"
        word = wordsDatabase[Random.nextInt(wordsDatabase.size)]
        blanks = CharArray(word.length) { '_' }
        updateWordLabel()
        drawLivePlayer()
    }

    private fun handleInput() {
        val response = inputField.text[0].toLowerCase()
        inputField.clear()
        if (!response.isLetter()) {
            messageLabel.text = "Oops! Looks like you slipped your finger there!"
            return
        }
        var matched = false
        for ((index, value) in word.withIndex()) {
            if (value.equals(response, true)) {
                blanks[index] = response
                matched = true
            }
        }
        if (!matched) {
            health--
            messageLabel.text = "Yikes! $response is not in the mystery word :("
            drawLivePlayer()
        } else {
            messageLabel.text = "Great! $response is in the mystery word!"
            drawLivePlayer()
        }
        updateWordLabel()
        checkGameStatus()
    }

    private fun checkGameStatus() {
        if (!blanks.contains('_')) {
            messageLabel.text = "YOU WON!"
            showPlayAgainDialog()
        } else if (health <= 0) {
            drawDead()
            messageLabel.text = "You lost :( The mystery word was: $word"
            showPlayAgainDialog()
        }
    }

    private fun showPlayAgainDialog() {
        val playAgainDialog = VBox(10.0)
        playAgainDialog.alignment = Pos.CENTER
        val playAgainLabel = Label("Wanna play again? (y/n)")
        val playAgainInput = TextField()
        playAgainDialog.children.addAll(playAgainLabel, playAgainInput)

        val playAgainScene = Scene(playAgainDialog, 200.0, 100.0)
        val playAgainStage = Stage()
        playAgainStage.title = "Play Again?"
        playAgainStage.scene = playAgainScene
        playAgainStage.show()

        playAgainInput.setOnAction {
            when (playAgainInput.text[0].toLowerCase()) {
                'y' -> {
                    playAgainStage.close()
                    startNewGame()
                }
                'n' -> {
                    playAgainStage.close()
                    primaryStage.close()
                }
            }
        }
    }

    private fun updateWordLabel() {
        wordLabel.text = blanks.joinToString(" ")
        wordLabel.font = Font.font(24.0)
    }

    private fun drawLivePlayer() {
        healthLabel.text = "Health: $health"
    }

    private fun drawDead() {
        healthLabel.text = "Health: 0"
    }
}

fun main() {
    Application.launch(HangmanGame::class.java)
}
