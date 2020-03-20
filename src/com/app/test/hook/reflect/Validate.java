
package com.app.test.hook.reflect;

class Validate {

    static void assertTrue(final boolean expression, final String message, final Object... values) throws ReflectIllegalArgumentsException {
        if (!expression) {
            throw new ReflectIllegalArgumentsException(String.format(message, values));
        }
    }
}
