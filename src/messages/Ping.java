package messages;

public class Ping {

	public Descriptor pingDesc; // 22 bytes
	public byte[] sender_node_port; // 2 bytes
	public byte[] sender_node_ip; // 4 bytes
	// total == 28 bytes

	public Ping(Descriptor pingDesc, byte[] sender_node_port, byte[] sender_node_ip) {
		super();
		this.pingDesc = pingDesc;
		this.sender_node_port = sender_node_port;
		this.sender_node_ip = sender_node_ip;
	}

	public byte[] message() {
		byte[] msg = new byte[22 + 2 + 4];
		System.arraycopy(this.pingDesc, 0, msg, 0, 22);
		System.arraycopy(this.sender_node_port, 0, msg, 22, 2);
		System.arraycopy(this.sender_node_ip, 0, msg, 28, 4);

		return msg;
	}

	public static Ping PingFromMsg(byte[] msg) {
		Descriptor tmp = Descriptor.DescFromMsg(msg);
		byte[] node_p = new byte[2];
		byte[] node_ip = new byte[4];

		System.arraycopy(msg, 22, node_p, 0, 2);
		System.arraycopy(msg, 26, node_ip, 0, 4);

		return new Ping(tmp, node_p, node_ip);
	}

	public Descriptor getPingDesc() {
		return pingDesc;
	}

	public void setPingDesc(Descriptor pingDesc) {
		this.pingDesc = pingDesc;
	}

	public byte[] getSender_node_port() {
		return sender_node_port;
	}

	public void setSender_node_port(byte[] sender_node_port) {
		this.sender_node_port = sender_node_port;
	}

	public byte[] getSender_node_ip() {
		return sender_node_ip;
	}

	public void setSender_node_ip(byte[] sender_node_ip) {
		this.sender_node_ip = sender_node_ip;
	}

}
