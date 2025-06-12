package com.company.pawpet.Controller;

import com.company.pawpet.Service.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/payment")
@CrossOrigin(origins = "http://localhost:3000")

public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/create-intent")
    public ResponseEntity<String> createIntent(@RequestParam Long amount) {
        try {
            String clientSecret = stripeService.createPaymentIntent(amount, "usd");
            return ResponseEntity.ok(clientSecret);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Stripe error: " + e.getMessage());
        }
    }
}

