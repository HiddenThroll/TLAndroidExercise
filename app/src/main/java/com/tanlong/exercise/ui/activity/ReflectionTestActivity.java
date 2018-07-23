package com.tanlong.exercise.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tanlong.exercise.model.entity.Cat;
import com.tanlong.exercise.ui.activity.base.BaseActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class ReflectionTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        testFiled();
        testMethod();
    }

    private void testFiled() {
        Class catClass = Cat.class;
        try {
            Field nameFiled = catClass.getDeclaredField("name");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("name is " + nameFiled.getName())
                    .append(" type is " + nameFiled.getType())
                    .append(" modifer is " + Modifier.toString(nameFiled.getModifiers()));
            Log.e("test", stringBuffer.toString());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        Cat cat = new Cat("amy", 2);
        try {
            Field ageFiled = catClass.getDeclaredField("age");
            Field nameFiled = catClass.getDeclaredField("name");
            int ageValue = ageFiled.getInt(cat);
            nameFiled.setAccessible(true);
            String nameValue = (String) nameFiled.get(cat);
            Log.e("test", "age is " + ageValue);
            Log.e("test", "name is " + nameValue);
            ageFiled.setInt(cat, 20);
            nameFiled.set(cat, "bell");
            Log.e("test", "cat is " + new Gson().toJson(cat));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void testMethod() {
        Class catClass = Cat.class;

        try {
            Method catchMouseMethod = catClass.getMethod("catchMouse", String.class, long.class);
            StringBuffer buffer = new StringBuffer();
            buffer.append("method is " + catchMouseMethod.getName())
                    .append(" return type is " + catchMouseMethod.getReturnType().getName())
                    .append(" parameter is ");
            for (Class parameter : catchMouseMethod.getParameterTypes()) {
                buffer.append(parameter.getName()).append(" ");
            }
            Log.e("test", buffer.toString());

            Method fightDogsMethod = catClass.getDeclaredMethod("figthDogs", String[].class, long[].class);
            fightDogsMethod.setAccessible(true);
            buffer = new StringBuffer();
            buffer.append("method is " + fightDogsMethod.getName())
                    .append(" return type is " + fightDogsMethod.getReturnType().getName())
                    .append(" parameter is ");
            for (Class parameter : fightDogsMethod.getParameterTypes()) {
                buffer.append(parameter.getName()).append(" ");
            }
            Log.e("test", buffer.toString());

            Cat cat = new Cat("amy", 2);
            catchMouseMethod.invoke(cat, "small mouse", 10);

            Constructor<Cat> constructor = catClass.getDeclaredConstructor(String.class, int.class);
            Cat newCat = constructor.newInstance("alvin", 10);
            Log.e("test", "newCat is " + new Gson().toJson(newCat));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
