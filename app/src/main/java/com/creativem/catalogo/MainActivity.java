package com.creativem.catalogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonNavigate;

    private Spinner spinnerProducts;
    private Button buttonAddToCart;
    private ListView listViewCart;
    private ArrayList<String> cartItems;
    private ArrayAdapter<String> cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNavigate = findViewById(R.id.buttonNavigate);


        // Obtener referencias de los elementos de la interfaz
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        spinnerProducts = findViewById(R.id.spinnerProducts);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        TextView textViewCart = findViewById(R.id.textViewCart);
        listViewCart = findViewById(R.id.listViewCart);

        // Configurar el título y los productos del catálogo
        textViewTitle.setText("Huerto");
        String[] products = {"Plantas en el Huerto","Zanahoria", "Ajo", "Remolacha", "Lechuga", "Acelga", "Albahaca", "Hierbabuena", "Hinojo", "Fastron", "Laurel"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, products);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(spinnerAdapter);

        // Configurar el carrito de compras
        cartItems = new ArrayList<>();
        cartAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartItems);
        listViewCart.setAdapter(cartAdapter);

        // Configurar el botón "Agregar al carrito"
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = spinnerProducts.getSelectedItem().toString();
                if (!cartItems.contains(selectedItem)) {
                    cartItems.add(selectedItem);
                    cartAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Planta Agregada Al huerto",
                            Toast.LENGTH_SHORT).show();

                    // Guardar la selección en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int totalElementos = sharedPreferences.getInt("totalElementos", 0);
                    editor.putString("seleccion" + (totalElementos + 1), selectedItem);
                    editor.putInt("totalElementos", totalElementos + 1);
                    editor.apply();
                } else {
                    Toast.makeText(getApplicationContext(), "Planta ya esta en el huerto",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, huerto.class);
                startActivity(intent);
            }
        });
    }
}