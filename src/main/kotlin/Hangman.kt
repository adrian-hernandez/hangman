package com.game

import java.util.*
import java.io.File
import java.io.InputStream

fun main(){

    var playAgain: Boolean
    var health: Int
    var choiceIsValid: Boolean
    var playAgainResp: Char
    var isCorrectResponse: Boolean

    val inputStream: InputStream = File("./src/main/resources/word-database.txt").inputStream()
    val wordsDatabase = mutableListOf<String>()

    inputStream.bufferedReader().useLines { lines -> lines.forEach { wordsDatabase.add(it)} }

    playAgain = true

    while(playAgain){

        health = 6 // Head, body, two legs, two arms (will add animation later)

        choiceIsValid = false

        println("Welcome. Let's play Hangman!")

        val random = Random()
        val index = random.nextInt(wordsDatabase.size)
        val word = wordsDatabase[index]

        val blanks = word.toCharArray()
        var count = 0
        while (count < word.length) {
            blanks[count] = '_'
            count++
        }
        var spacedblanks = ""
        for (letter in blanks) {
            spacedblanks += "$letter "
        }

        drawLivePlayer(health)
        println("$spacedblanks")

        while(health > 0){

            var response = '!'
            isCorrectResponse = false
            println("")
            println("Your current health: $health")
            println("")
            while(!isCorrectResponse){
                println("Please choose a letter.")
                 response = readLine()!![0]

                if(response.isLetter()) {
                    isCorrectResponse = true
                }
                else{
                    println("Oops! Looks like you slipped your finger there!")
                }
            }

            var matched = false
            for ((index, value) in word.withIndex()) {
                if (value.equals(response, true)) {
                    blanks[index] = response
                    matched = true
                }

            }
            if(!matched){
                health--
                println("Yikes! $response is not in the mystery word :(")
                drawLivePlayer(health)
            }else{
                println("Great! $response is in the mystery word!")
                if(health > 0){
                    drawLivePlayer(health)
                }
            }
            println("")

            // Reset spacedBlanks
            spacedblanks = ""
            for (letter in blanks) {
                spacedblanks += "$letter "
            }

            println(spacedblanks)

            if(!blanks.contains('_')){
                println("YOU WON!")
                break
            }
        }
        if(health < 1){
            drawDead()
            println("You lost :(")
            println("The mystery word was: $word")
        }

        while(!choiceIsValid){
            println("Wanna play again? (y, n)")
            playAgainResp = readLine()!![0]

            when (playAgainResp) {
                'y' -> playAgain = true
                'n' -> playAgain = false
            }
            when (playAgainResp) {
                'y' -> choiceIsValid = true
                'n' -> choiceIsValid = true
                else -> choiceIsValid = false
            }

            if(!playAgain){
                println("Thanks for playing! Good-bye")
            }
        }
    }
}

fun drawSixHealtStatus(){
    println("")
    println(" -----")
    println("|     |")
    println("|")
    println("|")
    println("|")
    println("|")
    println("|")
    println("")
}

fun drawFiveHealthStatus(){
    println("")
    println(" -----")
    println("|     |")
    println("|     O")
    println("|")
    println("|")
    println("|")
    println("|")
    println("")
}

fun drawFourHealthStatus(){
    println("")
    println(" -----")
    println("|     |")
    println("|     O")
    println("|     |")
    println("|")
    println("|")
    println("|")
    println("")
}

fun drawThreeHealthStatus(){
    println("")
    println(" -----")
    println("|     |")
    println("|     O")
    println("|    /|")
    println("|")
    println("|")
    println("|")
    println("")
}

fun drawTwoHealthStatus(){
    println("")
    println(" -----")
    println("|     |")
    println("|     O")
    println("""|    /|\""")
    println("|")
    println("|")
    println("|")
    println("")
}

fun drawOneHealthStatus(){
    println("")
    println(" -----")
    println("|     |")
    println("|     O")
    println("""|    /|\""")
    println("""|    /""")
    println("|")
    println("|")
    println("")
}

fun drawDead(){
    println("")
    println(" -----")
    println("|     |")
    println("|     O")
    println("""|    /|\""")
    println("""|    / \""")
    println("|")
    println("|")
    println("")
}

fun drawLivePlayer(health: Int){
    when(health){
        6 -> drawSixHealtStatus()
        5 -> drawFiveHealthStatus()
        4 -> drawFourHealthStatus()
        3 -> drawThreeHealthStatus()
        2 -> drawTwoHealthStatus()
        1 -> drawOneHealthStatus()
    }
}
