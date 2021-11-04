package com.example.theweather.presentation.chartFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.theweather.R
import com.example.theweather.presentation.applicationComponent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import javax.inject.Inject

class ChartFragment @Inject constructor() : Fragment() {
    private val viewModel: ChartViewModel by viewModels()
    private val entries: ArrayList<Entry> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.chart_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val graphView = view.findViewById<LineChart>(R.id.weatherGraph)
        val xAxis: XAxis = graphView.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(false)

        graphView.setTouchEnabled(false)
        graphView.setDragEnabled(false)
        graphView.setScaleEnabled(false)
        graphView.setPinchZoom(false)
        graphView.setDoubleTapToZoomEnabled(false)
        graphView.getLegend()?.setEnabled(false)
        graphView.getDescription()?.setEnabled(false);

        //entries.add(Entry((x - minX).toFloat(), y.toFloat()))
        val dataSet = LineDataSet(entries,"")
        val lineData = LineData(dataSet)
        graphView?.setData(lineData)
        graphView?.invalidate()
    }
}