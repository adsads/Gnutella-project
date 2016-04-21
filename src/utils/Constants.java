package utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
	public static final byte[] LOCAL_HOST = { (byte) 127, (byte) 0, (byte) 0, (byte) 1 };

	public static ArrayList<Neighbor> KNOWN_NODES = new ArrayList<>(
			Arrays.asList(new Neighbor(LOCAL_HOST, Conversion.shortToBytes((short) 9000)),
					new Neighbor(LOCAL_HOST, Conversion.shortToBytes((short) 9001))));
	// list of open ports on localhost

	public static final String BASE_DIR = "E:\\gnutella\\"; // base spot to
															// share files
}
