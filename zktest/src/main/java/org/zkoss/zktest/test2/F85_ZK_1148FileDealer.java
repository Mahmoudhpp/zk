/* F85_ZK_1148Writer.java

        Purpose:
                
        Description:
                
        History:
                Wed Jun 06 11:18:17 CST 2018, Created by klyve

Copyright (C) 2018 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zktest.test2;

import io.github.pixee.security.BoundedLineReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class F85_ZK_1148FileDealer {

	public static void writeMsg(String msg) {
		writeMsg(msg, true);
	}
	
	public static void writeMsg(String msg, boolean append) {
		try {
			File src = new File("src/main/webapp/test2/ZK1148.txt");
			FileWriter fw = new FileWriter(src, append);
			BufferedWriter bw = new BufferedWriter(fw);;
			bw.write(msg);
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
	}

	public static String readMsg() {
		String result = "";
		try {
			File src = new File("src/main/webapp/test2/ZK1148.txt");
			FileReader fw = new FileReader(src);
			BufferedReader bw = new BufferedReader(fw);;
			result = BoundedLineReader.readLine(bw, 5_000_000);
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	public static void close() { 
		File src = new File("src/main/webapp/test2/ZK1148.txt");
		if (src.delete()) {
			System.out.println(src.getName() + " is deleted!");
		} else {
			System.out.println("Delete operation is failed.");
		}
	}
}
