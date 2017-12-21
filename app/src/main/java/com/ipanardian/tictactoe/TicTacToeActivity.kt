/**
 * Tic Tac Toe
 * (c) 2017 Ipan Ardian
 * Android Tic Tac Toe games written in Kotlin with Anko DSL layout and support i18n
 *
 * https://github.com/ipanardian/tictactoe
 * GPL-3.0 License
 */

package com.ipanardian.tictactoe

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.Appcompat
import kotlin.properties.Delegates

class TicTacToeActivity : AppCompatActivity() {

    var squares = mutableMapOf<Int, String>()
    var xIsNext = true
    var winner: String by Delegates.notNull()
    val totalCell = 9

    companion object {
        const val X = "X"
        const val O = "O"
    }

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

    private lateinit var ui: TicTacToeActivityUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = TicTacToeActivityUI()
        ui.setContentView(this)
        winner = ""
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

    fun playGame(cellID: Int, btnSelected: Button) {
        if (!winner.isNullOrEmpty()) {
            toast(R.string.game_over)
            return
        }

        if (xIsNext) {
            squares[cellID] = X
            btnSelected.setBackgroundColor(Color.GREEN)
        }
        else {
            squares[cellID] = O
            btnSelected.setBackgroundColor(Color.BLUE)
        }
        btnSelected.text = squares[cellID]
        btnSelected.isEnabled = false

        xIsNext = !xIsNext

        checkWinner { winner ->
            winner?.let {
                this.winner = it
            }
        }

        ui.updateGuideText()
    }

    private inline fun checkWinner(result: (winner: String?) -> Unit) {
        if (squares.isNotEmpty()) for (line in lines) {
            val (a, b, c) = line
            if (squares[a] != null && squares[a] == squares[b] && squares[a] === squares[c]) {
                result(squares[a])
            }
        }
    }

    fun newGame() {
        squares = mutableMapOf()
        xIsNext = true
        winner = ""

        with(ui) {
            tvNextPlayer.text = resources.getString(R.string.next_player, X)
            tvNextPlayer.setTextColor(Color.GRAY)
            resetButton()
        }
    }

    private fun aboutDialog() {
        var aboutContent = SpannableString(resources.getString(R.string.about_content))
        Linkify.addLinks(aboutContent, Linkify.WEB_URLS)

        val alert = alert(Appcompat) {
            title = resources.getString(R.string.about)
            message = aboutContent
            yesButton { dialog ->  dialog.dismiss() }
        }.build()
        alert.show()

        var textView = alert.find<TextView>(android.R.id.message)
        textView.movementMethod = LinkMovementMethod.getInstance()

    }
}
