package com.example.vegetableclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity - the single screen of the Vegetable Client app.
 *
 * Shows 5 tabs, one per task. Each tab has a form that sends
 * an HTTP request to the Tomcat servlet, which calls the RMI engine.
 *
 * DIARY: Day 5 - Built the Android UI.
 *        Used runOnUiThread() because OkHttp callbacks arrive on
 *        a background thread - we can only update Views on the UI thread.
 */
public class MainActivity extends AppCompatActivity {

    // ── Panels (one per task) ─────────────────────────────────────────────────
    private LinearLayout panelAdd, panelUpdate, panelDelete, panelCost, panelReceipt;

    // ── Tab buttons ───────────────────────────────────────────────────────────
    private Button tabAdd, tabUpdate, tabDelete, tabCost, tabReceipt;

    // ── Result display ────────────────────────────────────────────────────────
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Wire up panels
        panelAdd     = findViewById(R.id.panelAdd);
        panelUpdate  = findViewById(R.id.panelUpdate);
        panelDelete  = findViewById(R.id.panelDelete);
        panelCost    = findViewById(R.id.panelCost);
        panelReceipt = findViewById(R.id.panelReceipt);

        // Wire up tab buttons
        tabAdd     = findViewById(R.id.tabAdd);
        tabUpdate  = findViewById(R.id.tabUpdate);
        tabDelete  = findViewById(R.id.tabDelete);
        tabCost    = findViewById(R.id.tabCost);
        tabReceipt = findViewById(R.id.tabReceipt);

        resultText = findViewById(R.id.resultText);

        // Tab click listeners - show only selected panel
        tabAdd.setOnClickListener(v     -> showPanel(0));
        tabUpdate.setOnClickListener(v  -> showPanel(1));
        tabDelete.setOnClickListener(v  -> showPanel(2));
        tabCost.setOnClickListener(v    -> showPanel(3));
        tabReceipt.setOnClickListener(v -> showPanel(4));

        // Show Add panel by default
        showPanel(0);

        // Wire up action buttons
        setupAddButton();
        setupUpdateButton();
        setupDeleteButton();
        setupCostButton();
        setupReceiptButton();
    }

    // ── Show only the selected panel, hide others ─────────────────────────────

    private void showPanel(int index) {
        panelAdd.setVisibility(index == 0     ? View.VISIBLE : View.GONE);
        panelUpdate.setVisibility(index == 1  ? View.VISIBLE : View.GONE);
        panelDelete.setVisibility(index == 2  ? View.VISIBLE : View.GONE);
        panelCost.setVisibility(index == 3    ? View.VISIBLE : View.GONE);
        panelReceipt.setVisibility(index == 4 ? View.VISIBLE : View.GONE);

        // Highlight selected tab
        int blue = 0xFF2979FF, grey = 0xFF757575;
        tabAdd.setTextColor(index == 0     ? blue : grey);
        tabUpdate.setTextColor(index == 1  ? blue : grey);
        tabDelete.setTextColor(index == 2  ? blue : grey);
        tabCost.setTextColor(index == 3    ? blue : grey);
        tabReceipt.setTextColor(index == 4 ? blue : grey);

        resultText.setText("Results will appear here after each action...");
    }

    // ── Task 1: Add ───────────────────────────────────────────────────────────

    private void setupAddButton() {
        Button btn        = findViewById(R.id.btnAdd);
        EditText etId     = findViewById(R.id.addId);
        EditText etName   = findViewById(R.id.addName);
        EditText etPrice  = findViewById(R.id.addPrice);

        btn.setOnClickListener(v -> {
            String id    = etId.getText().toString().trim();
            String name  = etName.getText().toString().trim();
            String price = etPrice.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty() || price.isEmpty()) {
                showResult("Please fill in all fields.");
                return;
            }

            showResult("Sending request...");
            VegetableApiClient.addVegetable(id, name,
                    Double.parseDouble(price),
                    new VegetableApiClient.ApiCallback() {
                        @Override public void onSuccess(String result) { showResult(result); }
                        @Override public void onError(String error)    { showResult("ERROR: " + error); }
                    });
        });
    }

    // ── Task 2: Update ────────────────────────────────────────────────────────

    private void setupUpdateButton() {
        Button btn       = findViewById(R.id.btnUpdate);
        EditText etId    = findViewById(R.id.updateId);
        EditText etName  = findViewById(R.id.updateName);
        EditText etPrice = findViewById(R.id.updatePrice);

        btn.setOnClickListener(v -> {
            String id    = etId.getText().toString().trim();
            String name  = etName.getText().toString().trim();
            String price = etPrice.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty() || price.isEmpty()) {
                showResult("Please fill in all fields."); return;
            }
            showResult("Sending request...");
            VegetableApiClient.updateVegetable(id, name,
                    Double.parseDouble(price),
                    new VegetableApiClient.ApiCallback() {
                        @Override public void onSuccess(String r) { showResult(r); }
                        @Override public void onError(String e)   { showResult("ERROR: " + e); }
                    });
        });
    }

    // ── Task 3: Delete ────────────────────────────────────────────────────────

    private void setupDeleteButton() {
        Button btn    = findViewById(R.id.btnDelete);
        EditText etId = findViewById(R.id.deleteId);

        btn.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();
            if (id.isEmpty()) { showResult("Please enter a vegetable ID."); return; }
            showResult("Sending request...");
            VegetableApiClient.deleteVegetable(id,
                    new VegetableApiClient.ApiCallback() {
                        @Override public void onSuccess(String r) { showResult(r); }
                        @Override public void onError(String e)   { showResult("ERROR: " + e); }
                    });
        });
    }

    // ── Task 4: Calculate cost ────────────────────────────────────────────────

    private void setupCostButton() {
        Button btn     = findViewById(R.id.btnCost);
        EditText etId  = findViewById(R.id.costId);
        EditText etQty = findViewById(R.id.costQty);

        btn.setOnClickListener(v -> {
            String id  = etId.getText().toString().trim();
            String qty = etQty.getText().toString().trim();
            if (id.isEmpty() || qty.isEmpty()) {
                showResult("Please fill in all fields."); return;
            }
            showResult("Sending request...");
            VegetableApiClient.calculateCost(id, Double.parseDouble(qty),
                    new VegetableApiClient.ApiCallback() {
                        @Override public void onSuccess(String r) { showResult(r); }
                        @Override public void onError(String e)   { showResult("ERROR: " + e); }
                    });
        });
    }

    // ── Task 5: Receipt ───────────────────────────────────────────────────────

    private void setupReceiptButton() {
        Button btn         = findViewById(R.id.btnReceipt);
        EditText etCashier = findViewById(R.id.cashierName);
        EditText etItems   = findViewById(R.id.receiptItems);
        EditText etGiven   = findViewById(R.id.amountGiven);

        btn.setOnClickListener(v -> {
            String cashier = etCashier.getText().toString().trim();
            String items   = etItems.getText().toString().trim();
            String given   = etGiven.getText().toString().trim();

            if (cashier.isEmpty() || items.isEmpty() || given.isEmpty()) {
                showResult("Please fill in all fields."); return;
            }
            showResult("Sending request...");
            VegetableApiClient.printReceipt(items, Double.parseDouble(given), cashier,
                    new VegetableApiClient.ApiCallback() {
                        @Override public void onSuccess(String r) { showResult(r); }
                        @Override public void onError(String e)   { showResult("ERROR: " + e); }
                    });
        });
    }

    // ── Show result on UI thread ──────────────────────────────────────────────

    private void showResult(String message) {
        // OkHttp callbacks run on background thread - must switch to UI thread
        runOnUiThread(() -> resultText.setText(message));
    }
}
