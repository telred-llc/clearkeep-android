package org.matrix.androidsdk.rest.model.login;

import java.lang.System;

/**
 * * Open class, parent to all possible authentication parameters
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/AuthParams;", "", "type", "", "(Ljava/lang/String;)V", "session", "matrix-sdk_debug"})
public class AuthParams {
    @org.jetbrains.annotations.Nullable()
    public java.lang.String session;
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String type = null;
    
    public AuthParams(@org.jetbrains.annotations.NotNull()
    java.lang.String type) {
        super();
    }
}