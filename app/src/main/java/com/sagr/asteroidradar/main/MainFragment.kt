package com.sagr.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sagr.asteroidradar.R
import com.sagr.asteroidradar.api.getNextSevenDaysFormattedDates
import com.sagr.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(requireNotNull(this.activity).application)
        )[MainViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onDoneNavigation()
            }


        })

        val adapter = MainAdapter(AsteroidListener {
            viewModel.startNavigation(it)
        })


        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            Timber.d("MainAdapter", "Triggred")
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.show_all_menu -> {
                Timber.d(item.title.toString())
                viewModel.updateFilter(AsteroidFilter.ALL)
            }
            R.id.show_week_menu -> {
                Timber.d(item.title.toString())

                viewModel.updateFilter(AsteroidFilter.WEEKLY)

            }
            else -> {
                Timber.d(item.title.toString())

                viewModel.updateFilter(AsteroidFilter.TODAY)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
