package org.matrix.androidsdk.login;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 2, d1 = {"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u0004\u0018\u00010\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0002\u001a\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u001a*\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u0005\u00a8\u0006\r"}, d2 = {"extractUrlAndName", "Lorg/matrix/androidsdk/login/UrlAndName;", "policyData", "", "getCaptchaPublicKey", "", "registrationFlowResponse", "Lorg/matrix/androidsdk/rest/model/login/RegistrationFlowResponse;", "getLocalizedLoginTerms", "", "Lorg/matrix/androidsdk/rest/model/login/LocalizedFlowDataLoginTerms;", "userLanguage", "defaultLanguage", "matrix-sdk_debug"})
public final class RegistrationToolsKt {
    
    /**
     * * Get the public key for captcha registration
     * *
     * * @return public key
     */
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.String getCaptchaPublicKey(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse registrationFlowResponse) {
        return null;
    }
    
    /**
     * * This method extract the policies from the login terms parameter, regarding the user language.
     * * For each policy, if user language is not found, the default language is used and if not found, the first url and name are used (not predictable)
     * *
     * * Example of Data:
     * * <pre>
     * * "m.login.terms": {
     * *       "policies": {
     * *         "privacy_policy": {
     * *           "version": "1.0",
     * *           "en": {
     * *             "url": "http:\/\/matrix.org\/_matrix\/consent?v=1.0",
     * *             "name": "Terms and Conditions"
     * *           }
     * *         }
     * *       }
     * *     }
     * *</pre>
     * *
     * * @param registrationFlowResponse the registration flow to extract data from
     * * @param userLanguage the user language
     * * @param defaultLanguage the default language to use if the user language is not found for a policy in registrationFlowResponse
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms> getLocalizedLoginTerms(@org.jetbrains.annotations.Nullable()
    org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse registrationFlowResponse, @org.jetbrains.annotations.NotNull()
    java.lang.String userLanguage, @org.jetbrains.annotations.NotNull()
    java.lang.String defaultLanguage) {
        return null;
    }
    
    private static final org.matrix.androidsdk.login.UrlAndName extractUrlAndName(java.lang.Object policyData) {
        return null;
    }
}