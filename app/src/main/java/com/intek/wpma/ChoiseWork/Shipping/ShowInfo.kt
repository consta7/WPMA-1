package com.intek.wpma.ChoiseWork.Shipping

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.intek.wpma.BarcodeDataReceiver
import com.intek.wpma.R
import kotlinx.android.synthetic.main.activity_show_info.*


class ShowInfo : BarcodeDataReceiver() {

    var iddoc: String = ""
    var number: String = ""
    var Barcode: String = ""
    var codeId:String = ""  //показатель по которому можно различать типы штрих-кодов
    val barcodeDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("IntentApiSample: ", "onReceive")
            if (ACTION_BARCODE_DATA == intent.action) {
                val version = intent.getIntExtra("version", 0)
                if (version >= 1) {
                    // ту прописываем что делать при событии сканирования

                    Barcode = intent.getStringExtra("data")
                    codeId = intent.getStringExtra("codeId")
                    reactionBarcode(Barcode)

                }
            }
        }
    }

    private fun reactionBarcode(Barcode: String) {
        val toast = Toast.makeText(applicationContext, "ШК не работают на данном экране!", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_info)
        iddoc = intent.extras!!.getString("Doc")!!
        number = intent.extras!!.getString("Number")!!
        //ParentForm = intent.extras!!.getString("ParentForm")!!
        terminalView.text = SS.terminal
        title = SS.helper.GetShortFIO(SS.FEmployer.Name)

        //getShowInfo()

        //scroll.setOnTouchListener(@this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == 21){ //нажали влево; вернемся к документу
            val loadingAct = Intent(this, Loading::class.java)
            loadingAct.putExtra("Doc",iddoc)
            loadingAct.putExtra("Number",number)
            loadingAct.putExtra("ParentForm","ShowInfo")
            startActivity(loadingAct)
            finish()
        }
//        else if (keyCode == 20){    //вниз
//
//        }
        return super.onKeyDown(keyCode, event)
    }


    private fun getShowInfo(){
        var textQuery =""

        textQuery = SS.QuerySetParam(textQuery, "Number", number)
        textQuery = SS.QuerySetParam(textQuery, "iddoc", iddoc)
        val dataTable = SS.ExecuteWithReadNew(textQuery) ?: return

        if(dataTable.isNotEmpty()){

            for (DR in dataTable){
                val row = TableRow(this)
                val number = TextView(this)
                val linearLayout = LinearLayout(this)
                number.text = DR[""]
                number.layoutParams = LinearLayout.LayoutParams(45,ViewGroup.LayoutParams.WRAP_CONTENT)
                number.gravity = Gravity.CENTER
                number.textSize = 16F
                number.setTextColor(-0x1000000)
                val address = TextView(this)
                address.text = DR[""]
                address.layoutParams = LinearLayout.LayoutParams(135,ViewGroup.LayoutParams.WRAP_CONTENT)
                address.textSize = 16F
                address.setTextColor(-0x1000000)
                val code = TextView(this)
                code.text = DR[""]
                code.layoutParams = LinearLayout.LayoutParams(135,ViewGroup.LayoutParams.WRAP_CONTENT)
                code.gravity = Gravity.CENTER
                code.textSize = 16F
                code.setTextColor(-0x1000000)
                val count = TextView(this)
                count.text = DR[""]
                count.layoutParams = LinearLayout.LayoutParams(40,ViewGroup.LayoutParams.WRAP_CONTENT)
                count.gravity = Gravity.CENTER
                count.textSize = 16F
                count.setTextColor(-0x1000000)
                val sum = TextView(this)
                sum.text = DR[""]
                sum.layoutParams = LinearLayout.LayoutParams(120,ViewGroup.LayoutParams.WRAP_CONTENT)
                sum.gravity = Gravity.CENTER
                sum.textSize = 16F
                sum.setTextColor(-0x1000000)


                linearLayout.addView(number)
                linearLayout.addView(address)
                linearLayout.addView(code)
                linearLayout.addView(count)
                linearLayout.addView(sum)

                row.addView(linearLayout)
                table.addView(row)
            }
        }
        return
    }



    override fun onResume() {
        super.onResume()
        registerReceiver(barcodeDataReceiver, IntentFilter(ACTION_BARCODE_DATA))
        claimScanner()
        Log.d("IntentApiSample: ", "onResume")
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(barcodeDataReceiver)
        releaseScanner()
        Log.d("IntentApiSample: ", "onPause")
    }
}
