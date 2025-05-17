package com.company.pawpet.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_51RPcbvP3riZKn9o2VUotXjDuyHgbgwA4zsm7qPCqTnikZkpN3F2V5U7T5UXN6glZPZncNY3rcJF29fWvBxjOKEAK00mVSWvM1I";
    }

    public String createPaymentIntent(Long amount, String currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount); // e.g., 5000 = $50.00
        params.put("currency", currency);
        params.put("automatic_payment_methods", Map.of("enabled", true));

        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();
    }
}

