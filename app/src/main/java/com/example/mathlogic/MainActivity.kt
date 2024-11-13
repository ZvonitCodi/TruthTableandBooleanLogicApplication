package com.example.mathlogic

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var inputFormula: EditText
    private lateinit var calculateButton: Button
    private lateinit var rpnOutput: TextView
    private lateinit var variablesOutput: TextView
    private lateinit var truthTableLayout: TableLayout
    private lateinit var zhegalkinOutput: TextView
    private lateinit var sdnfOutput: TextView
    private lateinit var sknfOutput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputFormula = findViewById(R.id.inputFormula)
        calculateButton = findViewById(R.id.calculateButton)
        rpnOutput = findViewById(R.id.rpnOutput)
        variablesOutput = findViewById(R.id.variablesOutput)
        truthTableLayout = findViewById(R.id.truthTableLayout)
        zhegalkinOutput = findViewById(R.id.zhegalkinOutput)
        sdnfOutput = findViewById(R.id.sdnfOutput)
        sknfOutput = findViewById(R.id.sknfOutput)

        calculateButton.setOnClickListener {
            val input = inputFormula.text.toString()

            if (input.isNotEmpty()) {
                val rpn = TruthTable.toRPN(input)
                val variables = TruthTable.getVariables(input)
                val truthTable = TruthTable.generateTruthTable(rpn, variables)
                val polynomial = ZhegalkinPolynomial.getZhegalkinPolynomial(truthTable, variables)
                val sdnf = NormalForms.getSDNF(truthTable, variables)
                val sknf = NormalForms.getSKNF(truthTable, variables)

                rpnOutput.text = "Formula in RPN: $rpn"
                variablesOutput.text = "Variables: ${variables.joinToString(", ")}"

                truthTableLayout.removeAllViews()

                val headerRow = TableRow(this)
                val cellParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                cellParams.setMargins(2, 2, 2, 2)

                for (variable in variables) {
                    val headerCell = TextView(this)
                    headerCell.text = variable.toString()
                    headerCell.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                    headerCell.setPadding(8, 8, 8, 8)
                    headerCell.layoutParams = cellParams
                    headerRow.addView(headerCell)
                }

                val resultHeaderCell = TextView(this)
                resultHeaderCell.text = "Result"
                resultHeaderCell.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                resultHeaderCell.setPadding(8, 8, 8, 8)
                resultHeaderCell.layoutParams = cellParams
                headerRow.addView(resultHeaderCell)

                truthTableLayout.addView(headerRow)

                val rows = 1 shl variables.size
                for (i in 0 until rows) {
                    val binary = i.toString(2).padStart(variables.size, '0')
                    val row = TableRow(this)

                    for (bit in binary) {
                        val cell = TextView(this)
                        cell.text = if (bit == '1') "1" else "0"
                        cell.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
                        cell.setPadding(8, 8, 8, 8)
                        cell.layoutParams = cellParams
                        row.addView(cell)
                    }

                    val resultCell = TextView(this)
                    val resultText = if (truthTable[i]) "1" else "0"
                    resultCell.text = resultText
                    resultCell.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
                    resultCell.setPadding(8, 8, 8, 8)
                    resultCell.layoutParams = cellParams
                    row.addView(resultCell)

                    truthTableLayout.addView(row)
                }

                zhegalkinOutput.text = "Zhegalkin polynomial: ${polynomial.joinToString(" + ")}"
                sdnfOutput.text = "SDNF: $sdnf"
                sknfOutput.text = "SKNF: $sknf"
            }
        }
    }
}
