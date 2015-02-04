package com.github.bingoohuang.utils.codec;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PasswdTest {
    @Test
    public void test1() {
        String originalPassword = "password";
        String generatedSecuredPasswordHash = Passwd.bcrypt(originalPassword);

        boolean matched = Passwd.bcryptMatch(originalPassword, generatedSecuredPasswordHash);
        assertThat(matched, is(true));
    }
}
