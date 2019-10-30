package main.com.deepinspire.sterlingbank;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;

import main.com.deepinspire.sterlingbank.http.Request;
import main.com.deepinspire.sterlingbank.http.Response;


import javax.net.ssl.HttpsURLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class StarlingClient {
    private String apiDomainUrl;
    private String punlicKeyUid;
    private String pathToKeys;
    private String accessToken;

    private KeyPair keyPair;

    /**
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public StarlingClient(String apiDomainUrl, String pathToKeys, String punlicKeyUid, String accessToken) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.apiDomainUrl = apiDomainUrl;
        this.pathToKeys = pathToKeys;
        this.punlicKeyUid = punlicKeyUid;
        this.accessToken = accessToken;

        this.keyPair = StarlingClient.LoadKeyPair(this.pathToKeys, "RSA");
    }

    /**
     * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts
     * Get an account holder's bank accounts
     */
    public Response getAccount() {
        return this.sendRequest(Request.GET, "/api/v2/accounts");
    }

    /**
     * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/identifiers
     * Get an account holder's bank account identifiers
     */
    public Response getAccountIdentifiers(String accountUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/identifiers", accountUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/balance
     * Get an account's balance
     * The balance of the account holder’s account is expressed in two ways – cleared balance and effective balance.
     * The cleared balance is the settled balance on the account and so does not include pending transactions.
     * The effective balance includes pending transactions, and is the value most commonly presented to the account holder.
     */
    public Response getAccountBalance(String accountUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/balance", accountUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/confirmation-of-funds
     * Get whether or not there are available funds for a requested amount
     */
    public Response getAccountConfirmationOfFunds(String accountUid, int targetAmountInMinorUnits) {
        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/confirmation-of-funds?targetAmountInMinorUnits=%s", accountUid, targetAmountInMinorUnits));
    }

    /**
     * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/statement/available-periods
     * Get list of statement periods which are available for an account
     */
    public Response getAccountStatementAvailablePeriods(String accountUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/statement/available-periods", accountUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/statement/download
     * Download a CSV statement for a given statement period
     */
    public Response getAccountStatementDownload(String accountUid, String yearMonth) {
        Map<String,String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("Accept", "application/pdf");
        requestHeaders.put("Content-Type", "application/json; charset=utf-8");

        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/statement/download?yearMonth=%s", accountUid, yearMonth), "", false, requestHeaders);
    }

    /**
     * https://developer.starlingbank.com/docs - Accounts - /api/v2/accounts/{accountUid}/statement/downloadForDateRange
     * Download a CSV statement for a given date range
     */
    public Response getAccountStatementDownloadForDateRange(String accountUid, String startDate, String endDate) {
        Map<String,String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("Accept", "application/pdf");
        requestHeaders.put("Content-Type", "application/json; charset=utf-8");

        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/statement/downloadForDateRange?start=%s&end=%s", accountUid, startDate, endDate), "", false, requestHeaders);
    }


    /**
     * https://developer.starlingbank.com/docs - Businesses - /api/v2/account-holder/business
     * Get a business account holder's details
     */
    public Response getAccountHolderBusiness() {
        return this.sendRequest(Request.GET, "/api/v2/account-holder/business");
    }

    /**
     * https://developer.starlingbank.com/docs - Businesses - /api/v2/account-holder/business/correspondence-address
     * Get a company's correspondence address. Will fallback to registered address if null
     */
    public Response getAccountHolderBusinessCorrespondenceAddress() {
        return this.sendRequest(Request.GET, "/api/v2/account-holder/business/correspondence-address");
    }

    /**
     * https://developer.starlingbank.com/docs - Businesses - /api/v2/account-holder/business/registered-address
     * Get a company's registered address
     */
    public Response getAccountHolderBusinessRegisteredAddress() {
        return this.sendRequest(Request.GET, "/api/v2/account-holder/business/registered-address");
    }


    /**
     * https://developer.starlingbank.com/docs - Individuals and Sole Traders - /api/v2/account-holder/business/registered-address
     * Get an individual account holder's details
     */
    public Response getAccountHolderIndividual() {
        return this.sendRequest(Request.GET, "/api/v2/account-holder/individual");
    }


    /**
     * https://developer.starlingbank.com/docs - Individuals and Sole Traders - /api/v2/account-holder/individual/email
     * Update an individual account holder's email address
     */
    public Response updateAccountHolderIndividualEmail(String payload) {
        return this.sendSignedRequest(Request.PUT, "/api/v2/account-holder/individual/email", payload);
    }


    /**
     * https://developer.starlingbank.com/docs - Joint Accounts - /api/v2/account-holder/joint
     * Get a joint account holder's details
     */
    public Response getAccountHolderJoint() {
        return this.sendRequest(Request.GET, "/api/v2/account-holder/joint");
    }


    /**
     * https://developer.starlingbank.com/docs - Spending Insights - /api/v2/accounts/{accountUid}/spending-insights/counter-party
     * Gets the spending insights for specified account, grouped by counter party
     * @param accountUid
     * @param year
     * @param month Available values : JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
     */
    public Response getAccountsSpendingInsightsCounterParty(String accountUid, String year, String month) {
        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/spending-insights/counter-party?year=%s&month=%s", accountUid, year, month));
    }

    /**
     * https://developer.starlingbank.com/docs - Spending Insights - /api/v2/accounts/{accountUid}/spending-insights/spending-category
     * Gets the spending insights for specified account, grouped by spending category
     * @param accountUid
     * @param year
     * @param month Available values : JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
     */
    public Response getAccountsSpendingInsightsSpendingCategory(String accountUid, String year, String month) {
        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/spending-insights/spending-category?year=%s&month=%s", accountUid, year, month));
    }

    /**
     * https://developer.starlingbank.com/docs - Spending Insights - /api/v2/accounts/{accountUid}/spending-insights/country
     * Gets the spending insights for specified account, grouped by country
     */
    public Response getAccountsSpendingInsightsSpendingCountry(String accountUid, String year, String month) {
        return this.sendRequest(Request.GET, String.format("/api/v2/accounts/%s/spending-insights/country?year=%s&month=%s", accountUid, year, month));
    }


    /**
     * https://developer.starlingbank.com/docs - Account Holders - /api/v2/account-holder
     * Get basic information about the account holder
     */
    public Response getAccountHolder() {
        return this.sendRequest(Request.GET, "/api/v2/account-holder");
    }

    /**
     * https://developer.starlingbank.com/docs - Account Holders - /api/v2/account-holder/name
     * Get name of the account holder
     */
    public Response getAccountHolderName() {
        return this.sendRequest(Request.GET, "/api/v2/account-holder/name");
    }


    /**
     * https://developer.starlingbank.com/docs - Addresses - /api/v2/addresses
     * Get the account holder's addresses
     */
    public Response getAddresses() {
        return this.sendRequest(Request.GET, "/api/v2/addresses");
    }

    /**
     * https://developer.starlingbank.com/docs - Addresses - /api/v2/addresses
     * Update the account holder's current address
     */
    public Response setAddresses(String addressUpdateRequest) {
        return this.sendSignedRequest(Request.POST, "/api/v2/addresses", addressUpdateRequest);
    }


    /**
     * https://developer.starlingbank.com/docs - Cards - /api/v2/cards
     * Get all the cards for an account holder
     */
    public Response getCards() {
        return this.sendRequest(Request.GET, "/api/v2/cards");
    }

    /**
     * https://developer.starlingbank.com/docs - Cards - /api/v2/cards/{cardUid}/controls/enabled
     * Update card lock
     */
    public Response updateCardLock(String cardUid, String enablingRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/cards/%s/controls/enabled", cardUid), enablingRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Cards - /api/v2/cards/{cardUid}/controls/atm-enabled
     * Update ATM withdrawal control
     */
    public Response updateCardControlATMWithdrawal(String cardUid, String enablingRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/cards/%s/controls/atm-enabled", cardUid), enablingRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Cards - /api/v2/cards/{cardUid}/controls/online-enabled
     * Update online payments control
     */
    public Response updateCardControlOnlinePayments(String cardUid, String enablingRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/cards/%s/controls/online-enabled", cardUid), enablingRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Cards - /api/v2/cards/{cardUid}/controls/mobile-wallet-enabled
     * Update mobile wallet payments control
     */
    public Response updateCardControlMobileWalletPayments(String cardUid, String enablingRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/cards/%s/controls/mobile-wallet-enabled", cardUid), enablingRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Cards - /api/v2/cards/{cardUid}/controls/gambling-enabled
     * Update gambling payments control
     */
    public Response updateCardControlGamblingPayments(String cardUid, String enablingRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/cards/%s/controls/gambling-enabled", cardUid), enablingRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Cards - /api/v2/cards/{cardUid}/controls/pos-enabled
     * Update card present payments (contactless and chip and pin) control
     */
    public Response updateCardControlPresentPayments(String cardUid, String enablingRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/cards/%s/controls/pos-enabled", cardUid), enablingRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Cards - /api/v2/cards/{cardUid}/controls/mag-stripe-enabled
     * Update magstripe payments control
     */
    public Response updateCardControlMagstripePayments(String cardUid, String enablingRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/cards/%s/controls/mag-stripe-enabled", cardUid), enablingRequest);
    }


    /**
     * https://developer.starlingbank.com/docs - Direct Debit Mandates - /api/v2/direct-debit/mandates
     * Get a list of direct debit mandates
     */
    public Response getListDirectDebitMandates() {
        return this.sendRequest(Request.GET, "/api/v2/direct-debit/mandates");
    }

    /**
     * https://developer.starlingbank.com/docs - Direct Debit Mandates - /api/v2/direct-debit/mandates/{mandateUid}
     * Get the direct debit mandate with the specified identifier
     */
    public Response getDirectDebitMandate(String mandateUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/direct-debit/mandates/%s", mandateUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Direct Debit Mandates - /api/v2/direct-debit/mandates/{mandateUid}
     * Cancel the direct debit mandate with the specified identifier
     */
    public Response cancelDirectDebitMandate(String mandateUid) {
        return this.sendRequest(Request.DELETE, String.format("/api/v2/direct-debit/mandates/%s", mandateUid));
    }


    /**
     * https://developer.starlingbank.com/docs - Transaction Feed - /api/v2/feed/account/{accountUid}/category/{categoryUid}/{feedItemUid}/spending-category
     * Changes the spending category for a transaction
     */
    public Response changesSpendingCategoryForTransaction(String accountUid, String categoryUid, String feedItemUid, String spendingCategory) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/feed/account/%s/category/%s/%s/spending-category", accountUid, categoryUid, feedItemUid), spendingCategory);
    }

    /**
     * https://developer.starlingbank.com/docs - Transaction Feed - /api/v2/feed/account/{accountUid}/category/{categoryUid}/{feedItemUid}
     * Fetches a single feed item
     */
    public Response fetchesSingleTransactionFeedItem(String accountUid, String categoryUid, String feedItemUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/feed/account/%s/category/%s/%s", accountUid, categoryUid, feedItemUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Transaction Feed - /api/v2/feed/account/{accountUid}/category/{categoryUid}
     * Get the account holder's feed items which were created or updated since a given date
     */
    public Response getTransactionFeedItems(String accountUid, String categoryUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/feed/account/%s/category/%s", accountUid, categoryUid));
    }

    public Response getTransactionFeedItems(String accountUid, String categoryUid, String changesSince) {
        return this.sendRequest(Request.GET, String.format("/api/v2/feed/account/%s/category/%s?changesSince=%s", accountUid, categoryUid, changesSince));
    }

    /**
     * https://developer.starlingbank.com/docs - Transaction Feed - /api/v2/feed/account/{accountUid}/category/{categoryUid}/transactions-between
     * Get the account holder's feed items which were created between two timestamps
     */
    public Response getTransactionFeedItemsBetweenTwoTimestamps(String accountUid, String categoryUid, String minTransactionTimestamp, String maxTransactionTimestamp) {
        return this.sendRequest(Request.GET, String.format("/api/v2/feed/account/%s/category/%s/transactions-betweens?minTransactionTimestamp=%s&maxTransactionTimestamp=%s", accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp));
    }

    /**
     * https://developer.starlingbank.com/docs - Transaction Feed - /api/v2/feed/account/{accountUid}/category/{categoryUid}/{feedItemUid}/attachments
     * Fetches the list of items attached to a feed item
     */
    public Response getListOfItemsAttachedToFeedItem(String accountUid, String categoryUid, String feedItemUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/feed/account/%s/category/%s/%s/attachments", accountUid, categoryUid, feedItemUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Transaction Feed - /api/v2/feed/account/{accountUid}/category/{categoryUid}/{feedItemUid}/attachments/{feedItemAttachmentUid}
     * Downloads the feed item attachment
     */
    public Response downloadFeedItemAttachment(String accountUid, String categoryUid, String feedItemUid, String feedItemAttachmentUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/feed/account/%s/category/%s/%s/attachments/%s", accountUid, categoryUid, feedItemUid, feedItemAttachmentUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Transaction Feed - /api/v2/feed/account/{accountUid}/category/{categoryUid}/{feedItemUid}/user-note
     * Changes the user-specified note attached to a transaction
     */
    public Response changesUserSpecifiedNoteAttachedTransaction(String accountUid, String categoryUid, String feedItemUid, String userNote) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/feed/account/%s/category/%s/%s/user-note", accountUid, categoryUid, feedItemUid), userNote);
    }


    /**
     * https://developer.starlingbank.com/docs - Feed Round-up - /api/v2/feed/account/{accountUid}/round-up
     * Returns the the round-up goal associated with an account if one has been created
     */
    public Response getRoundUpFeed(String accountUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/feed/account/%s/round-up", accountUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Feed Round-up - /api/v2/feed/account/{accountUid}/round-up
     * Activates transaction round-up and adds remainder to savings goal
     */
    public Response activateRoundUpGoal(String accountUid, String roundUpGoalPayload) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/feed/account/%s/round-up", accountUid), roundUpGoalPayload);
    }

    /**
     * https://developer.starlingbank.com/docs - Feed Round-up - /api/v2/feed/account/{accountUid}/round-up
     * Deletes the round-up goal associated with an account if one exists
     */
    public Response deletesRoundUpGoalAssociatedWithAccount(String accountUid) {
        return this.sendRequest(Request.DELETE, String.format("/api/v2/feed/account/%s/round-up", accountUid));
    }


    /**
     * https://developer.starlingbank.com/docs - API User Identities - /api/v2/identity/token
     * Get current token identity
     */
    public Response getIdentityToken() {
        return this.sendRequest(Request.GET, "/api/v2/identity/token");
    }

    /**
     * https://developer.starlingbank.com/docs - API User Identities - /api/v2/identity/individual
     * The individual who authorised the application
     */
    public Response getIdentityIndividual() {
        return this.sendRequest(Request.GET, "/api/v2/identity/individual");
    }

    /**
     * https://developer.starlingbank.com/docs - API User Identities - /api/v2/identity/logout
     * Logout the current individual
     */
    public Response logoutIdentity() {
        return this.sendRequest(Request.PUT, "/api/v2/identity/logout");
    }


    /**
     * https://developer.starlingbank.com/docs - KYC - /api/v2/kyc/result
     * Get KYC result for an account holder
     */
    public Response getKYCResult() {
        return this.sendRequest(Request.GET, "/api/v2/kyc/result");
    }


    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/payment-order/{paymentOrderUid}
     * Get a payment order
     */
    public Response getPaymentOrder(String paymentOrderUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/payments/local/payment-order/%s", paymentOrderUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/account/{accountUid}/category/{categoryUid}
     * Create domestic payment
     * Creates and sends an immediate payment within the UK under the Faster Payments Scheme
     * (foreign payment API to follow at a later time). The recipient of the payment must be a payee of the
     * account holder (new payees can be created using the ‘Payees’ endpoint) OR be defined in the
     * paymentRecipient member of the request.
     *
     * @param accountUid the account identifier of the recipient
     * @param categoryUid categories are subdivisions of a Starling account
     * @param payload payment data
     */
    public Response createDomesticPayment(String accountUid, String categoryUid, String payload) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/payments/local/account/%s/category/%s", accountUid, categoryUid), payload);
    }

    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/account/{accountUid}/category/{categoryUid}/standing-orders/{paymentOrderUid}
     * Get a standing order
     */
    public Response getStandingOrder(String accountUid, String categoryUid, String paymentOrderUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/payments/local/account/%s/category/%s/standing-orders/%s", accountUid, categoryUid, paymentOrderUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/account/{accountUid}/category/{categoryUid}/standing-orders/{paymentOrderUid}
     * Update a standing order
     */
    public Response updateStandingOrder(String accountUid, String categoryUid, String paymentOrderUid, String standingOrderRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/payments/local/account/%s/category/%s/standing-orders/%s", accountUid, categoryUid, paymentOrderUid), standingOrderRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/account/{accountUid}/category/{categoryUid}/standing-orders/{paymentOrderUid}
     * Cancel a standing order
     */
    public Response cancelStandingOrder(String accountUid, String categoryUid, String paymentOrderUid) {
        return this.sendSignedRequest(Request.DELETE, String.format("/api/v2/payments/local/account/%s/category/%s/standing-orders/%s", accountUid, categoryUid, paymentOrderUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/account/{accountUid}/category/{categoryUid}/standing-orders
     * List standing orders
     */
    public Response getListStandingOrders(String accountUid, String categoryUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/payments/local/account/%s/category/%s/standing-orders", accountUid, categoryUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/account/{accountUid}/category/{categoryUid}/standing-orders
     * Create a new standing order
     */
    public Response createStandingOrder(String accountUid, String categoryUid, String standingOrderRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/payments/local/account/%s/category/%s/standing-orders", accountUid, categoryUid), standingOrderRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/account/{accountUid}/category/{categoryUid}/standing-orders/{paymentOrderUid}/upcoming-payments
     * List next payment dates of a standing order
     */
    public Response listNextPaymentDatesOfStandingOrder(String accountUid, String categoryUid, String paymentOrderUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/payments/local/account/%s/category/%s/standing-orders/%s/upcoming-payments", accountUid, categoryUid, paymentOrderUid));
    }

    public Response listNextPaymentDatesOfStandingOrder(String accountUid, String categoryUid, String paymentOrderUid, int NumberOfNextPaymentDates) {
        return this.sendRequest(Request.GET, String.format("/api/v2/payments/local/account/%s/category/%s/standing-orders/%s/upcoming-payments?count=%s", accountUid, categoryUid, paymentOrderUid, NumberOfNextPaymentDates));
    }

    /**
     * https://developer.starlingbank.com/docs - Payments - /api/v2/payments/local/payment-order/{paymentOrderUid}/payments
     * Get the payments associated with a payment order
     */
    public Response getPaymentsAssociatedWithPaymentOrder(String paymentOrderUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/payments/local/payment-order/%s/payments", paymentOrderUid));
    }





    /**
     * https://developer.starlingbank.com/docs - Onboarding - /api/v2/onboarding
     * Get the onboarding status of the account
     */
    public Response getOnboardingStatus() {
        return this.sendSignedRequest(Request.GET, "/api/v2/onboarding");
    }

    /**
     * https://developer.starlingbank.com/docs - Onboarding - /api/v2/onboarding
     * Onboard an account
     */
    public Response onboardAccount(String onboardingRequest) {
        return this.sendSignedRequest(Request.PUT, "/api/v2/onboarding", onboardingRequest);
    }


    /**
     * https://developer.starlingbank.com/docs - Payees - /api/v2/payees/{payeeUid}/image
     * Serves the image for the payee
     */
    public Response getPayeesImage(String payeeUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/payees/%s/image", payeeUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Payees - /api/v2/payees
     * Get an account holder's payees
     */
    public Response getPayees() {
        return this.sendRequest(Request.GET, "/api/v2/payees");
    }

    /**
     * https://developer.starlingbank.com/docs - Payees - /api/v2/payees
     * Create a payee
     */
    public Response createPayee(String payee) {
        return this.sendSignedRequest(Request.PUT, "/api/v2/payees", payee);
    }

    /**
     * https://developer.starlingbank.com/docs - Payees - /api/v2/payees/{payeeUid}
     * Deletes a payee
     */
    public Response deletePayee(String payeeUid) {
        return this.sendRequest(Request.DELETE, String.format("/api/v2/payees/%s", payeeUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Payees - /api/v2/payees/{payeeUid}/account/{accountUid}
     * Delete a payee account
     */
    public Response deletePayeeAccount(String payeeUid, String accountUid) {
        return this.sendRequest(Request.DELETE, String.format("/api/v2/payees/%s/account/%s", payeeUid, accountUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Payees - /api/v2/payees/{payeeUid}/account
     * Create a payee account
     */
    public Response createPayeeAccount(String payeeUid, String payeeAccount) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/payees/%s/account", payeeUid), payeeAccount);
    }


    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}
     * Get a savings goal
     */
    public Response getSavingsGoal(String accountUid, String savingsGoalUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/account/%s/savings-goals/%s", accountUid, savingsGoalUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}
     * Update an existing goal
     */
    public Response updateSavingsGoal(String payeeUid, String savingsGoalUid, String savingsGoal) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/account/%s/savings-goals/%s", payeeUid, savingsGoalUid), savingsGoal);
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}
     * Delete a savings goal
     */
    public Response deleteSavingsGoal(String accountUid, String savingsGoalUid) {
        return this.sendRequest(Request.DELETE, String.format("/api/v2/account/%s/savings-goals/%s", accountUid, savingsGoalUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals
     * Get all savings goals
     */
    public Response getSavingsGoals(String accountUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/account/%s/savings-goals", accountUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals
     * Create a savings goal
     */
    public Response createSavingsGoal(String accountUid, String savingsGoal) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/account/%s/savings-goals", accountUid), savingsGoal);
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/photo
     * Get the photo associated with a savings goal
     */
    public Response getSavingsGoalPhoto(String accountUid, String savingsGoalUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/account/%s/savings-goals/%s/photo", accountUid, savingsGoalUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}
     * Add money into a savings goal
     */
    public Response addMoneyIntoSavingsGoal(String accountUid, String savingsGoalUid, String transferUid, String addMoneyRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/account/%s/savings-goals/%s/add-money/%s", accountUid, savingsGoalUid, transferUid), addMoneyRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/recurring-transfer
     * Withdraw money from a savings goal
     */
    public Response withdrawMoneyFromSavingsGoal(String accountUid, String savingsGoalUid, String transferUid, String withdrawMoneyRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/account/%s/savings-goals/%s/withdraw-money/%s", accountUid, savingsGoalUid, transferUid), withdrawMoneyRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/recurring-transfer
     * Get the recurring transfer of a savings goal
     */
    public Response getRecurringTransferOfSavingsGoal(String accountUid, String savingsGoalUid) {
        return this.sendRequest(Request.GET, String.format("/api/v2/account/%s/savings-goals/%s/recurring-transfer", accountUid, savingsGoalUid));
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/recurring-transfer
     * Create a recurring transfer into a savings goal
     */
    public Response createRecurringTransferOfSavingsGoal(String accountUid, String savingsGoalUid, String transferUid, String scheduledSavingsPaymentRequest) {
        return this.sendSignedRequest(Request.PUT, String.format("/api/v2/account/%s/savings-goals/%s/recurring-transfer", accountUid, savingsGoalUid, transferUid), scheduledSavingsPaymentRequest);
    }

    /**
     * https://developer.starlingbank.com/docs - Savings Goals - /api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/recurring-transfer
     * Delete the recurring transfer of a savings goal
     */
    public Response deleteRecurringTransferOfSavingsGoal(String accountUid, String savingsGoalUid) {
        return this.sendRequest(Request.DELETE, String.format("/api/v2/account/%s/savings-goals/%s/recurring-transfer", accountUid, savingsGoalUid));
    }











    /**
     * Send request to Starling Bank API
     * @param method
     * @param url
     */
    public Response sendRequest(String method, String url) {
        return this.sendRequest(method, url, "", false, new HashMap<>());
    }

    /**
     * Send request to Starling Bank API
     * @param method
     * @param url
     * @param payload
     */
    public Response sendRequest(String method, String url, String payload) {
        return this.sendRequest(method, url, payload, false, new HashMap<>());
    }

    /**
     * Send request to Starling Bank API
     * @param method
     * @param url
     * @param payload
     */
    public Response sendSignedRequest(String method, String url, String payload) {
        return this.sendRequest(method, url, payload, true, new HashMap<>());
    }

    /**
     * Send request to Starling Bank API
     * @param method
     * @param url
     */
    public Response sendSignedRequest(String method, String url) {
        return this.sendRequest(method, url, "", true, new HashMap<>());
    }

    /**
     * Send request to Starling Bank API
     * @param method
     * @param url
     * @param payload
     * @param isSigned
     */
    private Response sendRequest(String method, String url, String payload, Boolean isSigned, Map<String,String> requestHeaders) {

        if (null == requestHeaders) {
            requestHeaders = new HashMap<>();
        }

        if (requestHeaders.isEmpty()) {
            requestHeaders.put("Accept", "application/json");
            requestHeaders.put("Content-Type", "application/json; charset=utf-8");
        }

        try {
            URL request_url = new URL(this.apiDomainUrl + url);
            HttpsURLConnection con = (HttpsURLConnection) request_url.openConnection();
            System.out.println(">> URL: " + con.getURL());

            // PREVENT REDIRECT !!!
            con.setInstanceFollowRedirects(false);
            con.setDoOutput(true);
            con.setRequestMethod(method.toUpperCase());

            for(Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }

//            con.setRequestProperty("Accept", "application/pdf");

            if (isSigned) {
                System.out.println(">> StarlingClient - sendRequest - payload: " + payload);
                String date_time = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSXXX")).format(new Date());
                System.out.println(">> StarlingClient - sendRequest - date_time: " + date_time);

                String digest = GetDigest(payload);
                String signature = GetSignature(method.toLowerCase(), url, digest, date_time);

                con.setRequestProperty("Authorization", String.format(
                        "Bearer %s;Signature keyid=\"%s\",algorithm=\"rsa-sha512\",headers=\"(request-target) Date Digest\",signature=\"%s\"",
                        this.accessToken,
                        this.punlicKeyUid,
                        signature
                ));
                con.setRequestProperty("Date", date_time);
                con.setRequestProperty("Digest", digest);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
            } else {
                con.setRequestProperty("Authorization", String.format("Bearer %s", this.accessToken));
            }

            System.out.println(">> StarlingClient - sendRequest - status: " + con.getResponseCode());

            Reader streamReader = null;

            if (con.getResponseCode() < HttpsURLConnection.HTTP_BAD_REQUEST) {
                streamReader =  new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
            } else {
                try {
                    streamReader =  new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8);
                } catch (Exception ex) {
                    System.out.println(">> StarlingClient - sendRequest - ex: " + ex.toString());
                }
            }

            StringBuffer content = new StringBuffer();

            if (streamReader != null) {
                BufferedReader in = new BufferedReader(streamReader);
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
            }

            Response res = new Response(
                con.getResponseCode(),
                con.getResponseMessage(),
                con.getHeaderFields(),
                content.toString()
            );

            con.disconnect();
            return res;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new Response(
            500,
            "Internal Server Error",
            null,
            null
        );
    }

    /**
     * Generate signature
     *
     * @param request_method
     * @param request_url
     * @param digest
     * @param request_date
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    private String GetSignature(String request_method, String request_url, String digest, String request_date) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signatureToSign = "(request-target): " + request_method + " " + request_url + "\nDate: " + request_date + "\nDigest: " + digest;
//        System.out.println(">> StarlingClient - GetSignature - text2sign: " + signatureToSign);

        Signature instance = Signature.getInstance("SHA512withRSA"); // Could also use "SHA512withECDSA"
        instance.initSign(this.keyPair.getPrivate());
        instance.update(signatureToSign.getBytes());
        byte[] encodedSignedString = Base64.getEncoder().encode(instance.sign());
        String signature = new String(encodedSignedString);

//        System.out.println(">> StarlingClient - GetSignature - signature: " + signature);
        return signature;
    }

    /**
     * Generate digest
     * @param payload
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String GetDigest(String payload) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] byteArrPayload = payload.getBytes();
        String digestPayload = Base64.getEncoder().encodeToString(messageDigest.digest(byteArrPayload));

        //  System.out.println(">> StarlingClient - GetDigest - digest: " + digestPayload);
        return digestPayload;
    }

    /**
     *
     * @param algorithm
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static KeyPair generateKeyPair(String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        // keyPairGenerator.initialize(4096);
        keyPairGenerator.initialize(2048);
        KeyPair signingKey = keyPairGenerator.generateKeyPair();

        return signingKey;
    }

    /**
     * Load key pair
     * @param path
     * @param algorithm
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static KeyPair LoadKeyPair(String path, String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Read Public Key.
        String pathPublicKey = Paths.get(path).resolve("public.der").toString();
        System.out.println(">> LoadKeyPair - pathPublicKey: " + pathPublicKey);

        File filePublicKey = new File(pathPublicKey);
        FileInputStream fisPublicKey = new FileInputStream(pathPublicKey);
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fisPublicKey.read(encodedPublicKey);
        fisPublicKey.close();


        // Read Private Key.
        String pathPrivateKey = Paths.get(path).resolve("private.der").toString();
        System.out.println(">> LoadKeyPair - pathPrivateKey: " + pathPrivateKey);

        File filePrivateKey = new File(pathPrivateKey);
        FileInputStream fisPrivateKey = new FileInputStream(pathPrivateKey);
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fisPrivateKey.read(encodedPrivateKey);
        fisPrivateKey.close();


        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        KeyPair objKeyPair = new KeyPair(publicKey, privateKey);

        StarlingClient.dumpKeyPair(objKeyPair);

        return objKeyPair;
    }

    /**
     * Save key pair
     * @param path
     * @param keyPair
     * @throws IOException
     */
    public static void SaveKeyPair(String path, KeyPair keyPair) throws IOException {
        // Store Public Key.
        String pathPublicKey = Paths.get(path).resolve("public.der").toString();
        System.out.println(">> SaveKeyPair - pathPublicKey: " + pathPublicKey);

        PublicKey publicKey = keyPair.getPublic();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream fosPublic = new FileOutputStream(pathPublicKey);
        fosPublic.write(x509EncodedKeySpec.getEncoded());
        fosPublic.close();


        // Store Private Key.
        String pathPrivateKey = Paths.get(path).resolve("private.der").toString();
        System.out.println(">> SaveKeyPair - pathPrivateKey: " + pathPrivateKey);

        PrivateKey privateKey = keyPair.getPrivate();
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        FileOutputStream fosPrivate = new FileOutputStream(pathPrivateKey);
        fosPrivate.write(pkcs8EncodedKeySpec.getEncoded());
        fosPrivate.close();
    }

    /**
     * Dump key pair
     * @param keyPair
     */
    public static void dumpKeyPair(KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        String publicString = Base64.getEncoder().encodeToString(pub.getEncoded());
        System.out.println(">> dumpKeyPair - Public Key: " + publicString);
//        System.out.println("Public Key: " + getHexString(pub.getEncoded()));

        PrivateKey priv = keyPair.getPrivate();
        String privateString = Base64.getEncoder().encodeToString(priv.getEncoded());
        System.out.println(">> dumpKeyPair - Private Key: " + privateString);
//        System.out.println("Private Key: " + getHexString(priv.getEncoded()));
    }
}
