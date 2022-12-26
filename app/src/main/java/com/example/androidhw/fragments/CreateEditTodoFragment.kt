package com.example.androidhw.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.androidhw.MainActivity
import com.example.androidhw.R
import com.example.androidhw.RequestPermissionsProvider
import com.example.androidhw.data.TodoRepository
import com.example.androidhw.data.entities.Todo
import com.example.androidhw.databinding.CreateEditTodoFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateEditTodoFragment : Fragment(R.layout.create_edit_todo_fragment) {

    private var binding: CreateEditTodoFragmentBinding? = null
    private var repository: TodoRepository? = null
    private var deadline: Date? = null
    private var savedDeadline: Date? = null
    private var todoId: Int = 0
    private var arePermissionsGranted: Boolean? = null
    private var sharedPreferences: SharedPreferences? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var requestPermissionsProvider: RequestPermissionsProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        this.arguments?.let {
            arePermissionsGranted = it.getBoolean(ARE_PERMISSIONS_GRANTED)
            todoId = it.getInt(ARG_ID)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sharedPreferences = requireActivity().getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
        requestPermissionsProvider = RequestPermissionsProvider()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)

        menu.findItem(R.id.delete_all).isVisible = false
        menu.findItem(R.id.dark_mode).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.add_todo -> {
                createUpdateTodo()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateEditTodoFragmentBinding.bind(view)

        if (isLocationEnabled()) {
            getCurrentLocation()
        } else {
            val shouldRequestLocation = sharedPreferences?.getBoolean(MainActivity.SHOULD_REQUEST_LOCATION, true)

            if (arePermissionsGranted == true && shouldRequestLocation != false) {
                requestPermissionsProvider?.suggestUserToTurnOnLocation(requireContext())
            } else {
                sharedPreferences?.edit()
                    ?.putFloat(MainActivity.LAST_LATITUDE, -1.0F)
                    ?.apply()
                sharedPreferences?.edit()
                    ?.putFloat(MainActivity.LAST_LONGITUDE, -1.0F)
                    ?.apply()
            }
        }

        repository = TodoRepository(requireContext())

        if (todoId != -1) {
            setDataFromDb(todoId)
        }

        binding?.run {
            tvDate.setOnClickListener {
                showDateTimePicker()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        repository = null
    }

    private fun createUpdateTodo() {
        binding?.run {
            val title = tietTitle.text.toString()
            val description = tietDescription.text.toString()
            val date = if (deadline != null) deadline else savedDeadline

            val lastLongitude = sharedPreferences?.getFloat(MainActivity.LAST_LONGITUDE, -1.0F)?.toDouble()!!
            val lastLatitude = sharedPreferences?.getFloat(MainActivity.LAST_LATITUDE, -1.0F)?.toDouble()!!

            val longitude = if (lastLongitude == -1.0) null else lastLongitude
            val latitude = if (lastLatitude == -1.0) null else lastLatitude

            if (title == "") {
                tvDate.updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(16, 36, 16, 0) }
                tilTitle.isErrorEnabled = true
                tilTitle.error = "Title is required"
            } else {
                tvDate.updateLayoutParams<ViewGroup.MarginLayoutParams> { setMargins(16, 0, 16, 0) }
                tilTitle.isErrorEnabled = false
                if (description == "") {
                    tilDescription.isErrorEnabled = true
                    tilDescription.error = "Description is required"
                } else {
                    tilDescription.isErrorEnabled = false
                    if (todoId == -1) {
                        saveTodo(title, description, date, longitude, latitude)
                    } else {
                        updateTodo(title, description, date, longitude, latitude)
                    }
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun saveTodo(title: String, description: String, date: Date?, longitude: Double?, latitude: Double?) {
        lifecycleScope.launch {
            repository?.saveTodo(
                Todo(0, title, description, date, longitude, latitude)
            )
        }
    }

    private fun updateTodo(title: String, description: String, date: Date?, longitude: Double?, latitude: Double?) {
        lifecycleScope.launch {
            repository?.updateTodo(todoId, title, description, date, longitude, latitude)
        }
    }

    private fun setDataFromDb(todoId: Int) {
        lifecycleScope.launch {
            val todoModel = repository?.getTodoById(todoId)

            val date = todoModel?.date
            if (date != null) savedDeadline = date

            binding?.run {
                tietTitle.setText(todoModel?.title)
                tietDescription.setText(todoModel?.description)
                tvDate.text = if (date == null) "Without deadline" else SimpleDateFormat("E, d MMMM yyyy, HH:mm").format(date)
            }
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(requireContext(), { _, _year, _month, _day ->
            TimePickerDialog(requireContext(), { _, _hour, _minute ->
                calendar.set(_year, _month, _day, _hour, _minute)
                binding?.tvDate?.text = SimpleDateFormat("E, d MMMM yyyy, HH:mm").format(calendar.time)
                deadline = calendar.time
            },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (arePermissionsGranted == true) {
            fusedLocationProviderClient?.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)?.addOnCompleteListener {
                val location = it.result

                if (location != null) {
                    sharedPreferences?.edit()
                        ?.putFloat(MainActivity.LAST_LATITUDE, location.latitude.toFloat())
                        ?.apply()
                    sharedPreferences?.edit()
                        ?.putFloat(MainActivity.LAST_LONGITUDE, location.longitude.toFloat())
                        ?.apply()
                }
            }
        } else {
            sharedPreferences?.edit()
                ?.putFloat(MainActivity.LAST_LATITUDE, -1.0F)
                ?.apply()
            sharedPreferences?.edit()
                ?.putFloat(MainActivity.LAST_LONGITUDE, -1.0F)
                ?.apply()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        private const val ARE_PERMISSIONS_GRANTED = "ARE_PERMISSIONS_GRANTED"
        private const val ARG_ID = "ARG_ID"

        fun createBundle(arePermissionsGranted: Boolean, id: Int) = CreateEditTodoFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARE_PERMISSIONS_GRANTED, arePermissionsGranted)
                putInt(ARG_ID, id)
            }
        }
    }
}
