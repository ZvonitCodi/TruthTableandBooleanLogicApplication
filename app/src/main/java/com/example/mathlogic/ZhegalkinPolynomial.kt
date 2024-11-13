package com.example.mathlogic

object ZhegalkinPolynomial {
    fun getZhegalkinPolynomial(truthTable: List<Boolean>, variables: List<Char>): List<String> {
        val n = (Math.log(truthTable.size.toDouble()) / Math.log(2.0)).toInt()
        val coefficients = getTriangleCoefficients(truthTable)

        val polynomialTerms = mutableListOf<String>()
        for (i in coefficients.indices) {
            if (coefficients[i]) {
                polynomialTerms.add(getVarSet(i, variables))
            }
        }

        return if (polynomialTerms.isNotEmpty()) polynomialTerms else listOf("0")
    }

    private fun getTriangleCoefficients(truthTable: List<Boolean>): List<Boolean> {
        val triangle = truthTable.toMutableList()
        val coefficients = mutableListOf(triangle[0])

        for (i in 1 until truthTable.size) {
            for (j in 0 until truthTable.size - i) {
                triangle[j] = triangle[j] xor triangle[j + 1]
            }
            coefficients.add(triangle[0])
        }

        return coefficients
    }

    private fun getVarSet(index: Int, variables: List<Char>): String {
        val binaryIndex = index.toString(2).padStart(variables.size, '0')
        val variablesSet = mutableListOf<String>()

        for (i in binaryIndex.indices) {
            if (binaryIndex[i] == '1') {
                variablesSet.add(variables[i].toString())
            }
        }

        return if (variablesSet.isNotEmpty()) variablesSet.joinToString("*") else "1"
    }
}