package com.example.unproxy

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode

class MainActivity : AppCompatActivity() {

    private lateinit var scanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),123)

        }
        else
        {
            scan()
        }
    }
    private fun scan()
    {
        val scannerview: CodeScannerView = findViewById(R.id.scanner)
        scanner = CodeScanner(this,scannerview)
        scanner.camera = CodeScanner.CAMERA_BACK
        scanner.formats = CodeScanner.ALL_FORMATS
        scanner.autoFocusMode = AutoFocusMode.SAFE
        scanner.scanMode = ScanMode.SINGLE
        scanner.isAutoFocusEnabled = true
        scanner.decodeCallback = DecodeCallback{
            runOnUiThread{
                Toast.makeText(this,"SubjectToken "+it.text,Toast.LENGTH_LONG).show()
            }
        }
        scannerview.setOnClickListener{
            scanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==123)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(::scanner.isInitialized){
            scanner.startPreview()
        }

    }

    override fun onPause() {
        if(::scanner.isInitialized){
            scanner.releaseResources()
        }

        super.onPause()
    }
}