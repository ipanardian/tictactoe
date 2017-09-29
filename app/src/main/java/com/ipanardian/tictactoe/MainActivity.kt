package com.ipanardian.tictactoe

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var squares = mutableMapOf<Int, String>()
    private var xIsNext = true
    private var winner: String? = null

    private val lines: Array<IntArray> = arrayOf(
            intArrayOf(0,1,2),
            intArrayOf(3,4,5),
            intArrayOf(6,7,8),
            intArrayOf(0,3,6),
            intArrayOf(1,4,7),
            intArrayOf(2,5,8),
            intArrayOf(0,4,8),
            intArrayOf(2,4,6)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonClick(view: View) {
        val buSelected = view as Button
        var cellID = 0

        when (buSelected.id) {
            R.id.bu1 -> cellID = 0
            R.id.bu2 -> cellID = 1
            R.id.bu3 -> cellID = 2
            R.id.bu4 -> cellID = 3
            R.id.bu5 -> cellID = 4
            R.id.bu6 -> cellID = 5
            R.id.bu7 -> cellID = 6
            R.id.bu8 -> cellID = 7
            R.id.bu9 -> cellID = 8
        }

        playGame(cellID, buSelected)
    }

    private fun playGame(cellID: Int, buSellected: Button) {
        val tvNextPlayer = findViewById(R.id.tvNextPlayer) as TextView
        if (winner != null) {
            Toast.makeText(this, "Game Is Over", Toast.LENGTH_LONG).show()
            return
        }

        if (xIsNext) {
            squares[cellID] = "X"
            buSellected.setBackgroundColor(Color.GREEN)
        }
        else {
            squares[cellID] = "O"
            buSellected.setBackgroundColor(Color.BLUE)
        }
        buSellected.text = squares[cellID]

        xIsNext = !xIsNext

        winner = checkWinner()
        if (winner != null) {
            tvNextPlayer.text = "Winner: $winner"
            tvNextPlayer.setTextColor(Color.MAGENTA)
        }
        else tvNextPlayer.text = "Next Player: ${if (xIsNext) "X" else "O"}"

        buSellected.isEnabled = false
    }

    private fun checkWinner(): String? {
        if (squares.isNotEmpty()) {
            for (line in lines) {
                val (a, b, c) = line
                if (squares[a] != null && squares[a] == squares[b] && squares[a] === squares[c]) {
                    return squares[a]
                }
            }
        }
        return null
    }
}
