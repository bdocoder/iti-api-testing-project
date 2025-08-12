package iti.apitesting.data;

import org.testng.annotations.DataProvider;

import iti.apitesting.base.Helpers;

public class DataProviders {
        @DataProvider(name = "loginData")
        public TestCaseData[] loginData() {
                var valid = new TestCaseData()
                                .with("identity", "admin@localhost.localhost")
                                .with("password", "password")
                                .with("statusCode", "200");

                var invalid = new TestCaseData()
                                .with("statusCode", "400")
                                .with("message", "Failed to authenticate.");
                var wrongIdentity = invalid.clone()
                                .with("identity", "someone-else@localhost.localhost")
                                .with("password", "password");
                var wrongPassword = invalid.clone()
                                .with("identity", "admin@localhost.localhost")
                                .with("password", "aaaaaaaa");

                var missing = new TestCaseData()
                                .with("statusCode", "400")
                                .with("message", "An error occurred while validating the submitted data.");
                var noIdentity = missing.clone()
                                .with("password", "password");
                var noPassword = missing.clone()
                                .with("identity", "admin@localhost.localhost");
                var noData = missing.clone();

                return new TestCaseData[] {
                                valid,
                                wrongIdentity,
                                wrongPassword,
                                noIdentity,
                                noPassword,
                                noData,
                };
        }

        @DataProvider(name = "invalidCustomerCreationData")
        public TestCaseData[] getCustomerCreationData() {
                var invalid = new TestCaseData()
                                .with("message", "Failed to create record.")
                                .with("statusCode", "400");

                var invalidFormat = invalid.clone()
                                .with("fieldName", "phone")
                                .with("fieldMessage", "Invalid value format.");
                var shortPhone = invalidFormat.clone()
                                .with("phone", "+2012");
                var longPhone = invalidFormat.clone()
                                .with("phone", "+2012345678901");

                var missing = invalid.clone()
                                .with("fieldMessage", "Cannot be blank.");
                var missingName = missing.clone()
                                .with("fieldName", "name")
                                .with("phone", "+201234567890");
                var missingPhone = missing.clone()
                                .with("fieldName", "phone")
                                .with("name", "محمد علي");

                return new TestCaseData[] {
                                shortPhone,
                                longPhone,
                                missingName,
                                missingPhone
                };
        }

        @DataProvider(name = "invalidProductCreationData")
        public TestCaseData[] getProductCreationData() {
                var invalid = new TestCaseData()
                                .with("message", "Failed to create record.")
                                .with("statusCode", "400")
                                .with("name", "شاي أخضر")
                                .with("price", 50)
                                .with("stock", 20);

                var negativePrice = invalid.clone()
                                .with("fieldMessage", "Must be larger than 0.5")
                                .with("fieldName", "price")
                                .with("price", -12);
                var zeroPrice = invalid.clone()
                                .with("fieldMessage", "Must be larger than 0.5")
                                .with("fieldName", "price")
                                .with("price", 0);
                var negativeStock = invalid.clone()
                                .with("fieldMessage", "Must be larger than 0.0")
                                .with("fieldName", "stock")
                                .with("stock", -15);

                var missing = invalid.clone()
                                .with("fieldMessage", "Cannot be blank.");
                var missingName = missing.clone()
                                .without("name")
                                .with("fieldName", "name");
                var missingPrice = missing.clone()
                                .without("price")
                                .with("fieldName", "price");

                var invalidFormat = invalid.clone()
                                .with("fieldMessage", "Invalid value format.");
                var priceWithLetters = invalidFormat.clone()
                                .with("fieldName", "price")
                                .with("price", "abc");

                return new TestCaseData[] {
                                missingName,
                                missingPrice,
                                zeroPrice,
                                negativePrice,
                                priceWithLetters,
                                negativeStock,
                };
        }

        @DataProvider(name = "invalidSaleCreationData")
        public TestCaseData[] getSaleCreationData() {
                var customer = Helpers.findCustomerByPhone("+201012345678");
                var product = Helpers.findProductByName("شاي العروسة");

                var invalid = new TestCaseData()
                                .with("message", "Failed to create record.")
                                .with("statusCode", "400")
                                .with("customerId", customer.id())
                                .with("productId", product.id())
                                .with("quantity", 30);

                var negativeQuantity = invalid.clone()
                                .with("fieldMessage", "Must be larger than 0")
                                .with("fieldName", "quantity")
                                .with("quantity", -12);
                var zeroQuantity = invalid.clone()
                                .with("fieldMessage", "Must be larger than 0")
                                .with("fieldName", "quantity")
                                .with("quantity", 0);
                var noCustomer = invalid.clone()
                                .without("customerId")
                                .with("fieldMessage", "Missing required value.")
                                .with("fieldName", "customer");
                var noProduct = invalid.clone()
                                .without("productId")
                                .with("fieldMessage", "Missing required value.")
                                .with("fieldName", "product");

                return new TestCaseData[] {
                                negativeQuantity,
                                zeroQuantity,
                                noCustomer,
                                noProduct,
                };
        }
}
