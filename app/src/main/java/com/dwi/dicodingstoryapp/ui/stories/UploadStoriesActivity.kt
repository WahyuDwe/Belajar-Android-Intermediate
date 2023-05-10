package com.dwi.dicodingstoryapp.ui.stories

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dwi.dicodingstoryapp.R
import com.dwi.dicodingstoryapp.databinding.ActivityUploadStoryBinding
import com.dwi.dicodingstoryapp.utils.reduceFileImage
import com.dwi.dicodingstoryapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadStoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private val viewModel: UploadStoriesViewModel by viewModels()
    private var isFieldValid = false

    private val launchIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launchIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadStoriesActivity)
                getFile = myFile
                binding.previewImage.setImageURI(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this@UploadStoriesActivity,
                    "Tidak Mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this@UploadStoriesActivity,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSION
            )
        }

        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {
            if (checkField()) {
                uploadStory()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        com.dwi.dicodingstoryapp.utils.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadStoriesActivity,
                "com.dwi.dicodingstoryapp.fileprovider",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launchIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launchIntentGallery.launch(chooser)
    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val desc =
                binding.etDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            isLoading(true)
            viewModel.uploadStories(imageMultipart, desc).observe(this) {
                if (it != null) {
                    isLoading(false)
                    Toast.makeText(
                        this@UploadStoriesActivity,
                        getString(R.string.berhasil_upload_story),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    isLoading(false)
                    Toast.makeText(
                        this@UploadStoriesActivity,
                        getString(R.string.gagal_upload_story),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            Toast.makeText(
                this@UploadStoriesActivity,
                getString(R.string.gambar_tidak_boleh_kosong),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun checkField(): Boolean {
        isFieldValid = if (binding.etDescription.text.toString().isEmpty()) {
            Toast.makeText(
                this@UploadStoriesActivity,
                "Deskripsi tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            false
        } else {
            true
        }

        return isFieldValid
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 1
    }
}