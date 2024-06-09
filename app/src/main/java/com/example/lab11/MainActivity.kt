package com.example.lab11

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var db: BaseDatos
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = BaseDatos(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadUsers()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showUserDialog(null)
        }
    }

    private fun loadUsers() {
        userList = db.ListarDatos()
        adapter = UserAdapter(userList, ::onEditClicked, ::onDeleteClicked)
        recyclerView.adapter = adapter
    }

    private fun onEditClicked(user: Usuario) {
        showUserDialog(user)
    }

    private fun onDeleteClicked(user: Usuario) {
        val result = db.EliminarDatos(user.id)
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        loadUsers()
    }

    private fun showUserDialog(user: Usuario?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_user, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etAge = dialogView.findViewById<EditText>(R.id.etAge)

        val dialogTitle = if (user == null) "Añadir usuario" else "Editar usuario"
        if (user != null) {
            etName.setText(user.nombre)
            etAge.setText(user.edad.toString())
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val name = etName.text.toString()
                val age = etAge.text.toString().toIntOrNull() ?: 0
                if (user == null) {
                    val newUser = Usuario(name, age)
                    val result = db.insertarDatos(newUser)
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                } else {
                    user.nombre = name
                    user.edad = age
                    val result = db.actualizarDatos(user)
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                }
                loadUsers()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
}
