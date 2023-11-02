package com.creativem.catalogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


public class huerto extends AppCompatActivity {
    private ListView listViewSeleccionados;
    private ArrayList<String> seleccionadosList;
    private ArrayAdapter<String> seleccionadosAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huerto);

        // Obtener referencia de la ListView
        listViewSeleccionados = findViewById(R.id.listViewSeleccionados);

        // Obtener todas las selecciones guardadas en SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        seleccionadosList = new ArrayList<>();

        // Obtener el número total de elementos en SharedPreferences
        int totalElementos = sharedPreferences.getInt("totalElementos", 0);

        // Recorrer los elementos y agregarlos a la lista
        for (int i = 1; i <= totalElementos; i++) {
            String seleccion = sharedPreferences.getString("seleccion" + i, "");
            seleccionadosList.add(seleccion);
        }

        // Configurar el adaptador y mostrar las selecciones en la lista
        seleccionadosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, seleccionadosList);
        listViewSeleccionados.setAdapter(seleccionadosAdapter);

        // Configurar el CheckBox para eliminar elementos
        CheckBox checkBoxEliminar = findViewById(R.id.checkBoxEliminar);
        checkBoxEliminar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Habilitar el modo de eliminación
                    listViewSeleccionados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Obtener el elemento seleccionado en la posición
                            String elementoSeleccionado = seleccionadosList.get(position);

                            // Eliminar el elemento de la lista
                            seleccionadosList.remove(position);

                            // Actualizar los valores en SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("seleccion" + (position + 1));
                            editor.putInt("totalElementos", seleccionadosList.size());
                            for (int i = 0; i < seleccionadosList.size(); i++) {
                                editor.putString("seleccion" + (i + 1), seleccionadosList.get(i));
                            }
                            editor.apply();

                            // Notificar al adaptador que los datos han cambiado
                            seleccionadosAdapter.notifyDataSetChanged();

                            // Mostrar un mensaje indicando que el elemento ha sido eliminado
                            Toast.makeText(getApplicationContext(), "Elemento eliminado: " + elementoSeleccionado, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Deshabilitar el modo de eliminación
                    listViewSeleccionados.setOnItemClickListener(null);
                }
            }
        });
    }
}