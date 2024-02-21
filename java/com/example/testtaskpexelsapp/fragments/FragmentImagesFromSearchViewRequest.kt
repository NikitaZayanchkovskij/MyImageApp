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
import com.example.testtaskpexelsapp.R
import com.example.testtaskpexelsapp.adapters.TabLayoutFragmentsAdapter
import com.example.testtaskpexelsapp.databinding.FragmentImagesFromSearchViewRequestBinding
import com.example.testtaskpexelsapp.interfaces.IImagePressedListener
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels
import com.example.testtaskpexelsapp.mvvm.viewmodel.MyViewModel
import com.example.testtaskpexelsapp.utils.closeFragmentFromFragment
import com.example.testtaskpexelsapp.utils.openFragmentFromFragment


class FragmentImagesFromSearchViewRequest(
    private val searchedCategory: String,
    private val context: MainActivity): Fragment(), IImagePressedListener {

    private lateinit var binding: FragmentImagesFromSearchViewRequestBinding
    private val adapter = TabLayoutFragmentsAdapter(this, context)
    private val myViewModel: MyViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentImagesFromSearchViewRequestBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolBar()
        initRecyclerViewAdapter()
        initViewModel()
    }


    private fun setUpToolBar() = with(binding) {
        val toolBarTitle = "${context.getString(R.string.search_fragment_title)} $searchedCategory"
        tvSearchedCategory.text = toolBarTitle

        /* Кнопка стрелочка назад. */
        tbImagesFromSearchViewRequest.setNavigationOnClickListener {
            closeFragmentFromFragment()
            context.binding.mainLinearLayWithViewElements.visibility = View.VISIBLE
            context.binding.bottomNavView.visibility = View.VISIBLE
        }
    }


    private fun initRecyclerViewAdapter() = with(binding) {
        rcViewFragForImagesFromSearchViewRequest.layoutManager = GridLayoutManager(activity as AppCompatActivity, 2)
        rcViewFragForImagesFromSearchViewRequest.adapter = adapter
    }


    private fun initViewModel() {
        myViewModel.liveSearchViewImagesData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun imagePressed(image: ImageResourceFromPexels) {
        binding.tbImagesFromSearchViewRequest.visibility = View.GONE
        binding.rcViewFragForImagesFromSearchViewRequest.visibility = View.GONE
        openFragmentFromFragment(ImageDetailsFragment(
            false, true, context, image))
    }


}