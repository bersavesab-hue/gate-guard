package com.example.gateguard

data class Traveler(
    val name: String,
    val age: Int,
    val origin: String,
    val occupation: String,
    val passValid: Boolean,
    val wanted: Boolean,
    val hasContraband: Boolean,
    val baggageSummary: String
)

enum class Decision {
    ALLOW,
    ARREST
}

object DecisionEngine {
    fun expectedDecision(traveler: Traveler): Decision {
        return if (!traveler.passValid || traveler.wanted || traveler.hasContraband) {
            Decision.ARREST
        } else {
            Decision.ALLOW
        }
    }
}
