package com.game

import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
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
    private val hangmanImageView = ImageView()
    private lateinit var primaryStage: Stage  // Class-level variable to hold the primary stage
    private val root = VBox(10.0)
    private var replayed: Boolean = false  // Global boolean variable

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage  // Initialize the class-level variable
        val inputStream: InputStream = File("./src/main/resources/word-database.txt").inputStream()
        inputStream.bufferedReader().useLines { lines -> lines.forEach { wordsDatabase.add(it) } }

        root.alignment = Pos.CENTER
        root.children.addAll(healthLabel, hangmanImageView, wordLabel, messageLabel, inputField)

        // Set VBox alignment to center
        root.alignment = Pos.CENTER

        // Set inputField width to about a third of the window's width
        inputField.prefWidth = 200.0

        // Center input field within VBox
        VBox.setMargin(inputField, javafx.geometry.Insets(0.0, 200.0, 0.0, 200.0))

        // Center text in the input field
        inputField.alignment = Pos.CENTER

        // Limit text length to 1 character
        inputField.textProperty().addListener { _, oldValue, newValue ->
            if (newValue.length > 1) {
                inputField.text = newValue.take(1)
            }
        }

        inputField.setOnAction { handleInput() }

        startNewGame()

        val scene = Scene(root, 600.0, 500.0)
        primaryStage.title = "Hangman"
        primaryStage.scene = scene
        primaryStage.show()

        // Bring the window to the front and request focus
        primaryStage.toFront()
        primaryStage.requestFocus()
    }

    private fun startNewGame() {
        health = 6
        updateHealthLabel()
        if (replayed) {
            setFont(messageLabel, "Let's play Hangman!")
        } else {
            setFont(messageLabel, "Welcome!\nLet's play Hangman!")
        }
        word = wordsDatabase[Random.nextInt(wordsDatabase.size)]
        blanks = CharArray(word.length) { '_' }
        updateWordLabel()
        updateHangmanImage()
        replayed = true  // Set replayed to true after the first game
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
                setFont(messageLabel, "Oops! Looks like you slipped your finger there!")
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
                updateHealthLabel()
                setFont(messageLabel, "Yikes! $response is not in the mystery word :(")
            } else {
                setFont(messageLabel, "Great! $response is in the mystery word!")
            }
            updateWordLabel()
            updateHangmanImage()
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
            setFont(messageLabel, "YOU WON!\nWanna play again? (y/n)")
            showPlayAgainDialog()
        } else if (health <= 0) {
            setFont(messageLabel, "You lost :( The mystery word was: $word.\nWanna play again? (y/n)")
            updateHangmanImage()  // Update the image to show the final state
            showPlayAgainDialog()
        }
    }

    private fun showPlayAgainDialog() {
        inputField.requestFocus()
    }

    private fun updateWordLabel() {
        setFont(wordLabel, blanks.joinToString(" "))
    }

    private fun updateHangmanImage() {
        val imagePath = "file:images/hangman-$health.png"
        val image = Image(imagePath)
        hangmanImageView.image = image
    }

    private fun updateHealthLabel() {
        setFont(healthLabel, "Health: $health")
    }

    private fun setFont(label: Label, text: String) {
        label.text = text
        label.font = Font.font(20.0)
        label.alignment = Pos.CENTER
    }
}

fun main() {
    Application.launch(HangmanGame::class.java)
}
