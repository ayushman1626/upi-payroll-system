//package com.upipaysystem.payroll.service;
//
//import com.upipaysystem.payroll.exceptions.RazorpayServiceException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//import java.util.List;
//
//
//@Service
//public class RazorpayService {
//
//    private final RestTemplate restTemplate;
//    private final HttpHeaders headers;
//
//    public RazorpayService(
//            @Value("${razorpay.api.key}") String apiKey,
//            @Value("${razorpay.api.secret}") String apiSecret
//    ) {
//        String creds = Base64.getEncoder().encodeToString((apiKey + ":" + apiSecret).getBytes(StandardCharsets.UTF_8));
//
//        this.headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Basic " + creds);
//
//        this.restTemplate = new RestTemplate();
//        // Optional: customize error handler or timeouts here
//    }
//
//    public String createContact(String name, String email, String referenceId) {
//        // 1) Prepare payload
//        JSONObject body = new JSONObject()
//                .put("name", name)
//                .put("email", email)
//                .put("type", "employee")
//                .put("reference_id", referenceId);
//
//        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
//        String url = "https://api.razorpay.com/v1/contacts";
//
//        try {
//            // 2) Call Razorpay API
//            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//            // 3) Parse response JSON
//            JSONObject respJson = new JSONObject(response.getBody());
//            System.out.println(response.getBody());
//            String contactId = respJson.optString("id", null);
//
//            // 4) Validate output
//            if (contactId == null) {
//                throw new RazorpayServiceException("Missing contact_id in Razorpay response");
//            }
//            return contactId;
//
//        } catch (HttpClientErrorException | HttpServerErrorException ex) {
//            // 4xx or 5xx from Razorpay
//            String errorMsg = String.format(
//                    "Razorpay createContact failed (%d): %s",
//                    ex.getStatusCode().value(),
//                    ex.getResponseBodyAsString()
//            );
//            //log.error(errorMsg, ex);
//            System.out.println(errorMsg);
//            throw new RazorpayServiceException(errorMsg, ex);
//
//        } catch (RestClientException ex) {
//            // Network or other I/O error
//            String errorMsg = "Razorpay createContact I/O error: " + ex.getMessage();
//            System.out.println(errorMsg);
//            //log.error(errorMsg, ex);
//            throw new RazorpayServiceException(errorMsg, ex);
//        }
//    }
//
//    public String createFundAccountWithUpi(String contactId, String upiId) {
//        // 1) Prepare payload
//        JSONObject body = new JSONObject()
//                .put("contact_id", contactId)
//                .put("account_type", "vpa")
//                .put("vpa", new JSONObject().put("address", upiId));
//
//        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
//        String url = "https://api.razorpay.com/v1/fund_accounts";
//
//        try {
//            // 2) Call Razorpay API
//            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//            // 3) Parse response JSON
//            JSONObject respJson = new JSONObject(response.getBody());
//            String fundAccountId = respJson.optString("id", null);
//            System.out.println(response.getBody());
//
//            // 4) Validate output
//            if (fundAccountId == null) {
//                throw new RazorpayServiceException(
//                        "RazorpayService: createFundAccountWithUpi returned no 'id'; response=" + respJson);
//            }
//            return fundAccountId;
//
//        } catch (HttpClientErrorException | HttpServerErrorException ex) {
//            String errorMsg = String.format(
//                    "RazorpayService: createFundAccountWithUpi failed for contactId='%s' → HTTP %d: %s",
//                    contactId, ex.getStatusCode().value(), ex.getResponseBodyAsString());
//            //log.error(errorMsg, ex);
//            System.out.println(errorMsg);
//            throw new RazorpayServiceException(errorMsg, ex);
//
//        } catch (RestClientException ex) {
//            String errorMsg = String.format(
//                    "RazorpayService: createFundAccountWithUpi I/O error for contactId='%s' → %s",
//                    contactId, ex.getMessage());
//            System.out.println(errorMsg);
//            //log.error(errorMsg, ex);
//            throw new RazorpayServiceException(errorMsg, ex);
//        }
//    }
////
////    public String createFundAccountWithBank(String contactId, String accountNumber, String ifsc) throws RazorpayException {
////        JSONObject fundAccountRequest = new JSONObject();
////        fundAccountRequest.put("contact_id", contactId);
////        fundAccountRequest.put("account_type", "bank_account");
////
////        JSONObject bankAccount = new JSONObject();
////        bankAccount.put("account_number", accountNumber);
////        bankAccount.put("ifsc", ifsc);
////        fundAccountRequest.put("bank_account", bankAccount);
////
////        FundAccount fundAccount = razorpayClient.FundAccount.create(fundAccountRequest);
////        return fundAccount.get("id");
////    }
////
////    public String createPayout(String fundAccountId, int amount, String currency, String purpose, String narration) throws RazorpayException {
////        JSONObject payoutRequest = new JSONObject();
////        payoutRequest.put("account_number", "your_virtual_account_number"); // your company's account number
////        payoutRequest.put("fund_account_id", fundAccountId);
////        payoutRequest.put("amount", amount);
////        payoutRequest.put("currency", currency);
////        payoutRequest.put("mode", "UPI");
////        payoutRequest.put("purpose", purpose);
////        payoutRequest.put("queue_if_low_balance", true);
////        payoutRequest.put("narration", narration);
////
////        Payout payout = razorpayClient.Payout.create(payoutRequest);
////        return payout.get("id");
////    }
////
////    public void deleteContact(String contactId) {
////        try {
////            razorpayClient.Contact.fetch(contactId).delete();
////        } catch (Exception e) {
////            System.err.println("Failed to delete contact: " + e.getMessage());
////        }
////    }
////
////    public FundAccount fetchFundAccount(String fundAccountId) throws RazorpayException {
////        return razorpayClient.FundAccount.fetch(fundAccountId);
////    }
////
////    public Contact fetchContact(String contactId) throws RazorpayException {
////        return razorpayClient.Contact.fetch(contactId);
////    }
////
////    public List<Contact> listContacts() throws RazorpayException {
////        return razorpayClient.Contact.all();
////    }
////
////    public List<FundAccount> listFundAccounts() throws RazorpayException {
////        return razorpayClient.FundAccount.all();
////    }
//}
//
