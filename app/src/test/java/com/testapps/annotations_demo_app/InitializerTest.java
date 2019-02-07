package com.testapps.annotations_demo_app;

import com.testapps.annotation.Init;
import com.testapps.annotation.initializers.Initializer;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InitializerTest {

    @Init
    private String someString;


    @Init
    private JSONObject jsonObject;


    @Before
    public void setUp() {
        // run initializer
        Initializer.init(this);
    }

    @Test
    public void testFields() {

        // test if init annotation initialized them correctly
        Assert.assertNotNull("String cannot be null", someString);

        Assert.assertNotNull("Json cannot be null", jsonObject);


    }

}
