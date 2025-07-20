package com.example.doandidong.utils;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AccessToken {

    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken() {
        try {
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"appbanhang-5cc7f\",\n" +
                    "  \"private_key_id\": \"6ac2c062be22cba784ec6913d1a26a3f550d4c75\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCvY0eCxHvVoMmr\\nrmKTvqCqLb7yLFmOjeOPvr0pKHexmK34Xw+axkXkg4wsZsUBY2W3TQWpzUxNdkYN\\nqtyQostmpV8AINrvaen4RFRgZFFm07j/7qfjtOTmaIZupLl9NkfWjGyStRxCXs9H\\nryW51q2OETaYhwteSa8v0h2xEtGbCh5+kHVgfdnkKE/lO0CBP9jWHuw7t9evHTCK\\nT8NVGnVZkuhLZzOjr7mQmpgF7j/sIHrqdvu3JkdBZBA/PFFApOcUSdwnC0/qMRlt\\nGgcJVPWnb14B8sMDLkfDA1h/V69M4w11f5NQz4NOiWJiyvutW7KlUW9SshIHSMDr\\nC1osplbtAgMBAAECggEAJ/Uzbpx7JhGKwL1rIgP1MLBZZ20pPiwfYzlueTkKBAEn\\n6YUm1flEJSq/PcJwMb/GhbtbhfMK5j8C+Mwrkhw0wP7+JNZXauIzdueCXa+dSBMG\\ne/CQeXVWDRRVRFHtHJTISx4VAluWw5w8FzXVH4DEa7CKQrfJpEKfUgLJ1vTVoj+M\\nXcbcswCuUq+HNVHQ9dA7UyfFBSChIaKONEkgmcVy1qaL2CxSHg5KZiq4MRC1+xwy\\nz0uC9/sWDRXRsr/v31T4BCIcNyWXtNG4vLyTfrwsJJTv/2yiMarJkCX9zxLMf+UB\\nwJmL0ZiQsfzD9+IEBS+GSI6KJwNZZpLzgxbaw1dVVQKBgQDYpPrE4WRdVLeQIW4x\\n5/NFUOh82JX7hq4fJFxZ/KShCU/wVdSw7TS006HWDgezGHINl3nu6aDHu0/pvMdG\\n8og0i4/SmH+6IpKdvL9G5LZ+r7Zh85oYcI0qeRPjs7/yXnyNKlJNaRnHbaGwpu91\\nmkM6pAatNA7VzKwUlOWbsx4QUwKBgQDPP6pTEqFgYkgZTSutrnQTwyhoNHw6y/kt\\nkjKhCTZ3VOJhGbq6opWwbPxaI4Db8H/7LYmAcOZ9aXKpC8PQNiDS0UAKdgwt2oX0\\nm8ZEKIiswebA1EN7IDwSYqZUzPQ2EdlDJGw9z1tNqFWDHwKrLJCDhoQQSzl93QaQ\\nAuquIbYTvwKBgF7jvBa55jze21vc2KgxQSNuMbZlIyQ9NEzdnzmm5/DE5GePII6y\\nBvDg9TSNhp/SKDp9y2FyoQJ6r6DgF6SinDfvnPoP43oCpJdEUb9ac/h/O/IhQbBM\\ns/gTeju0wnEFI6atD8lqLGlmQnjeVWn8PFAWAfqhH5JTiFenc/5DcPqTAoGAYGgH\\nPTmPZByT2WEyjkp2qLLB2yd/GoLD2NUHRC3a7kvIOs8TM7pp03X/NZcKXEVV42vd\\naKgcL/6bXdOhNPvY7Ph1x+f95l6erZjA8/e/DE3un4ieE2p9Rzgok437lCNdY4RP\\n7bjFiixiCuqj/Shv2R770/CKP+hPIzKYJIR+BFUCgYAVvQE4KzG0lvVHBJPB2Iwg\\nLGcHEYQkr6uL0ZQsx+9p1Z3+abzw6/q9JGyATriJR1epVTQxVXnqHVrtpmVoNFnm\\ngreiNUeI3ZGsrVl5aBaEggPITVvyfvtofzk90ViOME3ekSVe7ljTTHqZzU48lVkw\\nDTgzNDf+LUONcnpDkVsQOg==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-fbsvc@appbanhang-5cc7f.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"108262065029614886010\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40appbanhang-5cc7f.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(firebaseMessagingScope);
            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            Log.e("AccessToken", "getAccessToken: " + e.getLocalizedMessage());
            return null;
        }
    }
}
