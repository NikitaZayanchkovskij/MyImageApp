package com.example.testtaskpexelsapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testtaskpexelsapp.MainActivity
import com.example.testtaskpexelsapp.adapters.BookmarksFragmentAdapter
import com.example.testtaskpexelsapp.databinding.BookmarksFragmentBinding
import com.example.testtaskpexelsapp.interfaces.IImagePressedListener
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels
import com.example.testtaskpexelsapp.room.MyRoomDataBase
import com.example.testtaskpexelsapp.utils.closeFragmentFromActivity
import com.example.testtaskpexelsapp.utils.closeFragmentFromFragment
import com.example.testtaskpexelsapp.utils.openFragmentFromFragment


class BookmarksFragment(private val context: MainActivity): Fragment(), IImagePressedListener {
    private lateinit var binding: BookmarksFragmentBinding
    private val adapter = BookmarksFragmentAdapter(this, context)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BookmarksFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewAdapter()
        receiveImagesFromRoom()
    }


    private fun receiveImagesFromRoom() {
        val myRoomDataBase = MyRoomDataBase.getDataBase(activity as AppCompatActivity)

        myRoomDataBase
            .receiveDaoInterface()
            .getAllImagesFromRoom()
            .asLiveData()
            .observe(viewLifecycleOwner) {

                if (it.isEmpty()) {
                    binding.tvNoBookmarksYet.visibility = View.VISIBLE
                    binding.tvExploreBookmarks.visibility = View.VISIBLE
                    exploreButtonAkaReturnToHomeScreen()

                } else {
                    adapter.submitList(it)
                }
            }
    }


    private fun initRecyclerViewAdapter() = with(binding) {
        rcViewBookmarksScreen.layoutManager = GridLayoutManager(context, 2)
        binding.rcViewBookmarksScreen.adapter = adapter
    }


    private fun exploreButtonAkaReturnToHomeScreen() {

        binding.tvExploreBookmarks.setOnClickListener {
            context.binding.mainLinearLayWithViewElements.visibility = View.VISIBLE
            closeFragmentFromFragment()
            context.homeScreenIsOpened = true
        }
    }


    override fun imagePressed(image: ImageResourceFromPexels) {
        context.binding.mainLinearLayWithViewElements.visibility = View.GONE
        context.binding.bottomNavView.visibility = View.GONE
        openFragmentFromFragment(ImageDetailsFragment(
            true, false, context, image))
    }


}