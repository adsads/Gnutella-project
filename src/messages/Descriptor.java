package messages;

import utils.Conversion;

public class Descriptor {
	public byte[] ID = new byte[16];
	public byte payloadType = -1;
	public byte TTL = -1;
	public byte hops = -1;
	public byte[] payloadLength = new byte[4];

	public static long nodeId = 0;

	/**
	 * 
	 * @param ID
	 * @param payloadType
	 * @param TTL
	 * @param hops
	 * @param payloadLength
	 */
	public Descriptor(byte[] ID, byte payloadType, byte TTL, byte hops, byte[] payloadLength) {
		super();

		this.ID = ID;
		this.payloadType = payloadType;
		this.TTL = TTL;
		this.hops = hops;
		this.payloadLength = payloadLength;
	}

	/**
	 * 
	 * @param ID
	 * @param ip
	 * @param port
	 * @param payloadType
	 * @param TTL
	 * @param hops
	 * @param payloadLength
	 */
	public Descriptor(byte[] ID, byte[] ip, short port, byte payloadType, byte TTL, byte hops, byte[] payloadLength) {
		super();

		if (null == ID) {
			ID = new byte[16];
			System.arraycopy(ip, 0, ID, 0, 4);
			System.arraycopy(Conversion.shortToBytes(port), 0, ID, 6, 2);
			System.arraycopy(Conversion.longToBytes(nodeId++), 0, ID, 8, 8);
		}

		this.ID = ID;
		this.payloadType = payloadType;
		this.TTL = TTL;
		this.hops = hops;
		this.payloadLength = payloadLength;
	}

	public Descriptor(byte[] ID, byte[] ip, byte[] port) {
		super();

		if (null == ID) {
			ID = new byte[16];
			System.arraycopy(ip, 0, ID, 0, 4);
			System.arraycopy(port, 0, ID, 6, 2);
			System.arraycopy(Conversion.longToBytes(nodeId++), 0, ID, 8, 8);
		}

		this.ID = ID;
	}

	public byte[] message() {
		byte message[] = new byte[22];
		System.arraycopy(ID, 0, message, 0, 16); // 0-15
		System.arraycopy(payloadType, 0, message, 16, 1); // 16
		System.arraycopy(TTL, 0, message, 17, 1); // 17
		System.arraycopy(hops, 0, message, 18, 1); // 18
		System.arraycopy(payloadLength, 0, message, 19, 4); // 19-22

		return message;
	}

	public static Descriptor DescFromMsg(byte[] msg) {
		byte[] id = new byte[16];
		byte type = -1;
		byte ttl = -1;
		byte hops = -1;
		byte[] length = new byte[4];

		System.arraycopy(msg, 0, id, 0, 16);
		System.arraycopy(msg, 16, type, 16, 1);
		System.arraycopy(msg, 17, ttl, 17, 1);
		System.arraycopy(msg, 18, hops, 18, 1);
		System.arraycopy(msg, 19, length, 19, 4);

		return new Descriptor(id, type, ttl, hops, length);

	}

	public byte[] getID() {
		return ID;
	}

	public void setID(byte[] iD) {
		ID = iD;
	}

	public byte getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(byte payloadType) {
		this.payloadType = payloadType;
	}

	public byte getTTL() {
		return TTL;
	}

	public void setTTL(byte tTL) {
		TTL = tTL;
	}

	public byte getHops() {
		return hops;
	}

	public void setHops(byte hops) {
		this.hops = hops;
	}

	public byte[] getPayloadLength() {
		return payloadLength;
	}

	public void setPayloadLength(byte[] payloadLength) {
		this.payloadLength = payloadLength;
	}

}
