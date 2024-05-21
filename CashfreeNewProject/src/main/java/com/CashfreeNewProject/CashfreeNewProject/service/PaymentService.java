package com.CashfreeNewProject.CashfreeNewProject.service;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.CashfreeNewProject.CashfreeNewProject.model.PaymentRequest;
import com.CashfreeNewProject.CashfreeNewProject.util.StaticValues;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class PaymentService {

    public String createOrder(PaymentRequest paymentRequest) throws MalformedURLException {
        JSONObject params = new JSONObject();
        int min = 100000;
        int max = 999999;
        int a = (int) (Math.random() * (max - min + 1) + min);
        String order_id = "Rahul_" + a;
        params.put("order_amount", paymentRequest.getAmount());
        params.put("order_id", order_id);
        params.put("order_currency", "INR");

        JSONObject customer_details = new JSONObject();
        customer_details.put("customer_id", StaticValues.customer_id);
        customer_details.put("customer_name", paymentRequest.getName());
        customer_details.put("customer_email", paymentRequest.getEmail());
        customer_details.put("customer_phone", paymentRequest.getMobile());

        JSONObject order_meta = new JSONObject();
        order_meta.put("return_url", "http://localhost:8080/return?order_id=" + order_id);
        order_meta.put("notify_url", "https://b8af79f41056.eu.ngrok.io/webhook.php");
        params.put("order_meta", order_meta);
        params.put("customer_details", customer_details);

        try {
            URL url = new URL(StaticValues.Sandbox_Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-Client-Id", StaticValues.Client_Id);
            connection.setRequestProperty("X-Client-Secret", StaticValues.Client_Secret);
            connection.setRequestProperty("x-api-version", "2022-09-01");
            connection.setDoOutput(true);

            String post_data = params.toString();
            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.flush();
            requestWriter.close();

            StringBuilder responseData = new StringBuilder();
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = responseReader.readLine()) != null) {
                responseData.append(line);
            }
            responseReader.close();

            JSONObject jsonObject = new JSONObject(responseData.toString());
            String paymentSessionId = jsonObject.getString("payment_session_id");
            return paymentSessionId;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

