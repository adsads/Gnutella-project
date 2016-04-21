package utils;

public class Conversion {
	public static byte[] longToBytes(long l) {
		byte[] result = new byte[8];
		for (int i = 7; i >= 0; i--) {
			result[i] = (byte) (l & 0xFF);
			l >>= 8;
		}
		return result;
	}

	public static long bytesToLong(byte[] b) {
		long result = 0;
		for (int i = 0; i < 8; i++) {
			result <<= 8;
			result |= (b[i] & 0xFF);
		}
		return result;
	}

	public static byte[] shortToBytes(short s) {
		byte[] result = new byte[2];
		for (int i = 7; i >= 0; i--) {
			result[i] = (byte) (s & 0xFF);
			s >>= 8;
		}
		return result;
	}

	public static short bytesToShort(byte[] b) {
		short result = 0;
		for (int i = 0; i < 2; i++) {
			result <<= 8;
			result |= (b[i] & 0xFF);
		}
		return result;
	}

	public static byte[] intToBytes(int j) {
		byte[] result = new byte[4];
		for (int i = 7; i >= 0; i--) {
			result[i] = (byte) (j & 0xFF);
			j >>= 8;
		}
		return result;
	}

	public static short bytesToInt(byte[] b) {
		short result = 0;
		for (int i = 0; i < 4; i++) {
			result <<= 8;
			result |= (b[i] & 0xFF);
		}
		return result;
	}

	public static String bytesToString(byte[] b) {
		return new String(b).split("\0")[0];
	}

}
