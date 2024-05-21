package com.CashfreeNewProject.CashfreeNewProject.controller;



import com.CashfreeNewProject.CashfreeNewProject.model.PaymentRequest;
import com.CashfreeNewProject.CashfreeNewProject.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/HelloCashfree")
    public String showForm(Model model) {
        model.addAttribute("paymentRequest", new PaymentRequest());
        return "form";
    }

    @PostMapping("/processPayment")
    public String processPayment(@ModelAttribute PaymentRequest paymentRequest, Model model) {
        try {
            String paymentSessionId = paymentService.createOrder(paymentRequest);
            model.addAttribute("paymentSessionId", paymentSessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "result";
    }

    @GetMapping("/return")
    public String returnPage(@ModelAttribute("order_id") String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "return";
    }
}

