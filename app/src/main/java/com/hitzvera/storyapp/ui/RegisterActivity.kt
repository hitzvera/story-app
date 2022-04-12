package com.hitzvera.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hitzvera.storyapp.R
import com.hitzvera.storyapp.databinding.ActivityRegisterBinding
import com.hitzvera.storyapp.model.UserRegisterResponse
import com.hitzvera.storyapp.network.StoryAppRetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnCreateAccount.setOnClickListener(this)
        binding.btnRegisToLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_create_account -> {
                if(validateCreateAccount()) {
                    requestCreateAccount()
                    clearEditText()
                } else {
                    clearEditText()
                }
            }
            R.id.btn_regis_to_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun requestCreateAccount() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etCreatePassword.text.toString().trim()
        showLoading(true)
        StoryAppRetrofitInstance.apiService!!
            .createAccount(name, email, password)
            .enqueue(object: Callback<UserRegisterResponse>{
                override fun onResponse(
                    call: Call<UserRegisterResponse>,
                    response: Response<UserRegisterResponse>
                ) {
                    if(response.isSuccessful) {
                        showLoading(false)
                        Toast.makeText(this@RegisterActivity, "Account has been made", Toast.LENGTH_SHORT).show()
                    } else {
                        showLoading(false)
                        Toast.makeText(this@RegisterActivity, "Email is already taken", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity, "Failed to make an Account", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun clearEditText() {
        binding.etName.text.clear()
        binding.etEmail.text!!.clear()
        binding.etCreatePassword.text!!.clear()
    }

    private fun validateCreateAccount(): Boolean {
        return if(binding.etEmail.text!!.isNotEmpty()
            && binding.etCreatePassword.text!!.isNotEmpty()
            && binding.etName.text.isNotEmpty()
            && android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
            && binding.etCreatePassword.text.toString().length > 6) {
            true
        } else {
            Toast.makeText(this, "Data have to be valid", Toast.LENGTH_SHORT).show()
            false
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}