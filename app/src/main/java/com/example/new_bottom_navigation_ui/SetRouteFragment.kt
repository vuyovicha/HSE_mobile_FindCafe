package com.example.new_bottom_navigation_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.set_route_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Result


class SetRouteFragment : Fragment() {

    companion object {
        const val TAG = "SetRouteFragment"
    }

    private val model: SharedViewModel by activityViewModels()
    private lateinit var adapter: SetRouteFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.set_route_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SetRouteFragmentAdapter(requireActivity().supportFragmentManager)
        set_route_fragment_recycler.adapter = adapter
        set_route_fragment_recycler.layoutManager = LinearLayoutManager(context)

        //todo change the look of the edit text

//        search_location.setOnEditorActionListener(OnEditorActionListener { _, keyAction, keyEvent ->
//            if (keyAction == EditorInfo.IME_ACTION_SEARCH ||
//                keyEvent != null && KeyEvent.KEYCODE_ENTER === keyEvent.keyCode
//                && keyEvent.action === KeyEvent.ACTION_DOWN
//            ) {
//                val apiService = TomTomApiService.create()
//                apiService.find(search_location.text.toString(), "en-US").enqueue(object : Callback<PlaceHolder> {
//                    override fun onResponse(
//                        call: Call<PlaceHolder>,
//                        response: Response<PlaceHolder>
//                    ) {
//                        if (response.body()?.results?.get(0) != null) {
//                            adapter.replaceItems(response.body()!!.results)
//                        }
//                    }
//                    override fun onFailure(call: Call<PlaceHolder>, t: Throwable) { }
//                })
//                return@OnEditorActionListener true
//            }
//            false
//        })
        set_route_fragment_recycler.addItemDecoration(
            DividerItemDecoration(
                set_route_fragment_recycler.context,
                DividerItemDecoration.VERTICAL
            )
        )


        search_location.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                val apiService = TomTomApiService.create()
                apiService.find(search_location.query.toString(), "en-US").enqueue(object : Callback<PlaceHolder> {
                    override fun onResponse(
                        call: Call<PlaceHolder>,
                        response: Response<PlaceHolder>
                    ) {
                        if (response.body()?.results?.get(0) != null) {
                            adapter.replaceItems(response.body()!!.results)
                        }
                    }
                    override fun onFailure(call: Call<PlaceHolder>, t: Throwable) { }
                })
                return false
            }
        })


    }


}