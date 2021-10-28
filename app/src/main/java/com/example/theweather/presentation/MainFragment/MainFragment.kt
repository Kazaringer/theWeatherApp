package com.example.theweather.presentation.MainFragment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theweather.R
import com.example.theweather.domain.models.WeatherModel
import com.example.theweather.presentation.applicationComponent
import dagger.Lazy
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainFragment @Inject constructor() : Fragment() {

    @Inject
    lateinit var viewModelFactory: Lazy<MainViewModel.Factory>

    @Inject
    lateinit var adapterFactory: WeatherRecycleViewAdapter.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory.get() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.applicationComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.weatherRecycleView)
        val adapter = viewModel.weatherModelsLiveData.value?.let { adapterFactory.create(it) }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter


        viewModel.weatherModelsLiveData.observe(viewLifecycleOwner, {
            adapter?.notifyDataSetChanged()
        })
    }


}