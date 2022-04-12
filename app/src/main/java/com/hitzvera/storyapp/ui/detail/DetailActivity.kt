package com.hitzvera.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hitzvera.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoUrl = intent.getStringExtra(EXTRA_AVATAR)
        val name = intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.setDataItem(photoUrl!!, name!!, description!!)
        showLoading(true)
        viewModel.dataItem.observe(this) {
            if(it != null) {
                showLoading(false)
                binding.apply {
                    tvName.text = it.name
                    tvDescription.text = it.description
                    Glide.with(this@DetailActivity)
                        .load(it.photoUrl)
                        .into(imgProfile)
                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.detailProgressBar.visibility = View.VISIBLE
        else binding.detailProgressBar.visibility = View.GONE
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
}