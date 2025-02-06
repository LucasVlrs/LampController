package br.com.lampcontroller

import android.os.Bundle
import android.widget.Toast
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        getStatus()
    }

    private fun setupClickListeners() {
        binding.buttonOn.setOnClickListener {
            controlLed("on")
        }

        binding.buttonOff.setOnClickListener {
            controlLed("off")
        }
    }

    private fun getStatus() {
        RetrofitClient.instance.getStatus().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let {
                    binding.statusLampada.text = "Status: $it"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                showError("Falha ao obter status: ${t.message}")
            }
        })
    }

    private fun controlLed(state: String) {
        RetrofitClient.instance.controlLed(state).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let {
                    binding.statusLampada.text = "Status: $it"
                    Toast.makeText(this@MainActivity, "Sucesso!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                showError("Falha ao controlar LED: ${t.message}")
            }
        })
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        binding.statusLampada.text = "Erro!"
    }
}

/**
#include <WiFi.h>
#include <WebServer.h>

const char* ssid = "SUA_REDE_WIFI"; // por o nome da rede certa
const char* password = "SUA_SENHA_WIFI"; // por a senha certa da rede

#define LED_PIN 2  // Trocar pra o pino certo
WebServer server(80);

void setup() {
Serial.begin(115200);
pinMode(LED_PIN, OUTPUT);

WiFi.begin(ssid, password);

while (WiFi.status() != WL_CONNECTED) {
delay(1000);
Serial.println("Conectando ao WiFi...");
}

Serial.print("IP do ESP32: ");
Serial.println(WiFi.localIP());

server.on("/", []() {
server.send(200, "text/plain", digitalRead(LED_PIN) ? "on" : "off");
});

server.on("/led", []() {
String state = server.arg("state");
if (state == "on") {
digitalWrite(LED_PIN, HIGH);
} else if (state == "off") {
digitalWrite(LED_PIN, LOW);
}
server.send(200, "text/plain", digitalRead(LED_PIN) ? "on" : "off");
});

server.begin();
}

void loop() {
server.handleClient();
}
* */