package com.example.gateguard

import org.junit.Assert.assertEquals
import org.junit.Test

class DecisionEngineTest {
    @Test
    fun normalTraveler_shouldBeAllowed() {
        val traveler = Traveler(
            name = "沈三郎",
            age = 31,
            origin = "河南府洛阳县",
            occupation = "药材商",
            passValid = true,
            wanted = false,
            hasContraband = false,
            baggageSummary = "药材"
        )

        assertEquals(Decision.ALLOW, DecisionEngine.expectedDecision(traveler))
    }

    @Test
    fun wantedTraveler_shouldBeArrested() {
        val traveler = Traveler(
            name = "韩九",
            age = 28,
            origin = "不详",
            occupation = "脚夫",
            passValid = true,
            wanted = true,
            hasContraband = false,
            baggageSummary = "空包袱"
        )

        assertEquals(Decision.ARREST, DecisionEngine.expectedDecision(traveler))
    }
}
