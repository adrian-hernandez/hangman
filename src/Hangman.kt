import java.util.*

fun main(){

    //Hangman version #1
    var isOver: Boolean
    var playAgain: Boolean
    var triesLeft: Int
    var word: String
    var wordLength: Int
    var choiceIsValid: Boolean

    //will move this to external DB
    val wordsDatabase = listOf("Well", "Good", "Great", "Fabulous", "Fascinating", "Wonderful", "Fantastic", "Awesome", "Excellent", "Perfect")

    //added print statements to test the base of the project
    isOver = false
    playAgain = true
    while(!isOver){

        while(playAgain){

            triesLeft = 6 //Head, body, two legs, two arms (will add animation later)
            choiceIsValid = true

            while(choiceIsValid){

                println("Welcome. Let's play Hangman!")
                val reader = Scanner(System.`in`)

                var random = Random()
                var index = random.nextInt(wordsDatabase.size)
                word = wordsDatabase[index]

                println("word is $word")

                var xWord = ""
                var count = 0
                while(count < word.length){
                    xWord += "?"
                    count++
                }
                println("xWord is $xWord")

                var revealedPart = xWord.toCharArray() //all question marks
                println("revealedPart is ${Arrays.toString(revealedPart)}")
                choiceIsValid = false
            }
            playAgain = false
        }
        isOver = true
    }
}
