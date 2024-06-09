package com.example.backend.baseTest;

import com.example.backend.DataGenerator.CleanDatabase;
import com.example.backend.DataGenerator.ProductGenerator;
import com.example.backend.DataGenerator.UserGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"test"})
public class TestDataGenerator {

    private final UserGenerator userGenerator;
   private final  ProductGenerator productGenerator;
    private final CleanDatabase cleanDatabase;


    public TestDataGenerator(UserGenerator userGenerator, ProductGenerator productGenerator, CleanDatabase cleanDatabase) {
        this.userGenerator = userGenerator;
        this.productGenerator = productGenerator;
        this.cleanDatabase = cleanDatabase;
    }

    public void cleanUp(){
        cleanDatabase.truncateAllTablesAndRestartIds();
        userGenerator.generateApplicationUsers();
        productGenerator.generateProducts();
    }
}
