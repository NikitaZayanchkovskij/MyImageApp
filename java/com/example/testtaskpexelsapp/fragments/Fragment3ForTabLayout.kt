package com.example.testtaskpexelsapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testtaskpexelsapp.MainActivity
import com.example.testtaskpexelsapp.adapters.TabLayoutFragmentsAdapter
import com.example.testtaskpexelsapp.databinding.Fragment3ForTabLayoutBinding
import com.example.testtaskpexelsapp.interfaces.IImagePressedListener
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels
import com.example.testtaskpexelsapp.mvvm.viewmodel.MyViewModel
import com.example.testtaskpexelsapp.utils.openFragmentFromFragment
import com.example.testtaskpexelsapp.utils.showToastForTheUser


class Fragment3ForTabLayout(private val context: MainActivity): Fragment(), IImagePressedListener {
    private lateinit var binding: Fragment3ForTabLayoutBinding
    private val adapter = TabLayoutFragmentsAdapter(this, context)
    private val myViewModel: MyViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = Fragment3ForTabLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewAdapter()
        initViewModel()
    }


    private fun initRecyclerViewAdapter() = with(binding) {
        rcViewFrag3ForTabLay.layoutManager = GridLayoutManager(activity as AppCompatActivity, 2)
        rcViewFrag3ForTabLay.adapter = adapter
    }


    private fun initViewModel() {
        myViewModel.liveFragment3ImageCategoryData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun imagePressed(image: ImageResourceFromPexels) {
        context.binding.mainLinearLayWithViewElements.visibility = View.GONE
        context.binding.bottomNavView.visibility = View.GONE
        openFragmentFromFragment(ImageDetailsFragment(
            false, false, context, image))
    }


}