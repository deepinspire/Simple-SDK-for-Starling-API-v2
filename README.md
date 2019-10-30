# StarlingAPIWrapper
*StarlingAPIWrapper* is a Java library that provides simple access to the Starling Bank API.
It have minimal dependencies and is compatible with Java 1.8+.

## Links
* Documentation here: [https://developer.starlingbank.com/payments/docs](https://developer.starlingbank.com/payments/docs)
* How to getting started: [https://developer.starlingbank.com/get-started](https://developer.starlingbank.com/get-started)
* Swagger 2.0 definition [https://payment-api-sandbox.starlingbank.com/api/swagger.json](https://payment-api-sandbox.starlingbank.com/api/swagger.json)

## Short plan how to start
1. Create an Account (Set up a developer account)
2. Register Application in the sandbox environment
   - Generating a key pair and upload to the portal
   - Add Sandbox Customer
     - Business / Personal / Joint
   - Add funds to your Business Customer Account
4. Clone and setup "StarlingAPIWrapper" locally
   - *[Optionals]* Copy already generated key pair to project folder "/keys"
   - Create new configuration file or change existing "cfg/sandbox.properties" with your current sandbox application data.
     Be sure that you set up next properties correctly:
     - starling.domain-url - for test enter their standard sandbox URL [https://api-sandbox.starlingbank.com
](https://api-sandbox.starlingbank.com
)
     - starling.signing.keys-dir-path - path where you store already generated "key pair" for example "d:\\StarlingAPIWrapper\\keys\\"
     - starling.signing.public-key-uid - unique identifier of your public key that you uploaded to the portal
     - starling.access.token - your current access token from your sandbox app options
5. Experiment in the sandbox environment
   - Run simple test (/src/tests/com/deepinspire/sterlingbank/SterlingApiV2WrapperTests.java) for checking that all configured correctly
6. Build and integrate lib to your project
7. Get your application approved

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

## License
The gem is available as open source under the terms of
the [MIT License](http://opensource.org/licenses/MIT).

## Contributing
All contributions are welcome - just make a pull request, making sure you include tests
and documentation for any public methods, and write a good, informative commit
message/pull request body.
