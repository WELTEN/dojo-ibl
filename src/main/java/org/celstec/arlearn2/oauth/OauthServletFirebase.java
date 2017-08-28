/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class OauthServletFirebase extends HttpServlet {

	private static final String FIREBASE_SNIPPET_PATH = "WEB-INF/dojo-ibl-firebase-adminsdk-ofvly-bf28455fa0.json";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String token = req.getParameter("token");

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

		final StringBuilder email = new StringBuilder();
		final CountDownLatch countDownLatch = new CountDownLatch(1);

		FirebaseApp app = FirebaseApp.getInstance("dojo-ibl");
		FirebaseAuth.getInstance(app).verifyIdToken(token)
				.addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
					@Override
					public void onSuccess(FirebaseToken decodedToken) {
						email.append(decodedToken.getEmail());
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
				System.out.print("d");
			}
		});

		try {
			countDownLatch.await(30L, TimeUnit.SECONDS);
			email.toString();
		} catch (InterruptedException e) {
			;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	

}
