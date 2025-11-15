package com.ifrs.movimentaif.ui.parq

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.ifrs.movimentaif.R
import com.ifrs.movimentaif.api.RetrofitInstance
import com.ifrs.movimentaif.model.ParQ
import com.ifrs.movimentaif.utils.setOnClickListenerWithSound
import kotlinx.coroutines.launch

class ParQActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val checkBoxes = mutableMapOf<String, CheckBox>()
    private lateinit var etObservacoes: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var progressBar: View
    
    private var existingParQ: ParQ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parq)

        auth = FirebaseAuth.getInstance()
        
        supportActionBar?.title = "Questionário PAR-Q"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeViews()
        loadExistingParQ()

        btnSave.setOnClickListenerWithSound {
            saveParQ()
        }

        btnCancel.setOnClickListenerWithSound {
            finish()
        }
    }

    private fun initializeViews() {
        checkBoxes["pergunta1"] = findViewById(R.id.cbPergunta1)
        checkBoxes["pergunta2"] = findViewById(R.id.cbPergunta2)
        checkBoxes["pergunta3"] = findViewById(R.id.cbPergunta3)
        checkBoxes["pergunta4"] = findViewById(R.id.cbPergunta4)
        checkBoxes["pergunta5"] = findViewById(R.id.cbPergunta5)
        checkBoxes["pergunta6"] = findViewById(R.id.cbPergunta6)
        checkBoxes["pergunta7"] = findViewById(R.id.cbPergunta7)
        
        etObservacoes = findViewById(R.id.etObservacoes)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun loadExistingParQ() {
        val currentUser = auth.currentUser ?: return
        
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getParQByUserId(currentUser.uid)
                if (response.isSuccessful) {
                    existingParQ = response.body()
                    existingParQ?.let { parq ->
                        // Preencher checkboxes
                        parq.respostas?.forEach { (pergunta, resposta) ->
                            checkBoxes[pergunta]?.isChecked = resposta
                        }
                        // Preencher observações
                        etObservacoes.setText(parq.observacoes ?: "")
                    }
                }
                // Se não encontrar dados (404), apenas deixa o formulário em branco para novo preenchimento
            } catch (e: Exception) {
                // Silenciosamente ignora erros ao carregar (provavelmente não existe questionário ainda)
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun saveParQ() {
        val currentUser = auth.currentUser ?: return

        val respostas = mutableMapOf<String, Boolean>()
        checkBoxes.forEach { (key, checkBox) ->
            respostas[key] = checkBox.isChecked
        }

        val observacoes = etObservacoes.text.toString().trim()

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val parq = existingParQ ?: ParQ().apply {
                    userId = currentUser.uid
                }
                
                parq.respostas = respostas
                parq.observacoes = observacoes

                val response = if (existingParQ != null) {
                    RetrofitInstance.api.updateParQ(parq.parqId, parq)
                } else {
                    RetrofitInstance.api.createParQ(parq)
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@ParQActivity, "PAR-Q salvo com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ParQActivity, "Erro ao salvar PAR-Q", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ParQActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
