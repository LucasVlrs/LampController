package br.com.lampcontroller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.lampcontroller.databinding.ActivityMainBinding
import br.com.lampcontroller.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.buttonOn.setOnClickListener {
            controlLed("on")
        }

        binding.buttonOff.setOnClickListener {
            controlLed("off")
        }

        getStatus()
    }

    private fun getStatus() {
        RetrofitClient.instance.getStatus().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                binding.statusLampada.text = response.body() ?: "No response"
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                binding.statusLampada.text = "Error: ${t.message}"
            }
        })
    }

    private fun controlLed(state: String) {
        RetrofitClient.instance.controlLed(state).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                binding.statusLampada.text = response.body() ?: "No response"
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                binding.statusLampada.text = "Error: ${t.message}"
            }
        })
    }
}

/**
#include <WiFi.h>
#include <WebServer.h>

const char* ssid = "nome_da_rede";
const char* password = "senha_da_rede";

WebServer server(80);

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }

  Serial.println("Connected to WiFi");
  server.on("/", []() {
    server.send(200, "text/plain", "Sucesso!");
  });

  server.on("/led", []() {
    String state = server.arg("state");
    if (state == "on") {
      // Liga o led
    } else if (state == "off") {
      // Desliga o led
    }
    server.send(200, "text/plain", "LED State Changed");
  });

  server.begin();
}

void loop() {
  server.handleClient();
}
* */
