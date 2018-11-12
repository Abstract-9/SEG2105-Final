package com.codeflo.seg2105_final;

import android.app.AlertDialog;
import android.app.Fragment;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

public class Deliverable2Test {

    @Rule
    public ActivityTestRule<AdminActivity> adminActivityTestRule = new ActivityTestRule<AdminActivity>(AdminActivity.class);

    private AdminActivity adminActivity;

    @Before
    public void setup(){
        adminActivity = adminActivityTestRule.getActivity();
    }

    @Test
    @UiThreadTest
    public void testAddServiceButton(){
        Button add = (Button) adminActivity.findViewById(R.id.addService);
        add.performClick();
        assertNotEquals(null, adminActivity.findViewById(R.id.createName));
    }

    @Test
    @UiThreadTest
    public void testAddName(){
        EditText name = (EditText) adminActivity.findViewById(R.id.createName);
        name.setText("TestService");
        assertEquals("TestService", name.getText().toString());
    }

    @Test
    @UiThreadTest
    public void testAddRate(){
        EditText rate = (EditText) adminActivity.findViewById(R.id.createRate);
        rate.setText("15.35");
        assertEquals("15.35", rate.getText().toString());
    }






}
