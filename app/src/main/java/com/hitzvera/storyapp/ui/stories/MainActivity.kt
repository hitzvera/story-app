package com.hitzvera.storyapp.ui.stories

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hitzvera.storyapp.R
import com.hitzvera.storyapp.databinding.ActivityMainBinding
import com.hitzvera.storyapp.databinding.ItemStoryUserBinding
import com.hitzvera.storyapp.ui.LoginActivity
import com.hitzvera.storyapp.ui.addstory.FormAddStoryActivity
import com.hitzvera.storyapp.ui.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var _itemBinding: ItemStoryUserBinding? = null
    private val itemBinding get() = _itemBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var name: String

    private lateinit var viewModel: StoriesViewModel
    private val storiesAdapter: StoriesAdapter by lazy { StoriesAdapter(StoriesAdapter.OnClickListener{ item ->
        Intent(this@MainActivity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_NAME, item.name)
            it.putExtra(DetailActivity.EXTRA_AVATAR, item.photoUrl)
            it.putExtra(DetailActivity.EXTRA_DESCRIPTION, item.description)

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    Pair(itemBinding!!.imgProfile, "profile"),
                    Pair(itemBinding!!.profileName, "name"),
                )
//            startActivity(it, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            startActivity(it, optionsCompat.toBundle())
        }
    }) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        _itemBinding = ItemStoryUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        name = preferences.getString(LoginActivity.NAME, "Anonymous").toString()
        val token = preferences.getString(LoginActivity.TOKEN, "").toString()

        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProvider(this)[StoriesViewModel::class.java]
        searchListItem(token)
        binding.mainRecyclerView.adapter = storiesAdapter

        viewModel.getListStoryItem().observe(this) {
            if(it != null){
                storiesAdapter.setData(it)
                showLoading(false)
            }
        }
        binding.addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, FormAddStoryActivity::class.java)
            intent.putExtra(FormAddStoryActivity.TOKEN, token)
            startActivity(intent)
        }
        binding.greetMainActivity.text = getString(R.string.greet_main_activity, name)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            (R.id.logout) -> {
                preferences.edit().apply {
                    clear()
                    apply()
                }
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
                return true
            }
            else -> false
        }
    }

    private fun searchListItem(token: String){
        showLoading(true)
        viewModel.getListStoryItem(token)
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.storiesProgressBar.visibility = View.VISIBLE
        else binding.storiesProgressBar.visibility = View.GONE
    }

}
