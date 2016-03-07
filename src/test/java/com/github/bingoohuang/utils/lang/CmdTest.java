package com.github.bingoohuang.utils.lang;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class CmdTest {
    @Test
    public void test1() {
        if (!Os.isWindows) {
            Cmd echo = new Cmd("echo", "abc");
            boolean succ = echo.syncExec(1200);

            String stdErr = echo.getStdErr();
            assertThat(succ).isTrue();
//            assertThat(stdErr).isEmpty();

            int exitValue = echo.getExitValue();
            assertThat(exitValue).isEqualTo(0);

            String stdOut = echo.getStdOut();
            assertThat(stdOut).isEqualTo("abc\n");
        }
    }
}
