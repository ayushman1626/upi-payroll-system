//package com.upipaysystem.payroll.service;
//
//import com.razorpay.Contact;
//import com.razorpay.Entity;
//import com.razorpay.FundAccount;
//import com.razorpay.Payout;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import org.json.JSONObject;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class RazorpayService {
//
//    private final RazorpayClient razorpayClient;
//
//    public RazorpayService() throws RazorpayException {
//        this.razorpayClient = new RazorpayClient("your_key_id", "your_secret_key");
//    }
//
//    public String createContact(String name, String email, String referenceId) throws RazorpayException {
//        try {
//            JSONObject body = new JSONObject()
//                    .put("name", name)
//                    .put("email", email)
//                    .put("type", "employee")
//                    .put("reference_id", referenceId);
//
//            Entity resp = razorpayClient.request
//                    .post("/v1/contacts", body);
//
//            String id = resp.get("id");
//            if (id == null) {
//                throw new RazorpayServiceException("Missing contact_id in response");
//            }
//            return id;
//
//        } catch (RazorpayException e) {
//            log.error("Razorpay createContact failed for ref={} – {}", referenceId, e.getMessage(), e);
//            // wrap in your own unchecked exception
//            throw new RazorpayServiceException("Failed to create Razorpay contact", e);
//        }
//    }
//
//    public String createFundAccountWithUpi(String contactId, String upiId) throws RazorpayException {
//        JSONObject fundAccountRequest = new JSONObject();
//        fundAccountRequest.put("contact_id", contactId);
//        fundAccountRequest.put("account_type", "vpa");
//
//        JSONObject vpa = new JSONObject();
//        vpa.put("address", upiId);
//        fundAccountRequest.put("vpa", vpa);
//
//        FundAccount fundAccount = razorpayClient.FundAccount.create(fundAccountRequest);
//        return fundAccount.get("id");
//    }
//
//    public String createFundAccountWithBank(String contactId, String accountNumber, String ifsc) throws RazorpayException {
//        JSONObject fundAccountRequest = new JSONObject();
//        fundAccountRequest.put("contact_id", contactId);
//        fundAccountRequest.put("account_type", "bank_account");
//
//        JSONObject bankAccount = new JSONObject();
//        bankAccount.put("account_number", accountNumber);
//        bankAccount.put("ifsc", ifsc);
//        fundAccountRequest.put("bank_account", bankAccount);
//
//        FundAccount fundAccount = razorpayClient.FundAccount.create(fundAccountRequest);
//        return fundAccount.get("id");
//    }
//
//    public String createPayout(String fundAccountId, int amount, String currency, String purpose, String narration) throws RazorpayException {
//        JSONObject payoutRequest = new JSONObject();
//        payoutRequest.put("account_number", "your_virtual_account_number"); // your company's account number
//        payoutRequest.put("fund_account_id", fundAccountId);
//        payoutRequest.put("amount", amount);
//        payoutRequest.put("currency", currency);
//        payoutRequest.put("mode", "UPI");
//        payoutRequest.put("purpose", purpose);
//        payoutRequest.put("queue_if_low_balance", true);
//        payoutRequest.put("narration", narration);
//
//        Payout payout = razorpayClient.Payout.create(payoutRequest);
//        return payout.get("id");
//    }
//
//    public void deleteContact(String contactId) {
//        try {
//            razorpayClient.Contact.fetch(contactId).delete();
//        } catch (Exception e) {
//            System.err.println("Failed to delete contact: " + e.getMessage());
//        }
//    }
//
//    public FundAccount fetchFundAccount(String fundAccountId) throws RazorpayException {
//        return razorpayClient.FundAccount.fetch(fundAccountId);
//    }
//
//    public Contact fetchContact(String contactId) throws RazorpayException {
//        return razorpayClient.Contact.fetch(contactId);
//    }
//
//    public List<Contact> listContacts() throws RazorpayException {
//        return razorpayClient.Contact.all();
//    }
//
//    public List<FundAccount> listFundAccounts() throws RazorpayException {
//        return razorpayClient.FundAccount.all();
//    }
//}
//
