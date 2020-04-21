package de.datlag.musicslide.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import de.datlag.musicslide.R
import rm.com.clocks.ClockImageView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max

class LockFragment : Fragment() {

    private lateinit var clockImageView: ClockImageView
    private lateinit var clockTextView: AppCompatTextView
    private lateinit var dateTextView: AppCompatTextView

    private val handlerTicker = Handler()
    private val initialDelay = TimeUnit.SECONDS.toMillis(1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clockImageView = view.findViewById(R.id.clockImage)
        clockTextView = view.findViewById(R.id.clockText)
        dateTextView = view.findViewById(R.id.dateText)

        scheduleNextTick(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTickerSchedule()
    }

    override fun onStop() {
        super.onStop()
        cancelTickerSchedule()
    }

    override fun onResume() {
        super.onResume()
        scheduleNextTick(true)
    }

    override fun onStart() {
        super.onStart()
        scheduleNextTick(true)
    }

    private fun cancelTickerSchedule() {
        handlerTicker.removeCallbacksAndMessages(null)
    }

    private fun scheduleNextTick(isFirst: Boolean = true) {
        val tickDelay = if (isFirst) initialDelay else calculateNextDelay()

        handlerTicker.postDelayed({
            showCurrentTime()
            scheduleNextTick(false)
        }, tickDelay)
    }

    private fun showCurrentTime() {
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)
        val clockFormat = SimpleDateFormat(requireContext().getString(R.string.lock_clock_format), Locale.getDefault())
        val dateFormat = SimpleDateFormat(requireContext().getString(R.string.lock_date_format), Locale.getDefault())

        clockImageView.animateToTime(hour, minute)
        clockTextView.text = clockFormat.format(now.time)
        dateTextView.text = dateFormat.format(now.time)
    }

    private fun calculateNextDelay(): Long {
        val calNextMinute = Calendar.getInstance()
        calNextMinute.set(Calendar.SECOND, 0)
        calNextMinute.set(Calendar.MILLISECOND, 0)
        calNextMinute.add(Calendar.MINUTE, 1)

        return max(0, calNextMinute.timeInMillis - System.currentTimeMillis())
    }

    companion object {
        fun newInstance(): LockFragment {
            return LockFragment()
        }
    }

}