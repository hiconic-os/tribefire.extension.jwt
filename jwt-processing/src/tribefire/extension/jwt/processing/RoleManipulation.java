package tribefire.extension.jwt.processing;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class RoleManipulation {
	public static UnaryOperator<String> regexReplace(String regex, String replacement) {
		Pattern pattern = Pattern.compile(regex);
		return s -> pattern.matcher(s).replaceAll(replacement);
	}

	public static UnaryOperator<String> removePrefix(String prefix) {
		return s -> s.startsWith(prefix) ? s.substring(prefix.length()) : s;
	}

	public static UnaryOperator<String> trim() {
		return String::trim;
	}

	public static UnaryOperator<String> toLowerCase() {
		return String::toLowerCase;
	}
}
