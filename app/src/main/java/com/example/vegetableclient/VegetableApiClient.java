package com.example.vegetableclient;

import okhttp3.*;
import java.io.IOException;

/**
 * VegetableApiClient - sends HTTP POST requests to each servlet endpoint.
 *
 * IMPORTANT: Change BASE_URL to match where your Tomcat server is running.
 *
 * If testing on Android EMULATOR  → use "http://10.0.2.2:8080/VegetableRMI"
 * If testing on REAL PHONE        → use your computer's local IP e.g.
 *                                    "http://192.168.1.100:8080/VegetableRMI"
 *
 * To find your computer's IP:
 *   Mac:     System Preferences → Network → Wi-Fi → IP Address
 *   Windows: cmd → ipconfig → look for IPv4 Address
 *
 * DIARY: Day 5 - Created the Android HTTP client.
 *        10.0.2.2 is Android emulator's special alias for the host machine's
 *        localhost. On a real device you must use the actual IP address.
 */
public class VegetableApiClient {

    // ── CHANGE THIS to match your setup ──────────────────────────────────────
    private static final String BASE_URL = "http://192.168.1.101:1099/VegetableRMI";
    //                                              ^^^^^^^^
    //                                              Use 10.0.2.2 for emulator
    //                                              Use 192.168.x.x for real phone

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");

    // ── Callback interface so results come back on UI thread ─────────────────

    public interface ApiCallback {
        void onSuccess(String result);
        void onError(String error);
    }

    // ── Task 1: Add vegetable ─────────────────────────────────────────────────

    public static void addVegetable(String id, String name, double price,
                                    ApiCallback callback) {
        String body = "id=" + id + "&name=" + name + "&price=" + price;
        postAsync("/vegetable/add", body, callback);
    }

    // ── Task 2: Update vegetable ──────────────────────────────────────────────

    public static void updateVegetable(String id, String name, double price,
                                       ApiCallback callback) {
        String body = "id=" + id + "&name=" + name + "&price=" + price;
        postAsync("/vegetable/update", body, callback);
    }

    // ── Task 3: Delete vegetable ──────────────────────────────────────────────

    public static void deleteVegetable(String id, ApiCallback callback) {
        String body = "id=" + id;
        postAsync("/vegetable/delete", body, callback);
    }

    // ── Task 4: Calculate cost ────────────────────────────────────────────────

    public static void calculateCost(String id, double quantity,
                                     ApiCallback callback) {
        String body = "id=" + id + "&quantity=" + quantity;
        postAsync("/vegetable/cost", body, callback);
    }

    // ── Task 5: Print receipt ─────────────────────────────────────────────────

    /**
     * @param items    format: "V001:2.5,V002:1.0"  (id:qty pairs)
     * @param given    cash amount given by customer
     * @param cashier  name of logged-in cashier
     */
    public static void printReceipt(String items, double given,
                                    String cashier, ApiCallback callback) {
        String body = "items=" + items
                + "&amountGiven=" + given
                + "&cashier=" + cashier;
        postAsync("/vegetable/receipt", body, callback);
    }

    // ── Internal: async POST request ──────────────────────────────────────────

    private static void postAsync(String endpoint, String bodyStr,
                                  ApiCallback callback) {
        RequestBody body = RequestBody.create(bodyStr, FORM);
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();

        // OkHttp runs this on a background thread automatically
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Connection failed: " + e.getMessage()
                        + "\nIs Tomcat running? Is the IP correct?");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body() != null
                        ? response.body().string()
                        : "Empty response";
                if (response.isSuccessful()) {
                    callback.onSuccess(result);
                } else {
                    callback.onError("Server error " + response.code() + ": " + result);
                }
            }
        });
    }
}
