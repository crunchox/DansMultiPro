package com.example.dansmultiprotest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import java.io.IOException
import java.net.SocketTimeoutException

class JobListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            try {
                val positions = RetrofitClientInstance.apiService.getPositions()
                Log.d("API Response", positions.toString())
            } catch (e: Exception) {
                Log.e("API Exception", e.message.toString())
            } catch (e: IOException) {
                Log.e("API IOException", e.message.toString())
                // handle the IO exception
            } catch (e: SocketTimeoutException) {
                Log.e("API SocketTimeoutException", e.message.toString())
                // handle the socket timeout exception
            }
        }

    }

}