import java.util.*

fun main(){

    //Hangman version #1
    //The game is currently very lazily programmed,
    // but it works as long as the user do not slip
    // and type something other than a single Char
    // at every turn.
    //Need to add checks on readLine()
    //Need to have a word database
    //Need to use as many Kotlin concepts as possible
    //Need to display more messages according to game state
    var playAgain: Boolean
    var health: Int
    var word: String
    var choiceIsValid: Boolean
    var playAgainResp: Char

    //will move this to external DB
    val wordsDatabase = listOf("Well", "Good", "Great", "Fabulous", "Fascinating", "Wonderful", "Fantastic", "Awesome", "Excellent", "Perfect")

    //added print statements to test the base of the project
    playAgain = true


    while(playAgain){

        health = 6 //Head, body, two legs, two arms (will add animation later)

        choiceIsValid = false

        println("Welcome. Let's play Hangman!")

        var random = Random()
        var index = random.nextInt(wordsDatabase.size)
        word = wordsDatabase[index]

        var blanks = word.toCharArray()
        var count = 0
        while (count < word.length) {
            blanks[count] = '_'
            count++
        }
        var spacedblanks = ""
        for (letter in blanks) {
            spacedblanks += "$letter "
        }
        println("")
        println("$spacedblanks")

        while(health > 0){
            println("")
            println("Your current health: $health")
            println("")
            println("Please choose a letter.")
            var response: Char = readLine()!![0]

            var matched: Boolean = false
            for ((index, value) in word.withIndex()) {
                if (value.equals(response, true)) {
                    blanks[index] = response
                    matched = true
                }

            }
            if(!matched){
                health--
                println("Opps $response is not in the secret word")
            }else{
                println("Great! $response is in the secret word!")
            }
            println("")

            //reset spacedBlanks
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
            println("You lost :(")
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
