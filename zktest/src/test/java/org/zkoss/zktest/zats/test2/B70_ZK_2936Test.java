/* B70_ZK_2936Test.java

	Purpose:
		
	Description:
		
	History:
		9:59 AM 11/19/15, Created by jumperchen

Copyright (C) 2015 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zktest.zats.test2;

import static io.github.pixee.security.ObjectInputFilters.createSafeObjectInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

import org.zkoss.io.RepeatableInputStream;

/**
 * @author jumperchen
 */
public class B70_ZK_2936Test {
	@Test
	public void test2() {
		try {
			InputStream instance = RepeatableInputStream.getInstance(
					this.getClass().getResourceAsStream("/WEB-INF/zk.xml"));
			BufferedReader in = new BufferedReader(new InputStreamReader(instance));
			String line;

			StringBuilder sb = new StringBuilder();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			in.close();


			ByteArrayOutputStream boa = new ByteArrayOutputStream();
			new ObjectOutputStream(boa).writeObject(instance);
			byte[] bs = boa.toByteArray();
			InputStream outInstance = (InputStream) createSafeObjectInputStream(new ByteArrayInputStream(bs)).readObject();

			BufferedReader in2 = new BufferedReader(new InputStreamReader(outInstance));

			StringBuilder sb2 = new StringBuilder();
			while ((line = in2.readLine()) != null) {
				sb2.append(line);
			}

			in2.close();

			assertEquals(sb2.toString(), sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
