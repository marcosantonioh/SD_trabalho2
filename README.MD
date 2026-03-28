SERVIDOR PYTHON (FLASK)
Código completo

Flask → cria o servidor web
request → pega dados enviados pelo cliente (app do Android Studio)
jsonify → transforma resposta em JSON

app = Flask(__name__)
Cria a aplicação Flask
__name__ ajuda o Flask a localizar arquivos

Rota da API:
@app.route('/dados', methods=['POST'])
Define um endpoint: /dados
Aceita apenas requisições POST

Exemplo:
http://IP:5000/dados
Função que recebe os dados
def receber_dados():
Executa quando o app envia dados
Captura do JSON
data = request.json
Recebe o JSON enviado pelo app

Exemplo recebido:

{
  "bateria": 80,
  "memoria": "1200 MB"
}

Exibir no terminal:
print("\nDados recebidos:")
Apenas imprime no console
print(f"Bateria: {data.get('bateria')}%")
data.get('bateria') → pega valor da bateria
f"" → string formatada
print(f"Memória: {data.get('memoria')}")
Mostra memória recebida

Resposta para o app:
return jsonify({"status": "ok"})
Retorna um JSON pro cliente
O app recebe isso como resposta

Inicialização do servidor:
if __name__ == '__main__':
Executa só quando rodar o arquivo diretamente
app.run(host='0.0.0.0', port=5000)
host='0.0.0.0' → aceita conexões da rede
port=5000 → porta usada
📱 PARTE 2 — APP ANDROID (KOTLIN)
📄 Estrutura geral



O app:
- Coleta bateria
- Coleta memória
- Envia pro servidor
- Mostra status na tela
- Imports
import okhttp3.*


Biblioteca para fazer requisições HTTP:
import org.json.JSONObject

Criar JSON para enviar:
import java.io.IOException

Tratamento de erro de rede:
import android.app.ActivityManager

Acesso à memória do sistema:
import android.content.Context

Contexto do app (acesso ao sistema):
import android.os.BatteryManager

Acesso à bateria:
import android.os.Bundle

Dados do ciclo de vida da Activity:
import android.widget.*

Componentes da tela (TextView, Button, etc.):
import androidx.appcompat.app.AppCompatActivity

Base da Activity moderna:

Classe principal:
class MainActivity : AppCompatActivity()



Define a tela principal do app:
Variáveis:
- private lateinit var txtBattery: TextView -> Mostra bateria
- private lateinit var txtStatus: TextView -> Mostra status (erro/sucesso)
- private lateinit var txtMemory: TextView -> Mostra memória
- private lateinit var edtIp: EditText -> Campo onde usuário digita o IP
- private lateinit var btnEnviar: Button -> Botão de envio

onCreate (inicialização)
override fun onCreate(savedInstanceState: Bundle?)

Executa quando o app abre:
setContentView(R.layout.activity_main)

Carrega o layout XML:
findViewById(...)

Conecta variáveis Kotlin com elementos da tela:
updateBattery()
updateMemory()

Atualiza dados ao abrir:
btnEnviar.setOnClickListener -> Define ação do botão

Função de bateria -> val batteryManager = getSystemService(BATTERY_SERVICE)

Acessa serviço do sistema:
getIntProperty(BATTERY_PROPERTY_CAPACITY)

Retorna % da bateria:
txtBattery.text = "Bateria: $level%"

Atualiza tela

Função de memória:
val activityManager = getSystemService(Context.ACTIVITY_SERVICE)

Acessa gerenciador de memória:
val memoryInfo = ActivityManager.MemoryInfo()

Estrutura com dados da memória:
activityManager.getMemoryInfo(memoryInfo)

Preenche os dados:
memoryInfo.availMem

Memória disponível:
memoryInfo.totalMem -> Memória total


Função enviarDados()
Pega IP
val ip = edtIp.text.toString()

Usuário digita IP:
if (ip.isEmpty())
//Evita envio sem IP

Coleta dados novamente -> (igual funções anteriores)

Criação do JSON:
val json = JSONObject()

Cria objeto JSON:
json.put("bateria", bateria)
json.put("memoria", memoria)
//Adiciona dados

Cliente HTTP:
val client = OkHttpClient()
//Cria cliente de requisição

Corpo da requisição:
RequestBody.create(...)

Define conteúdo enviado:
URL
.url("http://$ip:5000/dados")
//Monta endereço do servidor

Envio:
client.newCall(request).enqueue(...)
//Envia requisição ASSÍNCRONA

onFailure:
Executa se falhar

onResponse:
Executa se funcionar

Atualizar UI:
runOnUiThread
//Necessário pra atualizar tela
