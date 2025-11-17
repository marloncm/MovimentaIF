package com.ifrs.movimentaif.ui.anamnese

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
import com.ifrs.movimentaif.model.Anamnese
import com.ifrs.movimentaif.utils.setOnClickListenerWithSound
import kotlinx.coroutines.launch

class AnamneseActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val checkBoxes = mutableMapOf<String, CheckBox>()
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var progressBar: View
    
    private var existingAnamnese: Anamnese? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anamnese)

        auth = FirebaseAuth.getInstance()
        
        supportActionBar?.title = "Anamnese"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializeViews()
        loadExistingAnamnese()

        btnSave.setOnClickListenerWithSound {
            saveAnamnese()
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
        
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun loadExistingAnamnese() {
        val currentUser = auth.currentUser ?: return
        
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getAnamneseByUserId(currentUser.uid)
                if (response.isSuccessful) {
                    existingAnamnese = response.body()
                    existingAnamnese?.let { anamnese ->
                        // Preencher checkboxes
                        anamnese.respostas?.forEach { (pergunta, resposta) ->
                            checkBoxes[pergunta]?.isChecked = resposta
                        }
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

    private fun saveAnamnese() {
        val currentUser = auth.currentUser ?: return

        val respostas = mutableMapOf<String, Boolean>()
        checkBoxes.forEach { (key, checkBox) ->
            respostas[key] = checkBox.isChecked
        }

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val anamnese = existingAnamnese ?: Anamnese().apply {
                    userId = currentUser.uid
                }
                
                anamnese.respostas = respostas

                val response = if (existingAnamnese != null) {
                    RetrofitInstance.api.updateAnamnese(anamnese.anamneseId, anamnese)
                } else {
                    RetrofitInstance.api.createAnamnese(anamnese)
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@AnamneseActivity, "Anamnese salva com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AnamneseActivity, "Erro ao salvar Anamnese", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AnamneseActivity, "Erro de conexão", Toast.LENGTH_SHORT).show()
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
