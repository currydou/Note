package com.yodoo.android.baseutil.sp;

import com.yodoo.android.baseutil.RoboTestApplication;
import com.yodoo.android.baseutil.keys.SP;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, application = RoboTestApplication.class)
public class SPUtilTest {

    private String keyString = "keyString";
    private String keyInt = "keyInt";
    private String keyBool = "keyBool";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        SPUtil.init(RuntimeEnvironment.application);
    }

    @Test
    public void getBoolean1() {
        boolean b = SPUtil.getBoolean(keyBool, false);
        Assert.assertTrue(b);
    }

    @Test
    public void setAndGetString() {
        SPUtil.setString(keyString, "setString");

        String string = SPUtil.getString(keyString);
        Assert.assertEquals(string, "setString");
    }

    @Test
    public void setAndGetBoolean() {
        SPUtil.setBoolean(keyBool, true);

        boolean b = SPUtil.getBoolean(keyBool);
        Assert.assertTrue(b);
    }

    @Test
    public void setAndGetInt() {
        SPUtil.setInt(keyInt, 1);

        int i = SPUtil.getInt(keyInt);
        Assert.assertEquals(i, 1);
    }

    @Test
    public void testFirstEntry() {
        SPUtil.setFristEntry();
        boolean b = SPUtil.isFirstEntry();
        Assert.assertFalse(b);
    }

    @Test
    public void testLogin() {
        SPUtil.setUserToken("token000001");

        String token = SPUtil.getUserToken();
        Assert.assertEquals(token, "token000001");

        boolean isLogin = SPUtil.isLogin();
        Assert.assertTrue(isLogin);
    }

    @Test
    public void testUnLogin() {
        SPUtil.setUserToken("");

        String token = SPUtil.getUserToken();
        Assert.assertEquals(token, "");

        boolean isLogin = SPUtil.isLogin();
        Assert.assertFalse(isLogin);
    }
}