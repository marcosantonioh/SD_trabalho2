package com.example.monitor

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.app.ActivityManager
import android.content.Context
import android.os.BatteryManager
import android.os.Bundle
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class MainActivity : AppCompatActivity() {

    private lateinit var txtBattery: TextView
    private lateinit var txtStatus: TextView
    private lateinit var txtMemory: TextView

    private lateinit var edtIp: EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtBattery = findViewById(R.id.txtBattery)
        txtStatus = findViewById(R.id.txtStatus)
        txtMemory = findViewById(R.id.txtMemory)

        edtIp = findViewById(R.id.edtIp)
        btnEnviar = findViewById(R.id.btnEnviar)

        updateBattery()
        updateMemory()

        btnEnviar.setOnClickListener {
            enviarDados()
        }
    }

    private fun updateBattery() {
        try {
            val batteryManager = getSystemService(BATTERY_SERVICE) as BatteryManager
            val level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            txtBattery.text = "Bateria: $level%"
            txtStatus.text = "Status atualizado"
        } catch (e: Exception) {
            txtBattery.text = "Erro ao obter bateria"
            txtStatus.text = e.message
        }
    }

    private fun updateMemory() {
        try {
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()

            activityManager.getMemoryInfo(memoryInfo)

            val totalMem = memoryInfo.totalMem / (1024 * 1024)
            val availMem = memoryInfo.availMem / (1024 * 1024)

            txtMemory.text = "Memória: $availMem MB / $totalMem MB"
        } catch (e: Exception) {
            txtMemory.text = "Erro ao obter memória"
        }
    }

    private fun enviarDados() {
        val ip = edtIp.text.toString()

        if (ip.isEmpty()) {
            txtStatus.text = "Informe o IP do servidor"
            return
        }

        try {
            val batteryManager = getSystemService(BATTERY_SERVICE) as BatteryManager
            val bateria = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)

            val memoria = "${memoryInfo.availMem / (1024 * 1024)} MB"

            val json = JSONObject()
            json.put("bateria", bateria)
            json.put("memoria", memoria)


            val client = OkHttpClient()

            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            val request = Request.Builder()
                .url("http://$ip:5000/dados")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        txtStatus.text = "Erro ao enviar ❌"
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        txtStatus.text = "Enviado com sucesso 🚀"
                    }
                }
            })

        } catch (e: Exception) {
            txtStatus.text = "Erro: ${e.message}"
        }
    }
}