package com.example.administrator.qrcodever20kt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),View.OnClickListener {

    var df = SimpleDateFormat("dd-MMM-yyyy")
    var tvRefno: TextView? = null
    var edFltno: EditText? = null
    var edFltSecFr:EditText? = null
    var edFltSecTo:EditText? = null
    var edpaxName:EditText? = null
    var edseatNo:EditText? = null
    var edFltDate:EditText? = null

    var scanCam: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        scanCam = findViewById<View>(R.id.CameraScan) as TextView
        scanCam!!.setOnClickListener(this)
    }

    private fun init() {
        edFltDate = findViewById<View>(R.id.edbfltDate) as EditText
        edFltno = findViewById<View>(R.id.edbFltno) as EditText
        edFltSecFr = findViewById<View>(R.id.edbFltsecFr) as EditText
        edFltSecTo = findViewById<View>(R.id.edbFltsecTo) as EditText
        edpaxName = findViewById<View>(R.id.edbpaxName) as EditText
        tvRefno = findViewById<View>(R.id.tvRefNo) as TextView
        edseatNo = findViewById<View>(R.id.edbseatNo) as EditText
    }

    fun setMonth(getDays: Int): String? {
        val _year = Calendar.getInstance()[Calendar.YEAR]
        val month = IntArray(12)
        if (_year % 4 == 0) {
            month[0] = 31
            month[1] = 29
            month[2] = 31
            month[3] = 30
            month[4] = 31
            month[5] = 30
            month[6] = 31
            month[7] = 31
            month[8] = 30
            month[9] = 31
            month[10] = 30
            month[11] = 31

            // month = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        } else {
            month[0] = 31
            month[1] = 28
            month[2] = 31
            month[3] = 30
            month[4] = 31
            month[5] = 30
            month[6] = 31
            month[7] = 31
            month[8] = 30
            month[9] = 31
            month[10] = 30
            month[11] = 31
        }
        var mo = 0
        var da = 0
        var days = getDays
        var _d: String? = null
        for (i in month.indices) {
            if (days <= month[i]) {
                mo = i + 1
                da = days
                break
            } else {
                days = days - month[i]
            }
        }
        //System.out.println("Month : " + mo + "\nDate : " + da);
        Log.w("Month", mo.toString())
        val dateValue = "$_year-$mo-$da"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            val d = dateFormat.parse(dateValue)
            _d = df.format(d)
            println(_d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return _d
    }

    override fun onClick(p0: View?) {
        scanCode()
    }
    private fun scanCode() {
        val integrator = IntentIntegrator(this)
        integrator.captureActivity = CaptureAct::class.java
        integrator.setOrientationLocked(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Scanning code...")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val _result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (_result != null) {
            if (_result.contents != null) {
                val scanResult = _result.contents.trim { it <= ' ' }
                if (scanResult.trim { it <= ' ' }.length > 50) {
                    val _refNo = scanResult.substring(23, 29).trim { it <= ' ' }
                    val _name = scanResult.substring(2, 22).trim { it <= ' ' }
                    val _repname = _name.replace("/", " ")
                    val _secFr = scanResult.substring(30, 33).trim { it <= ' ' }
                    val _secTo = scanResult.substring(33, 36).trim { it <= ' ' }
                    val _flt = scanResult.substring(36, 38).trim { it <= ' ' }
                    val _fltNo = scanResult.substring(39, 43).trim { it <= ' ' }
                    val _seatno = scanResult.substring(48, 52).trim { it <= ' ' }
                    val _fltDate = scanResult.substring(44, 47).trim { it <= ' ' }
                    val _fDate = setMonth(_fltDate.toInt())
                    tvRefno!!.text = _refNo
                    edpaxName!!.setText(_repname)
                    edFltSecTo!!.setText(_secTo)
                    edFltSecFr!!.setText(_secFr)
                    edFltno!!.setText("$_flt $_fltNo")
                    edFltDate!!.setText(_fDate)
                    edseatNo!!.setText(_seatno)
                } else {
                    Toast.makeText(this, "Invalid Barcode..", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No Valid Result found..", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}