package org.celstec.arlearn2.oauth;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.internal.NonNull;
import com.google.firebase.tasks.OnCompleteListener;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import com.google.firebase.tasks.Task;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.manager.AccountManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * ****************************************************************************
 * Copyright (C) 2017 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Angel Suarez
 * Date: 25/08/17
 * ****************************************************************************
 */

public class OauthFirebaseWorker {

//    private static final Logger logger = (Logger) LoggerFactory.getLogger(FirebaseUtils.class);

    private static final String FIREBASE_SNIPPET_PATH = "WEB-INF/dojo-ibl-firebase-adminsdk-ofvly-4e5a54f674.json";

    public static String validateToken(final String token) {

        final AtomicBoolean authenticated = new AtomicBoolean(false);
        final StringBuilder stringToken = new StringBuilder("");

        if (token == null || token.isEmpty()) {
            return stringToken.toString();
        }

        FileInputStream serviceAccount = null;
		try {
			serviceAccount = new FileInputStream(FIREBASE_SNIPPET_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (FirebaseApp.getApps().isEmpty()) {

			FirebaseOptions options = null;
			try {
				options = new FirebaseOptions.Builder()
                        .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                        .setDatabaseUrl("https://dojo-ibl.firebaseio.com")
                        .build();
			} catch (IOException e) {
				e.printStackTrace();
			}

			FirebaseApp.initializeApp(options, "dojo-ibl");
		}

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        FirebaseApp app = FirebaseApp.getInstance("dojo-ibl");
        FirebaseAuth.getInstance(app).verifyIdToken(token)
                .addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
                    @Override
                    public void onSuccess(FirebaseToken decodedToken) {

                        UUID uuid = UUID.randomUUID();
                        saveAccount(decodedToken, uuid.toString());
                        stringToken.append(uuid.toString());

                        countDownLatch.countDown();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                countDownLatch.countDown();
            }
        }).addOnCompleteListener(new OnCompleteListener<FirebaseToken>() {
            @Override
            public void onComplete(@NonNull Task<FirebaseToken> task) {
            }
        });

        try {
            countDownLatch.await(30L, TimeUnit.SECONDS);
            return "{ \"token\": \""+stringToken.toString()+"\"}";
        } catch (InterruptedException e) {
            return stringToken.toString();
        }
    }
    public static final void saveAccount(FirebaseToken firebaseToken, String token) {
        AccountJDO account = AccountManager.addAccount(
                firebaseToken.getUid(),
                AccountJDO.FIREBASECLIENT,
                firebaseToken.getEmail(),
                firebaseToken.getName(),
                firebaseToken.getIssuer(),
                firebaseToken.getName(),
                firebaseToken.getPicture(), false);

        UserLoggedInManager.submitOauthUser(account.getUniqueId(), token);

    }
}
