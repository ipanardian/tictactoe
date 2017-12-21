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
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.indefiniteSnackbar
import org.jetbrains.anko.sdk25.coroutines.onClick

class TicTacToeActivityUI: AnkoComponent<TicTacToeActivity> {

    private lateinit var ankoContext: AnkoContext<TicTacToeActivity>
    private lateinit var tbLayout: TableLayout
    lateinit var tvNextPlayer: TextView

    override fun createView(ui: AnkoContext<TicTacToeActivity>) = ui.apply {
        tbLayout = tableLayout {
            gravity = Gravity.CENTER
            id = R.id.tbLayout

            tvNextPlayer = textView(R.string.next_player_x) {
                gravity = Gravity.CENTER
                id = R.id.tvNextPlayer
                textSize = 25f
            }.lparams(width = wrapContent, height = wrapContent) {
                bottomMargin = dip(20)
            }
            tableRow {
                gravity = Gravity.CENTER

                button {
                    id = R.id.bu1
                }.lparams(width = dip(100), height = dip(100))
                button {
                    id = R.id.bu2
                }.lparams(width = dip(100), height = dip(100))
                button {
                    id = R.id.bu3
                }.lparams(width = dip(100), height = dip(100))
            }.lparams(width = matchParent, height = matchParent)
            tableRow {
                gravity = Gravity.CENTER

                button {
                    id = R.id.bu4
                }.lparams(width = dip(100), height = dip(100))
                button {
                    id = R.id.bu5
                }.lparams(width = dip(100), height = dip(100))
                button {
                    id = R.id.bu6
                }.lparams(width = dip(100), height = dip(100))
            }.lparams(width = matchParent, height = matchParent)
            tableRow {
                gravity = Gravity.CENTER

                button {
                    id = R.id.bu7
                }.lparams(width = dip(100), height = dip(100))
                button {
                    id = R.id.bu8
                }.lparams(width = dip(100), height = dip(100))
                button {
                    id = R.id.bu9
                }.lparams(width = dip(100), height = dip(100))
            }.lparams(width = matchParent, height = matchParent)
        }.applyRecursively { view -> when(view) {
            is Button -> {
                with(view) {
                    textSize = 28f
                    textColor = ContextCompat.getColor(context, R.color.colorWhite)
                    onClick {
                        buttonClick(id, this@with, ui)
                    }
                }
            }
        }}
        ankoContext = ui
    }.view

    private fun buttonClick(id: Int, btnSelected: Button, ui: AnkoContext<TicTacToeActivity>) {
        var cellID = 0
        when (id) {
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
        ui.owner.playGame(cellID, btnSelected)
    }

    fun updateGuideText() {
        when {
            ankoContext.owner.winner.isNotEmpty() -> {
                tvNextPlayer.text = ankoContext.ctx.resources.getString(R.string.winner, ankoContext.owner.winner)
                tvNextPlayer.setTextColor(Color.MAGENTA)
                showSnackBarNewGame()
            }
            ankoContext.owner.squares.size == ankoContext.owner.totalCell -> {
                tvNextPlayer.setText(R.string.game_draw)
                showSnackBarNewGame()
            }
            else -> tvNextPlayer.text = ankoContext.ctx.resources.getString(R.string.next_player,
                    if (ankoContext.owner.xIsNext) TicTacToeActivity.X else TicTacToeActivity.O)
        }
    }

    private fun showSnackBarNewGame() {
        indefiniteSnackbar(tbLayout, R.string.game_over, R.string.new_game) {
            ankoContext.owner.newGame()
        }
    }

    fun resetButton() {
        for (i in 1..ankoContext.owner.totalCell) {
            val button = ankoContext.owner.find<Button>(ankoContext.ctx.resources.getIdentifier("bu$i", "id", ankoContext.ctx.packageName))
            button.text = ""
            button.isEnabled = true
            button.setBackgroundResource(R.drawable.abc_btn_default_mtrl_shape)
        }
    }
}