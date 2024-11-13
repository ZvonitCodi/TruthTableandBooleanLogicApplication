package com.example.mathlogic

object NormalForms {
    fun getSDNF(truthTable: List<Boolean>, variables: List<Char>): String {
        val terms = mutableListOf<String>()
        val n = variables.size
        var hasOne = false

        for (i in truthTable.indices) {
            if (truthTable[i]) {
                hasOne = true
                val term = mutableListOf<String>()
                val binary = i.toString(2).padStart(n, '0')

                for (j in binary.indices) {
                    if (binary[j] == '1')
                        term.add(variables[j].toString())
                    else
                        term.add("!" + variables[j])
                }
                terms.add("(" + term.joinToString(" & ") + ")")
            }
        }

        return if (hasOne) {
            terms.joinToString(" | ")
        } else {
            "SDNF does not exist"
        }
    }

    fun getSKNF(truthTable: List<Boolean>, variables: List<Char>): String {
        val terms = mutableListOf<String>()
        val n = variables.size
        var hasZero = false

        for (i in truthTable.indices) {
            if (!truthTable[i]) {
                hasZero = true
                val term = mutableListOf<String>()
                val binary = i.toString(2).padStart(n, '0')

                for (j in binary.indices) {
                    if (binary[j] == '1')
                        term.add("!" + variables[j])
                    else
                        term.add(variables[j].toString())
                }
                terms.add("(" + term.joinToString(" | ") + ")")
            }
        }

        return if (hasZero) {
            terms.joinToString(" & ")
        } else {
            "SKNF does not exist"
        }
    }

}
