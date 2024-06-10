package com.game

import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
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
    private val root = VBox(10.0)

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage  // Initialize the class-level variable
        val inputStream: InputStream = File("./src/main/resources/word-database.txt").inputStream()
        inputStream.bufferedReader().useLines { lines -> lines.forEach { wordsDatabase.add(it) } }

        root.alignment = Pos.CENTER
        root.children.addAll(healthLabel, wordLabel, messageLabel, inputField)

        inputField.setOnAction { handleInput() }

        startNewGame()

        val scene = Scene(root, 400.0, 300.0)
        primaryStage.title = "Hangman"
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun startNewGame() {
        health = 6
        healthLabel.text = "Health: $health"
        messageLabel.text = "Welcome!\nLet's play Hangman!"
        messageLabel.font = Font.font(24.0)
        word = wordsDatabase[Random.nextInt(wordsDatabase.size)]
        blanks = CharArray(word.length) { '_' }
        updateWordLabel()
        drawLivePlayer()
    }

    private fun handleInput() {
        val responseText = inputField.text
        if (responseText.isNotEmpty()) {
            val response = responseText[0].toLowerCase()
            inputField.clear()
            if (messageLabel.text.contains("Wanna play again?")) {
                handlePlayAgainInput(response)
                return
            }
            if (!response.isLetter()) {
                messageLabel.text = "Oops! Looks like you slipped your finger there!"
                messageLabel.font = Font.font(24.0)
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
                messageLabel.font = Font.font(24.0)
                drawLivePlayer()
            } else {
                messageLabel.text = "Great! $response is in the mystery word!"
                messageLabel.font = Font.font(24.0)
                drawLivePlayer()
            }
            updateWordLabel()
            checkGameStatus()
        }
    }

    private fun handlePlayAgainInput(response: Char) {
        when (response) {
            'y' -> {
                startNewGame()
            }
            'n' -> {
                primaryStage.close()
            }
        }
    }

    private fun checkGameStatus() {
        if (!blanks.contains('_')) {
            messageLabel.text = "YOU WON!\nWanna play again? (y/n)"
            showPlayAgainDialog()
        } else if (health <= 0) {
            drawDead()
            messageLabel.text = "You lost :( The mystery word was: $word.\nWanna play again? (y/n)"
            showPlayAgainDialog()
        }
    }

    private fun showPlayAgainDialog() {
        inputField.requestFocus()
    }

    private fun updateWordLabel() {
        wordLabel.text = blanks.joinToString(" ")
        wordLabel.font = Font.font(24.0)
    }

    private fun drawLivePlayer() {
        healthLabel.text = "Health: $health"
        healthLabel.font = Font.font(24.0)
    }

    private fun drawDead() {
        healthLabel.text = "Health: 0"
        healthLabel.font = Font.font(24.0)
    }
}

fun main() {
    Application.launch(HangmanGame::class.java)
}
