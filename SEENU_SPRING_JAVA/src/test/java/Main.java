
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class Main {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("123"));
		
	

			StringBuilder str = new StringBuilder("your balance is #$.balance#.");
			System.out.println( str);
			String temp = str.substring(str.indexOf("#"),str.indexOf("#"));
			System.out.println( temp);

			String mydata = "your balance is $.balance#.";
			Pattern pattern = Pattern.compile("#(.*?)#");
			Matcher matcher = pattern.matcher(mydata);
			if (matcher.find())
			{
			    System.out.println(matcher.group(1));
			}
	}
}