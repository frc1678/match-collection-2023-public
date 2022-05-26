// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.subjective_ranking_counter_panel.*

/* The subjective ranking counter panel fragment for subjective scouting a single team.
 * Every team being scouted gets a single panel.
 * @see R.layout.subjective_ranking_counter_panel */
class SubjectiveRankingCounterPanel : Fragment() {
    // Inflates the view for this counter panel.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.subjective_ranking_counter_panel, container)

    // Shows the team number at the top of the panel.
    fun setTeamNumber(teamNumber: String) {
        tv_team_number.text = teamNumber
    }

    // Sets the text color of the team number to the alliance color.
    fun setAllianceColor() {
        tv_team_number.setTextColor(
            resources.getColor(
                if (alliance_color == Constants.AllianceColor.RED) R.color.alliance_red_light
                else R.color.alliance_blue_light,
                null
            )
        )
    }

    /* Sets the OnCheckedChangeListener for the defense toggle button. This is only used to show or
     * hide the checkmark icon.
     * @see R.drawable.tb_defense_check */
    fun setListener() {
        defense_toggle.setOnCheckedChangeListener { _, checked ->
            // Sets or removes the drawable at the bottom of the toggle button, depending on if the
            // button is checked or not.
            defense_toggle.setCompoundDrawablesRelativeWithIntrinsicBounds(
                // start, top, end,
                null, null, null,
                // bottom
                if (checked) resources.getDrawable(R.drawable.tb_defense_check, null) else null
            )
        }
    }

    /** Gets a map containing the ranking data for the team represented by this panel. The format of
     * this map is a string, giving the name of the data point, to its value, as an integer. */
    val rankingData: Map<String, Int>
        get() = mapOf(
            "quickness" to counter_quickness.value,
            "field_awareness" to counter_field_awareness.value
        )

    // Whether this team played defense during the match.
    val playedDefense: Boolean
        get() = defense_toggle.isChecked

}
