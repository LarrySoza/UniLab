package com.gaspersoft.unilab

import com.gaspersoft.unilab.domain.calculator.calculateOhmsLaw
import com.gaspersoft.unilab.domain.calculator.calculateParallelResistance
import com.gaspersoft.unilab.domain.calculator.calculateSeriesResistance
import com.gaspersoft.unilab.domain.calculator.convertVoltage
import com.gaspersoft.unilab.domain.model.OhmsLawTarget
import com.gaspersoft.unilab.domain.model.OperationResult
import com.gaspersoft.unilab.domain.model.ResistanceInput
import com.gaspersoft.unilab.domain.model.ResistanceUnit
import com.gaspersoft.unilab.domain.model.UnitOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun calculateCurrentFromVoltageAndResistance() {
        val result = calculateOhmsLaw(
            target = OhmsLawTarget.CURRENT,
            voltageInput = "12",
            currentInput = "",
            resistanceInput = "1000",
        )

        assertTrue(result is OperationResult.Success)
        val calculation = (result as OperationResult.Success).data
        assertEquals(0.012, calculation.value, 0.000001)
    }

    @Test
    fun calculateSeriesResistanceTotal() {
        val result = calculateSeriesResistance(
            listOf(
                ResistanceInput("100", ResistanceUnit.OHM),
                ResistanceInput("200", ResistanceUnit.OHM),
                ResistanceInput("300", ResistanceUnit.OHM),
            ),
        )

        assertTrue(result is OperationResult.Success)
        val calculation = (result as OperationResult.Success).data
        assertEquals(600.0, calculation.totalOhms, 0.000001)
    }

    @Test
    fun calculateParallelResistanceForTwoEqualResistors() {
        val result = calculateParallelResistance(
            listOf(
                ResistanceInput("100", ResistanceUnit.OHM),
                ResistanceInput("100", ResistanceUnit.OHM),
            ),
        )

        assertTrue(result is OperationResult.Success)
        val calculation = (result as OperationResult.Success).data
        assertEquals(50.0, calculation.totalOhms, 0.000001)
    }

    @Test
    fun rejectDivisionByZeroInOhmsLaw() {
        val result = calculateOhmsLaw(
            target = OhmsLawTarget.CURRENT,
            voltageInput = "12",
            currentInput = "",
            resistanceInput = "0",
        )

        assertTrue(result is OperationResult.Failure)
    }

    @Test
    fun rejectEmptyInput() {
        val result = calculateOhmsLaw(
            target = OhmsLawTarget.CURRENT,
            voltageInput = "",
            currentInput = "",
            resistanceInput = "1000",
        )

        assertTrue(result is OperationResult.Failure)
    }

    @Test
    fun rejectInvalidNumber() {
        val result = convertVoltage(
            valueInput = "abc",
            fromUnit = UnitOption("V", 1.0),
            toUnit = UnitOption("mV", 0.001),
        )

        assertTrue(result is OperationResult.Failure)
    }

    @Test
    fun rejectZeroResistanceInParallel() {
        val result = calculateParallelResistance(
            listOf(
                ResistanceInput("100", ResistanceUnit.OHM),
                ResistanceInput("0", ResistanceUnit.OHM),
            ),
        )

        assertTrue(result is OperationResult.Failure)
    }
}
