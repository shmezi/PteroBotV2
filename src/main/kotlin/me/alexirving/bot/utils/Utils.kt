package me.alexirving.bot.utils

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.Utilization;


fun getStatus(utilizatioמ: Utilization): String {
    return when (utilizatioמ.state) {
        UtilizationState.STOPPING -> "\uD83D\uDFE0"
        UtilizationState.STARTING -> "\uD83D\uDFE1"
        UtilizationState.RUNNING -> "\uD83D\uDFE2"
        UtilizationState.OFFLINE -> "\uD83D\uDD34"
        else -> return "⚪"
    }
}