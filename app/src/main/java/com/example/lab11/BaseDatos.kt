package com.example.lab11

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


val BD = "BDatos"


class BaseDatos(contexto: Context):
    SQLiteOpenHelper(contexto, BD, null, 1){


    override fun onCreate(db: SQLiteDatabase?){
        var sql = "CREATE TABLE Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(256), edad integer)\n"
        db?.execSQL(sql);
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion:Int){
        TODO("Not yet implemented")
    }


    fun insertarDatos (usuario: Usuario): String{
        val db= this.writableDatabase


        val contenedor = ContentValues();
        contenedor.put("nombre", usuario.nombre)
        contenedor.put("edad", usuario.edad)
        var resultado = db.insert("Usuario", null, contenedor)
        db.close()
        if (resultado == -1.toLong())
            return "Error al insertar registro ..."
        else
            return "Ok"


    }


    fun actualizarDatos(usuario: Usuario): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("nombre", usuario.nombre)
        contenedor.put("edad", usuario.edad)
        val resultado = db.update("Usuario", contenedor, "id = ?", arrayOf(usuario.id.toString()))
        db.close()
        return if (resultado == 1) {
            "Registro actualizado exitosamente"
        } else {
            "Error al actualizar registro"
        }
    }


    @SuppressLint ("Range")
    fun ListarDatos(): MutableList<Usuario>{
        var lista:MutableList<Usuario> = ArrayList()
        var db = this.readableDatabase
        var sql = "SELECT * from Usuario"
        var rs:Cursor = db.rawQuery(sql, null)


        while(rs.moveToNext()){
            var usu = Usuario()
            //usu.id = rs.getString(0).toInt()
            usu.id = rs.getString(rs.getColumnIndex("id")).toString().toInt()
            usu.nombre = rs.getString(1).toString()
            usu.edad= rs.getString(2).toInt()
            lista.add(usu)
        }
        rs.close()
        db.close()
        return lista
    }


    fun EliminarDatos(id: Int): String {
        val db = this.writableDatabase
        val resultado = db.delete("Usuario", "id=?", arrayOf(id.toString()))
        db.close()
        return if (resultado == -1)
            "Error al eliminar registro ..."
        else
            "Ok"
    }
}