package tests.com.deepinspire.starlingbank;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.*;

import main.com.deepinspire.starlingbank.StarlingClient;
import main.com.deepinspire.starlingbank.http.Response;
import tests.com.deepinspire.starlingbank.accounts.AccountAssert;

import javax.net.ssl.*;


public class StarlingApiV2WrapperTests {
    private static String accountUid;
    private static String categoryUid;

    private static StarlingClient client;

    @Before
    public void setUp() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("./cfg/sandbox.properties"));

            // SET HTTPS PROXY
            if (Boolean.parseBoolean(properties.getProperty("starling.proxy.enabled"))) {
                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
                };

                // Install the all-trusting trust manager
                SSLContext sc = null;
                try {
                    sc = SSLContext.getInstance("SSL");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                try {
                    sc.init(null, trustAllCerts, new SecureRandom());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };

                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                System.setProperty("https.proxyHost", properties.getProperty("starling.proxy.host"));
                System.setProperty("https.proxyPort", properties.getProperty("starling.proxy.port"));
            }

            this.client = new StarlingClient(
                properties.getProperty("starling.domain-url"),
                properties.getProperty("starling.signing.keys-dir-path"),
                properties.getProperty("starling.signing.public-key-uid"),
                properties.getProperty("starling.access.token")
            );

            this.accountUid = properties.getProperty("starling.account.account-uid");
            this.categoryUid = properties.getProperty("starling.account.category-uid");
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param msg
     */
    public void failOnException(String msg) {
        Assert.fail("Exception: " + msg);
    }


    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - ACCOUNTS
     * -------------------------------------------------------------------------
     */

    /**
     * Get an account holder's bank accounts
     */
    @Test
    public void testGetAccount() {
        try {
            client.getAccount().forEach((account) -> {
                AccountAssert.assertThat(account).isValid();
            });
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get an account holder's bank account identifiers
     */
    @Test
    public void testGetAccountIdentifiers() {
        try {
            Response response = client.getAccountIdentifiers(accountUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get an account's balance
     */
    @Test
    public void testGetAccountBalance() {
        try {
            Response response = client.getAccountBalance(accountUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get whether or not there are available funds for a requested amount
     */
    @Test
    public void testGetAccountConfirmationOfFunds() {
        try {
            int targetAmountInMinorUnits = 100;
            Response response = client.getAccountConfirmationOfFunds(accountUid, targetAmountInMinorUnits);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get list of statement periods which are available for an account
     */
    @Test
    public void testGetAccountStatementAvailablePeriods() {
        try {
            Response response = client.getAccountStatementAvailablePeriods(accountUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Download a CSV statement for a given statement period
     */
    @Test
    public void testGetAccountStatementDownload() {
        try {
            // yyyy-MM
            String yearMonth = "2019-09";

            // TODO: Files Download
            Response response = client.getAccountStatementDownload(accountUid, yearMonth);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Download a CSV statement for a given date range
     */
    @Test
    public void testGetAccountStatementDownloadForDateRange() {
        try {
            // yyyy-MM-dd
            String startDate = "2019-09-01";

            // yyyy-MM-dd
            String endDate = "2019-09-25";

            // TODO: Files Download
            Response response = client.getAccountStatementDownloadForDateRange(accountUid, startDate, endDate);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - BUSINESSES
     * -------------------------------------------------------------------------
     */

    /**
     * Get a business account holder's details
     */
    @Test
    public void testGetAccountHolderBusiness() {
        try {
            Response response = client.getAccountHolderBusiness();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get a company's correspondence address. Will fallback to registered address if null
     */
    @Test
    public void testGetAccountHolderBusinessCorrespondenceAddress() {
        try {
            Response response = client.getAccountHolderBusinessCorrespondenceAddress();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get a company's registered address
     */
    @Test
    public void testGetAccountHolderBusinessRegisteredAddress() {
        try {
            Response response = client.getAccountHolderBusinessRegisteredAddress();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - INDIVIDUALS
     * -------------------------------------------------------------------------
     */

    /**
     * Get an individual account holder's details
     */
    @Test
    public void testGetAccountHolderIndividual() {
        try {
            // TODO: {"errors":[{"message":"endpoint called with non-individual account"}],"success":false}
            Response response = client.getAccountHolderIndividual();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update an individual account holder's email address
     */
    @Test
    public void testUpdateAccountHolderIndividualEmail() {
        try {
            // TODO: {"errors":[{"message":"endpoint called with non-individual account"}],"success":false}
            String payload = "{\"email\": \"email@example.com\"}";
            Response response = client.updateAccountHolderIndividualEmail(payload);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }


    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - JOINT ACCOUNTS
     * -------------------------------------------------------------------------
     */

    /**
     * Get a joint account holder's details
     */
    @Test
    public void testGetAccountHolderJoint() {
        try {
            // TODO: {"errors":[{"message":"endpoint called with non-joint account"}],"success":false}
            Response response = client.getAccountHolderJoint();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }


    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - SPENDING INSIGHTS
     * -------------------------------------------------------------------------
     */

    /**
     * Gets the spending insights for specified account, grouped by counter party
     * Month available values: JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
     */
    @Test
    public void testGetAccountsSpendingInsightsCounterParty() {
        try {
            Response response = client.getAccountsSpendingInsightsCounterParty(accountUid, "2019", "OCTOBER");

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Gets the spending insights for specified account, grouped by spending category
     * Month available values: JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
     */
    @Test
    public void testGetAccountsSpendingInsightsSpendingCategory() {
        try {
            Response response = client.getAccountsSpendingInsightsSpendingCategory(accountUid, "2019", "OCTOBER");

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Gets the spending insights for specified account, grouped by country
     * Month available values: JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
     */
    @Test
    public void testGetAccountsSpendingInsightsSpendingCountry() {
        try {
            Response response = client.getAccountsSpendingInsightsSpendingCountry(accountUid, "2019", "OCTOBER");

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }


    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - ACCOUNT HOLDERS
     * -------------------------------------------------------------------------
     */

    /**
     * Get basic information about the account holder
     */
    @Test
    public void testGetAccountHolder() {
        try {
            Response response = client.getAccountHolder();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get name of the account holder
     */
    @Test
    public void testGetAccountHolderName() {
        try {
            Response response = client.getAccountHolderName();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - ADDRESSES
     * -------------------------------------------------------------------------
     */

    /**
     * Get the account holder's addresses
     */
    @Test
    public void testGetAddresses() {
        try {
            Response response = client.getAddresses();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update the account holder's current address
     */
    @Test
    public void testSetAddresses() {
        try {
            /*
                {
                    "current": {
                        "line1": "24 Malvern Close",
                        "line2": "Royton",
                        "postTown": "OLDHAM",
                        "postCode": "OL2 5HH",
                        "countryCode": "GB"
                    },
                    "previous": []
                }

                {"current":{"line1":"24 Malvern Close","line2":"Royton","postTown":"OLDHAM","postCode":"OL2 5HH","countryCode":"GB"},"previous":[]}


                {
                  "line1": "1A Admiralty Arch",
                  "line2": "The Mall",
                  "line3": "City of Westminster",
                  "postTown": "London",
                  "postCode": "SW1A 2WH",
                  "countryCode": "GB",
                  "udprn": "23748063",
                  "umprn": "923827402",
                  "from": "2018-01-01"
                }

                {"line1": "1A Admiralty Arch","line2": "The Mall","line3": "City of Westminster","postTown": "London","postCode": "SW1A 2WH","countryCode": "GB","udprn": "23748063","umprn": "923827402","from": "2018-01-01"}

            */

            // TODO: 500 Server Error
            String payload = "{\n" +
            "  \"line1\": \"1A Admiralty Arch\",\n" +
            "  \"line2\": \"The Mall\",\n" +
            "  \"line3\": \"City of Westminster\",\n" +
            "  \"postTown\": \"London\",\n" +
            "  \"postCode\": \"SW1A 2WH\",\n" +
            "  \"countryCode\": \"GB\",\n" +
            "  \"udprn\": \"23748063\",\n" +
            "  \"umprn\": \"923827402\",\n" +
            "  \"from\": \"2018-01-01\"\n" +
            "}";
            Response response = client.setAddresses(payload);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }


    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - CARDS
     * -------------------------------------------------------------------------
     */

    /**
     * Get all the cards for an account holder
     */
    @Test
    public void testGetCards() {
        try {
            Response response = client.getCards();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get all the cards for an account holder
     */
    @Test
    public void testUpdateCardLock() {
        try {
            String cardUid = "f93c134d-973d-4366-bc8c-c0b896dc6911";
            String enablingRequest = "{\"enabled\": true}";
            Response response = client.updateCardLock(cardUid, enablingRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update ATM withdrawal control
     */
    @Test
    public void testUpdateCardControlATMWithdrawal() {
        try {
            String cardUid = "f93c134d-973d-4366-bc8c-c0b896dc6911";
            String enablingRequest = "{\"enabled\": true}";
            Response response = client.updateCardControlATMWithdrawal(cardUid, enablingRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update online payments control
     */
    @Test
    public void testUpdateCardControlOnlinePayments() {
        try {
            String cardUid = "f93c134d-973d-4366-bc8c-c0b896dc6911";
            String enablingRequest = "{\"enabled\": true}";
            Response response = client.updateCardControlOnlinePayments(cardUid, enablingRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update mobile wallet payments control
     */
    @Test
    public void testUpdateCardControlMobileWalletPayments() {
        try {
            String cardUid = "f93c134d-973d-4366-bc8c-c0b896dc6911";
            String enablingRequest = "{\"enabled\": true}";
            Response response = client.updateCardControlMobileWalletPayments(cardUid, enablingRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update gambling payments control
     */
    @Test
    public void testUpdateCardControlGamblingPayments() {
        try {
            String cardUid = "f93c134d-973d-4366-bc8c-c0b896dc6911";
            String enablingRequest = "{\"enabled\": true}";
            Response response = client.updateCardControlGamblingPayments(cardUid, enablingRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update card present payments (contactless and chip and pin) control
     */
    @Test
    public void testUpdateCardControlPresentPayments() {
        try {
            String cardUid = "f93c134d-973d-4366-bc8c-c0b896dc6911";
            String payload = "{\"enabled\": true}";
            Response response = client.updateCardControlPresentPayments(cardUid, payload);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update magstripe payments control
     */
    @Test
    public void testUpdateCardControlMagstripePayments() {
        try {
            String cardUid = "f93c134d-973d-4366-bc8c-c0b896dc6911";
            String payload = "{\"enabled\": true}";
            Response response = client.updateCardControlMagstripePayments(cardUid, payload);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - DIRECT DEBIT MANDATES
     * -------------------------------------------------------------------------
     */

    /**
     * Get a list of direct debit mandates
     */
    @Test
    public void testGetListDirectDebitMandates() {
        try {
            Response response = client.getListDirectDebitMandates();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get the direct debit mandate with the specified identifier
     */
    @Test
    public void testGetDirectDebitMandate() {
        try {
            // TODO: Need to set up valid mandateUid
            String mandateUid = "mandateUid";
            Response response = client.getDirectDebitMandate(mandateUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Cancel the direct debit mandate with the specified identifier
     */
    @Test
    public void testCancelDirectDebitMandate() {
        try {
            // TODO: Need to set up valid mandateUid
            String mandateUid = "mandateUid";
            Response response = client.cancelDirectDebitMandate(mandateUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(204);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - TRANSACTION FEED
     * -------------------------------------------------------------------------
     */

    /**
     * Changes the user-specified note attached to a transaction
     */
    @Test
    public void testChangesSpendingCategoryForTransaction() {
        try {
            String feedItemUid = "4cf21aa8-2a68-4c1c-887d-4d7f62edda94";
            // TODO: we need to clarify with the helpdesk what to do with that argument
            String spendingCategory = "{\"spendingCategory\": \"GROCERIES\"}";
            Response response = client.changesSpendingCategoryForTransaction(accountUid, categoryUid, feedItemUid, spendingCategory);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get the account holder's feed items which were created or updated since a given date - ALL
     */
    @Test
    public void testGetTransactionFeedItems() {
        try {
            // TODO: we need to clarify with the helpdesk what to do with arguments
            Response response = client.getTransactionFeedItems(accountUid, categoryUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Fetches a single feed item
     */
    @Test
    public void testFetchesSingleTransactionFeedItem() {
        try {
            String feedItemUid = "4cf21aa8-2a68-4c1c-887d-4d7f62edda94";
            Response response = client.fetchesSingleTransactionFeedItem(accountUid, categoryUid, feedItemUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get the account holder's feed items which were created between two timestamps
     */
    @Test
    public void testGetTransactionFeedItemsBetweenTwoTimestamps() {
        try {
            Date date= new Date();
            // TODO: we need to clarify with the helpdesk what to do with that argument
            String minTransactionTimestamp = Long.toString(0);
            // TODO: we need to clarify with the helpdesk what to do with that argument
            String maxTransactionTimestamp = Long.toString(date.getTime());
            Response response = client.getTransactionFeedItemsBetweenTwoTimestamps(accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Fetches the list of items attached to a feed item
     */
    @Test
    public void testGetListOfItemsAttachedToFeedItem() {
        try {
            String feedItemUid = "4cf21aa8-2a68-4c1c-887d-4d7f62edda94";
            Response response = client.getListOfItemsAttachedToFeedItem(accountUid, categoryUid, feedItemUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Downloads the feed item attachment
     */
    @Test
    public void testDownloadFeedItemAttachment() {
        try {
            String feedItemUid = "4cf21aa8-2a68-4c1c-887d-4d7f62edda94";
            // TODO: we need to clarify with the helpdesk what to do with that argument
            // TODO: Need to set up valid AttachmentUid
            String feedItemAttachmentUid = "feedItemAttachmentUid";
            Response response = client.downloadFeedItemAttachment(accountUid, categoryUid, feedItemUid, feedItemAttachmentUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Changes the user-specified note attached to a transaction
     */
    @Test
    public void testChangesUserSpecifiedNoteAttachedTransaction() {
        try {
            String feedItemUid = "4cf21aa8-2a68-4c1c-887d-4d7f62edda94";
            // TODO: we need to clarify with the helpdesk what to do with that argument
            String userNote = "{\"userNote\": \"Test test test test\"}";
            Response response = client.changesUserSpecifiedNoteAttachedTransaction(accountUid, categoryUid, feedItemUid, userNote);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }


    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - FEED ROUND-UP
     * -------------------------------------------------------------------------
     */

    /**
     * Returns the the round-up goal associated with an account if one has been created
     */
    @Test
    public void testGetRoundUpFeed() {
        try {
            Response response = client.getRoundUpFeed(accountUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Activates transaction round-up and adds remainder to savings goal
     */
    @Test
    public void testActivateRoundUpGoal() {
        try {
            String roundUpGoalPayload = "{" +
            "   \"roundUpGoalUid\": \"4cf21aa8-2a68-4c1c-887d-4d7f62edda94\"," +
            "   \"roundUpMultiplier\": 2" +
            "}";
            Response response = client.activateRoundUpGoal(accountUid, roundUpGoalPayload);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Deletes the round-up goal associated with an account if one exists
     */
    @Test
    public void testDeletesRoundUpGoalAssociatedWithAccount() {
        try {
            Response response = client.deletesRoundUpGoalAssociatedWithAccount(accountUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(204);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - KYC
     * -------------------------------------------------------------------------
     */

    /**
     * Get KYC result for an account holder
     */
    @Test
    public void testGetKYCResult() {
        try {
            // TODO: we need to clarify with the helpdesk what to do with that endpoint
            // TODO: {"error":"signature_check_failed","error_description":"request signed incorrectly"}
            Response response = client.getKYCResult();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - PAYMENTS
     * -------------------------------------------------------------------------
     */

    /**
     * Get a payment order
     */
    @Test
    public void testGetPaymentOrder() {
        try {
            String paymentOrderUid = "8a174347-537a-4806-872e-fa588025a8f8";
            Response response = client.getPaymentOrder(paymentOrderUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Creates and sends an immediate payment within the UK under the Faster Payments Scheme
     */
    @Test
    public void testCreateDomesticPayment() {
        try {
            String localPayment = String.format(
                "{\n" +
                "\t\"externalIdentifier\":\"%s\",\n" +
                "\t\"reference\":\"%s\",\n" +
                "\t\"amount\":{\n" +
                "\t\t\"currency\":\"GBP\",\n" +
                "\t\t\"minorUnits\":100\n" +
                "\t},\n" +
                "\t\"destinationPayeeAccountUid\":\"6f4f5b68-cb54-4dbf-becb-5574ba6f126f\"\n" +
                "}",
                UUID.randomUUID().toString(),
                Long.toHexString(Double.doubleToLongBits(Math.random()))
            );

            Response response = client.createDomesticPayment(
                accountUid,
                categoryUid,
                localPayment
            );

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get a standing order
     */
    @Test
    public void testGetStandingOrder() {
        try {
            String paymentOrderUid = "6adb5c96-9843-4d55-8e43-ff45e93fd9c8";
            Response response = client.getStandingOrder(accountUid, categoryUid, paymentOrderUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update a standing order
     */
    @Test
    public void testUpdateStandingOrder() {
        try {
            String paymentOrderUid = "05811a38-825d-4c8e-a393-f980b5afb5a1";
            String standingOrderRequest =
            "{\n" +
            "\t\"paymentOrderUid\": \"05811a38-825d-4c8e-a393-f980b5afb5a1\",\n" +
            "\t\"amount\": {\n" +
            "\t\t\"currency\": \"GBP\",\n" +
            "\t\t\"minorUnits\": 200\n" +
            "\t},\n" +
            "\t\"reference\": \"TEST-RECURRING-PAY\",\n" +
            "\t\"payeeUid\": \"2af09025-f844-4b2b-b670-5f924f5bdaab\",\n" +
            "\t\"payeeAccountUid\": \"8755457b-fae2-4f80-8376-d297aab6144d\",\n" +
            "\t\"standingOrderRecurrence\": {\n" +
            "\t\t\"startDate\": \"2018-04-02\",\n" +
            "\t\t\"frequency\": \"MONTHLY\",\n" +
            "\t\t\"interval\": 1,\n" +
            "\t\t\"count\": 1,\n" +
            "\t\t\"untilDate\": \"2020-02-02\"\n" +
            "\t}\n" +
            "}";
            Response response = client.updateStandingOrder(accountUid, categoryUid, paymentOrderUid, standingOrderRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get a standing order
     */
    @Test
    public void testCancelStandingOrder() {
        try {
            String paymentOrderUid = "e16a231f-9fea-48a9-bd4c-ad75bfe50c1a";
            Response response = client.cancelStandingOrder(accountUid, categoryUid, paymentOrderUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(204);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * List standing orders
     */
    @Test
    public void testGetListStandingOrders() {
        try {
            Response response = client.getListStandingOrders(accountUid, categoryUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Create a new standing order
     */
    @Test
    public void testCreateStandingOrder() {
        try {
            String standingOrderRequest = "{\n" +
            "  \"externalIdentifier\": \"23cfcb22-b3d5-427f-ad45-25104025192a\",\n" +
            "  \"destinationPayeeAccountUid\": \"6f4f5b68-cb54-4dbf-becb-5574ba6f126f\",\n" +
            "  \"reference\": \"Payment reference\",\n" +
            "  \"amount\": {\n" +
            "    \"currency\": \"GBP\",\n" +
            "    \"minorUnits\": 100\n" +
            "  },\n" +
            "  \"standingOrderRecurrence\": {\n" +
            "    \"startDate\": \"2017-09-23\",\n" +
            "    \"frequency\": \"WEEKLY\",\n" +
            "    \"interval\": 4,\n" +
            "    \"count\": 12,\n" +
            "    \"untilDate\": \"2018-09-23\"\n" +
            "  }\n" +
            "}";
            Response response = client.createStandingOrder(accountUid, categoryUid, standingOrderRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * List next payment dates of a standing order
     */
    @Test
    public void testListNextPaymentDatesOfStandingOrder() {
        try {
            String paymentOrderUid = "6adb5c96-9843-4d55-8e43-ff45e93fd9c8";
            Response response = client.listNextPaymentDatesOfStandingOrder(accountUid, categoryUid, paymentOrderUid, 3);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get the payments associated with a payment order
     */
    @Test
    public void testGetPaymentsAssociatedWithPaymentOrder() {
        try {
            String paymentOrderUid = "6adb5c96-9843-4d55-8e43-ff45e93fd9c8";
            Response response = client.getPaymentsAssociatedWithPaymentOrder(paymentOrderUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }


    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - ONBOARDING
     * -------------------------------------------------------------------------
     */

    /**
     * Get the onboarding status of the account
     */
    @Test
    public void testGetOnboardingStatus() {
        try {
            // TODO: we need to clarify with the helpdesk what to do with that endpoint
            Response response = client.getOnboardingStatus();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Onboard an account
     */
    @Test
    public void testOnboardAccount() {
        try {
            // TODO: we need to clarify with the helpdesk what to do with that endpoint
            // TODO: {"error":"insufficient_scope","error_description":"Insufficient scope. Required: [customer-onboarding:write]. Granted: [transaction:edit, transaction:read, statement-csv:read, statement-pdf:read, standing-order:delete, standing-order:read, standing-order:create, savings-goal-transfer:delete, savings-goal-transfer:create, savings-goal-transfer:read, savings-goal:delete, savings-goal:create, savings-goal:read, receipt:write, receipt:read, receipts:read, pay-foreign:create, pay-local:read, pay-local-once:create, pay-local:create, payee-image:read, payee:read, payee:edit, payee:delete, payee:create, metadata:edit, metadata:create, merchant:read, mandate:delete, mandate:read, email:edit, customer:read, confirmation-of-funds:read, card-control:edit, card:read, balance:read, authorising-individual:read, attachment:read, address:edit, address:read, account-holder-type:read, account-holder-name:read, account-identifier:read, account-list:read, account:read]"}
            String onboardingRequest = "{\n" +
            "  \"mobileNumber\": \"+447812345678\",\n" +
            "  \"title\": \"MISS\",\n" +
            "  \"firstName\": \"Gytha\",\n" +
            "  \"lastName\": \"Ogg\",\n" +
            "  \"dateOfBirth\": \"2000-12-30\",\n" +
            "  \"email\": \"gytha.ogg@example.com\",\n" +
            "  \"currentAddress\": {\n" +
            "    \"line1\": \"Flat 101\",\n" +
            "    \"line2\": \"Hudson House\",\n" +
            "    \"line3\": \"4 Yeo Street\",\n" +
            "    \"subBuildingName\": \"Flat 101\",\n" +
            "    \"buildingName\": \"Hudson House\",\n" +
            "    \"buildingNumber\": \"4\",\n" +
            "    \"thoroughfare\": \"Yeo Street\",\n" +
            "    \"dependantLocality\": \"Langdon Park\",\n" +
            "    \"postTown\": \"London\",\n" +
            "    \"postCode\": \"E3 3NU\",\n" +
            "    \"countryCode\": \"GB\",\n" +
            "    \"udprn\": \"52379171\",\n" +
            "    \"umprn\": \"1234567890\",\n" +
            "    \"from\": \"2018-01-01\",\n" +
            "    \"to\": \"2018-01-02\"\n" +
            "  },\n" +
            "  \"previousAddresses\": [\n" +
            "    {\n" +
            "      \"line1\": \"Flat 101\",\n" +
            "      \"line2\": \"Hudson House\",\n" +
            "      \"line3\": \"4 Yeo Street\",\n" +
            "      \"subBuildingName\": \"Flat 101\",\n" +
            "      \"buildingName\": \"Hudson House\",\n" +
            "      \"buildingNumber\": \"4\",\n" +
            "      \"thoroughfare\": \"Yeo Street\",\n" +
            "      \"dependantLocality\": \"Langdon Park\",\n" +
            "      \"postTown\": \"London\",\n" +
            "      \"postCode\": \"E3 3NU\",\n" +
            "      \"countryCode\": \"GB\",\n" +
            "      \"udprn\": \"52379171\",\n" +
            "      \"umprn\": \"1234567890\",\n" +
            "      \"from\": \"2018-01-01\",\n" +
            "      \"to\": \"2018-01-02\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"incomeDetails\": {\n" +
            "    \"annualIncome\": {\n" +
            "      \"currency\": \"GBP\",\n" +
            "      \"minorUnits\": 11223344\n" +
            "    },\n" +
            "    \"sourceOfFunds\": [\n" +
            "      \"BENEFITS\"\n" +
            "    ]\n" +
            "  },\n" +
            "  \"taxLiabilityDeclaration\": {\n" +
            "    \"taxLiabilityDeclarationAnswer\": \"YES\",\n" +
            "    \"usTaxLiabilityDeclarationAnswer\": \"YES\",\n" +
            "    \"taxLiabilityDeclarationCountries\": [\n" +
            "      {\n" +
            "        \"countryCode\": \"GB\",\n" +
            "        \"taxIdentificationNumber\": \"123456789\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"termsAcceptance\": [\n" +
            "    {\n" +
            "      \"policyDocument\": \"STARLING_PRIVACY_POLICY\",\n" +
            "      \"version\": 4,\n" +
            "      \"termsAccepted\": true,\n" +
            "      \"acceptanceDate\": \"2018-04-16\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
            Response response = client.onboardAccount(onboardingRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - PAYEES
     * -------------------------------------------------------------------------
     */

    /**
     * Get an account holder's payees
     */
    @Test
    public void testGetPayees() {
        try {
            Response response = client.getPayees();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Create a payee account
     */
    @Test
    public void testCreatePayee() {
        try {
            String payee = "{\n" +
            "  \"payeeName\": \"Bob's accounts\",\n" +
            "  \"phoneNumber\": \"012345678912\",\n" +
            "  \"payeeType\": \"INDIVIDUAL\",\n" +
            "  \"firstName\": \"Bob\",\n" +
            "  \"middleName\": \"Bobity\",\n" +
            "  \"lastName\": \"Bobbington\",\n" +
            "  \"businessName\": \"Trotters Independent Traders\",\n" +
            "  \"dateOfBirth\": \"1990-11-30\",\n" +
            "  \"accounts\": [\n" +
            "    {\n" +
            "      \"description\": \"Bob's business account\",\n" +
            "      \"defaultAccount\": true,\n" +
            "      \"countryCode\": \"GB\",\n" +
            "      \"accountIdentifier\": \"00004588\",\n" +
            "      \"bankIdentifier\": \"203002\",\n" +
            "      \"bankIdentifierType\": \"SORT_CODE\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
            Response response = client.createPayee(payee);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Create a payee account
     */
    @Test
    public void testCreatePayeeAccount() {
        try {
            String payeeUid = "2196009c-dff1-436d-986e-4a1eb6eb5039";
            String payeeAccount = "{\n" +
            "  \"description\": \"Bob's business account\",\n" +
            "  \"defaultAccount\": true,\n" +
            "  \"countryCode\": \"GB\",\n" +
            "  \"accountIdentifier\": \"00004588\",\n" +
            "  \"bankIdentifier\": \"203002\",\n" +
            "  \"bankIdentifierType\": \"SORT_CODE\"\n" +
            "}";
            Response response = client.createPayeeAccount(payeeUid, payeeAccount);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Deletes a payee
     */
    @Test
    public void testDeletePayee() {
        try {
            String payeeUid = "554be9db-87fc-4b2c-b9cf-8188d17c4bee";
            Response response = client.deletePayee(payeeUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(204);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Delete a payee account
     */
    @Test
    public void testDeletePayeeAccount() {
        try {
            String payeeUid = "554be9db-87fc-4b2c-b9cf-8188d17c4bee";
            String accountUid = "ea98c567-5d7d-4a5e-83c9-ed73caaaeea4";
            Response response = client.deletePayeeAccount(payeeUid, accountUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(204);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Serves the image for the payee
     */
    @Test
    public void testGetPayeesImage() {
        try {
            String payeeUid = "08dbe4a3-1723-4d11-b593-8dfc71215c2a";
            Response response = client.getPayeesImage(payeeUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - SAVINGS GOALS
     * -------------------------------------------------------------------------
     */

    /**
     * Get a savings goal
     */
    @Test
    public void testGetSavingsGoal() {
        try {
            String savingsGoalUid = "1f8366d5-77ba-4a3a-814f-9a51c8fe381a";
            Response response = client.getSavingsGoal(accountUid, savingsGoalUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Update an existing goal
     */
    @Test
    public void testUpdateSavingsGoal() {
        try {
            String savingsGoalUid = "1f8366d5-77ba-4a3a-814f-9a51c8fe381a";
            String savingsGoal = "{\n" +
            "  \"name\": \"Trip to Paris\",\n" +
            "  \"currency\": \"GBP\",\n" +
            "  \"target\": {\n" +
            "    \"currency\": \"GBP\",\n" +
            "    \"minorUnits\": 300\n" +
            "  },\n" +
            "  \"base64EncodedPhoto\": \"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\"\n" +
            "}";
            Response response = client.updateSavingsGoal(accountUid, savingsGoalUid, savingsGoal);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Delete a savings goal
     */
    @Test
    public void testDeleteSavingsGoal() {
        try {
            // TODO: Need to set up valid savingsGoalUid
            // TODO: insert valid savingsGoalUid - {"errors":[{"message":"UNKNOWN_SAVINGS_GOAL"}],"success":false}
            String savingsGoalUid = "1f8366d5-77ba-4a3a-814f-9a51c8fe381a";
            Response response = client.deleteSavingsGoal(accountUid, savingsGoalUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(204);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get all savings goals
     */
    @Test
    public void testGetSavingsGoals() {
        try {
            Response response = client.getSavingsGoals(accountUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Create a savings goal
     */
    @Test
    public void testCreateSavingsGoal() {
        try {
            String savingsGoal = "{\n" +
            "  \"name\": \"Trip to Paris\",\n" +
            "  \"currency\": \"GBP\",\n" +
            "  \"target\": {\n" +
            "    \"currency\": \"GBP\",\n" +
            "    \"minorUnits\": 100\n" +
            "  },\n" +
            "  \"base64EncodedPhoto\": \"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\"\n" +
            "}";
            Response response = client.createSavingsGoal(accountUid, savingsGoal);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get the photo associated with a savings goal
     */
    @Test
    public void testGetSavingsGoalPhoto() {
        try {
            String savingsGoalUid = "3173e3ac-296b-4f0a-af67-5349c14bf166";
            Response response = client.getSavingsGoalPhoto(accountUid, savingsGoalUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Add money into a savings goal
     */
    @Test
    public void testAddMoneyIntoSavingsGoal() {
        try {
            String savingsGoalUid = "3173e3ac-296b-4f0a-af67-5349c14bf166";
            String transferUid = UUID.randomUUID().toString();
            String addMoneyRequest = "{\n" +
            "  \"amount\": {\n" +
            "    \"currency\": \"GBP\",\n" +
            "    \"minorUnits\": 50\n" +
            "  }\n" +
            "}";
            Response response = client.addMoneyIntoSavingsGoal(accountUid, savingsGoalUid, transferUid, addMoneyRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Withdraw money from a savings goal
     */
    @Test
    public void testWithdrawMoneyFromSavingsGoal() {
        try {
            String savingsGoalUid = "1f8366d5-77ba-4a3a-814f-9a51c8fe381a";
            String transferUid = UUID.randomUUID().toString();
            String withdrawMoneyRequest = "{\n" +
            "  \"amount\": {\n" +
            "    \"currency\": \"GBP\",\n" +
            "    \"minorUnits\": 10\n" +
            "  }\n" +
            "}";
            Response response = client.withdrawMoneyFromSavingsGoal(accountUid, savingsGoalUid, transferUid, withdrawMoneyRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Create a recurring transfer into a savings goal
     */
    @Test
    public void testCreateRecurringTransferOfSavingsGoal() {
        try {
            String savingsGoalUid = "3173e3ac-296b-4f0a-af67-5349c14bf166";
            String transferUid = UUID.randomUUID().toString();
            String scheduledSavingsPaymentRequest = "{\n" +
            "  \"recurrenceRule\": {\n" +
            "    \"startDate\": \"2019-09-23\",\n" +
            "    \"frequency\": \"MONTHLY\",\n" +
            "    \"interval\": 2,\n" +
            "    \"count\": 10,\n" +
            "    \"untilDate\": \"2022-09-23\",\n" +
            "    \"days\": [\n" +
            "      \"MONDAY\"\n" +
            "    ]\n" +
            "  },\n" +
            "  \"amount\": {\n" +
            "    \"currency\": \"GBP\",\n" +
            "    \"minorUnits\": 30\n" +
            "  }\n" +
            "}";
            Response response = client.createRecurringTransferOfSavingsGoal(accountUid, savingsGoalUid, transferUid, scheduledSavingsPaymentRequest);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get the photo associated with a savings goal
     */
    @Test
    public void testGetRecurringTransferOfSavingsGoal() {
        try {
            String savingsGoalUid = "3173e3ac-296b-4f0a-af67-5349c14bf166";
            Response response = client.getRecurringTransferOfSavingsGoal(accountUid, savingsGoalUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get the photo associated with a savings goal
     */
    @Test
    public void testDeleteRecurringTransferOfSavingsGoal() {
        try {
            String savingsGoalUid = "1f8366d5-77ba-4a3a-814f-9a51c8fe381a";
            Response response = client.deleteRecurringTransferOfSavingsGoal(accountUid, savingsGoalUid);

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(204);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * -------------------------------------------------------------------------
     * https://developer.starlingbank.com/docs - API USER IDENTITIES
     * -------------------------------------------------------------------------
     */

    /**
     * Get current token identity
     */
    @Test
    public void testGetIdentityToken() {
        try {
            Response response = client.getIdentityToken();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Get the individual who authorised the application
     */
    @Test
    public void testGetIdentityIndividual() {
        try {
            Response response = client.getIdentityIndividual();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(200);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }

    /**
     * Logout the current individual
     */
    @Test
    public void testLogoutIdentity() {
        try {
            Response response = client.logoutIdentity();

            System.out.println(response.toString());
            assertThat(response.getStatusCode()).isEqualTo(202);
        } catch (Exception e) {
            failOnException(e.getMessage());
        }
    }
}
