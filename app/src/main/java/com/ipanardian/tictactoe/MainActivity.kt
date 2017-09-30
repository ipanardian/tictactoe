package com.ipanardian.tictactoe

import android.content.res.Resources
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var squares = mutableMapOf<Int, String>()
    private var xIsNext = true
    private var winner: String? = null
    private val totalCell = 9
    private val X = "X"
    private val O = "O"

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
                R.menu.main_menu,
                menu
        )

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) when (item.itemId) {
            R.id.menuAbout -> aboutDialog()
        }
        return super.onOptionsItemSelected(item)
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
        if (winner != null) {
            Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show()
            return
        }

        if (xIsNext) {
            squares[cellID] = X
            buSellected.setBackgroundColor(Color.GREEN)
        }
        else {
            squares[cellID] = O
            buSellected.setBackgroundColor(Color.BLUE)
        }
        buSellected.text = squares[cellID]
        buSellected.isEnabled = false

        xIsNext = !xIsNext

        winner = checkWinner()
        updateGuideText()
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

    private fun updateGuideText() {
        val tvNextPlayer = findViewById(R.id.tvNextPlayer) as TextView
        if (winner != null) {
            tvNextPlayer.text = "${resources.getString(R.string.winner)}: $winner"
            tvNextPlayer.setTextColor(Color.MAGENTA)
            showSnackBarNewGame()
        }
        else if (squares.size == totalCell) {
            tvNextPlayer.setText(R.string.game_draw)
            showSnackBarNewGame()
        }
        else tvNextPlayer.text = "${resources.getString(R.string.next_player)}: ${if (xIsNext) X else O}"
    }

    private fun showSnackBarNewGame() {
        val snackBarNewGame: Snackbar  = Snackbar.make(
                findViewById(R.id.tbLayout),
                R.string.game_over,
                Snackbar.LENGTH_INDEFINITE)
        snackBarNewGame.setAction(R.string.new_game, {
            newGame()
        }).show()
    }

    private fun newGame() {
        squares = mutableMapOf()
        xIsNext = true
        winner = null

        val tvNextPlayer = findViewById(R.id.tvNextPlayer) as TextView
        tvNextPlayer.text = "${resources.getString(R.string.next_player)}: $X"
        tvNextPlayer.setTextColor(Color.GRAY)

        resetButton()
    }

    private fun resetButton() {
        for (i in 1..totalCell) {
            val button: Button? = findViewById(resources.getIdentifier("bu$i", "id", packageName)) as Button
            button?.text = ""
            button?.isEnabled = true
            button?.setBackgroundResource(R.drawable.abc_btn_default_mtrl_shape)
        }
    }

    private fun aboutDialog() {
        var aboutContent = SpannableString("Tic Tac Toe v0.1.0\n" +
                "beta version\n" +
                "This game is open source project\n\n" +
                "Web: http://ipanardian.com\n" +
                "Github: https://github.com/ipanardian/tictactoe")
        Linkify.addLinks(aboutContent, Linkify.WEB_URLS)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.about)
        builder.setMessage(aboutContent)
        builder.setPositiveButton(android.R.string.yes, { dialog, _ ->
            dialog.dismiss()
        })
        val alert: AlertDialog = builder.create()
        alert.show()

        var textView = alert.findViewById(android.R.id.message) as TextView
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}
