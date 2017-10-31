package com.ocarmon.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author 李浩铭
 * @since 2017年7月3日 下午3:58:03
 */
public class FileUtil {
	private static final String path = "E://zhihu.txt";

	public static void fileWirters(String content) {
		File file = new File(path);
		FileReader fr = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		BufferedReader br = null;
		try {
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			String str = null;
			bw.write(content);
			bw.newLine();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
}
