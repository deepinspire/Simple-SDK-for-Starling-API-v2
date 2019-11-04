# This is SDK/wrapper for Starling API v2.
<p align="left">
    <img src="https://img.shields.io/badge/version-0.1.0-blue.svg" />
</p>
It was intentionally made simple to provide a quick start and a better understanding of "what's under the hood".
It has minimal dependencies and is compatible with Java 1.8+.

## Links
* Documentation: [https://developer.starlingbank.com/payments/docs](https://developer.starlingbank.com/payments/docs)
* Getting started: [https://developer.starlingbank.com/get-started](https://developer.starlingbank.com/get-started)
* Swagger 2.0 definition: [https://payment-api-sandbox.starlingbank.com/api/swagger.json](https://payment-api-sandbox.starlingbank.com/api/swagger.json)

## Roadmap
1. API Response wrapper classes
2. Unit testing
3. Documentation (Adding metadata for generation documentation from code)
4. Simplify client initialization

## How to start
1. Create an Account (Set up a developer account)
2. Register Application in the sandbox environment - [https://www.youtube.com/watch?v=b8V1kXG9jnk](https://www.youtube.com/watch?v=b8V1kXG9jnk)
   - Generate a "key pair" and upload to the portal
   - Add a sandbox customer with one of the following type:
     - Business / Personal / Joint
   - Add funds to your Business Customer Account
3. Clone and setup "StarlingAPIWrapper" locally - [https://www.youtube.com/watch?v=dB80KGWYQy8](https://www.youtube.com/watch?v=dB80KGWYQy8)
   - *[Optionals]* Copy already generated "key pair" to project folder "/keys"
   - Create new configuration file or change existing "cfg/sandbox.properties" file with your current sandbox application data.
     Make sure that you've set up next properties correctly:
     - starling.domain-url - enter their standard sandbox URL [https://api-sandbox.starlingbank.com
](https://api-sandbox.starlingbank.com
)
     - starling.signing.keys-dir-path - path where you store already generated "key pair" for example "d:\\StarlingAPIWrapper\\keys\\"
     - starling.signing.public-key-uid - unique identifier of your public key that you uploaded to the portal
     - starling.access.token - your current access token from your sandbox app options
4. Experiment in the sandbox environment
   - Run simple test (/src/tests/com/deepinspire/sterlingbank/SterlingApiV2WrapperTests.java) for checking that all is configured correctly
5. Build and integrate lib to your project
6. Get your application approved

## Fast way to generating a key pair
1. Open the console
2. Go to the project folder
3. Make sure the project has a "keys" subfolder, if not create it
4. Run the following commands
   ```
   openssl genrsa -out keys/private_key.pem 2048
   openssl genrsa -out keys/rotation_private_key.pem 2048
   openssl rsa -in keys/private_key.pem -outform PEM -pubout -out keys/public_key.pem
   openssl rsa -in keys/rotation_private_key.pem -outform PEM -pubout -out keys/rotation_public_key.pem
   openssl pkcs8 -in keys/private_key.pem -topk8 -nocrypt -out keys/private_key.der -outform der
   openssl rsa -in keys/private_key.pem -outform DER -pubout -out keys/public_key.der
   ```

## Sample Usage
This is simple api wrapper in beta stage that provide simple interaction with "Starling Bank" API...

### Initialise Client
```java
StarlingClient client = new StarlingClient(
    <domain url>,
    <keys dir path>,
    <public key uid>,
    <access token>
);
```

### Fetch your account details
```java
Response response = client.getAccount();
System.out.println(response.toString());
```

### Check your account balance
```java
Response response = client.getAccountBalance(<account uid>);
System.out.println(response.toString());
```

### Get all transaction feed items
```java
Response response = client.getTransactionFeedItems(accountUid, categoryUid);
System.out.println(response.toString());
```

## License
The gem is available as open source under the terms of
the [MIT License](http://opensource.org/licenses/MIT).

## Contributing
All contributions are welcome - just make a pull request, making sure you include tests
and documentation for any public methods. Then write a good, informative commit
message/pull request body.
