// Copyright (c) 2023 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.subjective_ranking_counter.view.*

/**mClass for each counter in the Subjective Collection.
 *  Allows for an extra XML attribute to define the data point name displayed above the counter.
 *  @see R.layout.subjective_ranking_counter */

class SubjectiveRankingCounter(
    context: Context, attrs: AttributeSet
) : LinearLayout(context, attrs) {

    companion object {

        // The maximum value that a counter can hold.
        const val max = 3

        // The minimum value that a counter can hold.
        const val min = 1

        // How much to increment a counter by when pressing the plus or minus buttons.
        const val increment = 1

        // The starting value of each counter when the subjective collection activity is initialized.
        const val startingValue = 2
    }

    /* The current value of the counter. Anything that needs to get the current value of the counter
     * should use this. Has a custom setter method to automatically update the displayed value when
     * this value is changed. */
    var value: Int = startingValue
        set(updatedValue) {
            field = updatedValue
            tv_score_counter.text = updatedValue.toString()
        }

    /* Initializes the OnClickListeners for the plus and minus buttons. Should be called when this
     * counter is initialized. */
    private fun initOnClickListeners() {
        // Increment the value of this counter when the plus button is pressed if the counter is not
        // already at the maximum value.
        btn_plus.setOnClickListener {
            if (value != max) {
                value += increment
            }
        }
        // Decrement the value of this counter when the minus button is pressed if the counter is
        // not already at the minimum value.
        btn_minus.setOnClickListener {
            if (value != min) {
                value -= increment
            }
        }
    }

    // Initialize variables to be used in this counter.
    init {
        // Inflate the counter layout.
        val inflater =
            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.subjective_ranking_counter, this, true)

        // Get the custom attributes defined in the XML for this counter.
        val counterAttributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SubjectiveRankingCounter,
            0, 0
        )

        // Initialize the starting value of the counter.
        value = startingValue

        // Show the name of the data point for this counter. If there is no data name defined in the
        // XML, then set it to an empty string.
        tv_data_name.text =
            counterAttributes.getString(R.styleable.SubjectiveRankingCounter_dataName) ?: ""

        // Initialize the listeners for the plus and minus buttons.
        initOnClickListeners()

        // Recycle the attributes since they won't need to be used again.
        counterAttributes.recycle()
    }
}
