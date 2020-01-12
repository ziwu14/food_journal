package com.example.foodJournal;

import com.example.foodJournal.activity.SignUpActivity;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class SignUpUnitTest {

    public static SignUpActivity signUpActivity;
    @BeforeClass
    public static void beforeClass(){
        signUpActivity = new SignUpActivity();
    }

    @Test
    public void private_do_validate_passwd()throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException{

        Method method = SignUpActivity.class.getDeclaredMethod("do_validate_passwd", String.class);
        method.setAccessible(true);

        //Test 1:
        String input = "wert1234567A@";
        boolean output = (boolean) method.invoke(signUpActivity,input);
        assertTrue(output);

        //Test 2:
    }

    @Test
    public void private_do_validate_email()throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException{

        Method method = SignUpActivity.class.getDeclaredMethod("do_validate_email", String.class);
        method.setAccessible(true);

        //Test 1:
        String input = "zuhao.wu@outlook.com";
        boolean output = (boolean) method.invoke(signUpActivity,input);
        assertTrue(output);

        //Test 2:

    }
}
