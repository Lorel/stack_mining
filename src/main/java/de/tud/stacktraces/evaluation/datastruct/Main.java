package de.tud.stacktraces.evaluation.datastruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Main {
	public static void main(String[] args) throws IOException {
		String content = new String(Files.readAllBytes(new File(args[0])
				.toPath()));
		List<StackTrace> traces = StackTraceParser.parseAll(content);
		for (StackTrace stackTrace : traces) {
			System.out.println(stackTrace);
		}
	}
}
