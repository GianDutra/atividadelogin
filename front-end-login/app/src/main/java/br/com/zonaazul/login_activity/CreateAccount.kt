package br.com.zonaazul.login_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class CreateAccount : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var etNome: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnSinUp: MaterialButton

    private lateinit var functions: FirebaseFunctions

    // usando o logcat (ferramenta do Android studio para verificar a saída)
    private val logEntry = "CADASTRO_PRODUTO";

    // instanciando um objeto gson
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)


        functions = Firebase.functions("southamerica-east1")

        auth = Firebase.auth
        etNome = findViewById(R.id.etNome)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSinUp = findViewById(R.id.btnSinUp)



        // ajustando o listener do btnSinUp
        btnSinUp.setOnClickListener {

            auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())

            val u = usuarios (etNome.text.toString(), etEmail.text.toString(), etPassword.text.toString())
            cadastrarUsuario(u)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {

                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }

                        // tratar a exceção...

                    }else{

                        /**
                         * Lembre-se que na Function criamos um padrão de retorno.
                         * Um JSON composto de status, message e payload.
                         * Podemos obter esse Json genérico e convertê-lo para um
                         * objeto da classe nossa FunctionsGenericResponse
                         * e a partir dali, tratar o não a conversão do payload. Veja:
                         */

                        /**
                         * Lembre-se que na Function criamos um padrão de retorno.
                         * Um JSON composto de status, message e payload.
                         * Podemos obter esse Json genérico e convertê-lo para um
                         * objeto da classe nossa FunctionsGenericResponse
                         * e a partir dali, tratar o não a conversão do payload. Veja:
                         */
                        /**
                         * Lembre-se que na Function criamos um padrão de retorno.
                         * Um JSON composto de status, message e payload.
                         * Podemos obter esse Json genérico e convertê-lo para um
                         * objeto da classe nossa FunctionsGenericResponse
                         * e a partir dali, tratar o não a conversão do payload. Veja:
                         */
                        /**
                         * Lembre-se que na Function criamos um padrão de retorno.
                         * Um JSON composto de status, message e payload.
                         * Podemos obter esse Json genérico e convertê-lo para um
                         * objeto da classe nossa FunctionsGenericResponse
                         * e a partir dali, tratar o não a conversão do payload. Veja:
                         */







                        // convertendo.
                        val genericResp = gson.fromJson(task.result, FunctionsGenericResponse::class.java)


                        // abra a aba Logcat e selecione "INFO" e filtre por
                        Log.i(logEntry, genericResp.status.toString())
                        Log.i(logEntry, genericResp.message.toString())

                        // claro, o payload deve ser convertido para outra coisa depois.
                        Log.i(logEntry, genericResp.payload.toString())

                        /*
                            Converter o "payload" para um objeto mais específico para
                            tratar o docId. Veja a classe "GenericInsertResponse" que eu criei...
                            Lembrando que para cada situação o payload é um campo "polimorfico"
                            por isso na classe de resposta genérica é um Any.
                        */
                        val insertInfo = gson.fromJson(genericResp.payload.toString(), GenericInsertResponse::class.java)

                        Snackbar.make(btnSinUp, "Usuário cadastrado: " + insertInfo.docId,
                            Snackbar.LENGTH_LONG).show();
                    }
                })
        }




    }


    private fun cadastrarUsuario(u: usuarios): Task<String> {
        val data = hashMapOf(
            "nome" to u.nome,
            "email" to u.email
        )
        return functions
            .getHttpsCallable("addUsuario")
            .call(data)
            .continueWith { task ->
                // convertendo o resultado em string Json válida
                val res = gson.toJson(task.result?.data)
                res
            }
    }

}

