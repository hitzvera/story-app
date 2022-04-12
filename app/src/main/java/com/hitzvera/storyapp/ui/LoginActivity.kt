package com.hitzvera.storyapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.hitzvera.storyapp.R
import com.hitzvera.storyapp.databinding.ActivityLoginBinding
import com.hitzvera.storyapp.model.UserLoginResponse
import com.hitzvera.storyapp.network.StoryAppRetrofitInstance
import com.hitzvera.storyapp.ui.stories.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var  binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var isRemembered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean(CHECKBOX, false)
        hasLogin(isRemembered)

        binding.btnSignIn.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
        binding.settingLanguage.setOnClickListener(this)
    }


    private fun hasLogin(boolean: Boolean) {
        if(boolean) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        showLoading(true)
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etLoginPassword.text.toString().trim()
        StoryAppRetrofitInstance.apiService!!
            .getLoginUser(email, password)
            .enqueue(object: Callback<UserLoginResponse>{
                override fun onResponse(
                    call: Call<UserLoginResponse>,
                    response: Response<UserLoginResponse>
                ) {
                    if(response.isSuccessful) {
                        response.body()?.loginResult?.apply {
                            validateLogin(userId, name, token)
                        }
                        val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        showLoading(false)
                        startActivity(mainIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
                        finish()
                    } else  {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, "Data Is not valid", Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.btn_sign_in -> {
                login()
            }
            R.id.btn_sign_up -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.setting_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
    }

    private fun validateLogin(userId: String, name: String, token: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(NAME, name)
        editor.putString(USER_ID, userId)
        editor.putString(TOKEN, token)
        editor.putBoolean(CHECKBOX, binding.rememberMe.isChecked)
        editor.apply()
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.loginProgressBar.visibility = View.VISIBLE
        else binding.loginProgressBar.visibility = View.GONE
    }

    companion object {
        const val SHARED_PREFERENCES = "shared_preferences"
        const val CHECKBOX = "checkbox"
        const val NAME = "name"
        const val USER_ID = "user_id"
        const val TOKEN = "token"
    }
}